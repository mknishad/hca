package com.telvo.telvoterminaladmin.model.agent.action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by invar on 30-Oct-17.
 */

public class ConfirmWithdrawRequest implements Serializable {

    @SerializedName("agentId")
    @Expose
    private String agentId;

    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;

    @SerializedName("nid")
    @Expose
    private String nid;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("withdrawSecret")
    @Expose
    private String withdrawSecret;

    @SerializedName("otpKey")
    @Expose
    private String otpKey;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWithdrawSecret() {
        return withdrawSecret;
    }

    public void setWithdrawSecret(String withdrawSecret) {
        this.withdrawSecret = withdrawSecret;
    }

    public String getOtpKey() {
        return otpKey;
    }

    public void setOtpKey(String otpKey) {
        this.otpKey = otpKey;
    }
}
