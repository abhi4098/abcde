package com.CobaltConnect1.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.CobaltConnect1.R;
import com.CobaltConnect1.api.ApiAdapter;
import com.CobaltConnect1.api.RetrofitInterface;
import com.CobaltConnect1.generated.model.OauthVerification;
import com.CobaltConnect1.generated.model.OauthVerificationResponse;
import com.CobaltConnect1.generated.model.SignIn;
import com.CobaltConnect1.generated.model.SignInResponse;
import com.CobaltConnect1.utils.LogUtils;
import com.CobaltConnect1.utils.NetworkUtils;
import com.CobaltConnect1.utils.PrefUtils;
import com.CobaltConnect1.utils.SnakBarUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.CobaltConnect1.api.ApiEndPoints.BASE_URL;


public class CloverAuthActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LogUtils.makeLogTag(CloverAuthActivity.class);
    private RetrofitInterface.MerchantOauthClient CloverOauthAdapter;

    public static final int OAUTH_REQUEST_CODE = 0;

    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String MERCHANT_ID_KEY = "merchant_id";
    public static final String EMPLOYEE_ID_KEY = "employee_id";

    Button oauthVerificationBtn,oathGoBackBtn;
    String token,merchantId,employeeId,tokenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);
        oauthVerificationBtn = (Button) findViewById(R.id.oauth_verification_button);
        oathGoBackBtn = (Button) findViewById(R.id.oauth_go_back);
          oauthVerificationBtn.setOnClickListener(this);
        oathGoBackBtn.setOnClickListener(this);


      //  setUpRestAdapter();
    }


   /* private void setUpRestAdapter() {
        CloverOauthAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantOauthClient.class, BASE_URL, CloverAuthActivity.this);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == OAUTH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            // Access data from the completed intent
            token = data.getStringExtra(ACCESS_TOKEN_KEY);
            merchantId = data.getStringExtra(MERCHANT_ID_KEY);
            employeeId = data.getStringExtra(EMPLOYEE_ID_KEY);
            Toast.makeText(CloverAuthActivity.this, token, Toast.LENGTH_LONG).show();
            Log.e("abhi", "onActivityResult: token" +token + " merchantid" + merchantId + " employeeid" + employeeId  );

           //getOauthDetails();
        }
        else {
            Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }
*/
    /*private void getOauthDetails() {
        LoadingDialog.showLoadingDialog(this,"Loading...");
        Call<OauthVerificationResponse> call = CloverOauthAdapter.merchantOauth(new OauthVerification(PrefUtils.getAuthToken(this),merchantId,employeeId,token,"oauth"));
        if (NetworkUtils.isNetworkConnected(CloverAuthActivity.this)) {
            call.enqueue(new Callback<OauthVerificationResponse>() {

                @Override
                public void onResponse(Call<OauthVerificationResponse> call, Response<OauthVerificationResponse> response) {

                    if (response.isSuccessful()) {


                        if (response.body().getTokenid() !=null) {
                            PrefUtils.storeAuthToken(response.body().getTokenid(), CloverAuthActivity.this);
                            PrefUtils.storeCloverId(response.body().getCloverId(), CloverAuthActivity.this);
                            Intent intent = new Intent(CloverAuthActivity.this, NavigationalDrawerActivity.class);
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
            SnakBarUtils.networkConnected(CloverAuthActivity.this);
            LoadingDialog.cancelLoading();
        }
    }
*/
    @Override
    public void onClick(View view) {
        /*if (view.getId() == R.id.oauth_verification_button) {
            Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
            startActivityForResult(intent, OAUTH_REQUEST_CODE);
        }
        else
        {
            finish();
        }*/

    }
}
