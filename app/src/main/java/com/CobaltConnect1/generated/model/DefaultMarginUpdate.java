package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinandan on 1/12/17.
 */

public class DefaultMarginUpdate {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("tokenid")
    @Expose
    private String tokenid;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("margin")
    @Expose
    private String margin;

    public DefaultMarginUpdate(String type,String tokenid,String categoryId,String margin)
    {
        this.type = type;
        this.tokenid = tokenid;
        this.categoryId = categoryId;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }
}
