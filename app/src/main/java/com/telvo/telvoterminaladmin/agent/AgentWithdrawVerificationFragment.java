package com.telvo.telvoterminaladmin.agent;


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
import com.telvo.telvoterminaladmin.model.agent.action.ConfirmWithdrawRequest;
import com.telvo.telvoterminaladmin.model.agent.action.ConfirmWithdrawResponse;
import com.telvo.telvoterminaladmin.model.agent.action.WithdrawRequest;
import com.telvo.telvoterminaladmin.model.agent.action.WithdrawResponse;
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
public class AgentWithdrawVerificationFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AgentVerifyFragment";
    private TerminalPreferences terminalPreferences;

    private EditText etVerificationCode;
    private AppCompatButton btnCancelVerification;
    private AppCompatButton btnVerify;
    private TextView[] textViews;
    private FragmentManager fragmentManager;
    private CustomAlertDialog alertDialog;
    private ProgressDialog progress;

    private Context context;

    private LoginResponseAgent loginResponseAgent;
    private String agentId;
    private String mobileNumber;
    private String nid;
    private String amount;
    private String withdrawSecret;
    private String otpKey;
    private String token;
    private WithdrawRequest withdrawRequest;
    private WithdrawResponse withdrawResponse;

    private ApiInterface apiInterface;
    private ConfirmWithdrawRequest confirmWithdrawRequest = new ConfirmWithdrawRequest();
    private ConfirmWithdrawResponse confirmWithdrawResponse = new ConfirmWithdrawResponse();

    public void setTextViews(TextView[] textViews) {
        this.textViews = textViews;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public AgentWithdrawVerificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_agent_withdraw_verification, container, false);

        context = getActivity();
        terminalPreferences = new TerminalPreferences(context);

        loginResponseAgent = ((AgentMainActivity)getActivity()).getLoginResponseAgent();
        withdrawRequest = (WithdrawRequest) getArguments().getSerializable(Constants.PAYMENT_REQUEST);
        withdrawResponse = (WithdrawResponse) getArguments().getSerializable(Constants.PAYMENT_RESPONSE);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        token = "Bearer " + loginResponseAgent.getToken();

        initializeViews(rootView);

        return rootView;
    }

    private void initializeViews(View rootView) {
        etVerificationCode = rootView.findViewById(R.id.edit_text_recharge_verification_code);
        btnCancelVerification = rootView.findViewById(R.id.btn_cancel_withdraw_verification);
        btnCancelVerification.setOnClickListener(this);
        btnVerify = rootView.findViewById(R.id.btn_verify_withdraw);
        btnVerify.setOnClickListener(this);
        alertDialog = new CustomAlertDialog(context);
        progress = new ProgressDialog(context);
    }

    private void showProgressDialog() {
        progress.setMessage(getString(R.string.please_wait));
        progress.show();
    }

    private void hideProgressDialog() {
        progress.dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel_withdraw_verification:
                hideKeyboard(view);
                goToWithdrawFragment();
                break;
            case R.id.btn_verify_withdraw:
                hideKeyboard(view);

                agentId = loginResponseAgent.getAgent().getId();
                mobileNumber = withdrawRequest.getMobileNumber();
                nid = withdrawRequest.getNid();
                amount = withdrawRequest.getAmount();
                withdrawSecret = etVerificationCode.getText().toString().trim();
                otpKey = withdrawResponse.getOtpKey();

                if (TextUtils.isEmpty(withdrawSecret)) {
                    Toast.makeText(getActivity(), getString(R.string.please_enter_verification_code), Toast.LENGTH_SHORT).show();
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        showProgressDialog();
                        confirmWithdraw();
                    } else {
                        alertDialog.showDialog(context.getResources().getString(R.string.no_internet_connection));
                    }
                }
                break;
            default:
        }
    }

    private void confirmWithdraw() {
        setConfirmPaymentRequestValues();

        Call<ConfirmWithdrawResponse> confirmPaymentResponseCall = apiInterface.confirmWithdraw(token, confirmWithdrawRequest);
        confirmPaymentResponseCall.enqueue(new Callback<ConfirmWithdrawResponse>() {
            @Override
            public void onResponse(Call<ConfirmWithdrawResponse> call, Response<ConfirmWithdrawResponse> response) {
                hideProgressDialog();
                confirmWithdrawResponse = response.body();

                if (confirmWithdrawResponse.getStatus().equals(Constants.SUCCESS)) {
                    //Toast.makeText(context, "Payment successful!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Balance: " + confirmWithdrawResponse.getBalance());

                    updateBalance();

                    goToSuccessFragment();
                } else {
                    alertDialog.showDialog(confirmWithdrawResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ConfirmWithdrawResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG,getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private void goToSuccessFragment() {
        SuccessFragment successFragment = new SuccessFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACTION_TYPE, Constants.AGENT_WITHDRAW);
        bundle.putString(Constants.MOBILE_NUMBER, mobileNumber);
        bundle.putString(Constants.AMOUNT, amount);
        successFragment.setArguments(bundle);

        successFragment.setTextViews(textViews);
        successFragment.setFragmentManager(fragmentManager);

        fragmentManager.beginTransaction()
                .replace(R.id.agent_detail_container, successFragment)
                .commit();
    }

    private void updateBalance() {
        terminalPreferences.putAgentTotalBalance(NumberFormatUtils.getFormattedDouble(confirmWithdrawResponse.getBalance()));
        terminalPreferences.putAgentTotalCommission(NumberFormatUtils.getFormattedDouble(confirmWithdrawResponse.getCommission().getTotal().getAmount()));
        terminalPreferences.putAgentTodayCommission(NumberFormatUtils.getFormattedDouble(confirmWithdrawResponse.getCommission().getToday().getAmount()));
    }

    private void setConfirmPaymentRequestValues() {
        confirmWithdrawRequest.setAgentId(agentId);
        confirmWithdrawRequest.setMobileNumber(mobileNumber);
        confirmWithdrawRequest.setNid(nid);
        confirmWithdrawRequest.setAmount(amount);
        confirmWithdrawRequest.setWithdrawSecret(withdrawSecret);
        confirmWithdrawRequest.setOtpKey(otpKey);
    }

    private void goToWithdrawFragment() {
        AgentWithdrawFragment agentWithdrawFragment = new AgentWithdrawFragment();
        agentWithdrawFragment.setFragmentManager(fragmentManager);
        agentWithdrawFragment.setTextViews(textViews);
        fragmentManager.beginTransaction()
                .replace(R.id.agent_detail_container, agentWithdrawFragment)
                .commit();
    }

    private void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
