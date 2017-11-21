
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateProfile {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("tokenid")
    @Expose
    private String tokenid;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("cloverId")
    @Expose
    private String cloverId;
    @SerializedName("cloverToken")
    @Expose
    private String cloverToken;
    @SerializedName("stateid")
    @Expose
    private String stateid;

    public UpdateProfile(String fullName, String tokenid, String emailId, String cloverId, String cloverToken, String stateid , String type)
    {
        this.fullName = fullName;
        this.tokenid = tokenid;
        this.emailId = emailId;
        this.cloverId = cloverId;
        this.cloverToken = cloverToken;
        this.stateid = stateid;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getCloverId() {
        return cloverId;
    }

    public void setCloverId(String cloverId) {
        this.cloverId = cloverId;
    }

    public String getCloverToken() {
        return cloverToken;
    }

    public void setCloverToken(String cloverToken) {
        this.cloverToken = cloverToken;
    }

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

}
