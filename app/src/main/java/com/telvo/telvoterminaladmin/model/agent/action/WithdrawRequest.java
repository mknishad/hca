package com.telvo.telvoterminaladmin.model.agent.action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by invar on 30-Oct-17.
 */

public class WithdrawRequest implements Serializable {

    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;

    @SerializedName("nid")
    @Expose
    private String nid;

    @SerializedName("amount")
    @Expose
    private String amount;

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
}

