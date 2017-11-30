
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryListResponse {

    @SerializedName("list")
    @Expose
    private List<CategoryList> list = null;
    @SerializedName("type")
    @Expose
    private Integer type;

    public List<CategoryList> getList() {
        return list;
    }

    public void setList(List<CategoryList> list) {
        this.list = list;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
