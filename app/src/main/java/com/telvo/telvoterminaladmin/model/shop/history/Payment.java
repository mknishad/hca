package com.telvo.telvoterminaladmin.model.shop.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telvo.telvoterminaladmin.model.agent.history.Currency;
import com.telvo.telvoterminaladmin.model.agent.history.User;
import com.telvo.telvoterminaladmin.model.shop.login.Shop;

import java.io.Serializable;

/**
 * Created by invar on 09-Nov-17.
 */

public class Payment implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("shop")
    @Expose
    private Shop shop;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("currency")
    @Expose
    private Currency currency;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
