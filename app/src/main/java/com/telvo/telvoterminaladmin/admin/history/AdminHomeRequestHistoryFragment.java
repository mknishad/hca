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
import com.telvo.telvoterminaladmin.model.admin.history.home.HomeWithdraw;
import com.telvo.telvoterminaladmin.model.admin.history.home.HomeWithdrawHistoryResponse;
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
public class AdminHomeRequestHistoryFragment extends Fragment {

    private static final String TAG = "HomeHistoryFragment";

    private static Context context;

    private static RecyclerView homeRequestHistoryRecyclerView;
    private static ProgressDialog progress;
    private static CustomAlertDialog alertDialog;
    private static AdminHomeWithdrawAdapter adminHomeWithdrawAdapter;

    private LoginResponseAdmin loginResponseAdmin;
    private static ApiInterface apiInterface;
    private static HomeWithdrawHistoryResponse homeWithdrawHistoryResponse = new HomeWithdrawHistoryResponse();
    private static List<HomeWithdraw> homeWithdraws = new ArrayList<>();

    private static String token;

    public AdminHomeRequestHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_home_request_history, container, false);

        context = getActivity();
        initializeViews(rootView);

        loginResponseAdmin = ((AdminMainActivity) getActivity()).getLoginResponseAdmin();
        token = "Bearer " + loginResponseAdmin.getToken();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        return rootView;
    }

    public static void getPreciseHomeWithdraws(String start, String end) {
        if (AppManager.hasInternetConnection(context)) {
            showProgressDialog();
            requestForPreciseHomeWithdraws(start, end);
        } else {
            alertDialog.showDialog("No internet connection!");
        }
    }

    private static void requestForPreciseHomeWithdraws(String start, String end) {
        Call<HomeWithdrawHistoryResponse> responseCall = apiInterface.getPreciseHomeWithdraws(token, "done", start, end);
        responseCall.enqueue(new Callback<HomeWithdrawHistoryResponse>() {
            @Override
            public void onResponse(Call<HomeWithdrawHistoryResponse> call, Response<HomeWithdrawHistoryResponse> response) {
                hideProgressDialog();
                homeWithdrawHistoryResponse = response.body();

                if (homeWithdrawHistoryResponse.getStatus().equals(Constants.SUCCESS)) {
                    homeWithdraws = homeWithdrawHistoryResponse.getHomeWithdraws();
                    setupRecyclerView();
                } else {
                    alertDialog.showDialog(homeWithdrawHistoryResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<HomeWithdrawHistoryResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog("Unable to connect to the server!");
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getDeliveredHomeWithdraws() {
        if (AppManager.hasInternetConnection(context)) {
            showProgressDialog();
            requestForDeliveredHomeWithdraws();
        } else {
            alertDialog.showDialog(getString(R.string.no_internet_connection));
        }
    }

    private void requestForDeliveredHomeWithdraws() {
        Call<HomeWithdrawHistoryResponse> withdrawResponseCall = apiInterface.getHomeWithdraws(token, "done");
        withdrawResponseCall.enqueue(new Callback<HomeWithdrawHistoryResponse>() {
            @Override
            public void onResponse(Call<HomeWithdrawHistoryResponse> call, Response<HomeWithdrawHistoryResponse> response) {
                hideProgressDialog();
                homeWithdrawHistoryResponse = response.body();

                if (homeWithdrawHistoryResponse.getStatus().equals(Constants.SUCCESS)) {
                    homeWithdraws = homeWithdrawHistoryResponse.getHomeWithdraws();
                    setupRecyclerView();
                } else {
                    alertDialog.showDialog(homeWithdrawHistoryResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<HomeWithdrawHistoryResponse> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private static void setupRecyclerView() {
        Collections.reverse(homeWithdraws);
        adminHomeWithdrawAdapter = new AdminHomeWithdrawAdapter(context, homeWithdraws);
        homeRequestHistoryRecyclerView.setAdapter(adminHomeWithdrawAdapter);
    }

    private void initializeViews(View view) {
        progress = new ProgressDialog(context);
        alertDialog = new CustomAlertDialog(context);

        homeRequestHistoryRecyclerView = view.findViewById(R.id.homeRequestHistoryRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        homeRequestHistoryRecyclerView.setLayoutManager(layoutManager);
        homeRequestHistoryRecyclerView.setHasFixedSize(true);

        // Add item divider to recycler view
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        homeRequestHistoryRecyclerView.addItemDecoration(itemDecoration);
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
        getDeliveredHomeWithdraws();
    }

}
