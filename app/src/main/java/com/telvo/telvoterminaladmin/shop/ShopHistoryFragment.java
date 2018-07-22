package com.telvo.telvoterminaladmin.shop;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.telvo.telvoterminaladmin.model.shop.history.Payment;
import com.telvo.telvoterminaladmin.model.shop.history.ShopHistory;
import com.telvo.telvoterminaladmin.model.shop.history.ShopHistoryResponse;
import com.telvo.telvoterminaladmin.model.shop.login.LoginResponseShop;
import com.telvo.telvoterminaladmin.networking.ApiClient;
import com.telvo.telvoterminaladmin.networking.ApiInterface;
import com.telvo.telvoterminaladmin.sharedpreference.TerminalPreferences;
import com.telvo.telvoterminaladmin.util.AppManager;
import com.telvo.telvoterminaladmin.util.Constants;
import com.telvo.telvoterminaladmin.util.CustomAlertDialog;
import com.telvo.telvoterminaladmin.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopHistoryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ShopHistoryFragment";

    private Context context;
    private TerminalPreferences terminalPreferences;

    private TextView shopTotalBalanceTextView;
    private static EditText startDateEditText;
    private static EditText endDateEditText;
    private Button submitButton;
    private RecyclerView shopHistoryRecyclerView;
    private FragmentManager fragmentManager;
    private ShopHistoryAdapter shopHistoryAdapter;
    private CustomAlertDialog alertDialog;
    private ProgressDialog progress;

    private ApiInterface apiInterface;
    private List<Payment> payments = new ArrayList<>();
    private List<ShopHistory> shopHistories = new ArrayList<>();
    private LoginResponseShop loginResponseShop;
    private ShopHistoryResponse shopHistoryResponse = new ShopHistoryResponse();

    private String token;
    private String start;
    private String end;

    public ShopHistoryFragment() {
        // Required empty public constructor
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shop_history, container, false);

        context = getActivity();

        loginResponseShop = ((ShopMainActivity) getActivity()).getLoginResponseShop();
        terminalPreferences = new TerminalPreferences(context);
        token = "Bearer " + loginResponseShop.getToken();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        initializeViews(rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setAmountView();
        getDefaultShopHistory();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setAmountView() {
        shopTotalBalanceTextView.setText(terminalPreferences.getShopTotalBalance());
    }

    private void getDefaultShopHistory() {
        if (AppManager.hasInternetConnection(context)) {
            showProgressDialog();
            requestForDefaultShopHistory();
        } else {
            alertDialog.showDialog(getString(R.string.no_internet_connection));
        }
    }

    private void requestForDefaultShopHistory() {
        Call<ShopHistoryResponse> shopHistoryResponseCall = apiInterface.getDefaultShopHistory(token);
        shopHistoryResponseCall.enqueue(new Callback<ShopHistoryResponse>() {
            @Override
            public void onResponse(Call<ShopHistoryResponse> call, Response<ShopHistoryResponse> response) {
                hideProgressDialog();
                shopHistoryResponse = response.body();

                if (shopHistoryResponse.getStatus().equals(Constants.SUCCESS)) {
                    payments = shopHistoryResponse.getPayments();
                    sortPayments();
                    setupRecyclerView();
                } else {
                    alertDialog.showDialog(shopHistoryResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ShopHistoryResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private void setupRecyclerView() {
        shopHistoryAdapter = new ShopHistoryAdapter(context, shopHistories);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        shopHistoryRecyclerView.setLayoutManager(layoutManager);
        shopHistoryRecyclerView.setAdapter(shopHistoryAdapter);
    }

    private void sortPayments() {
        shopHistories.clear();
        ShopHistory shopHistory;
        for (Payment payment : payments) {
            shopHistory = new ShopHistory(new SimpleDateFormat("dd MMM yyyy, hh:mm aa").format(new Date(DateUtils.getFormattedDate(payment.getCreatedAt()))), payment.getAmount(), payment.getUser().getMobileNumber());
            shopHistories.add(shopHistory);
        }

        Collections.reverse(shopHistories);
    }

    private void initializeViews(View view) {
        shopTotalBalanceTextView = view.findViewById(R.id.shopTotalBalanceTextView);
        startDateEditText = view.findViewById(R.id.startDateEditText);
        startDateEditText.setOnClickListener(this);
        endDateEditText = view.findViewById(R.id.endDateEditText);
        endDateEditText.setOnClickListener(this);
        submitButton = view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        shopHistoryRecyclerView = view.findViewById(R.id.shopHistoryRecyclerView);
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
                break;
            default:
        }
    }

    private void getPreciseAgentHistories(String start, String end) {
        Call<ShopHistoryResponse> responseCall = apiInterface.getPreciseShopHistory(token, start, end);
        responseCall.enqueue(new Callback<ShopHistoryResponse>() {
            @Override
            public void onResponse(Call<ShopHistoryResponse> call, Response<ShopHistoryResponse> response) {
                hideProgressDialog();
                shopHistoryResponse = response.body();

                if (shopHistoryResponse.getStatus().equals(Constants.SUCCESS)) {
                    payments = shopHistoryResponse.getPayments();

                    sortPayments();
                    setupRecyclerView();
                } else {
                    alertDialog.showDialog(shopHistoryResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ShopHistoryResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    public void showStartDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(fragmentManager, "startDatePicker");
        Log.e(TAG, "showStartDatePickerDialog");
    }

    public void showEndDatePickerDialog() {
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
