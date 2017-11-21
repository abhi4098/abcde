package com.CobaltConnect1.generated.parser;
import com.CobaltConnect1.generated.model.Login;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by abhinandan on 6/4/15.
 */
public class UserProfileDeserializer implements JsonDeserializer<Login.OTPResponse> {

    @Override
    public Login.OTPResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new GsonBuilder().create();
        Login.OTPResponse otpResponse = gson.fromJson(json, Login.OTPResponse.class);
        JsonObject jsonObject = json.getAsJsonObject();


        return otpResponse;
    }
}
