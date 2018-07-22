package com.telvo.telvoterminaladmin.model.agent.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by invar on 07-Nov-17.
 */

public class AgentHistoryResponse implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("transactions")
    @Expose
    private Transactions transactions;
    @SerializedName("message")
    @Expose
    private String message;

    public String getState() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Transactions getTransactions() {
        return transactions;
    }

    public void setTransactions(Transactions transactions) {
        this.transactions = transactions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
