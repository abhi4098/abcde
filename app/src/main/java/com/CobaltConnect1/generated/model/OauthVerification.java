
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OauthVerification {

    @SerializedName("merchantid")
    @Expose
    private String merchantid;
    @SerializedName("employeeid")
    @Expose
    private String employeeid;
    @SerializedName("accesstoken")
    @Expose
    private String accesstoken;
    @SerializedName("type")
    @Expose
    private String type;

    public OauthVerification( String merchantid,String employeeid,String accesstoken,String type) {
        this.merchantid = merchantid;
        this.accesstoken = accesstoken;
        this.employeeid = employeeid;
        this.type = type;
    }

}
