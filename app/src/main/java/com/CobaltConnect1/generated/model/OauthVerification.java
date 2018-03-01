
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OauthVerification {

    @SerializedName("tokenid")
    @Expose
    private String tokenid;
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
    @SerializedName("employee_id")
    @Expose
    private String employeeId;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("type")
    @Expose
    private String type;

    public OauthVerification(String tokenid, String merchantId,String employeeId,String accessToken,String type) {
        this.tokenid = tokenid;
        this.merchantId = merchantId;
        this.accessToken = accessToken;
        this.employeeId = employeeId;
        this.type = type;
    }

}
