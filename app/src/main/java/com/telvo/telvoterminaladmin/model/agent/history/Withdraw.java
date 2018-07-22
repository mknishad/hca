package com.telvo.telvoterminaladmin.model.agent.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by invar on 07-Nov-17.
 */

public class Withdraw implements Serializable {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("agent")
    @Expose
    private Agent agent;
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

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
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
