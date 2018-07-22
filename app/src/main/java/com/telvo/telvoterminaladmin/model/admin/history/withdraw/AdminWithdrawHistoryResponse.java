package com.telvo.telvoterminaladmin.model.admin.history.withdraw;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by invar on 14-Nov-17.
 */

public class AdminWithdrawHistoryResponse implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("agentWithdraws")
    @Expose
    private List<AgentWithdraw> agentWithdraws = null;
    @SerializedName("shopWithdraws")
    @Expose
    private List<ShopWithdraw> shopWithdraws = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AgentWithdraw> getAgentWithdraws() {
        return agentWithdraws;
    }

    public void setAgentWithdraws(List<AgentWithdraw> agentWithdraws) {
        this.agentWithdraws = agentWithdraws;
    }

    public List<ShopWithdraw> getShopWithdraws() {
        return shopWithdraws;
    }

    public void setShopWithdraws(List<ShopWithdraw> shopWithdraws) {
        this.shopWithdraws = shopWithdraws;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
