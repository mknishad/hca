package com.telvo.telvoterminaladmin.admin;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.model.admin.action.HomeWithdrawRequest;
import com.telvo.telvoterminaladmin.model.admin.action.HomeWithdrawResponse;
import com.telvo.telvoterminaladmin.model.admin.history.home.HomeWithdraw;
import com.telvo.telvoterminaladmin.model.admin.login.LoginResponseAdmin;
import com.telvo.telvoterminaladmin.networking.ApiClient;
import com.telvo.telvoterminaladmin.networking.ApiInterface;
import com.telvo.telvoterminaladmin.sharedpreference.TerminalPreferences;
import com.telvo.telvoterminaladmin.success.SuccessFragment;
import com.telvo.telvoterminaladmin.util.AppManager;
import com.telvo.telvoterminaladmin.util.Constants;
import com.telvo.telvoterminaladmin.util.CustomAlertDialog;
import com.telvo.telvoterminaladmin.util.NumberFormatUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminHomeWithdrawFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AdminVerifyFragment";

    private Context context;
    private TerminalPreferences terminalPreferences;

    private EditText mobileNumberEditText;
    private EditText securityCodeEditText;
    private AppCompatButton submitButton;
    private CustomAlertDialog alertDialog;
    private ProgressDialog progress;
    private TextView[] textViews = new TextView[7];
    private FragmentManager fragmentManager;

    private String adminId;
    private String mobileNumber;
    private String withdrawSecret;
    private String token;

    private ApiInterface apiInterface;
    private LoginResponseAdmin loginResponseAdmin;
    private HomeWithdrawRequest homeWithdrawRequest = new HomeWithdrawRequest();
    private HomeWithdrawResponse homeWithdrawResponse = new HomeWithdrawResponse();

    public AdminHomeWithdrawFragment() {
        // Required empty public constructor
    }

    public void setTextViews(TextView[] textViews) {
        this.textViews = textViews;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_home_withdraw, container, false);

        context = getActivity();
        terminalPreferences = new TerminalPreferences(context);
        loginResponseAdmin = ((AdminMainActivity) getActivity()).getLoginResponseAdmin();

        initializeViews(rootView);
        setValuesIfAvailable();

        adminId = loginResponseAdmin.getAdmin().getId();
        token = "Bearer " + loginResponseAdmin.getToken();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        return rootView;
    }

    private void setValuesIfAvailable() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            HomeWithdraw homeWithdraw = (HomeWithdraw) bundle.getSerializable(Constants.PENDING_HOME_WITHDRAW);
            mobileNumberEditText.setText(homeWithdraw.getUser().getMobileNumber().substring(2));
        }
    }

    private void initializeViews(View view) {
        mobileNumberEditText = view.findViewById(R.id.edit_text_mobile_number);
        securityCodeEditText = view.findViewById(R.id.edit_text_security_code);
        submitButton = view.findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(this);
        alertDialog = new CustomAlertDialog(context);
        progress = new ProgressDialog(context);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_submit:
                hideKeyboard(view);

                mobileNumber = mobileNumberEditText.getText().toString().trim();
                withdrawSecret = securityCodeEditText.getText().toString().trim();

                if (TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(context, getString(R.string.please_enter_mobile_number), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(withdrawSecret)) {
                    Toast.makeText(context, getString(R.string.please_enter_security_code), Toast.LENGTH_SHORT).show();
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        showProgressDialog();
                        verifyHomeRequest();
                    } else {
                        alertDialog.showDialog(getString(R.string.no_internet_connection));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void verifyHomeRequest() {
        setHomeRequestValues();

        Call<HomeWithdrawResponse> homeVerificationResponseCall = apiInterface.verifyHomeWithdraw(token, homeWithdrawRequest);
        homeVerificationResponseCall.enqueue(new Callback<HomeWithdrawResponse>() {
            @Override
            public void onResponse(Call<HomeWithdrawResponse> call, Response<HomeWithdrawResponse> response) {
                hideProgressDialog();
                homeWithdrawResponse = response.body();

                ////////////////  DEBUG  //////////////
                if (homeWithdrawResponse == null) {
                    Log.e(TAG, "homeWithdrawResponse is null");
                }
                ///////////////////////////////////////

                if (homeWithdrawResponse.getStatus().equals(Constants.SUCCESS)) {
                    updateAdminBalance();
                    gotoSuccessFragment();
                    //alertDialog.showDialog(homeWithdrawResponse.getMessage());
                } else {
                    alertDialog.showDialog(homeWithdrawResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<HomeWithdrawResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private void updateAdminBalance() {
        terminalPreferences.putAdminBalance(NumberFormatUtils.getFormattedDouble(homeWithdrawResponse.getBalance()));
    }

    private void gotoSuccessFragment() {
        SuccessFragment successFragment = new SuccessFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACTION_TYPE, Constants.HOME_WITHDRAW);
        bundle.putString(Constants.AMOUNT, NumberFormatUtils.getFormattedDouble(homeWithdrawResponse.getAmount()));
        bundle.putString(Constants.MOBILE_NUMBER, mobileNumber);
        bundle.putString(Constants.ADDRESS, homeWithdrawResponse.getAddress());
        successFragment.setArguments(bundle);

        successFragment.setTextViews(textViews);
        successFragment.setFragmentManager(fragmentManager);

        fragmentManager.beginTransaction()
                .replace(R.id.admin_detail_container, successFragment)
                .commit();
    }

    private void setHomeRequestValues() {
        homeWithdrawRequest.setAdminId(adminId);
        homeWithdrawRequest.setMobileNumber(mobileNumber);
        homeWithdrawRequest.setWithdrawSecret(withdrawSecret);
    }

    private void showProgressDialog() {
        progress.setMessage(getString(R.string.please_wait));
        progress.show();
    }

    private void hideProgressDialog() {
        progress.dismiss();
    }

    private void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
