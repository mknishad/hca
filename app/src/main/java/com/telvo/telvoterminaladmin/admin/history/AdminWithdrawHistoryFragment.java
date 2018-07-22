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
import com.telvo.telvoterminaladmin.agent.AgentHistoryAdapter;
import com.telvo.telvoterminaladmin.model.admin.history.withdraw.AdminWithdrawHistoryResponse;
import com.telvo.telvoterminaladmin.model.admin.history.withdraw.AgentWithdraw;
import com.telvo.telvoterminaladmin.model.admin.history.withdraw.ShopWithdraw;
import com.telvo.telvoterminaladmin.model.admin.login.LoginResponseAdmin;
import com.telvo.telvoterminaladmin.model.agent.history.AgentHistory;
import com.telvo.telvoterminaladmin.networking.ApiClient;
import com.telvo.telvoterminaladmin.networking.ApiInterface;
import com.telvo.telvoterminaladmin.util.AppManager;
import com.telvo.telvoterminaladmin.util.Constants;
import com.telvo.telvoterminaladmin.util.CustomAlertDialog;
import com.telvo.telvoterminaladmin.util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminWithdrawHistoryFragment extends Fragment {

    private static final String TAG = "WithdrawHistoryFragment";

    private static Context context;

    private static RecyclerView withdrawHistoryRecyclerView;
    // AgentHistoryAdapter can be used because it's same as we will be using here
    private static AgentHistoryAdapter agentHistoryAdapter;
    private static CustomAlertDialog alertDialog;
    private static ProgressDialog progress;

    private static ApiInterface apiInterface;
    // AgentHistory is the same model that we will be using here
    private static List<AgentHistory> agentHistories = new ArrayList<>();
    private LoginResponseAdmin loginResponseAdmin;
    private static AdminWithdrawHistoryResponse adminWithdrawHistoryResponse = new AdminWithdrawHistoryResponse();
    private static List<AgentWithdraw> agentWithdraws = new ArrayList<>();
    private static List<ShopWithdraw> shopWithdraws = new ArrayList<>();

    private static String token;

    public AdminWithdrawHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_withdraw_history, container, false);

        context = getActivity();
        initializeViews(rootView);

        loginResponseAdmin = ((AdminMainActivity) getActivity()).getLoginResponseAdmin();
        token = "Bearer " + loginResponseAdmin.getToken();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        return rootView;
    }

    public static void getPreciseWithdrawHistory(String start, String end) {
        if (AppManager.hasInternetConnection(context)) {
            showProgressDialog();
            requestForPreciseWithdrawHistory(start, end);
        } else {
            alertDialog.showDialog("No internet connection!");
        }
    }

    private static void requestForPreciseWithdrawHistory(String start, String end) {
        Call<AdminWithdrawHistoryResponse> responseCall = apiInterface.getPreciseAdminWithdrawHistory(token, start, end);
        responseCall.enqueue(new Callback<AdminWithdrawHistoryResponse>() {
            @Override
            public void onResponse(Call<AdminWithdrawHistoryResponse> call, Response<AdminWithdrawHistoryResponse> response) {
                hideProgressDialog();
                adminWithdrawHistoryResponse = response.body();

                if (adminWithdrawHistoryResponse.getStatus().equals(Constants.SUCCESS)) {
                    agentWithdraws = adminWithdrawHistoryResponse.getAgentWithdraws();
                    shopWithdraws = adminWithdrawHistoryResponse.getShopWithdraws();
                    try {
                        sortTransactions();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    setupRecyclerView();
                } else {
                    alertDialog.showDialog(adminWithdrawHistoryResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AdminWithdrawHistoryResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog("Unable to connect to the internet!");
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getDefaultAdminWithdrawHistory() {
        if (AppManager.hasInternetConnection(context)) {
            showProgressDialog();
            requestForWithdrawHistory();
        } else {
            alertDialog.showDialog(getString(R.string.no_internet_connection));
        }
    }

    private void requestForWithdrawHistory() {
        Call<AdminWithdrawHistoryResponse> responseCall = apiInterface.getDefaultAdminWithdrawHistory(token);
        responseCall.enqueue(new Callback<AdminWithdrawHistoryResponse>() {
            @Override
            public void onResponse(Call<AdminWithdrawHistoryResponse> call, Response<AdminWithdrawHistoryResponse> response) {
                hideProgressDialog();
                adminWithdrawHistoryResponse = response.body();

                if (adminWithdrawHistoryResponse.getStatus().equals(Constants.SUCCESS)) {
                    agentWithdraws = adminWithdrawHistoryResponse.getAgentWithdraws();
                    shopWithdraws = adminWithdrawHistoryResponse.getShopWithdraws();
                    try {
                        sortTransactions();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    setupRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<AdminWithdrawHistoryResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private static void sortTransactions() throws ParseException {
        agentHistories.clear();
        AgentHistory agentHistory;
        for (AgentWithdraw agentWithdraw : agentWithdraws) {
            agentHistory = new AgentHistory(new SimpleDateFormat("dd MMM yyyy, hh:mm aa").parse(DateUtils.getFormattedDate(agentWithdraw.getCreatedAt())), agentWithdraw.getAmount(), agentWithdraw.getAgent().getMobileNumber(), "Agent");
            agentHistories.add(agentHistory);
        }

        for (ShopWithdraw shopWithdraw : shopWithdraws) {
            agentHistory = new AgentHistory(new SimpleDateFormat("dd MMM yyyy, hh:mm aa").parse(DateUtils.getFormattedDate(shopWithdraw.getCreatedAt())), shopWithdraw.getAmount(), shopWithdraw.getShop().getMobileNumber(), "Shop");
            agentHistories.add(agentHistory);
        }

        Collections.sort(agentHistories);
        Collections.reverse(agentHistories);
    }

    private static void setupRecyclerView() {
        agentHistoryAdapter = new AgentHistoryAdapter(context, agentHistories);
        withdrawHistoryRecyclerView.setAdapter(agentHistoryAdapter);
    }

    private void initializeViews(View view) {
        alertDialog = new CustomAlertDialog(context);
        progress = new ProgressDialog(context);

        withdrawHistoryRecyclerView = view.findViewById(R.id.withdrawHistoryRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        withdrawHistoryRecyclerView.setLayoutManager(layoutManager);
        withdrawHistoryRecyclerView.setHasFixedSize(true);

        // Add item divider to recycler view
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        withdrawHistoryRecyclerView.addItemDecoration(itemDecoration);
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
        getDefaultAdminWithdrawHistory();
    }
}
