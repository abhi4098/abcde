package com.CobaltConnect1.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.CobaltConnect1.R;
import com.CobaltConnect1.api.RetrofitInterface;
import com.CobaltConnect1.generated.model.OauthVerification;
import com.CobaltConnect1.generated.model.OauthVerificationResponse;
import com.CobaltConnect1.utils.LogUtils;
import com.CobaltConnect1.utils.NetworkUtils;
import com.CobaltConnect1.utils.PrefUtils;
import com.CobaltConnect1.utils.SnakBarUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private static final String TAG = LogUtils.makeLogTag(SplashActivity.class);
    public static final int OAUTH_REQUEST_CODE = 0;
    private RetrofitInterface.MerchantOauthClient CloverOauthAdapter;

    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String MERCHANT_ID_KEY = "merchant_id";
    public static final String EMPLOYEE_ID_KEY = "employee_id";

    String token,merchantId,employeeId,tokenId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (PrefUtils.getCloverId(this).equals(""))
        {
            Log.e("abhi", "onCreate: ..............inside if"+PrefUtils.getCloverId(this) );
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            startActivityForResult(intent, OAUTH_REQUEST_CODE);
        }
        else {
            Log.e("abhi", "onCreate: ..............inside else"+PrefUtils.getCloverId(this) );
            splashTimer();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == OAUTH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            // Access data from the completed intent
            token = data.getStringExtra(ACCESS_TOKEN_KEY);
            merchantId = data.getStringExtra(MERCHANT_ID_KEY);
            employeeId = data.getStringExtra(EMPLOYEE_ID_KEY);
            Toast.makeText(SplashActivity.this, token, Toast.LENGTH_LONG).show();
            Log.e("abhi", "onActivityResult: token" +token + " merchantid" + merchantId + " employeeid" + employeeId  );

            getOauthDetails();
        }
        else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getOauthDetails() {
        LoadingDialog.showLoadingDialog(this,"Loading...");
        Call<OauthVerificationResponse> call = CloverOauthAdapter.merchantOauth(new OauthVerification(PrefUtils.getAuthToken(this),merchantId,employeeId,token,"oauth"));
        if (NetworkUtils.isNetworkConnected(SplashActivity.this)) {
            call.enqueue(new Callback<OauthVerificationResponse>() {

                @Override
                public void onResponse(Call<OauthVerificationResponse> call, Response<OauthVerificationResponse> response) {

                    if (response.isSuccessful()) {


                        if (response.body().getTokenid() !=null) {
                            PrefUtils.storeAuthToken(response.body().getTokenid(), SplashActivity.this);
                            PrefUtils.storeCloverId(response.body().getCloverId(), SplashActivity.this);
                            Intent intent = new Intent(SplashActivity.this, NavigationalDrawerActivity.class);
                            startActivity(intent);
                            finish();
                            LoadingDialog.cancelLoading();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),response.body().getMsg(),Toast.LENGTH_SHORT).show();
                            LoadingDialog.cancelLoading();
                        }
                    }
                }

                @Override
                public void onFailure(Call<OauthVerificationResponse> call, Throwable t) {
                    LoadingDialog.cancelLoading();
                }


            });

        } else {
            SnakBarUtils.networkConnected(SplashActivity.this);
            LoadingDialog.cancelLoading();
        }
    }

    private void splashTimer() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                CheckLogin();
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void CheckLogin() {
        Boolean isLoggedIn = PrefUtils.getUserLoggedIn(this);
        if (isLoggedIn) {
            Intent i = new Intent(SplashActivity.this, NavigationalDrawerActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(SplashActivity.this, SignInActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
