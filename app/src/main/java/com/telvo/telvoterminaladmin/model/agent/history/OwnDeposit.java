package com.telvo.telvoterminaladmin.model.agent.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.telvo.telvoterminaladmin.model.admin.login.Admin;

/**
 * Created by monir on 2/8/18.
 */

public class OwnDeposit {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("agent")
    @Expose
    private Agent agent;
    @SerializedName("admin")
    @Expose
    private Admin admin;
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

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
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
