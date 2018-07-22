package com.telvo.telvoterminaladmin.model.shop.action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by invar on 31-Oct-17.
 */

public class QrCode implements Serializable {
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("shopName")
    @Expose
    private String shopName;
    @SerializedName("secretCode")
    @Expose
    private String secretCode;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(String secretCode) {
        this.secretCode = secretCode;
    }
}
