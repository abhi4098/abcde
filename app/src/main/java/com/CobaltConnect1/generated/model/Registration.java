
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Registration {

    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("cobaltId")
    @Expose
    private String cobaltId;
    @SerializedName("cloverId")
    @Expose
    private String cloverId;
    @SerializedName("cloverToken")
    @Expose
    private String cloverToken;
    @SerializedName("loginPassword")
    @Expose
    private String loginPassword;
    @SerializedName("stateid")
    @Expose
    private String stateid;
    @SerializedName("type")
    @Expose
    private String type;

    public Registration(String fullName, String emailId, String cobaltId, String cloverId, String cloverToken, String loginPassword,String stateid, String type)
    {
        this.fullName = fullName;
        this.emailId = emailId;
        this.cobaltId = cobaltId;
        this.cloverId = cloverId;
        this.cloverToken = cloverToken;
        this.loginPassword = loginPassword;
        this.stateid = stateid;
        this.type = type;

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

    public String getCobaltId() {
        return cobaltId;
    }

    public void setCobaltId(String cobaltId) {
        this.cobaltId = cobaltId;
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

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
