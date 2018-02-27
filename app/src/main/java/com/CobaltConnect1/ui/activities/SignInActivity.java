package com.CobaltConnect1.ui.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.CobaltConnect1.R;
import com.CobaltConnect1.api.ApiAdapter;
import com.CobaltConnect1.api.RetrofitInterface;
import com.CobaltConnect1.generated.model.SignIn;
import com.CobaltConnect1.generated.model.SignInResponse;
import com.CobaltConnect1.utils.NetworkUtils;
import com.CobaltConnect1.utils.PrefUtils;
import com.CobaltConnect1.utils.SnakBarUtils;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.CobaltConnect1.api.ApiEndPoints.BASE_URL;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private RetrofitInterface.MerchantLoginClient SignInAdapter;
    Button btSignIn ,btSignUp;
    TextView tvSignUp, tvForgotPassword;
    EditText etCobaltPaymentId;
    EditText etPassword;
    CheckBox cbRememberMe;
    String cobaltId,userPassword,userName,userBusinessName,userContactNo,userEmail;
    LinearLayout loginView,signUpView;
    View viewBelowlogin,viewBelowSignUp;
    TextView tvLoginHeader,tvSignUpHeader;
    EditText etUserName,etuserBusinessName,etuserContactNo,etuserEmail;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        btSignIn = (Button) findViewById(R.id.button_sign_in);
        btSignUp = (Button) findViewById(R.id.button_sign_up);
        cbRememberMe = (CheckBox) findViewById(R.id.checkbox_remember_me);
        tvSignUp = (TextView) findViewById(R.id.text_sign_up);
        tvForgotPassword = (TextView) findViewById(R.id.text_forgot_password);
        etCobaltPaymentId = (EditText) findViewById(R.id.cobalt_payment_id);
        etPassword = (EditText) findViewById(R.id.user_password);
        loginView = (LinearLayout) findViewById(R.id.loginview);
        signUpView = (LinearLayout) findViewById(R.id.signUp_view);
        viewBelowSignUp = (View) findViewById(R.id.view_below_signup);
        viewBelowlogin = (View) findViewById(R.id.view_below_login);
        tvLoginHeader = (TextView) findViewById(R.id.login_header);
        etUserName = (EditText) findViewById(R.id.userName);
        etuserBusinessName = (EditText) findViewById(R.id.business_name);
        etuserEmail = (EditText) findViewById(R.id.user_email);
        etuserContactNo = (EditText) findViewById(R.id.contact_no);
        btSignIn.setOnClickListener(this);
        btSignUp.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        tvLoginHeader.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE, -1);
        Log.e("abhi", "onCreate: " +dateFormat.format(cal.getTime()));


        if (PrefUtils.getUserCobaltId(getBaseContext()) != null)
        {
            etCobaltPaymentId.setText(PrefUtils.getUserCobaltId(getBaseContext()));
        }

        if (PrefUtils.getUserPassword(getBaseContext()) != null)
        {
            etPassword.setText(PrefUtils.getUserPassword(getBaseContext()));
        }
        setUpRestAdapter();
        etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }


    private void setUpRestAdapter() {
        SignInAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantLoginClient.class, BASE_URL, SignInActivity.this);
    }


    private void getSignInDetails() {
        LoadingDialog.showLoadingDialog(this,"Loading...");
        Call<SignInResponse> call = SignInAdapter.merchantSignIn(new SignIn(cobaltId,userPassword,"signin"));
        if (NetworkUtils.isNetworkConnected(SignInActivity.this)) {
            call.enqueue(new Callback<SignInResponse>() {

                @Override
                public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {

                    if (response.isSuccessful()) {


                        if (response.body().getTokenid() !=null) {
                            PrefUtils.storeAuthToken(response.body().getTokenid(), SignInActivity.this);
                            PrefUtils.storeUserName(response.body().getFullName(), SignInActivity.this);
                            Intent intent = new Intent(SignInActivity.this, NavigationalDrawerActivity.class);
                            startActivity(intent);
                            LoadingDialog.cancelLoading();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Invalid Details",Toast.LENGTH_SHORT).show();
                            LoadingDialog.cancelLoading();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SignInResponse> call, Throwable t) {
                    LoadingDialog.cancelLoading();
                }


            });

        } else {
            SnakBarUtils.networkConnected(SignInActivity.this);
            LoadingDialog.cancelLoading();
        }
    }


    private void getRegisterRequestDetails() {
        LoadingDialog.showLoadingDialog(this,"Loading...");
        Call<SignInResponse> call = SignInAdapter.merchantSignIn(new SignIn(cobaltId,userPassword,"signin"));
        if (NetworkUtils.isNetworkConnected(SignInActivity.this)) {
            call.enqueue(new Callback<SignInResponse>() {

                @Override
                public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {

                    if (response.isSuccessful()) {


                        if (response.body().getTokenid() !=null) {
                            PrefUtils.storeAuthToken(response.body().getTokenid(), SignInActivity.this);
                            PrefUtils.storeUserName(response.body().getFullName(), SignInActivity.this);
                            Intent intent = new Intent(SignInActivity.this, NavigationalDrawerActivity.class);
                            startActivity(intent);
                            LoadingDialog.cancelLoading();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Invalid Details",Toast.LENGTH_SHORT).show();
                            LoadingDialog.cancelLoading();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SignInResponse> call, Throwable t) {
                    LoadingDialog.cancelLoading();
                }


            });

        } else {
            SnakBarUtils.networkConnected(SignInActivity.this);
            LoadingDialog.cancelLoading();
        }
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button_sign_in) {

            /*Intent intent = new Intent(SignInActivity.this, NavigationalDrawerActivity.class);
            startActivity(intent);*/


            cobaltId = etCobaltPaymentId.getText().toString();
            userPassword = etPassword.getText().toString();

            if (cbRememberMe.isChecked())
            {
                PrefUtils.storeUserCobaltId(cobaltId, SignInActivity.this);
                PrefUtils.storeUserPassword(userPassword, SignInActivity.this);
            }

            if (isSignUpValid()) {
                getSignInDetails();
            }
        }

        else  if (view.getId() == R.id.button_sign_up)
        {

            userName = etUserName.getText().toString();
            userBusinessName = etuserBusinessName.getText().toString();
            userContactNo = etuserContactNo.getText().toString();
            userEmail = etuserEmail.getText().toString();

            if (isRegistrationValid()) {
                getRegisterRequestDetails();
            }
        }

        else  if (view.getId() == R.id.login_header)
        {
            loginView.setVisibility(View.VISIBLE);
            signUpView.setVisibility(View.GONE);
            viewBelowlogin.setVisibility(View.VISIBLE);
            viewBelowSignUp.setVisibility(View.GONE);
            tvSignUp.setTextColor(Color.parseColor("#757577"));
            tvLoginHeader.setTextColor(Color.WHITE);
        }

        else  if (view.getId() == R.id.text_forgot_password)
        {
            Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        }
        else
        {
             loginView.setVisibility(View.GONE);
             signUpView.setVisibility(View.VISIBLE);
             viewBelowlogin.setVisibility(View.GONE);
             viewBelowSignUp.setVisibility(View.VISIBLE);
             tvSignUp.setTextColor(Color.WHITE);
             tvLoginHeader.setTextColor(Color.parseColor("#757577"));

        }
    }

    private boolean isSignUpValid() {

        if (cobaltId == null || cobaltId.equals("") || userPassword == null || userPassword.equals("")) {
            if (cobaltId == null || cobaltId.equals(""))
                etCobaltPaymentId.setError(getString(R.string.error_compulsory_field));

            if (userPassword == null || userPassword.equals(""))
                etPassword.setError(getString(R.string.error_compulsory_field));

            return false;
        } else
            return true;
    }


    private boolean isRegistrationValid() {

        if (userName == null || userName.equals("") || userContactNo == null || userContactNo.equals("")
                || userEmail.equals("") || userEmail == null || !isValidEmail(userEmail)) {

            if (userName == null || userName.equals(""))
                etUserName.setError(getString(R.string.error_compulsory_field));

            if (userContactNo == null || userContactNo.equals(""))
                etuserContactNo.setError(getString(R.string.error_compulsory_field));

            if ( userEmail == null || userEmail.equals(""))
                etuserEmail.setError(getString(R.string.error_compulsory_field));

            if (!isValidEmail(userEmail) )
                etuserEmail.setError("Invalid Email");

            return false;
        } else
            return true;
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
