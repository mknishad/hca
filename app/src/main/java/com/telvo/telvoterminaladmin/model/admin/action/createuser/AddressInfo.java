package com.telvo.telvoterminaladmin.model.admin.action.createuser;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Invariant on 9/26/17.
 */

public class AddressInfo implements Serializable {

    @SerializedName("presentAddress")
    @Expose
    private PresentAddress presentAddress;
    @SerializedName("permanentAddress")
    @Expose
    private PermanentAddress permanentAddress;

    public AddressInfo(PresentAddress presentAddress, PermanentAddress permanentAddress) {
        this.presentAddress = presentAddress;
        this.permanentAddress = permanentAddress;
    }

    public PresentAddress getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(PresentAddress presentAddress) {
        this.presentAddress = presentAddress;
    }

    public PermanentAddress getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(PermanentAddress permanentAddress) {
        this.permanentAddress = permanentAddress;
    }
}
