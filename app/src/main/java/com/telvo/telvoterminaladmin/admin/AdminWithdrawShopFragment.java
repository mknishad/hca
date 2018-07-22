package com.telvo.telvoterminaladmin.admin;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.model.admin.action.WithdrawShopRequest;
import com.telvo.telvoterminaladmin.model.admin.action.WithdrawShopResponse;
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
public class AdminWithdrawShopFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AdminWthdrwShopFrgmnt";

    private Context context;
    private TerminalPreferences terminalPreferences;

    private EditText mobileNumberEditText;
    private EditText amountEditText;
    private Button submitButton;
    private CustomAlertDialog alertDialog;
    private ProgressDialog progress;
    private TextView[] textViews = new TextView[7];
    private FragmentManager fragmentManager;

    private String mobileNumber;
    private String amount;
    private String token;

    private ApiInterface apiInterface;
    private LoginResponseAdmin loginResponseAdmin;
    private WithdrawShopRequest withdrawShopRequest = new WithdrawShopRequest();
    private WithdrawShopResponse withdrawShopResponse = new WithdrawShopResponse();

    public AdminWithdrawShopFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_admin_withdraw_shop, container, false);

        context = getActivity();
        loginResponseAdmin = ((AdminMainActivity) getActivity()).getLoginResponseAdmin();
        terminalPreferences = new TerminalPreferences(context);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        token = "Bearer " + loginResponseAdmin.getToken();

        initializeViews(rootView);

        return rootView;
    }

    private void initializeViews(View view) {
        mobileNumberEditText = view.findViewById(R.id.mobileNumberEditText);
        amountEditText = view.findViewById(R.id.amountEditText);
        submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        alertDialog = new CustomAlertDialog(context);
        progress = new ProgressDialog(context);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.submitButton:
                hideKeyboard(view);

                mobileNumber = "88" + mobileNumberEditText.getText().toString().trim();
                amount = amountEditText.getText().toString().trim();

                if (TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(context, getString(R.string.please_enter_mobile_number), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(amount)) {
                    Toast.makeText(context, getString(R.string.please_enter_amount), Toast.LENGTH_SHORT).show();
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        showProgressDialog();
                        withdrawShop();
                    } else {
                        alertDialog.showDialog(getString(R.string.no_internet_connection));
                    }
                }
                break;
            default:
        }
    }

    private void withdrawShop() {
        setWithdrawShopValues();

        Call<WithdrawShopResponse> responseCall = apiInterface.withdrawShop(token, withdrawShopRequest);
        responseCall.enqueue(new Callback<WithdrawShopResponse>() {
            @Override
            public void onResponse(Call<WithdrawShopResponse> call, Response<WithdrawShopResponse> response) {
                hideProgressDialog();

                withdrawShopResponse = response.body();

                if (withdrawShopResponse.getStatus().equals(Constants.SUCCESS)) {
                    updateAdminBalance();

                    //alertDialog.showDialog(withdrawShopResponse.getMessage() + " Balance: " + withdrawShopResponse.getBalance());
                    gotoSuccessFragment();
                } else {
                    alertDialog.showDialog(withdrawShopResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<WithdrawShopResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private void gotoSuccessFragment() {
        SuccessFragment successFragment = new SuccessFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACTION_TYPE, Constants.WITHDRAW_SHOP);
        bundle.putString(Constants.MOBILE_NUMBER, mobileNumber);
        bundle.putString(Constants.AMOUNT, amount);
        successFragment.setArguments(bundle);

        successFragment.setTextViews(textViews);
        successFragment.setFragmentManager(fragmentManager);

        fragmentManager.beginTransaction()
                .replace(R.id.admin_detail_container, successFragment)
                .commit();
    }

    private void updateAdminBalance() {
        terminalPreferences.putAdminBalance(NumberFormatUtils.getFormattedDouble(withdrawShopResponse.getBalance()));
    }

    private void setWithdrawShopValues() {
        withdrawShopRequest.setMobileNumber(mobileNumber);
        withdrawShopRequest.setAmount(amount);
    }

    private void showProgressDialog() {
        progress.setMessage(getString(R.string.please_wait));
        progress.show();
    }

    private void hideProgressDialog() {
        progress.dismiss();
    }

    private void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}