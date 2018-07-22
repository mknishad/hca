package com.telvo.telvoterminaladmin.model.admin.history.deposit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by invar on 14-Nov-17.
 */

public class AdminDepositHistoryResponse implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("agentDeposits")
    @Expose
    private List<AgentDeposit> agentDeposits = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AgentDeposit> getAgentDeposits() {
        return agentDeposits;
    }

    public void setAgentDeposits(List<AgentDeposit> agentDeposits) {
        this.agentDeposits = agentDeposits;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
