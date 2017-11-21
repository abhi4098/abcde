
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarginUpdate {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("tokenid")
    @Expose
    private String tokenid;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("margin")
    @Expose
    private String margin;

    public MarginUpdate(String type,String tokenid,String productId,String margin)
    {
        this.type = type;
        this.tokenid = tokenid;
        this.productId = productId;
        this.margin = margin;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

}
