
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

    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("repassword")
    @Expose
    private String repassword;
    public RegisterRequest(String name, String businessname,String email,String contact,String password,String repassword,String type) {
        this.name = name;
        this.businessname = businessname;
        this.email = email;
        this.contact = contact;
        this.password = password;
        this.repassword = repassword;
        this.type = type;
    }

}
