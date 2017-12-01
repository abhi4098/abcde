package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinandan on 6/10/17.
 */

public class MarginLocalData {

    @SerializedName("productId")
    @Expose
    private String productId;

    @SerializedName("categoryId")
    @Expose
    private String categoryId;

    @SerializedName("margin")
    @Expose
    private String margin;
    @SerializedName("newPrice")
    @Expose
    private String newPrice;
    @SerializedName("bUpdate")
    @Expose
    private Integer bUpdate;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("minStock")
    @Expose
    private String minStock;
    @SerializedName("defaultMargin")
    @Expose
    private String defaultMargin;

    public String getMinStock() {
        return minStock;
    }

    public void setMinStock(String minStock) {
        this.minStock = minStock;
    }

    public String getDefaultMargin() {
        return defaultMargin;
    }

    public void setDefaultMargin(String defaultMargin) {
        this.defaultMargin = defaultMargin;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
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

    public Integer getBUpdate() {
        return bUpdate;
    }

    public void setBUpdate(Integer bUpdate) {
        this.bUpdate = bUpdate;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

}
