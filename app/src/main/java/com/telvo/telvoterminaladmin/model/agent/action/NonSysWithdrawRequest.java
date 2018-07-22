package com.telvo.telvoterminaladmin.model.agent.action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by monir on 2/11/18.
 */

public class NonSysWithdrawRequest implements Serializable {

    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;

    @SerializedName("nid")
    @Expose
    private String nid;

    @SerializedName("withdrawSecret")
    @Expose
    private String withdrawSecret;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getWithdrawSecret() {
        return withdrawSecret;
    }

    public void setWithdrawSecret(String withdrawSecret) {
        this.withdrawSecret = withdrawSecret;
    }
}
