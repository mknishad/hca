package com.telvo.telvoterminaladmin.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.telvo.telvoterminaladmin.R;
import com.telvo.telvoterminaladmin.admin.AdminMainActivity;
import com.telvo.telvoterminaladmin.agent.AgentMainActivity;
import com.telvo.telvoterminaladmin.baseactivity.BaseActivity;
import com.telvo.telvoterminaladmin.model.admin.login.LoginResponseAdmin;
import com.telvo.telvoterminaladmin.model.agent.login.LoginResponseAgent;
import com.telvo.telvoterminaladmin.model.login.LoginRequest;
import com.telvo.telvoterminaladmin.model.shop.login.LoginResponseShop;
import com.telvo.telvoterminaladmin.networking.ApiClient;
import com.telvo.telvoterminaladmin.networking.ApiInterface;
import com.telvo.telvoterminaladmin.sharedpreference.TerminalPreferences;
import com.telvo.telvoterminaladmin.shop.ShopMainActivity;
import com.telvo.telvoterminaladmin.util.AppManager;
import com.telvo.telvoterminaladmin.util.Constants;
import com.telvo.telvoterminaladmin.util.CustomAlertDialog;
import com.telvo.telvoterminaladmin.util.NumberFormatUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private TerminalPreferences terminalPreferences;

    private EditText mobileNumberEditText;
    private Spinner adminTypeSpinner;
    private EditText passwordEditText;
    private TextView forgotPasswordTextView;
    private AppCompatButton loginButton;
    private ProgressDialog progress;
    private CustomAlertDialog alertDialog;

    private String mobileNumber;
    private String role;
    private String password;

    private ApiInterface apiInterface;
    private LoginResponseAgent loginResponseAgent = new LoginResponseAgent();
    private LoginResponseShop loginResponseShop = new LoginResponseShop();
    private LoginResponseAdmin loginResponseAdmin = new LoginResponseAdmin();
    private LoginRequest loginRequest = new LoginRequest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setToolbarWithoutBack(R.id.toolbar_login, getStringResources(R.string.login));

        terminalPreferences = new TerminalPreferences(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        initializeViews();
        setupAdminTypeSpinner();
        setForgotPasswordText();

        // Auto login
        //loginButton.performClick();
    }

    private void initializeViews() {
        mobileNumberEditText = getEditText(R.id.mobileNumberEditText);
        mobileNumberEditText.setText(terminalPreferences.getLoginAccount());
        adminTypeSpinner = getSpinner(R.id.adminTypeSpinner);
        Log.e(TAG, "init login role " + terminalPreferences.getLoginRole());
        passwordEditText = getEditText(R.id.passwordEditText);
        forgotPasswordTextView = getTextView(R.id.forgotPasswordTextView);
        loginButton = getAppcompatButton(R.id.loginButton);
        loginButton.setOnClickListener(this);
        alertDialog = new CustomAlertDialog(this);
        progress = new ProgressDialog(this);
    }

    private void setupAdminTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.admin_type_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        adminTypeSpinner.setAdapter(adapter);
        adminTypeSpinner.setSelection(terminalPreferences.getLoginRole());
    }

    private void setForgotPasswordText() {
        String forgotPassword = getString(R.string.forgot_password);
        SpannableString content = new SpannableString(forgotPassword);
        content.setSpan(new UnderlineSpan(), 0, forgotPassword.length(), 0);
        forgotPasswordTextView.setText(content);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.loginButton:
                hideKeyboard(view);

                mobileNumber = mobileNumberEditText.getText().toString().trim();
                role = adminTypeSpinner.getSelectedItem().toString().trim().toLowerCase();
                password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(mobileNumber)) {
                    Toast.makeText(this, getString(R.string.please_enter_mobile_number), Toast.LENGTH_SHORT).show();
                } else if (adminTypeSpinner.getSelectedItemPosition() < 1) {
                    Toast.makeText(this, getString(R.string.please_select_role), Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(this, getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
                } else {
                    if (AppManager.hasInternetConnection(context)) {
                        showProgressDialog();

                        switch (role) {
                            case Constants.AGENT:
                                loginAsAgent();
                                break;
                            case Constants.SHOP:
                                loginAsShop();
                                break;
                            case Constants.ADMIN:
                                loginAsAdmin();
                                break;
                            default:
                                break;
                        }

                    } else {
                        alertDialog.showDialog(getStringResources(R.string
                                .no_internet_connection));
                    }
                }
                break;
            default:
                return;
        }
    }

    private void showProgressDialog() {
        progress.setMessage(getString(R.string.please_wait));
        progress.show();
    }

    private void hideProgressDialog() {
        progress.dismiss();
    }

    private void loginAsAdmin() {
        setLoginInfo();

        Call<LoginResponseAdmin> adminCall = apiInterface.loginAsAdmin(loginRequest);

        adminCall.enqueue(new Callback<LoginResponseAdmin>() {
            @Override
            public void onResponse(Call<LoginResponseAdmin> call, Response<LoginResponseAdmin> response) {
                hideProgressDialog();
                loginResponseAdmin = response.body();

                if (loginResponseAdmin != null) {
                    if (loginResponseAdmin.getStatus().equals(Constants.SUCCESS)) {
                        //Toast.makeText(LoginActivity.this, loginResponseAgent.getToken(), Toast.LENGTH_SHORT).show();
                        saveAdminBalance();
                        saveLoginInfo();

                        intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                        intent.putExtra(Constants.ADMIN_LOGIN_RESPONSE, loginResponseAdmin);
                        startActivity(intent);
                        finish();
                    } else {
                        alertDialog.showDialog(loginResponseAdmin.getMessage());
                        Log.i(TAG, loginResponseAdmin.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponseAdmin> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private void saveLoginInfo() {
        terminalPreferences.putLoginAccount(mobileNumber);
        Log.e(TAG, "Login Account " + mobileNumber);
        terminalPreferences.putLoginRole(adminTypeSpinner.getSelectedItemPosition());
        Log.e(TAG, "Login Role " + adminTypeSpinner.getSelectedItemPosition());
    }

    private void saveAdminBalance() {
        terminalPreferences.putAdminBalance(NumberFormatUtils.getFormattedDouble(loginResponseAdmin.getAdmin().getBalance()));
    }

    private void loginAsShop() {
        setLoginInfo();

        Call<LoginResponseShop> shopCall = apiInterface.loginAsShop(loginRequest);

        shopCall.enqueue(new Callback<LoginResponseShop>() {
            @Override
            public void onResponse(Call<LoginResponseShop> call, Response<LoginResponseShop> response) {
                hideProgressDialog();
                loginResponseShop = response.body();

                if (loginResponseShop != null) {
                    if (loginResponseShop.getStatus().equals(Constants.SUCCESS)) {
                        //Toast.makeText(LoginActivity.this, loginResponseAgent.getToken(), Toast.LENGTH_SHORT).show();
                        saveShopBalance();
                        saveLoginInfo();

                        intent = new Intent(LoginActivity.this, ShopMainActivity.class);
                        intent.putExtra(Constants.ADMIN_LOGIN_RESPONSE, loginResponseShop);
                        startActivity(intent);
                        finish();
                    } else {
                        alertDialog.showDialog(loginResponseShop.getMessage());
                        Log.i(TAG, loginResponseShop.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponseShop> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private void saveShopBalance() {
        terminalPreferences.putShopTotalBalance(NumberFormatUtils.getFormattedDouble(loginResponseShop.getShop().getBalance()));
    }

    private void loginAsAgent() {
        setLoginInfo();

        Call<LoginResponseAgent> agentCall = apiInterface.loginAsAgent(loginRequest);

        agentCall.enqueue(new Callback<LoginResponseAgent>() {
            @Override
            public void onResponse(Call<LoginResponseAgent> call, Response<LoginResponseAgent> response) {
                hideProgressDialog();
                loginResponseAgent = response.body();

                //-------------- DEBUG -------------------------
                //Agent agent = loginResponseAgent.getAgent();
                /*Toast.makeText(LoginActivity.this, "onResponse",
                        Toast.LENGTH_SHORT).show();*/
                //-------------- DEBUG -------------------------

                if (loginResponseAgent != null) {
                    if (loginResponseAgent.getStatus().equals(Constants.SUCCESS)) {
                        //Toast.makeText(LoginActivity.this, loginResponseAgent.getToken(), Toast.LENGTH_SHORT).show();
                        // Save agent balance to shared preferences
                        saveAgentBalance();
                        saveLoginInfo();

                        intent = new Intent(LoginActivity.this, AgentMainActivity.class);
                        intent.putExtra(Constants.ADMIN_LOGIN_RESPONSE, loginResponseAgent);
                        startActivity(intent);
                        finish();
                    } else {
                        alertDialog.showDialog(loginResponseAgent.getMessage());
                        Log.i(TAG, loginResponseAgent.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponseAgent> call, Throwable t) {
                hideProgressDialog();
                alertDialog.showDialog(getString(R.string.unable_to_connect));
                Log.e(TAG, getString(R.string.on_failure) + t.getMessage());
            }
        });
    }

    private void saveAgentBalance() {
        terminalPreferences.putAgentTotalBalance(NumberFormatUtils.getFormattedDouble(loginResponseAgent.getAgent().getBalance()));
        terminalPreferences.putAgentTotalCommission(NumberFormatUtils.getFormattedDouble(loginResponseAgent.getAgent().getCommission().getTotal().getAmount()));
        terminalPreferences.putAgentTodayCommission(NumberFormatUtils.getFormattedDouble(loginResponseAgent.getAgent().getCommission().getToday().getAmount()));
    }

    private void setLoginInfo() {
        loginRequest.setMobileNumber(mobileNumber);
        loginRequest.setRole(role);
        loginRequest.setPassword(password);
    }

    private void hideKeyboard(View view) {
        final InputMethodManager imm = (InputMethodManager) this.getSystemService(
                INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}