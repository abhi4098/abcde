
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryList {

    @SerializedName("categoryId")
    @Expose
    private String categoryId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("minStock")
    @Expose
    private String minStock;
    @SerializedName("defaultMargin")
    @Expose
    private String defaultMargin;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

}
