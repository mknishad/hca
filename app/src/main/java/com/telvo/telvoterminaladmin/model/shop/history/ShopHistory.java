package com.telvo.telvoterminaladmin.model.shop.history;

import java.io.Serializable;

/**
 * Created by invar on 09-Nov-17.
 */

public class ShopHistory implements Serializable {
    private String date;
    private Double amount;
    private String mobileNumber;

    public ShopHistory(String date, Double amount, String mobileNumber) {
        this.date = date;
        this.amount = amount;
        this.mobileNumber = mobileNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
}
