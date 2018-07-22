package com.telvo.telvoterminaladmin.agent;


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
import com.telvo.telvoterminaladmin.model.agent.action.NonSysWithdrawRequest;
import com.telvo.telvoterminaladmin.model.agent.action.NonSysWithdrawResponse;
import com.telvo.telvoterminaladmin.model.agent.login.LoginResponseAgent;
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
public class AgentNonSystemWithdrawFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "NonSysWithdraw";
    private TerminalPreferences terminalPreferences;

    private TextView[] textViews = new TextView[4];
    private FragmentManager fragmentManager;
    private EditText etNonSysWithdrawPhone;
    private EditText etNonSysWithdrawNid;
    private EditText etNonSysWithdrawSecret;
    private Button btnSubmit;
    private CustomAlertDialog alertDialog;
    private ProgressDialog progress;

    private Context context;

    private LoginResponseAgent loginResponseAgent;
    private String mobileNumber;
    private String nid;
    private String withdrawSecret;
    private String token;

    private ApiInterface apiInterface;
    private NonSysWithdrawRequest nonSysWithdrawRequest = new NonSysWithdrawRequest();
    private NonSysWithdrawResponse nonSysWithdrawResponse = new NonSysWithdrawResponse();

    public AgentNonSystemWithdrawFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_agent_non_system_withdraw, container, false);

        context = getActivity();
        terminalPreferences = new TerminalPreferences(context);
        loginResponseAgent = ((AgentMainActivity) getActivity()).getLoginResponseAgent();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        token = "Bearer " + loginResponseAgent.getToken();

        initializeViews(rootView);

        return rootView;
    }

    private void initializeViews(View view) {
        etNonSysWithdrawPhone = view.findViewById(R.id.edit_text_withdraw_phone);
        etNonSysWithdrawNid = view.findViewById(R.id.edit_text_withdraw_nid);
        etNonSysWithdrawSecret = view.findViewById(R.id.edit_text_withdraw_secret);
        btnSubmit = view.findViewById(R.id.btn_submit_withdraw);
        btnSubmit.setOnClickListener(this);
        alertDialog = new CustomAlertDialog(context);
        progress = new ProgressDialog(context);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_withdraw:
                hideKeyboard(view);

                mobileNumber = "88" + etNonSysWithdrawPhone.getText().toString().trim();
                nid = etNonSysWithdrawNid.getText().toString().trim();
                withdrawSecret = etNonSysWithdrawSecret.getText().toString().trim();

                if (TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(context, getString(R.string.please_enter_mobile_number), Toast.LENGTH_SHORT).show();
                    etNonSysWithdrawPhone.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(nid)) {
                    Toast.makeText(getActivity(), getString(R.string.please_enter_nid_number), Toast.LENGTH_SHORT).show();
                    etNonSysWithdrawNid.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(withdrawSecret)) {
                    Toast.makeText(getActivity(), getString(R.string.please_enter_withdraw_secret), Toast.LENGTH_SHORT).show();
                    etNonSysWithdrawSecret.requestFocus();
                    return;
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        showProgressDialog();
                        withdraw();
                    } else {
                        alertDialog.showDialog(getString(R.string.no_internet_connection));
                    }
                }
                break;
            default:
        }
    }

    private void withdraw() {
        setNonSysWithdrawRequestValues();

        Call<NonSysWithdrawResponse> responseCall = apiInterface.withdrawNonSysUser(token, nonSysWithdrawRequest);
        responseCall.enqueue(new Callback<NonSysWithdrawResponse>() {
            @Override
            public void onResponse(Call<NonSysWithdrawResponse> call, Response<NonSysWithdrawResponse> response) {
                hideProgressDialog();

                nonSysWithdrawResponse = response.body();

                if (nonSysWithdrawResponse.getStatus().equals(Constants.SUCCESS)) {
                    Log.e(TAG, "Balance: " + nonSysWithdrawResponse.getBalance());
                    updateBalance();
                    goToSuccessFragment();
                } else {
                    alertDialog.showDialog(nonSysWithdrawResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<NonSysWithdrawResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, "onFailure: "+ t.getMessage());
            }
        });
    }

    private void setNonSysWithdrawRequestValues() {
        nonSysWithdrawRequest.setMobileNumber(mobileNumber);
        nonSysWithdrawRequest.setNid(nid);
        nonSysWithdrawRequest.setWithdrawSecret(withdrawSecret);
    }

    private void updateBalance() {
        terminalPreferences.putAgentTotalBalance(NumberFormatUtils.getFormattedDouble(nonSysWithdrawResponse.getBalance()));
        terminalPreferences.putAgentTotalCommission(NumberFormatUtils.getFormattedDouble(nonSysWithdrawResponse.getCommission().getTotal().getAmount()));
        terminalPreferences.putAgentTodayCommission(NumberFormatUtils.getFormattedDouble(nonSysWithdrawResponse.getCommission().getToday().getAmount()));
    }

    private void goToSuccessFragment() {
        SuccessFragment successFragment = new SuccessFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACTION_TYPE, Constants.AGENT_WITHDRAW);
        bundle.putString(Constants.MOBILE_NUMBER, mobileNumber);
        bundle.putString(Constants.AMOUNT, String.valueOf(nonSysWithdrawResponse.getAmount()));
        successFragment.setArguments(bundle);

        successFragment.setTextViews(textViews);
        successFragment.setFragmentManager(fragmentManager);

        fragmentManager.beginTransaction()
                .replace(R.id.agent_detail_container, successFragment)
                .commit();
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
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
