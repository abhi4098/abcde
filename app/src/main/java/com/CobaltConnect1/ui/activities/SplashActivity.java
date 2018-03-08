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
    private CloverAuth.AuthResult mCloverAuth;
    private MerchantConnector merchantConnector;
    private static int SPLASH_TIME_OUT = 2000;
    private static final String TAG = LogUtils.makeLogTag(SplashActivity.class);
    public static final int OAUTH_REQUEST_CODE = 0;
    private RetrofitInterface.MerchantOauthClient CloverOauthAdapter;

    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String MERCHANT_ID_KEY = "merchant_id";
    public static final String EMPLOYEE_ID_KEY = "employee_id";

    String token,merchantId,employeeId;
   // private Account account;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == OAUTH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            // Access data from the completed intent
            token = data.getStringExtra(ACCESS_TOKEN_KEY);
            merchantId = data.getStringExtra(MERCHANT_ID_KEY);
            employeeId = data.getStringExtra(EMPLOYEE_ID_KEY);
           // Toast.makeText(SplashActivity.this, token, Toast.LENGTH_LONG).show();
            Log.e("abhi", "onActivityResult: token" +token + " merchantid" + merchantId + " employeeid" + employeeId  );
            setUpRestAdapter();
            getOauthDetails();
        }
        else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void getOauthDetails() {
        LoadingDialog.showLoadingDialog(this,"Loading...");
        Call<OauthVerificationResponse> call = CloverOauthAdapter.merchantOauth(new OauthVerification(merchantId,employeeId,token,"oauth"));
        if (NetworkUtils.isNetworkConnected(SplashActivity.this)) {
            call.enqueue(new Callback<OauthVerificationResponse>() {

                @Override
                public void onResponse(Call<OauthVerificationResponse> call, Response<OauthVerificationResponse> response) {

                    if (response.isSuccessful()) {


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
                Log.e(TAG, "onResume: ...."+mAccount );

                if (mAccount == null) {
                    Toast.makeText(this, "No Account", Toast.LENGTH_SHORT).show();
                    finish();
                }

                // Use this account to get the access token (and other Clover authentication data)
                getCloverAuth();
            }

            connect();

            // Get the merchant object
            getMerchant();
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
                    Log.e(TAG, "Authentication cancelled", e);
                } catch (Exception e) {
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
                    Toast.makeText(getApplicationContext(), mCloverAuth.authToken, Toast.LENGTH_SHORT).show();

                   // mToken.setText(getString(R.string.token) + mCloverAuth.authToken);
                } else {
                    Log.e(TAG, "onPostExecute: ....auth_token_error");
                    Toast.makeText(getApplicationContext(), "auth_token_error", Toast.LENGTH_SHORT).show();
                   // mToken.setText(getString(R.string.auth_error));
                }
            }
        }.execute();
    }
    @Override
    protected void onPause() {
        disconnect();
        super.onPause();
    }

    private void connect() {
        disconnect();
        if (mAccount != null) {
            merchantConnector = new MerchantConnector(this, mAccount, null);
            merchantConnector.connect();
        }
    }

    private void disconnect() {
        if (merchantConnector != null) {
            merchantConnector.disconnect();
            merchantConnector = null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getMerchant() {

        new AsyncTask<Void, Void, Merchant>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(getApplicationContext(), "merchant_id_pre_error", Toast.LENGTH_SHORT).show();
                // Show progressBar while waiting
                //progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected Merchant doInBackground(Void... params) {
                Merchant merchant = null;
                try {
                    merchant = merchantConnector.getMerchant();
                    Toast.makeText(getApplicationContext(), "merchant_id_error", Toast.LENGTH_SHORT).show();

                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (ClientException e) {
                    e.printStackTrace();
                } catch (ServiceException e) {
                    e.printStackTrace();
                } catch (BindingException e) {
                    e.printStackTrace();
                }
                return merchant;
            }

            @Override
            protected void onPostExecute(Merchant merchant) {
                super.onPostExecute(merchant);

                if (!isFinishing()) {
                    // Populate the merchant information
                    if (merchant != null) {
                        Log.e(TAG, "onPostExecute: merchant.id" +merchant.getId());
                        merchantId =merchant.getId();
                        Toast.makeText(getApplicationContext(), merchant.getId(), Toast.LENGTH_SHORT).show();
                        setUpRestAdapter();
                        getOauthDetails();
                        /*merchantName.setText(merchant.getName());
                        address1.setText(merchant.getAddress().getAddress1());
                        address2.setText(merchant.getAddress().getAddress2());
                        address3.setText(merchant.getAddress().getAddress3());
                        city.setText(merchant.getAddress().getCity());
                        state.setText(merchant.getAddress().getState());
                        zip.setText(merchant.getAddress().getZip());
                        country.setText(merchant.getAddress().getCountry());
                        phone.setText(merchant.getPhoneNumber());*/
                    }

                    // Hide the progressBar
                //    progressBar.setVisibility(View.GONE);
                }
            }
        }.execute();
    }
}
