package com.telvo.telvoterminaladmin.model.agent.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by invariant on 10/26/17.
 */

public class Total implements Serializable {

    @SerializedName("amount")
    @Expose
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
