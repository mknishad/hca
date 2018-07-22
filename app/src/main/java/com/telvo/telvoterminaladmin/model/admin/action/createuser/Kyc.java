package com.telvo.telvoterminaladmin.model.admin.action.createuser;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by monir on 2/19/18.
 */

public class Kyc implements Serializable {

    @SerializedName("personalInfo")
    @Expose
    private PersonalInfo personalInfo;
    @SerializedName("addressInfo")
    @Expose
    private AddressInfo addressInfo;
    @SerializedName("introducerInfo")
    @Expose
    private IntroducerInfo introducerInfo;

    public Kyc(PersonalInfo personalInfo, AddressInfo addressInfo, IntroducerInfo introducerInfo) {
        this.personalInfo = personalInfo;
        this.addressInfo = addressInfo;
        this.introducerInfo = introducerInfo;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public IntroducerInfo getIntroducerInfo() {
        return introducerInfo;
    }

    public void setIntroducerInfo(IntroducerInfo introducerInfo) {
        this.introducerInfo = introducerInfo;
    }
}
