package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinandan on 22/11/17.
 */

public class DashboardDataResponse {
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("myProducts")
    @Expose
    private String myProducts;
    @SerializedName("productsUpdate")
    @Expose
    private String productsUpdate;
    @SerializedName("affectedProducts")
    @Expose
    private String affectedProducts;

    @SerializedName("missingMargins")
    @Expose
    private String missingMargins;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMyProducts() {
        return myProducts;
    }

    public void setMyProducts(String myProducts) {
        this.missingMargins = myProducts;
    }

    public String getProductsUpdate() {
        return productsUpdate;
    }

    public void setProductsUpdate(String productsUpdate) {
        this.productsUpdate = productsUpdate;
    }

    public String getAffectedProducts() {
        return affectedProducts;
    }

    public void setAffectedProducts(String affectedProducts) {
        this.affectedProducts = affectedProducts;
    }

    public String getMissingMargins() {
        return missingMargins;
    }

    public void setMissingMargins(String missingMargins) {
        this.missingMargins = missingMargins;
    }

}
