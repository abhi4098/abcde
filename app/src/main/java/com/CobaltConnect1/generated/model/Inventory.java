
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Inventory {



    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("cloverId")
    @Expose
    private String cloverId;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("stock")
    @Expose
    private String stock;

    @SerializedName("wholeSaler")
    @Expose
    private String wholeSaler;
    @SerializedName("previousCost")
    @Expose
    private String previousCost;
    @SerializedName("newCost")
    @Expose
    private String newCost;
    @SerializedName("margins")
    @Expose
    private String margins;
    @SerializedName("previousPrice")
    @Expose
    private String previousPrice;
    @SerializedName("newPrice")
    @Expose
    private String newPrice;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("bUpdate")
    @Expose
    private Integer bUpdate;
    @SerializedName("margin")
    @Expose
    private String margin;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCloverId() {
        return cloverId;
    }

    public void setCloverId(String cloverId) {
        this.cloverId = cloverId;
    }
    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public String getWholeSaler() {
        return wholeSaler;
    }

    public void setWholeSaler(String wholeSaler) {
        this.wholeSaler = wholeSaler;
    }

    public String getPreviousCost() {
        return previousCost;
    }

    public void setPreviousCost(String previousCost) {
        this.previousCost = previousCost;
    }

    public String getNewCost() {
        return newCost;
    }

    public void setNewCost(String newCost) {
        this.newCost = newCost;
    }

    public String getMargins() {
        return margins;
    }

    public void setMargins(String margins) {
        this.margins = margins;
    }

    public String getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(String previousPrice) {
        this.previousPrice = previousPrice;
    }

    public String getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(String newPrice) {
        this.newPrice = newPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBUpdate() {
        return bUpdate;
    }

    public void setBUpdate(Integer bUpdate) {
        this.bUpdate = bUpdate;
    }

}
