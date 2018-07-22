package com.telvo.telvoterminaladmin.model.agent.action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telvo.telvoterminaladmin.model.agent.login.Commission;

/**
 * Created by monir on 2/11/18.
 */

public class NonSysWithdrawResponse {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("balance")
    @Expose
    private Double balance;

    @SerializedName("commission")
    @Expose
    private Commission commission;

    @SerializedName("amount")
    @Expose
    private Double amount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Commission getCommission() {
        return commission;
    }

    public void setCommission(Commission commission) {
        this.commission = commission;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
