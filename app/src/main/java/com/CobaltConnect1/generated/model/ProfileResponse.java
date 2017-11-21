
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileResponse {

    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("tokenid")
    @Expose
    private String tokenid;
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
    @SerializedName("stateid")
    @Expose
    private String stateid;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public String getStateid() {
        return stateid;
    }

    public void setStateid(String stateid) {
        this.stateid = stateid;
    }

}
