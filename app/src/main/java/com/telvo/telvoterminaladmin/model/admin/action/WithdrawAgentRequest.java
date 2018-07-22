package com.telvo.telvoterminaladmin.model.admin.action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by invar on 13-Nov-17.
 */

public class WithdrawAgentRequest implements Serializable {
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("amount")
    @Expose
    private String amount;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
