package com.telvo.telvoterminaladmin.model.agent.history;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by invar on 07-Nov-17.
 */

public class AgentHistory implements Serializable, Comparable<AgentHistory> {
    private Date date;
    private Double amount;
    private String mobileNumber;
    private String transactionType;

    public AgentHistory(Date date, Double amount, String mobileNumber, String transactionType) {
        this.date = date;
        this.amount = amount;
        this.mobileNumber = mobileNumber;
        this.transactionType = transactionType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public int compareTo(@NonNull AgentHistory o) {
        return getDate().compareTo(o.getDate());
    }
}
