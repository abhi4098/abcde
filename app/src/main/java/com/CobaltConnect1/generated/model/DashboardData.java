package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Abhinandan on 22/11/17.
 */

public class DashboardData {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("tokenid")
    @Expose
    private String tokenid;

    public DashboardData(String type, String tokenid)
    {
        this.type = type;
        this.tokenid = tokenid;
    }
}
