
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyCloverProduct {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("tokenid")
    @Expose
    private String tokenid;

    @SerializedName("orderField")
    @Expose
    private String orderField;

    @SerializedName("orderType")
    @Expose
    private String orderType;




    public MyCloverProduct(String tokenid, String type,String orderField,String orderType)
    {
       this.tokenid = tokenid;
        this.type = type;
        this.orderField = orderField;
        this.orderType = orderType;
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

}
