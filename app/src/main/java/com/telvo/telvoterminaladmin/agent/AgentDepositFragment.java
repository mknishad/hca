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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.success.SuccessFragment;
import com.telvo.telvoterminaladmin.model.agent.action.DepositRequest;
import com.telvo.telvoterminaladmin.model.agent.action.DepositResponse;
import com.telvo.telvoterminaladmin.model.agent.login.LoginResponseAgent;
import com.telvo.telvoterminaladmin.model.currency.Currency;
import com.telvo.telvoterminaladmin.model.currency.CurrencyData;
import com.telvo.telvoterminaladmin.networking.ApiClient;
import com.telvo.telvoterminaladmin.networking.ApiInterface;
import com.telvo.telvoterminaladmin.sharedpreference.TerminalPreferences;
import com.telvo.telvoterminaladmin.util.AppManager;
import com.telvo.telvoterminaladmin.util.Constants;
import com.telvo.telvoterminaladmin.util.CustomAlertDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgentDepositFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AgentDepositFragment";
    private TerminalPreferences terminalPreferences;

    private AppCompatButton btnSubmitRecharge;
    private EditText etRechargePhoneNumber;
    private EditText etRechargeNid;
    private EditText etRechargeAmount;
    private Spinner currencySpinner;
    private FragmentManager fragmentManager;
    private TextView[] textViews = new TextView[4];
    private CustomAlertDialog alertDialog;
    private ProgressDialog progress;

    private Context context;

    private LoginResponseAgent loginResponseAgent;
    private String agentId;
    private String mobileNumber;
    private String nid;
    private String amount;
    private String currency;
    private String token;

    private ApiInterface apiInterface;
    private DepositRequest depositRequest = new DepositRequest();
    private DepositResponse depositResponse = new DepositResponse();


    DecimalFormat df = new DecimalFormat("#.##");

    public void setTextViews(TextView[] textViews) {
        this.textViews = textViews;
    }

    public AgentDepositFragment() {
        // Required empty public constructor
    }

    public void setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_agent_deposit, container, false);

        context = getActivity();
        loginResponseAgent = ((AgentMainActivity)getActivity()).getLoginResponseAgent();
        terminalPreferences = new TerminalPreferences(context);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        agentId = loginResponseAgent.getAgent().getId();
        token = "Bearer " + loginResponseAgent.getToken();

        initializeViews(rootView);
        setupCurrencySpinner();

        return rootView;
    }

    private void setupCurrencySpinner() {
        String currencyString = AppManager.loadJSONFromAsset(context, "currency");
        Currency currency = new Gson().fromJson(currencyString, Currency.class);
        ArrayAdapter<String> adapter;
        List<String> currencyTypes = new ArrayList<>();
        //Log.e("CurrencyActivity", "CcyNtry size: " + currency.getCcyNtry().size());

        currencyTypes.add("Select Currency");

        for (CurrencyData currencyData : currency.getCcyNtry()) {
            String result = currencyData.getCcy();
            //Log.e("CurrencyActivity", "Ccy: " + result);
            currencyTypes.add(result);
            //Log.e("CurrencyActivity", "currencyTypes size: " + currencyTypes.size());
        }
        adapter = new ArrayAdapter<>(context, R.layout.spinner_item, currencyTypes);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        currencySpinner.setAdapter(adapter);
        currencySpinner.setSelection(1);
        currencySpinner.setEnabled(false);
    }

    private void initializeViews(View rootView) {
        etRechargePhoneNumber = rootView.findViewById(R.id.edit_text_recharge_phone);
        etRechargeNid = rootView.findViewById(R.id.edit_text_recharge_nid);
        etRechargeAmount = rootView.findViewById(R.id.edit_text_recharge_amount);
        currencySpinner = rootView.findViewById(R.id.spinner_currency);
        btnSubmitRecharge = rootView.findViewById(R.id.btn_submit_recharge);
        btnSubmitRecharge.setOnClickListener(this);
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
            case R.id.btn_submit_recharge:
                hideKeyboard(view);

                mobileNumber = "88" + etRechargePhoneNumber.getText().toString().trim();
                nid = etRechargeNid.getText().toString().trim();
                amount = etRechargeAmount.getText().toString().trim();
                currency = currencySpinner.getSelectedItem().toString().trim();
                if (TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(getActivity(), getString(R.string.please_enter_mobile_number), Toast.LENGTH_SHORT).show();
                    etRechargePhoneNumber.requestFocus();
                } else if (TextUtils.isEmpty(nid)) {
                    Toast.makeText(getActivity(), getString(R.string.please_enter_nid_number), Toast.LENGTH_SHORT).show();
                    etRechargeNid.requestFocus();
                } else if (TextUtils.isEmpty(amount)) {
                    Toast.makeText(getActivity(), getString(R.string.please_enter_amount), Toast.LENGTH_SHORT).show();
                    etRechargeAmount.requestFocus();
                } else if (currencySpinner.getSelectedItemPosition() < 1) {
                    Toast.makeText(getActivity(), getString(R.string.please_select_currency), Toast.LENGTH_SHORT).show();
                    etRechargeAmount.requestFocus();
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        showProgressDialog();
                        deposit();
                    } else {
                        alertDialog.showDialog(context.getResources().getString(R.string.no_internet_connection));
                    }
                }
                break;
            default:
                break;
        }
    }

    private void deposit() {
        setRechargeRequestValues();

        Call<DepositResponse> rechargeResponseCall = apiInterface.depositUser(token, depositRequest);
        rechargeResponseCall.enqueue(new Callback<DepositResponse>() {
            @Override
            public void onResponse(Call<DepositResponse> call, Response<DepositResponse> response) {
                hideProgressDialog();
                depositResponse = response.body();

                if (depositResponse.getStatus().equals(Constants.SUCCESS)) {
                    //Toast.makeText(context, "Recharge successful!", Toast.LENGTH_SHORT).show();
                    updateAgentBalance();

                    goToSuccessFragment();
                } else {
                    alertDialog.showDialog(depositResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<DepositResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, t.getMessage());
            }
        });
    }

    private void goToSuccessFragment() {
        SuccessFragment successFragment = new SuccessFragment();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.ACTION_TYPE, Constants.AGENT_DEPOSIT);
        bundle.putString(Constants.MOBILE_NUMBER, mobileNumber);
        bundle.putString(Constants.AMOUNT, amount);
        successFragment.setArguments(bundle);

        successFragment.setTextViews(textViews);
        successFragment.setFragmentManager(fragmentManager);

        fragmentManager.beginTransaction()
                .replace(R.id.agent_detail_container, successFragment)
                .commit();
    }

    private void updateAgentBalance() {
        terminalPreferences.putAgentTotalBalance(df.format(depositResponse.getBalance()));
        terminalPreferences.putAgentTotalCommission(df.format(depositResponse.getCommission().getTotal().getAmount()));
        terminalPreferences.putAgentTodayCommission(df.format(depositResponse.getCommission().getToday().getAmount()));
    }

    private void setRechargeRequestValues() {
        depositRequest.setAgentId(agentId);
        depositRequest.setMobileNumber(mobileNumber);
        depositRequest.setNid(nid);
        depositRequest.setAmount(amount);
        depositRequest.setCurrency(currency);
    }

    private void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
