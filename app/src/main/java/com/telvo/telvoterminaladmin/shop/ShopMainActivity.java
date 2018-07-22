package com.telvo.telvoterminaladmin.shop;

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
import com.telvo.telvoterminaladmin.model.shop.login.LoginResponseShop;
import com.telvo.telvoterminaladmin.util.Constants;

public class ShopMainActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvShopMasterItemHome;
    private TextView tvShopMasterItemHistory;
    private FragmentManager fragmentManager;
    private TextView[] textViews = new TextView[3];

    private LoginResponseShop loginResponseShop;
    private boolean inHome = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_main);

        setToolbarWithoutBack(R.id.toolbar_shop, getStringResources(R.string.app_name));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initializeViews();
        fragmentManager = getSupportFragmentManager();
        loginResponseShop = (LoginResponseShop) getIntent().getSerializableExtra(Constants.ADMIN_LOGIN_RESPONSE);

        goToShopHomeFragment();
    }

    public LoginResponseShop getLoginResponseShop() {
        return loginResponseShop;
    }

    private void initializeViews() {
        tvShopMasterItemHome = findViewById(R.id.text_view_shop_master_item_home);
        tvShopMasterItemHome.setOnClickListener(this);
        tvShopMasterItemHistory = findViewById(R.id.text_view_shop_master_item_history);
        tvShopMasterItemHistory.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_shop_master_item_home:
                goToShopHomeFragment();
                inHome = true;
                break;
            case R.id.text_view_shop_master_item_history:
                goToShopHistoryFragment();
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
        intent = new Intent(ShopMainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void goToShopHomeFragment() {
        ShopHomeFragment shopHomeFragment = new ShopHomeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.shop_detail_container, shopHomeFragment)
                .commit();
        tvShopMasterItemHome.setBackgroundResource(R.drawable.bg_white_master_item);
        tvShopMasterItemHome.setTextColor(getResources().getColor(R.color.colorText));
        tvShopMasterItemHistory.setBackgroundResource(R.drawable.bg_red_master_item);
        tvShopMasterItemHistory.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void goToShopHistoryFragment() {
        ShopHistoryFragment shopHistoryFragment = new ShopHistoryFragment();
        shopHistoryFragment.setFragmentManager(fragmentManager);
        fragmentManager.beginTransaction()
                .replace(R.id.shop_detail_container, shopHistoryFragment)
                .commit();
        tvShopMasterItemHistory.setBackgroundResource(R.drawable.bg_white_master_item);
        tvShopMasterItemHistory.setTextColor(getResources().getColor(R.color.colorText));
        tvShopMasterItemHome.setBackgroundResource(R.drawable.bg_red_master_item);
        tvShopMasterItemHome.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    @Override
    public void onBackPressed() {
        if (inHome) {
            showExitDialog();
        } else {
            goToShopHomeFragment();
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
                        ShopMainActivity.super.onBackPressed();
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