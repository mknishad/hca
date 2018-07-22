package com.telvo.telvoterminaladmin.agent;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.model.agent.history.AgentHistory;
import com.telvo.telvoterminaladmin.model.agent.history.AgentHistoryResponse;
import com.telvo.telvoterminaladmin.model.agent.history.Deposit;
import com.telvo.telvoterminaladmin.model.agent.history.OwnDeposit;
import com.telvo.telvoterminaladmin.model.agent.history.OwnWithdraw;
import com.telvo.telvoterminaladmin.model.agent.history.Withdraw;
import com.telvo.telvoterminaladmin.model.agent.login.LoginResponseAgent;
import com.telvo.telvoterminaladmin.networking.ApiClient;
import com.telvo.telvoterminaladmin.networking.ApiInterface;
import com.telvo.telvoterminaladmin.sharedpreference.TerminalPreferences;
import com.telvo.telvoterminaladmin.util.AppManager;
import com.telvo.telvoterminaladmin.util.Constants;
import com.telvo.telvoterminaladmin.util.CustomAlertDialog;
import com.telvo.telvoterminaladmin.util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgentHomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AgentHomeFragment";

    private Intent intent;
    private Context context;
    private TerminalPreferences terminalPreferences;

    private TextView totalBalanceTextView;
    private TextView totalCommissionTextView;
    private TextView todayCommissionTextView;
    private static EditText startDateEditText;
    private static EditText endDateEditText;
    private Button submitButton;
    private RecyclerView historyRecyclerView;
    private FragmentManager fragmentManager;
    private AgentHistoryAdapter agentHistoryAdapter;
    private CustomAlertDialog alertDialog;
    private ProgressDialog progress;
    private TextView[] textViews = new TextView[4];

    private ApiInterface apiInterface;
    private List<AgentHistory> agentHistories = new ArrayList<>();
    private LoginResponseAgent loginResponseAgent;
    private AgentHistoryResponse agentHistoryResponse = new AgentHistoryResponse();
    List<Deposit> deposits = new ArrayList<>();
    List<Withdraw> withdraws = new ArrayList<>();
    List<OwnDeposit> ownDeposits = new ArrayList<>();
    List<OwnWithdraw> ownWithdraws = new ArrayList<>();

    private String token;
    private String start;
    private String end;

    public AgentHomeFragment() {
        // Required empty public constructor
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setTextViews(TextView[] textViews) {
        this.textViews = textViews;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_agent_home, container, false);

        context = getActivity();

        initializeViews(rootView);

        loginResponseAgent = ((AgentMainActivity) getActivity()).getLoginResponseAgent();
        terminalPreferences = new TerminalPreferences(context);
        token = "Bearer " + loginResponseAgent.getToken();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setAmountViews();
        getDefaultAgentHistories();
        super.onViewCreated(view, savedInstanceState);
    }

    private void getDefaultAgentHistories() {
        if (AppManager.hasInternetConnection(context)) {
            showProgressDialog();
            requestForHistories();
        } else {
            alertDialog.showDialog(getString(R.string.no_internet_connection));
        }
    }

    private void requestForHistories() {
        Call<AgentHistoryResponse> historyResponseCall = apiInterface.getDefaultAgentHistory(token);
        historyResponseCall.enqueue(new Callback<AgentHistoryResponse>() {
            @Override
            public void onResponse(Call<AgentHistoryResponse> call, Response<AgentHistoryResponse> response) {
                hideProgressDialog();
                agentHistoryResponse = response.body();

                if (agentHistoryResponse.getState().equals(Constants.SUCCESS)) {
                    //Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                    deposits = agentHistoryResponse.getTransactions().getDeposits();
                    withdraws = agentHistoryResponse.getTransactions().getWithdraws();
                    ownDeposits = agentHistoryResponse.getTransactions().getOwnDeposits();
                    ownWithdraws = agentHistoryResponse.getTransactions().getOwnWithdraws();
                    try {
                        sortTransactions();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    setupRecyclerView();
                } else {
                    alertDialog.showDialog(agentHistoryResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AgentHistoryResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private void sortTransactions() throws ParseException {
        agentHistories.clear();
        AgentHistory agentHistory;
        for (Deposit deposit : deposits) {
            agentHistory = new AgentHistory(new SimpleDateFormat("dd MMM yyyy, hh:mm aa").parse(DateUtils.getFormattedDate(deposit.getCreatedAt())), deposit.getAmount(), deposit.getUser().getMobileNumber(), "User Deposit");
            agentHistories.add(agentHistory);
        }

        for (Withdraw withdraw : withdraws) {
            agentHistory = new AgentHistory(new SimpleDateFormat("dd MMM yyyy, hh:mm aa").parse(DateUtils.getFormattedDate(withdraw.getCreatedAt())), withdraw.getAmount(), withdraw.getUser().getMobileNumber(), "User Withdraw");
            agentHistories.add(agentHistory);
        }

        for (OwnDeposit ownDeposit : ownDeposits) {
            agentHistory = new AgentHistory(new SimpleDateFormat("dd MMM yyyy, hh:mm aa").parse(DateUtils.getFormattedDate(ownDeposit.getCreatedAt())), ownDeposit.getAmount(), ownDeposit.getAdmin().getMobileNumber(), "Own Deposit");
            agentHistories.add(agentHistory);
        }

        for (OwnWithdraw ownWithdraw : ownWithdraws) {
            agentHistory = new AgentHistory(new SimpleDateFormat("dd MMM yyyy, hh:mm aa").parse(DateUtils.getFormattedDate(ownWithdraw.getCreatedAt())), ownWithdraw.getAmount(), ownWithdraw.getAdmin().getMobileNumber(), "Own Withdraw");
            agentHistories.add(agentHistory);
        }

        Collections.sort(agentHistories);
        Collections.reverse(agentHistories);
    }

    private void showProgressDialog() {
        progress.setMessage(getString(R.string.please_wait));
        progress.show();
    }

    private void hideProgressDialog() {
        progress.dismiss();
    }

    private void setAmountViews() {
        totalBalanceTextView.setText(terminalPreferences.getAgentTotalBalance());
        totalCommissionTextView.setText(terminalPreferences.getAgentTotalCommission());
        todayCommissionTextView.setText(terminalPreferences.getAgentTodayCommission());
    }

    private void setupRecyclerView() {
        agentHistoryAdapter = new AgentHistoryAdapter(context, agentHistories);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        historyRecyclerView.setLayoutManager(layoutManager);
        historyRecyclerView.setAdapter(agentHistoryAdapter);
    }

    private void initializeViews(View rootView) {
        totalBalanceTextView = rootView.findViewById(R.id.totalBalanceTextView);
        totalCommissionTextView = rootView.findViewById(R.id.totalCommissionTextView);
        todayCommissionTextView = rootView.findViewById(R.id.todayCommissionTextView);
        startDateEditText = rootView.findViewById(R.id.startDateEditText);
        startDateEditText.setOnClickListener(this);
        endDateEditText = rootView.findViewById(R.id.endDateEditText);
        endDateEditText.setOnClickListener(this);
        submitButton = rootView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        historyRecyclerView = rootView.findViewById(R.id.recycler_view_history);
        alertDialog = new CustomAlertDialog(context);
        progress = new ProgressDialog(context);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.startDateEditText:
                showStartDatePickerDialog();
                break;
            case R.id.endDateEditText:
                showEndDatePickerDialog();
                break;
            case R.id.submitButton:
                hideKeyboard(view);

                start = startDateEditText.getText().toString().trim();
                end = endDateEditText.getText().toString().trim();

                if (TextUtils.isEmpty(start)) {
                    Toast.makeText(context, getString(R.string.please_select_start_date), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(end)) {
                    Toast.makeText(context, getString(R.string.please_select_end_date), Toast.LENGTH_SHORT).show();
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        showProgressDialog();

                        getPreciseAgentHistories(start, end);
                    } else {
                        alertDialog.showDialog(getString(R.string.no_internet_connection));
                    }
                }
                //showStartDatePickerDialog();
                break;
            default:
        }
    }

    private void getPreciseAgentHistories(String start, String end) {
        Call<AgentHistoryResponse> responseCall = apiInterface.getPreciseAgentHistory(token, start, end);
        responseCall.enqueue(new Callback<AgentHistoryResponse>() {
            @Override
            public void onResponse(Call<AgentHistoryResponse> call, Response<AgentHistoryResponse> response) {
                hideProgressDialog();
                agentHistoryResponse = response.body();

                if (agentHistoryResponse.getState().equals(Constants.SUCCESS)) {
                    //Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                    deposits = agentHistoryResponse.getTransactions().getDeposits();
                    withdraws = agentHistoryResponse.getTransactions().getWithdraws();

                    Log.e(TAG, "depositUser size: " + deposits.size());
                    Log.e(TAG, "withdrawUser size: " + withdraws.size());

                    try {
                        sortTransactions();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    setupRecyclerView();
                } else {
                    alertDialog.showDialog(agentHistoryResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AgentHistoryResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private void showStartDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(fragmentManager, "startDatePicker");
        Log.e(TAG, "showStartDatePickerDialog");
    }

    private void showEndDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(fragmentManager, "endDatePicker");
        Log.e(TAG, "showEndDatePickerDialog");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            if (getTag().equals("startDatePicker")) {
                startDateEditText.setText(year + "-" + (month+1) + "-" + day);
            } else if (getTag().equals("endDatePicker")) {
                endDateEditText.setText(year + "-" + (month+1) + "-" + day);
            }
        }
    }

    private void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
