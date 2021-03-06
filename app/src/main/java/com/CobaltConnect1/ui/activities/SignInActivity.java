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
import com.CobaltConnect1.generated.model.RegisterRequest;
import com.CobaltConnect1.generated.model.RegisterRequestResponse;
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
    private RetrofitInterface.MerchantRegisterRequestClient RegisterRequestAdapter;
    Button btSignIn ,btSignUp;
    TextView tvSignUp, tvForgotPassword;
    EditText etCobaltPaymentId;
    EditText etPassword;
    CheckBox cbRememberMe;
    Boolean signUpRequest =false;
    String cobaltId,userPassword,userName,userBusinessName,userContactNo,userEmail,userRegistrationResponse,userRegisPassword,userRegisRePassword;
    LinearLayout loginView,signUpView,registerRequestView;
    View viewBelowlogin,viewBelowSignUp;
    TextView tvLoginHeader,tvregistrationResponse;
    EditText etUserName,etuserBusinessName,etuserContactNo,etuserEmail,etRegistrationPassword,etRePassword;




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
        registerRequestView = (LinearLayout) findViewById(R.id.registration_request_confirmation);
        viewBelowSignUp = (View) findViewById(R.id.view_below_signup);
        viewBelowlogin = (View) findViewById(R.id.view_below_login);
        tvLoginHeader = (TextView) findViewById(R.id.login_header);
        tvregistrationResponse = (TextView) findViewById(R.id.registration_response);
        etUserName = (EditText) findViewById(R.id.userName);
        etuserBusinessName = (EditText) findViewById(R.id.business_name);
        etuserEmail = (EditText) findViewById(R.id.user_email);
        etuserContactNo = (EditText) findViewById(R.id.contact_no);
        etRegistrationPassword = (EditText) findViewById(R.id.password);
        etRePassword = (EditText) findViewById(R.id.re_password);

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
        RegisterRequestAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantRegisterRequestClient.class, BASE_URL, SignInActivity.this);
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
                            if (response.body().getCloverId() == null)
                            {
                                Intent intent = new Intent(SignInActivity.this, CloverAuthActivity.class);
                                startActivity(intent);


                            }
                            else {
                                PrefUtils.storeCloverId(response.body().getCloverId(), SignInActivity.this);
                                Intent intent = new Intent(SignInActivity.this, NavigationalDrawerActivity.class);
                                startActivity(intent);
                                finish();
                            }
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
        Call<RegisterRequestResponse> call = RegisterRequestAdapter.merchantRegisterRequest(new RegisterRequest(userName,userBusinessName,userEmail,userContactNo,userRegisPassword,userRegisRePassword,"registration-request"));
        if (NetworkUtils.isNetworkConnected(SignInActivity.this)) {
            call.enqueue(new Callback<RegisterRequestResponse>() {

                @Override
                public void onResponse(Call<RegisterRequestResponse> call, Response<RegisterRequestResponse> response) {

                    if (response.isSuccessful()) {


                        if (response.body().getType() ==1) {
                            PrefUtils.storeAuthToken(response.body().getTokenid(), SignInActivity.this);
                            PrefUtils.storeUserName(response.body().getFullName(), SignInActivity.this);
                            Log.e("abhi", "onResponse: tokenid........... " +response.body().getTokenid() );
                            Intent intent = new Intent(SignInActivity.this, CloverAuthActivity.class);
                            startActivity(intent);

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
                public void onFailure(Call<RegisterRequestResponse> call, Throwable t) {
                    Log.e("abhi", "onFailure: "+t.getMessage() );
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
            userRegisPassword = etRegistrationPassword.getText().toString();
            userRegisRePassword = etRePassword.getText().toString();

            if (isRegistrationValid()) {

               getRegisterRequestDetails();
            }
        }

        else  if (view.getId() == R.id.login_header)
        {
            tvSignUp.setEnabled(true);
            registerRequestView.setVisibility(View.GONE);
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
            if (signUpRequest)
            {
                loginView.setVisibility(View.GONE);
                signUpView.setVisibility(View.GONE);
                registerRequestView.setVisibility(View.VISIBLE);
                tvregistrationResponse.setText(userRegistrationResponse);

            }
            else {
                loginView.setVisibility(View.GONE);
                signUpView.setVisibility(View.VISIBLE);
                registerRequestView.setVisibility(View.GONE);
                viewBelowlogin.setVisibility(View.GONE);
                viewBelowSignUp.setVisibility(View.VISIBLE);
                tvSignUp.setTextColor(Color.WHITE);
                tvLoginHeader.setTextColor(Color.parseColor("#757577"));
            }

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
                || userRegisPassword == null || userRegisPassword.equals("")|| userRegisRePassword == null || userRegisRePassword.equals("")|| userEmail.equals("") || userEmail == null || !isValidEmail(userEmail)) {

            if (userName == null || userName.equals(""))
                etUserName.setError(getString(R.string.error_compulsory_field));

            if (userContactNo == null || userContactNo.equals(""))
                etuserContactNo.setError(getString(R.string.error_compulsory_field));

            if (userRegisPassword == null || userRegisPassword.equals(""))
                etRegistrationPassword.setError(getString(R.string.error_compulsory_field));

            if (userRegisRePassword == null || userRegisRePassword.equals(""))
                etRePassword.setError(getString(R.string.error_compulsory_field));

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
