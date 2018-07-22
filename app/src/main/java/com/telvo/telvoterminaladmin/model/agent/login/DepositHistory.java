package com.telvo.telvoterminaladmin.model.agent.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by invariant on 10/26/17.
 */

public class DepositHistory implements Serializable {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("agentEarning")
    @Expose
    private Double agentEarning;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("depositId")
    @Expose
    private String depositId;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAgentEarning() {
        return agentEarning;
    }

    public void setAgentEarning(Double agentEarning) {
        this.agentEarning = agentEarning;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDepositId() {
        return depositId;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }
}
