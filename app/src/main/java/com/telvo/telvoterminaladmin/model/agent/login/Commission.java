package com.telvo.telvoterminaladmin.model.agent.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by invar on 06-Nov-17.
 */

public class Commission implements Serializable {
    @SerializedName("total")
    @Expose
    private Total total;
    @SerializedName("today")
    @Expose
    private Today today;

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    public Today getToday() {
        return today;
    }

    public void setToday(Today today) {
        this.today = today;
    }
}
