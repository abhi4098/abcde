
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPassword {

    @SerializedName("cobaltId")
    @Expose
    private String cobaltId;
    @SerializedName("type")
    @Expose
    private String type;

    public ForgotPassword(String cobaltId,String type)
    {
        this.cobaltId = cobaltId;
        this.type = type;
    }

    public String getCobaltId() {
        return cobaltId;
    }

    public void setCobaltId(String cobaltId) {
        this.cobaltId = cobaltId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
