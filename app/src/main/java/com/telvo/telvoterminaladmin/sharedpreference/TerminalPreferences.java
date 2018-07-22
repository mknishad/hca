package com.telvo.telvoterminaladmin.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;

import com.telvo.telvoterminaladmin.util.Constants;

/**
 * Created by invar on 07-Nov-17.
 */

public class TerminalPreferences {

    private static final String PREFERENCE_TITLE = "TerminalPreferences";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public TerminalPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putAgentTotalBalance(String balance) {
        editor.putString(Constants.AGENT_TOTAL_BALANCE, balance);
        editor.commit();
    }

    public String getAgentTotalBalance() {
        String balance = sharedPreferences.getString(Constants.AGENT_TOTAL_BALANCE, "Not found!");
        return balance;
    }

    public void putAgentTotalCommission(String totalCommission) {
        editor.putString(Constants.AGENT_TOTAL_COMMISSION, totalCommission);
        editor.commit();
    }

    public String getAgentTotalCommission() {
        String totalCommission = sharedPreferences.getString(Constants.AGENT_TOTAL_COMMISSION, "Not found!");
        return totalCommission;
    }

    public void putAgentTodayCommission(String todayCommission) {
        editor.putString(Constants.AGENT_TODAY_COMMISSION, todayCommission);
        editor.commit();
    }

    public String getAgentTodayCommission() {
        String todayCommission = sharedPreferences.getString(Constants.AGENT_TODAY_COMMISSION, "Not found!");
        return todayCommission;
    }

    public void putShopTotalBalance(String balance) {
        editor.putString(Constants.SHOP_TOTAL_BALANCE, balance);
        editor.commit();
    }

    public String getShopTotalBalance() {
        String balance = sharedPreferences.getString(Constants.SHOP_TOTAL_BALANCE, "Not found!");
        return balance;
    }

    public void putAdminBalance(String balance) {
        editor.putString(Constants.ADMIN_TOTAL_BALANCE, balance);
        editor.commit();
    }

    public String getAdminBalance() {
        String balance = sharedPreferences.getString(Constants.ADMIN_TOTAL_BALANCE, "Not found!");
        return balance;
    }

    public void putLoginAccount(String account) {
        editor.putString(Constants.LOGIN_ACCOUNT, account);
        editor.commit();
    }

    public String getLoginAccount() {
        String account = sharedPreferences.getString(Constants.LOGIN_ACCOUNT, "");
        return account;
    }

    public void putLoginRole(int role) {
        editor.putInt(Constants.LOGIN_ROLE, role);
        editor.commit();
    }

    public int getLoginRole() {
        int role = sharedPreferences.getInt(Constants.LOGIN_ROLE, 0);
        return role;
    }
}
