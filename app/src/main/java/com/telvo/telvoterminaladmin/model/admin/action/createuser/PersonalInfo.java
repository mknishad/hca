package com.telvo.telvoterminaladmin.model.admin.action.createuser;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Invariant on 9/26/17.
 */

public class PersonalInfo implements Serializable {

    @SerializedName("fatherName")
    @Expose
    private String fatherName;
    @SerializedName("motherName")
    @Expose
    private String motherName;
    @SerializedName("nationality")
    @Expose
    private String nationality;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("spouseName")
    @Expose
    private String spouseName;
    @SerializedName("nid")
    @Expose
    private String nid;
    @SerializedName("nidType")
    @Expose
    private String nidType;

    public PersonalInfo(String fatherName, String motherName, String nationality, String dob, String gender, String spouseName, String nid, String nidType) {
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.nationality = nationality;
        this.dob = dob;
        this.gender = gender;
        this.spouseName = spouseName;
        this.nid = nid;
        this.nidType = nidType;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSpouseName() {
        return spouseName;
    }

    public void setSpouseName(String spouseName) {
        this.spouseName = spouseName;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getNidType() {
        return nidType;
    }

    public void setNidType(String nidType) {
        this.nidType = nidType;
    }
}
