package com.telvo.telvoterminaladmin.model.agent.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by invar on 14-Nov-17.
 */

public class Agent implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("commission")
    @Expose
    private Double commission;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }
}
