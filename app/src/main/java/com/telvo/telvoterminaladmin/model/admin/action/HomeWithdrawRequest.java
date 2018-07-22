package com.telvo.telvoterminaladmin.model.admin.action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by invar on 01-Nov-17.
 */

public class HomeWithdrawRequest implements Serializable {
    @SerializedName("adminId")
    @Expose
    private String adminId;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("withdrawSecret")
    @Expose
    private String withdrawSecret;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getWithdrawSecret() {
        return withdrawSecret;
    }

    public void setWithdrawSecret(String withdrawSecret) {
        this.withdrawSecret = withdrawSecret;
    }
}
