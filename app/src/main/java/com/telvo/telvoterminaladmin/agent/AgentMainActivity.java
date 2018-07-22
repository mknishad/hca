package com.telvo.telvoterminaladmin.agent;

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
import com.telvo.telvoterminaladmin.auth.LoginActivity;
import com.telvo.telvoterminaladmin.baseactivity.BaseActivity;
import com.telvo.telvoterminaladmin.model.agent.login.LoginResponseAgent;
import com.telvo.telvoterminaladmin.util.Constants;

public class AgentMainActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvMasterItemHome;
    private TextView tvMasterItemDeposit;
    private TextView tvMasterItemWithdraw;
    private TextView tvMasterItemNonSysWithdraw;
    private TextView[] textViews = new TextView[4];
    private FragmentManager fragmentManager;

    private LoginResponseAgent loginResponseAgent;
    private boolean inHome = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_main);

        setToolbarWithoutBack(R.id.toolbar_agent, getStringResources(R.string.app_name));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        intent = getIntent();
        loginResponseAgent = (LoginResponseAgent) intent.getSerializableExtra(Constants.ADMIN_LOGIN_RESPONSE);
        //Toast.makeText(this, loginResponseAgent.getToken(), Toast.LENGTH_SHORT).show();

        initializeViews();
        fragmentManager = getSupportFragmentManager();

        // Load AgentHomeFragment on activity startup
        goToHomeFragment();

        // Start AdminMainActivity
        /*Intent intent = new Intent(this, AgentMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);*/
    }

    public LoginResponseAgent getLoginResponseAgent() {
        return loginResponseAgent;
    }

    private void initializeViews() {
        tvMasterItemHome = getTextView(R.id.text_view_agent_master_item_home);
        tvMasterItemHome.setOnClickListener(this);
        textViews[0]=tvMasterItemHome;
        tvMasterItemDeposit = getTextView(R.id.text_view_agent_master_item_deposit);
        tvMasterItemDeposit.setOnClickListener(this);
        textViews[1]= tvMasterItemDeposit;
        tvMasterItemWithdraw = getTextView(R.id.text_view_agent_master_item_withdraw);
        tvMasterItemWithdraw.setOnClickListener(this);
        textViews[2]= tvMasterItemWithdraw;
        tvMasterItemNonSysWithdraw = getTextView(R.id.text_view_agent_master_item_non_sys_withdraw);
        tvMasterItemNonSysWithdraw.setOnClickListener(this);
        textViews[3]= tvMasterItemNonSysWithdraw;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.text_view_agent_master_item_home:
                goToHomeFragment();
                inHome = true;
                break;
            case R.id.text_view_agent_master_item_deposit:
                goToDepositFragment();
                inHome = false;
                break;
            case R.id.text_view_agent_master_item_withdraw:
                goToWithdrawFragment();
                inHome = false;
                break;
            case R.id.text_view_agent_master_item_non_sys_withdraw:
                goToNonSysWithdrawFragment();
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
        intent = new Intent(AgentMainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToHomeFragment() {
        AgentHomeFragment agentHomeFragment = new AgentHomeFragment();
        agentHomeFragment.setFragmentManager(fragmentManager);
        agentHomeFragment.setTextViews(textViews);

        fragmentManager.beginTransaction()
                .replace(R.id.agent_detail_container, agentHomeFragment)
                .commit();


        changeMasterItemBackgroundForAgentHome();
    }

    private void changeMasterItemBackgroundForAgentHome() {
        tvMasterItemHome.setBackgroundResource(R.drawable.bg_white_master_item);
        tvMasterItemHome.setTextColor(getResources().getColor(R.color.colorText));
        tvMasterItemDeposit.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemDeposit.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemNonSysWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemNonSysWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void goToDepositFragment() {
        AgentDepositFragment agentDepositFragment = new AgentDepositFragment();
        agentDepositFragment.setFragmentManager(fragmentManager);
        agentDepositFragment.setTextViews(textViews);

        fragmentManager.beginTransaction()
                .replace(R.id.agent_detail_container, agentDepositFragment)
                .commit();


        changeMasterItemBackgroundForAgentDeposit();
    }

    private void changeMasterItemBackgroundForAgentDeposit() {
        tvMasterItemHome.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHome.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemDeposit.setBackgroundResource(R.drawable.bg_white_master_item);
        tvMasterItemDeposit.setTextColor(getResources().getColor(R.color.colorText));
        tvMasterItemWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemNonSysWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemNonSysWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void goToWithdrawFragment() {
        AgentWithdrawFragment agentWithdrawFragment = new AgentWithdrawFragment();
        agentWithdrawFragment.setFragmentManager(fragmentManager);
        agentWithdrawFragment.setTextViews(textViews);

        fragmentManager.beginTransaction()
                .replace(R.id.agent_detail_container, agentWithdrawFragment)
                .commit();

        changeMasterItemBackgroundForAgentWithdraw();
    }

    private void changeMasterItemBackgroundForAgentWithdraw() {
        tvMasterItemHome.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHome.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemDeposit.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemDeposit.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemWithdraw.setBackgroundResource(R.drawable.bg_white_master_item);
        tvMasterItemWithdraw.setTextColor(getResources().getColor(R.color.colorText));
        tvMasterItemNonSysWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemNonSysWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void goToNonSysWithdrawFragment() {
        AgentNonSystemWithdrawFragment nonSystemWithdrawFragment = new AgentNonSystemWithdrawFragment();
        nonSystemWithdrawFragment.setFragmentManager(fragmentManager);
        nonSystemWithdrawFragment.setTextViews(textViews);

        fragmentManager.beginTransaction()
                .replace(R.id.agent_detail_container, nonSystemWithdrawFragment)
                .commit();


        changeMasterItemBackgroundForAgentNonSysWithdraw();
    }

    private void changeMasterItemBackgroundForAgentNonSysWithdraw() {
        tvMasterItemHome.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemHome.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemDeposit.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemDeposit.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemWithdraw.setBackgroundResource(R.drawable.bg_red_master_item);
        tvMasterItemWithdraw.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMasterItemNonSysWithdraw.setBackgroundResource(R.drawable.bg_white_master_item);
        tvMasterItemNonSysWithdraw.setTextColor(getResources().getColor(R.color.colorText));
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
                        AgentMainActivity.super.onBackPressed();
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
