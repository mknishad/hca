package com.telvo.telvoterminaladmin.baseactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by monir on 8/7/17.
 */

public class BaseActivity extends AppCompatActivity {
    protected Bundle bundle;
    protected Context context;
    protected Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
    }

    protected void setToolbar(int id) {
        Toolbar toolbar = (Toolbar) findViewById(id);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected void setToolbar(int id,String title) {
        Toolbar toolbar = (Toolbar) findViewById(id);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    protected void setToolbarWithoutBack(int id) {
        Toolbar toolbar = (Toolbar) findViewById(id);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
    }

    protected void setToolbarWithoutBack(int id, String title) {
        Toolbar toolbar = (Toolbar) findViewById(id);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    protected void ToastShort(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    protected void ToastLong(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    protected String getStringResources(int id) {
        return getResources().getString(id);
    }

    protected TextView getTextView(int id) {
        return (TextView) findViewById(id);
    }

    protected RadioButton getRadioButton(int id) {
        return (RadioButton) findViewById(id);
    }

    protected RadioGroup getRadioGroup(int id) {
        return (RadioGroup) findViewById(id);
    }

    protected EditText getEditText(int id) {
        return (EditText) findViewById(id);
    }

    protected ImageView getImageView(int id) {
        return (ImageView) findViewById(id);
    }

    protected AppCompatButton getButton(int id) {
        return (AppCompatButton) findViewById(id);
    }

    protected ListView getListView(int id) {
        return (ListView) findViewById(id);
    }

    protected LinearLayout getLinearLayout(int id) {
        return (LinearLayout) findViewById(id);
    }

    protected Spinner getSpinner(int id) {
        return (Spinner) findViewById(id);
    }

    protected AutoCompleteTextView getAutoCompleteTextView(int id) {
        return (AutoCompleteTextView) findViewById(id);
    }

    protected RecyclerView getRecyclerView(int id){
        return (RecyclerView)findViewById(id);
    }

    protected RelativeLayout getRelativeLayout(int id) {
        return (RelativeLayout) findViewById(id);
    }

    protected AppCompatButton getAppcompatButton(int id) {
        return (AppCompatButton) findViewById(id);
    }

    protected WebView getWebView(int id) {
        WebView webView = (WebView) findViewById(id);
        webView.clearCache(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.setBackgroundColor(Color.TRANSPARENT);
        return webView;
    }

    protected CheckBox getCheckBox(int id) {
        CheckBox checkBox = (CheckBox) findViewById(id);
        return checkBox;
    }

    protected void setBrowsUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
        ((Activity) context).overridePendingTransition(0, 0);
    }
}
