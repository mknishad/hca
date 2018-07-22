package com.telvo.telvoterminaladmin.admin.history;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.admin.AdminMainActivity;
import com.telvo.telvoterminaladmin.model.admin.history.deposit.AdminDepositHistoryResponse;
import com.telvo.telvoterminaladmin.model.admin.history.deposit.AgentDeposit;
import com.telvo.telvoterminaladmin.model.admin.login.LoginResponseAdmin;
import com.telvo.telvoterminaladmin.networking.ApiClient;
import com.telvo.telvoterminaladmin.networking.ApiInterface;
import com.telvo.telvoterminaladmin.util.AppManager;
import com.telvo.telvoterminaladmin.util.Constants;
import com.telvo.telvoterminaladmin.util.CustomAlertDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminDepositHistoryFragment extends Fragment {

    private static final String TAG = "DepositHistoryFragment";

    private static Context context;

    private static RecyclerView depositHistoryRecyclerView;
    private static ProgressDialog progress;
    private static CustomAlertDialog alertDialog;
    private static AdminDepositHistoryAdapter adminDepositHistoryAdapter;

    private LoginResponseAdmin loginResponseAdmin;
    private static ApiInterface apiInterface;
    private static AdminDepositHistoryResponse adminDepositHistoryResponse = new AdminDepositHistoryResponse();
    private static List<AgentDeposit> agentDeposits = new ArrayList<>();

    private static String token;

    public AdminDepositHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_deposit_history, container, false);

        context = getActivity();
        initializeViews(rootView);

        loginResponseAdmin = ((AdminMainActivity) getActivity()).getLoginResponseAdmin();
        token = "Bearer " + loginResponseAdmin.getToken();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        return rootView;
    }

    public static void getPreciseDepositHistory(String start, String end) {
        if (AppManager.hasInternetConnection(context)) {
            showProgressDialog();
            requestForPreciseDepositHistory(start, end);
        } else {
            alertDialog.showDialog("No internet connection!");
        }
    }

    private static void requestForPreciseDepositHistory(String start, String end) {
        Call<AdminDepositHistoryResponse> responseCall = apiInterface.getPreciseAdminDepositHistory(token, start, end);
        responseCall.enqueue(new Callback<AdminDepositHistoryResponse>() {
            @Override
            public void onResponse(Call<AdminDepositHistoryResponse> call, Response<AdminDepositHistoryResponse> response) {
                hideProgressDialog();
                adminDepositHistoryResponse = response.body();

                if (adminDepositHistoryResponse.getStatus().equals(Constants.SUCCESS)) {
                    agentDeposits = adminDepositHistoryResponse.getAgentDeposits();
                    setupRecyclerView();
                } else {
                    alertDialog.showDialog(adminDepositHistoryResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AdminDepositHistoryResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog("Unable to connect to the internet!");
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getDefaultDepositHistory() {
        if (AppManager.hasInternetConnection(context)) {
            showProgressDialog();
            requestForDepositHistory();
        } else {
            alertDialog.showDialog(getString(R.string.no_internet_connection));
        }
    }

    private void requestForDepositHistory() {
        Call<AdminDepositHistoryResponse> responseCall = apiInterface.getDefaultAdminDepositHistory(token);
        responseCall.enqueue(new Callback<AdminDepositHistoryResponse>() {
            @Override
            public void onResponse(Call<AdminDepositHistoryResponse> call, Response<AdminDepositHistoryResponse> response) {
                hideProgressDialog();

                adminDepositHistoryResponse = response.body();

                if (adminDepositHistoryResponse.getStatus().equals(Constants.SUCCESS)) {
                    agentDeposits = adminDepositHistoryResponse.getAgentDeposits();
                    setupRecyclerView();
                } else {
                    alertDialog.showDialog(adminDepositHistoryResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AdminDepositHistoryResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private static void setupRecyclerView() {
        Collections.reverse(agentDeposits);
        adminDepositHistoryAdapter = new AdminDepositHistoryAdapter(context, agentDeposits);
        depositHistoryRecyclerView.setAdapter(adminDepositHistoryAdapter);
    }

    private void initializeViews(View view) {
        progress = new ProgressDialog(context);
        alertDialog = new CustomAlertDialog(context);

        depositHistoryRecyclerView = view.findViewById(R.id.depositHistoryRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        depositHistoryRecyclerView.setLayoutManager(layoutManager);
        depositHistoryRecyclerView.setHasFixedSize(true);

        // Add item divider to recycler view
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        depositHistoryRecyclerView.addItemDecoration(itemDecoration);
    }

    private static void showProgressDialog() {
        progress.setMessage("Please wait...");
        progress.show();
    }

    private static void hideProgressDialog() {
        progress.dismiss();
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            onResume();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }

        //Add your code this section
        getDefaultDepositHistory();
    }

}
