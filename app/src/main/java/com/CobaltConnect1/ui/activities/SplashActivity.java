package com.CobaltConnect1.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.CobaltConnect1.R;
import com.CobaltConnect1.utils.LogUtils;
import com.CobaltConnect1.utils.PrefUtils;



public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;
    private static final String TAG = LogUtils.makeLogTag(SplashActivity.class);
  //  private OffSwitchInterface.OffSwitchClient OffSwitchAdapter;
    //String appName = "connectCobalt";
   // private String appVersion = "1.0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashTimer();
        //CheckLogin();
        //SetUpRestAdapter();
        //offSwitch();

    }

  /*  private void offSwitch() {

        Call<OffSwitchResponse> call = OffSwitchAdapter.docAppOffSwitch(appName);
        if (NetworkUtils.isNetworkConnected(getContext())) {
            call.enqueue(new Callback<OffSwitchResponse>() {
                @Override
                public void onResponse(Call<OffSwitchResponse> call, Response<OffSwitchResponse> response) {

                    if (response.isSuccessful()) {

                        String lastSupportedVersion = response.body().getLastSupportedVersion().replace(".", "");
                        String currentAppVersion = appVersion.replace(".", "");

                        if (Integer.parseInt(currentAppVersion) < Integer.parseInt(lastSupportedVersion)) {
                            showUpdateDialog();
                        } else {
                            splashTimer();
                        }
                    }
                }

                @Override
                public void onFailure(Call<OffSwitchResponse> call, Throwable t) {
                }
            });
        } else {
            SnakBarUtils.networkConnected(SplashActivity.this);
        }

    }*/

/*
    private void showUpdateDialog() {
        final Dialog dialog = new Dialog(SplashActivity.this);
        dialog.setContentView(R.layout.update_app_layout);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Button update = (Button) dialog.findViewById(R.id.btn_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(ApiEndPoints.APP_LINK));
                    startActivity(viewIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Unable to Connect Try Again...",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                SplashActivity.this.finish();
            }
        });
        dialog.show();
    }*/


  /*  private void SetUpRestAdapter() {
        OffSwitchAdapter = ApiAdapter.createRestAdapter(OffSwitchInterface.OffSwitchClient.class, ApiEndPoints.BASE_URL, getContext());

    }*/


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
