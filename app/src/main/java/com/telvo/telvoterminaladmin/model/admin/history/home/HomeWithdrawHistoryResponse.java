package com.telvo.telvoterminaladmin.model.admin.history.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by invar on 12-Nov-17.
 */

public class HomeWithdrawHistoryResponse implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("homeWithdraws")
    @Expose
    private List<HomeWithdraw> homeWithdraws = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<HomeWithdraw> getHomeWithdraws() {
        return homeWithdraws;
    }

    public void setHomeWithdraws(List<HomeWithdraw> homeWithdraws) {
        this.homeWithdraws = homeWithdraws;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
