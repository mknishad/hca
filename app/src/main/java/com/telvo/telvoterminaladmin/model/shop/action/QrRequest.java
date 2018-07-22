package com.telvo.telvoterminaladmin.model.shop.action;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by invar on 31-Oct-17.
 */

public class QrRequest implements Serializable {

    @SerializedName("shopId")
    @Expose
    private String shopId;

    @SerializedName("amount")
    @Expose
    private String amount;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
