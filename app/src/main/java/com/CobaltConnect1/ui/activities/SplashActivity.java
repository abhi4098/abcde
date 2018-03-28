package com.CobaltConnect1.ui.activities;


import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.CobaltConnect1.R;
import com.CobaltConnect1.api.ApiAdapter;
import com.CobaltConnect1.api.RetrofitInterface;
import com.CobaltConnect1.generated.model.OauthVerification;
import com.CobaltConnect1.generated.model.OauthVerificationResponse;
import com.CobaltConnect1.utils.LogUtils;
import com.CobaltConnect1.utils.NetworkUtils;
import com.CobaltConnect1.utils.PrefUtils;
import com.CobaltConnect1.utils.SnakBarUtils;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.util.CloverAuth;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v1.merchant.Merchant;
import com.clover.sdk.v1.merchant.MerchantConnector;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.CobaltConnect1.api.ApiEndPoints.BASE_URL;


public class SplashActivity extends Activity {


    private Account mAccount;
    TextView errorText;

    private CloverAuth.AuthResult mCloverAuth;
    private MerchantConnector merchantConnector;
    private static int SPLASH_TIME_OUT = 2000;
    private static final String TAG = LogUtils.makeLogTag(SplashActivity.class);
    public static final int OAUTH_REQUEST_CODE = 0;
    private RetrofitInterface.MerchantOauthClient CloverOauthAdapter;

    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String MERCHANT_ID_KEY = "merchant_id";
    public static final String EMPLOYEE_ID_KEY = "employee_id";

    String token,merchantId,accountDetails,userName,userEmail;

   // private Account account;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        errorText = (TextView) findViewById(R.id.error_text);



    }

    private void getOauthDetails() {
        LoadingDialog.showLoadingDialog(this,"Loading...");
        Call<OauthVerificationResponse> call = CloverOauthAdapter.merchantOauth(new OauthVerification(merchantId,accountDetails,token,"oauth"));
        if (NetworkUtils.isNetworkConnected(SplashActivity.this)) {
            call.enqueue(new Callback<OauthVerificationResponse>() {

                @Override
                public void onResponse(Call<OauthVerificationResponse> call, Response<OauthVerificationResponse> response) {

                    if (response.isSuccessful()) {

                        Log.e(TAG, "onResponse: ........."+response.body().getTokenid() );
                        if (response.body().getTokenid() !=null) {
                            Log.e(TAG, "onResponse: ........."+response.body().getCloverId() );
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
                            errorText.setText(response.body().getMsg());
                            LoadingDialog.cancelLoading();
                        }
                    }
                }

                @Override
                public void onFailure(Call<OauthVerificationResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    errorText.setText(t.getMessage());
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

    private void setUpRestAdapter() {
        CloverOauthAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantOauthClient.class, BASE_URL, SplashActivity.this);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (PrefUtils.getCloverId(this).equals(""))
        {
            if (mAccount == null) {
                mAccount = CloverAccount.getAccount(this);

                accountDetails = mAccount.name;
                String currentAccount = accountDetails;
                Log.e(TAG, "onResume: ...account"+ accountDetails );
                if (currentAccount !=null) {
                    String[] separated = currentAccount.split(" | ");
                    userName= separated[0].concat(" ").concat(separated[1]);
                    PrefUtils.storeUserName(userName, SplashActivity.this);
                    userEmail= separated[3];
                    Log.e(TAG, "onResume: user details " +userName + " " + userEmail);
                }

                if (mAccount == null) {
                    Toast.makeText(this, "No Account", Toast.LENGTH_SHORT).show();
                    finish();
                }

                // Use this account to get the access token (and other Clover authentication data)
                getCloverAuth();
            }

       }
        else {
            Log.e("abhi", "onCreate: ..............inside else"+PrefUtils.getCloverId(this) );
            splashTimer();
        }
        // Retrieve the Clover account

    }

    @SuppressLint("StaticFieldLeak")
    private void getCloverAuth() {
        // This needs to be done on a background thread
        new AsyncTask<Void, Void, CloverAuth.AuthResult>() {
            @Override
            protected CloverAuth.AuthResult doInBackground(Void... params) {
                try {
                    return CloverAuth.authenticate(SplashActivity.this, mAccount);
                } catch (OperationCanceledException e) {
                    errorText.setText("Authentication cancelled");
                    Log.e(TAG, "Authentication cancelled", e);
                } catch (Exception e) {
                    errorText.setText("Error retrieving authentication");
                    Log.e(TAG, "Error retrieving authentication", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(CloverAuth.AuthResult result) {
                mCloverAuth = result;
                Log.e(TAG, "onPostExecute: result"+result );

                // To get a valid auth result you need to have installed the app from the App Market. The Clover servers
                // only creates the token once installed the first time.
                if (mCloverAuth != null && mCloverAuth.authToken !=null) {
                    Log.e(TAG, "onPostExecute: ....auth token" + mCloverAuth.authToken);
                    token  = mCloverAuth.authToken;
                    merchantId =mCloverAuth.merchantId;
                    Log.e(TAG, "onPostExecute: ....auth token" + mCloverAuth.merchantId);
                    //Toast.makeText(getApplicationContext(), mCloverAuth.authToken, Toast.LENGTH_SHORT).show();

                    setUpRestAdapter();
                    getOauthDetails();
                } else {
                    Log.e(TAG, "onPostExecute: ....auth_token_error");
                   Toast.makeText(getApplicationContext(), "auth_token_error", Toast.LENGTH_SHORT).show();
                   errorText.setText("auth token error");
                }
            }
        }.execute();
    }

}
