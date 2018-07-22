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
import com.telvo.telvoterminaladmin.model.agent.action.WithdrawRequest;
import com.telvo.telvoterminaladmin.model.agent.action.WithdrawResponse;
import com.telvo.telvoterminaladmin.model.agent.login.LoginResponseAgent;
import com.telvo.telvoterminaladmin.networking.ApiClient;
import com.telvo.telvoterminaladmin.networking.ApiInterface;
import com.telvo.telvoterminaladmin.util.AppManager;
import com.telvo.telvoterminaladmin.util.Constants;
import com.telvo.telvoterminaladmin.util.CustomAlertDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgentWithdrawFragment extends Fragment implements View.OnClickListener{

    private String TAG = "AgentWithdrawFragment";

    private AppCompatButton btnSubmitWithdraw;
    private EditText etWithdrawPhoneNumber;
    private EditText etWithdrawNid;
    private EditText etWithdrawAmount;
    private FragmentManager fragmentManager;
    private TextView[] textViews = new TextView[4];
    private CustomAlertDialog alertDialog;
    private ProgressDialog progress;

    private Context context;

    private LoginResponseAgent loginResponseAgent;
    private String mobileNumber;
    private String nid;
    private String amount;
    private String token;

    private ApiInterface apiInterface;
    private WithdrawRequest withdrawRequest = new WithdrawRequest();
    private WithdrawResponse withdrawResponse = new WithdrawResponse();

    public void setTextViews(TextView[] textViews) {
        this.textViews = textViews;
    }

    public AgentWithdrawFragment() {
        // Required empty public constructor
    }

    public void setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_agent_withdraw, container, false);

        context = getActivity();
        loginResponseAgent = ((AgentMainActivity) getActivity()).getLoginResponseAgent();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        token = "Bearer " + loginResponseAgent.getToken();

        initializeViews(rootView);

        return rootView;
    }

    private void initializeViews(View rootView) {
        etWithdrawPhoneNumber = rootView.findViewById(R.id.edit_text_withdraw_phone);
        etWithdrawNid = rootView.findViewById(R.id.edit_text_withdraw_nid);
        etWithdrawAmount = rootView.findViewById(R.id.edit_text_withdraw_amount);
        btnSubmitWithdraw = rootView.findViewById(R.id.btn_submit_withdraw);
        btnSubmitWithdraw.setOnClickListener(this);
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
            case R.id.btn_submit_withdraw:
                hideKeyboard(view);

                mobileNumber = "88" + etWithdrawPhoneNumber.getText().toString().trim();
                nid = etWithdrawNid.getText().toString().trim();
                amount = etWithdrawAmount.getText().toString().trim();
                if (TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(getActivity(), getString(R.string.please_enter_mobile_number), Toast.LENGTH_SHORT).show();
                    etWithdrawPhoneNumber.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(nid)) {
                    Toast.makeText(getActivity(), getString(R.string.please_enter_nid_number), Toast.LENGTH_SHORT).show();
                    etWithdrawNid.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(amount)) {
                    Toast.makeText(getActivity(), getString(R.string.please_enter_amount), Toast.LENGTH_SHORT).show();
                    etWithdrawAmount.requestFocus();
                    return;
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        showProgressDialog();
                        withdraw();
                    } else {
                        alertDialog.showDialog(context.getResources().getString(R.string.no_internet_connection));
                    }
                }
                break;
            default:
        }
    }

    private void withdraw() {
        setWithdrawRequestValues();

        Call<WithdrawResponse> withdrawCall = apiInterface.withdrawUser(token, withdrawRequest);
        withdrawCall.enqueue(new Callback<WithdrawResponse>() {
            @Override
            public void onResponse(Call<WithdrawResponse> call, Response<WithdrawResponse> response) {
                hideProgressDialog();
                withdrawResponse = response.body();
                Log.e(TAG, "Status: " + withdrawResponse.getStatus());
                Log.e(TAG, "Message: " + withdrawResponse.getMessage());
                Log.e(TAG, "Otp Key: " + withdrawResponse.getOtpKey());

                if (withdrawResponse.getStatus().equals(Constants.SUCCESS)) {
                    goToVerificationFragment();
                } else {
                    alertDialog.showDialog(withdrawResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<WithdrawResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private void setWithdrawRequestValues() {
        withdrawRequest.setMobileNumber(mobileNumber);
        withdrawRequest.setNid(nid);
        withdrawRequest.setAmount(amount);
    }

    private void goToVerificationFragment() {
        AgentWithdrawVerificationFragment agentWithdrawVerificationFragment = new AgentWithdrawVerificationFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PAYMENT_REQUEST, withdrawRequest);
        bundle.putSerializable(Constants.PAYMENT_RESPONSE, withdrawResponse);
        agentWithdrawVerificationFragment.setArguments(bundle);

        agentWithdrawVerificationFragment.setTextViews(textViews);
        agentWithdrawVerificationFragment.setFragmentManager(fragmentManager);

        fragmentManager.beginTransaction()
                .replace(R.id.agent_detail_container, agentWithdrawVerificationFragment)
                .commit();
    }

    private void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
