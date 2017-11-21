package com.CobaltConnect1.api;

import android.content.Context;

import com.CobaltConnect1.generated.model.Login;
import com.CobaltConnect1.generated.parser.UserProfileDeserializer;
import com.CobaltConnect1.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiAdapter {

    public final String TAG = LogUtils.makeLogTag(ApiAdapter.class);

    private ApiAdapter() {
    }


    public static <A> A createRestAdapter(Class<A> AdapterClass, String baseUrl, Context context){


        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Login.class,new UserProfileDeserializer());
        Gson gson = gsonBuilder.serializeNulls().create();

        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(Client.getClientInstance(context))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return restAdapter.create(AdapterClass);


    }


}
