
package com.CobaltConnect1.generated.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("businessname")
    @Expose
    private String businessname;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("type")
    @Expose
    private String type;
    public RegisterRequest(String name, String businessname,String email,String contact,String type) {
        this.name = name;
        this.businessname = businessname;
        this.email = email;
        this.contact = contact;
        this.type = type;
    }

}
