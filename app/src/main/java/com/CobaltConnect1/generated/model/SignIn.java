
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignIn {

    @SerializedName("cobaltId")
    @Expose
    private String cobaltId;
    @SerializedName("loginPassword")
    @Expose
    private String loginPassword;
    @SerializedName("type")
    @Expose
    private String type;

    public SignIn(String cobaltId, String loginPassword, String type)
    {
        this.cobaltId = cobaltId;
        this.loginPassword = loginPassword;
        this.type = type;
    }

    public String getCobaltId() {
        return cobaltId;
    }

    public void setCobaltId(String cobaltId) {
        this.cobaltId = cobaltId;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
