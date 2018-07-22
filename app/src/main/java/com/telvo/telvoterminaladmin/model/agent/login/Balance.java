package com.telvo.telvoterminaladmin.model.agent.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by invariant on 10/26/17.
 */

public class Balance implements Serializable {
    @SerializedName("today")
    @Expose
    private Today today;
    @SerializedName("total")
    @Expose
    private Total total;

    public Today getToday() {
        return today;
    }

    public void setToday(Today today) {
        this.today = today;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }
}
