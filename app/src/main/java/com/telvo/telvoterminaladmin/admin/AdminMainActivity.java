package com.telvo.telvoterminaladmin.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.admin.history.AdminHistoryFragment;
import com.telvo.telvoterminaladmin.auth.LoginActivity;
import com.telvo.telvoterminaladmin.baseactivity.BaseActivity;
import com.telvo.telvoterminaladmin.model.admin.login.LoginResponseAdmin;
import com.telvo.telvoterminaladmin.util.Constants;

public class AdminMainActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvMasterItemHome;
    private TextView tvMasterItemHomeWithdraw;
    private TextView tvMasterItemAgentDeposit;
    private TextView tvMasterItemAgentWithdraw;
    private TextView tvMasterItemShopWithdraw;
    private TextView tvMasterItemHistory;
    private TextView tvMasterItemCreateUser;
    private TextView[] textViews = new TextView[7];
    private FragmentManager fragmentManager;

    private LoginResponseAdmin loginResponseAdmin;
    private boolean inHome = true;

    public LoginResponseAdmin getLoginResponseAdmin() {
        return loginResponseAdmin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        setToolbarWithoutBack(R.id.toolbar_support_admin, getStringResources(R.string.app_name));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        intent = getIntent();
        loginResponseAdmin = (LoginResponseAdmin) intent.getSerializableExtra(Constants.ADMIN_LOGIN_RESPONSE);

        initializeViews();
        fragmentManager = getSupportFragmentManager();

        goToHomeFragment();
    }

    private void initializeViews() {
        tvMasterItemHome = getTextView(R.id.text_view_master_item_home);
        tvMasterItemHome.setOnClickListener(this);
        textViews[0] = tvMasterItemHome;
        tvMasterItemHomeWithdraw = getTextView(R.id.text_view_master_item_home_withdraw);
        tvMasterItemHomeWithdraw.setOnClickListener(this);
        textViews[1] = tvMasterItemHomeWithdraw;
        tvMasterItemAgentDeposit = getTextView(R.id.text_view_master_item_deposit_agent);
        tvMasterItemAgentDeposit.setOnClickListener(this);
        textViews[2] = tvMasterItemAgentDeposit;
        tvMasterItemAgentWithdraw = getTextView(R.id.text_view_master_item_withdraw_agent);
        tvMasterItemAgentWithdraw.setOnClickListener(this);
        textViews[3] = tvMasterItemAgentWithdraw;
        tvMasterItemShopWithdraw = getTextView(R.id.text_view_master_item_withdraw_shop);
        tvMasterItemShopWithdraw.setOnClickListener(this);
        textViews[4] = tvMasterItemShopWithdraw;
        tvMasterItemHistory = getTextView(R.id.text_view_master_item_history);
        tvMasterItemHistory.setOnClickListener(this);
        textViews[5] = tvMasterItemHistory;
        tvMasterItemCreateUser = getTextView(R.id.text_view_master_item_create_user);
        tvMasterItemCreateUser.setOnClickListener(this);
        textViews[6] = tvMasterItemCreateUser;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_master_item_home:
                goToHomeFragment();
                inHome = true;
                break;
            case R.id.text_view_master_item_home_withdraw:
                goToHomeWithdrawFragment();
                inHome = false;
                break;
            case R.id.text_view_master_item_deposit_agent:
                goToDepositAgentFragment();
                inHome = false;
                break;
            case R.id.text_view_master_item_withdraw_agent:
                goToWithdrawAgentFragment();
                inHome = false;
                break;
            case R.id.text_view_master_item_withdraw_shop:
                goToWithdrawShopFragment();
                inHome = false;
                break;
            case R.id.text_view_master_item_history:
                goToHistoryFragment();
                inHome = false;
                break;
            case R.id.text_view_master_item_create_user:
                goToCreateUserFragment();
                inHome = false;
                break;
            default:
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.log_out:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        intent = new Intent(AdminMainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToHomeFragment() {
        AdminHomeFragment adminHomeFragment = new AdminHomeFragment();
        adminHomeFragment.setTextViews(textViews);
        adminHomeFragment.setFragmentManager(fragmentManager);
        fragmentManager.beginTransaction()
                .replace(R.id.admin_detail_container, adminHomeFragment)
                .commit();

        changeMasterItemBackgroundForHome();
    }

    private void changeMasterItemBackgroundForHome() {
        tvMasterItemHome.setBackgroundResource(R.drawable.bg_white_master_item);
        tvMasterItemHome.setTextColor(getResources().getColor(R.color.colorText));
        tvMasterItemHomeWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHomeWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemAgentDeposit.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemAgentDeposit.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemAgentWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemAgentWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemShopWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemShopWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemHistory.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHistory.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemCreateUser.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemCreateUser.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void goToHomeWithdrawFragment() {
        AdminHomeWithdrawFragment adminVerificationFragment = new AdminHomeWithdrawFragment();
        adminVerificationFragment.setTextViews(textViews);
        adminVerificationFragment.setFragmentManager(fragmentManager);
        fragmentManager.beginTransaction()
                .replace(R.id.admin_detail_container, adminVerificationFragment)
                .commit();

        changeMasterItemBackgroundForHomeWithdraw();
    }

    private void changeMasterItemBackgroundForHomeWithdraw() {
        tvMasterItemHome.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHome.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemHomeWithdraw.setBackgroundResource(R.drawable.bg_white_master_item);
        tvMasterItemHomeWithdraw.setTextColor(getResources().getColor(R.color.colorText));
        tvMasterItemAgentDeposit.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemAgentDeposit.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemAgentWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemAgentWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemShopWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemShopWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemHistory.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHistory.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemCreateUser.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemCreateUser.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void goToDepositAgentFragment() {
        AdminDepositAgentFragment adminDepositAgentFragment = new AdminDepositAgentFragment();
        adminDepositAgentFragment.setTextViews(textViews);
        adminDepositAgentFragment.setFragmentManager(fragmentManager);
        fragmentManager.beginTransaction()
                .replace(R.id.admin_detail_container, adminDepositAgentFragment)
                .commit();

        changeMasterItemBackgroundForDepositAgent();
    }

    private void changeMasterItemBackgroundForDepositAgent() {
        tvMasterItemHome.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHome.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemHomeWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHomeWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemAgentDeposit.setBackgroundResource(R.drawable.bg_white_master_item);
        tvMasterItemAgentDeposit.setTextColor(getResources().getColor(R.color.colorText));
        tvMasterItemAgentWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemAgentWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemShopWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemShopWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemHistory.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHistory.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemCreateUser.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemCreateUser.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void goToWithdrawAgentFragment() {
        AdminWithdrawAgentFragment adminWithdrawAgentFragment = new AdminWithdrawAgentFragment();
        adminWithdrawAgentFragment.setTextViews(textViews);
        adminWithdrawAgentFragment.setFragmentManager(fragmentManager);
        fragmentManager.beginTransaction()
                .replace(R.id.admin_detail_container, adminWithdrawAgentFragment)
                .commit();

        changeMasterItemBackgroundForWithdrawAgent();
    }

    private void changeMasterItemBackgroundForWithdrawAgent() {
        tvMasterItemHome.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHome.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemHomeWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHomeWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemAgentDeposit.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemAgentDeposit.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemAgentWithdraw.setBackgroundResource(R.drawable.bg_white_master_item);
        tvMasterItemAgentWithdraw.setTextColor(getResources().getColor(R.color.colorText));
        tvMasterItemShopWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemShopWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemHistory.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHistory.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemCreateUser.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemCreateUser.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void goToWithdrawShopFragment() {
        AdminWithdrawShopFragment adminWithdrawShopFragment = new AdminWithdrawShopFragment();
        adminWithdrawShopFragment.setTextViews(textViews);
        adminWithdrawShopFragment.setFragmentManager(fragmentManager);
        fragmentManager.beginTransaction()
                .replace(R.id.admin_detail_container, adminWithdrawShopFragment)
                .commit();

        changeMasterItemBackgroundForWithdrawShop();
    }

    private void changeMasterItemBackgroundForWithdrawShop() {
        tvMasterItemHome.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHome.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemHomeWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHomeWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemAgentDeposit.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemAgentDeposit.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemAgentWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemAgentWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemShopWithdraw.setBackgroundResource(R.drawable.bg_white_master_item);
        tvMasterItemShopWithdraw.setTextColor(getResources().getColor(R.color.colorText));
        tvMasterItemHistory.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHistory.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemCreateUser.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemCreateUser.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void goToHistoryFragment() {
        AdminHistoryFragment adminHistoryFragment = new AdminHistoryFragment();
        adminHistoryFragment.setFragmentManager(fragmentManager);

        fragmentManager.beginTransaction()
                .replace(R.id.admin_detail_container, adminHistoryFragment)
                .commit();

        changeMasterItemBackgroundForHistory();
    }

    private void changeMasterItemBackgroundForHistory() {
        tvMasterItemHome.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHome.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemHomeWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHomeWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemAgentDeposit.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemAgentDeposit.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemAgentWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemAgentWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemShopWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemShopWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemHistory.setBackgroundResource(R.drawable.bg_white_master_item);
        tvMasterItemHistory.setTextColor(getResources().getColor(R.color.colorText));
        tvMasterItemCreateUser.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemCreateUser.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void goToCreateUserFragment() {
        AdminCreateUserFragment createUserFragment = new AdminCreateUserFragment();
        createUserFragment.setFragmentManager(fragmentManager);

        fragmentManager.beginTransaction()
                .replace(R.id.admin_detail_container, createUserFragment)
                .commit();

        changeMasterItemBackgroundForAddUser();
    }

    private void changeMasterItemBackgroundForAddUser() {
        tvMasterItemHome.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHome.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemHomeWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHomeWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemAgentDeposit.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemAgentDeposit.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemAgentWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemAgentWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemShopWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemShopWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemHistory.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHistory.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemCreateUser.setBackgroundResource(R.drawable.bg_white_master_item);
        tvMasterItemCreateUser.setTextColor(getResources().getColor(R.color.colorText));
    }

    @Override
    public void onBackPressed() {
        if (inHome) {
            showExitDialog();
        } else {
            goToHomeFragment();
            inHome = true;
        }
    }

    private void showExitDialog() {
        AlertDialog.Builder builder;
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        //} else {
        builder = new AlertDialog.Builder(context);
        //}
        builder.setTitle(getString(R.string.exit_title))
                .setMessage(getString(R.string.exit_message))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        //System.exit(0);
                        AdminMainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
