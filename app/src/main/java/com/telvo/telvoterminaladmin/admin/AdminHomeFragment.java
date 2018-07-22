package com.telvo.telvoterminaladmin.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.admin.history.AdminHomeWithdrawAdapter;
import com.telvo.telvoterminaladmin.model.admin.history.home.HomeWithdraw;
import com.telvo.telvoterminaladmin.model.admin.history.home.HomeWithdrawHistoryResponse;
import com.telvo.telvoterminaladmin.model.admin.login.LoginResponseAdmin;
import com.telvo.telvoterminaladmin.networking.ApiClient;
import com.telvo.telvoterminaladmin.networking.ApiInterface;
import com.telvo.telvoterminaladmin.sharedpreference.TerminalPreferences;
import com.telvo.telvoterminaladmin.util.AppManager;
import com.telvo.telvoterminaladmin.util.Constants;
import com.telvo.telvoterminaladmin.util.CustomAlertDialog;
import com.telvo.telvoterminaladmin.util.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminHomeFragment extends Fragment {

    private static final String TAG = "AdminHomeFragment";

    private Context context;
    private TerminalPreferences terminalPreferences;

    private TextView adminBalanceTextView;
    private RecyclerView pendingRecyclerView;
    private FragmentManager fragmentManager;
    private AdminHomeWithdrawAdapter adminHomeWithdrawAdapter;
    private ProgressDialog progress;
    private CustomAlertDialog alertDialog;
    private TextView[] textViews = new TextView[7];

    private LoginResponseAdmin loginResponseAdmin;
    private ApiInterface apiInterface;
    private HomeWithdrawHistoryResponse homeWithdrawHistoryResponse = new HomeWithdrawHistoryResponse();
    private List<HomeWithdraw> homeWithdraws = new ArrayList<>();

    private String token;

    public AdminHomeFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_admin_home, container, false);

        context = getActivity();

        initializeViews(rootView);

        loginResponseAdmin = ((AdminMainActivity) getActivity()).getLoginResponseAdmin();
        terminalPreferences = new TerminalPreferences(context);
        token = "Bearer " + loginResponseAdmin.getToken();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setAmountViews();
        getPendingWithdraws();
        super.onViewCreated(view, savedInstanceState);
    }

    private void getPendingWithdraws() {
        if (AppManager.hasInternetConnection(context)) {
            showProgressDialog();
            requestForPendingWithdraws();
        } else {
            alertDialog.showDialog(getString(R.string.no_internet_connection));
        }
    }

    private void requestForPendingWithdraws() {
        Call<HomeWithdrawHistoryResponse> withdrawResponseCall = apiInterface.getHomeWithdraws(token, "pending");
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

    private void setupRecyclerView() {
        adminHomeWithdrawAdapter = new AdminHomeWithdrawAdapter(context, homeWithdraws);
        pendingRecyclerView.setAdapter(adminHomeWithdrawAdapter);
    }

    private void setAmountViews() {
        adminBalanceTextView.setText(terminalPreferences.getAdminBalance());
    }

    private void initializeViews(View view) {
        adminBalanceTextView = view.findViewById(R.id.adminBalanceTextView);
        progress = new ProgressDialog(context);
        alertDialog = new CustomAlertDialog(context);

        pendingRecyclerView = view.findViewById(R.id.pendingRecyclerView);
        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        pendingRecyclerView.setHasFixedSize(true);

        // Add item divider to recycler view
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        pendingRecyclerView.addItemDecoration(itemDecoration);

        // Set on item click listener
        ItemClickSupport.addTo(pendingRecyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        HomeWithdraw homeWithdraw = homeWithdraws.get(position);
                        RequestDetailsFragment requestDetailsFragment = new RequestDetailsFragment();
                        requestDetailsFragment.setHomeWithdraw(homeWithdraw);
                        requestDetailsFragment.setTextViews(textViews);
                        requestDetailsFragment.setFragmentManager(fragmentManager);
                        fragmentManager.beginTransaction()
                                .replace(R.id.admin_detail_container, requestDetailsFragment)
                                .commit();
                    }
                }
        );
    }

    private void showProgressDialog() {
        progress.setMessage(getString(R.string.please_wait));
        progress.show();
    }

    private void hideProgressDialog() {
        progress.dismiss();
    }
}
