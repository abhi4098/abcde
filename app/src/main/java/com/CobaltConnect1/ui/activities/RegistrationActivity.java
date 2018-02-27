package com.CobaltConnect1.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.CobaltConnect1.R;
import com.CobaltConnect1.api.ApiAdapter;
import com.CobaltConnect1.api.RetrofitInterface;
import com.CobaltConnect1.generated.model.Registration;
import com.CobaltConnect1.generated.model.RegistrationResponse;
import com.CobaltConnect1.generated.model.StateList;
import com.CobaltConnect1.generated.model.StateListResponse;
import com.CobaltConnect1.utils.NetworkUtils;
import com.CobaltConnect1.utils.PrefUtils;
import com.CobaltConnect1.utils.SnakBarUtils;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.CobaltConnect1.api.ApiEndPoints.BASE_URL;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private RetrofitInterface.MerchantRegisterClient SignUpAdapter;
    private RetrofitInterface.MerchantStatesClient StatesAdapter;

    Button btSignUp;
    EditText etUserFullName;
    EditText etUserEmailId;
    EditText etUserCobaltId;
    EditText etUserCloverId;
    EditText etUserCloverApiKey;
    Spinner spUserState;
    EditText etUserPassword;
    CheckBox cbTerms;
    List<String> stateList =  new ArrayList<>();
    String stateName;
    String[] statesName;
    boolean isPasswordValid =false;

    String userCobaltId,userPassword,userFullName,userEmail,userCloverId,userCloverApiKey,userState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        btSignUp = (Button) findViewById(R.id.button_sign_up);
        cbTerms = (CheckBox) findViewById(R.id.checkbox_terms);
        etUserFullName = (EditText) findViewById(R.id.user_name);
        etUserEmailId = (EditText) findViewById(R.id.user_email);
        etUserCobaltId = (EditText) findViewById(R.id.user_cobalt_id);
        etUserCloverId = (EditText) findViewById(R.id.user_clover_Id);
        etUserCloverApiKey = (EditText) findViewById(R.id.user_Clover_Api_key);
        etUserPassword = (EditText) findViewById(R.id.user_add_password);
        spUserState = (Spinner) findViewById(R.id.user_state);
        btSignUp.setOnClickListener(this);
        setUpRestAdapter();
        getStateList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, stateList);
        spUserState.setPrompt("Select");
        spUserState.setAdapter(adapter);



    }

    private void getStateList() {
        Call<StateListResponse> call = StatesAdapter.merchantState(new StateList("stateListCustom"));
        if (NetworkUtils.isNetworkConnected(RegistrationActivity.this)) {
            call.enqueue(new Callback<StateListResponse>() {

                @Override
                public void onResponse(Call<StateListResponse> call, Response<StateListResponse> response) {

                    if (response.isSuccessful()) {


                        if (response.body()!=null) {
                            stateList.add("Select");

                         for (int i=0; i<response.body().getStates().size(); i++)
                         {
                            stateName = response.body().getStates().get(i).getName();
                             stateList.add(stateName);
                             Log.e("abhi", "onResponse: " +stateList.get(i) );
                         }

                            statesName = new String[stateList.size()];
                            statesName = stateList.toArray(statesName);

                            setStateSpinner(statesName);

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Invalid Details",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<StateListResponse> call, Throwable t) {

                }


            });

        } else {
            SnakBarUtils.networkConnected(RegistrationActivity.this);
        }


    }



    private void setStateSpinner(String[] statesName) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, statesName);
        spUserState.setAdapter(adapter);
    }

    private void setUpRestAdapter() {
        SignUpAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantRegisterClient.class, BASE_URL, RegistrationActivity.this);
        StatesAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantStatesClient.class, BASE_URL, RegistrationActivity.this);
    }


    private void getRegistrationDetails() {
        Log.e("abhi", "getRegistrationDetails: ...." );
        LoadingDialog.showLoadingDialog(this,"Loading...");
        Call<RegistrationResponse> call = SignUpAdapter.merchantSignUp(new Registration(userFullName, userEmail, userCobaltId, userCloverId, userCloverApiKey, userPassword, userState,"registration"));
        if (NetworkUtils.isNetworkConnected(RegistrationActivity.this)) {
            call.enqueue(new Callback<RegistrationResponse>() {

                @Override
                public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {

                    if (response.isSuccessful()) {


                        if (response.body().getTokenid() !=null) {

                            if (response.body().getType() == 1) {
                                PrefUtils.storeAuthToken(response.body().getTokenid(), RegistrationActivity.this);
                                Intent intent = new Intent(RegistrationActivity.this, NavigationalDrawerActivity.class);
                                startActivity(intent);
                                LoadingDialog.cancelLoading();
                            }
                            else
                                Toast.makeText(getApplicationContext(),"You have already registered with given Cobalt Id!",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Invalid Details",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegistrationResponse> call, Throwable t) {

                }


            });

        } else {
            SnakBarUtils.networkConnected(RegistrationActivity.this);
        }
    }

    @Override
    public void onClick(View view) {

        userFullName = etUserFullName.getText().toString();
        userEmail = etUserEmailId.getText().toString();
        userCobaltId = etUserCobaltId.getText().toString();
        userState = spUserState.getSelectedItem().toString();
        userPassword = etUserPassword.getText().toString();
        userCloverId = etUserCloverId.getText().toString();
        userCloverApiKey = etUserCloverApiKey.getText().toString();
        PrefUtils.storeUserName(userFullName, RegistrationActivity.this);
        PrefUtils.storeStateId(userState, RegistrationActivity.this);
        PrefUtils.storeEmail(userEmail, RegistrationActivity.this);
        PrefUtils.storeCloverId(userCloverId, RegistrationActivity.this);
        PrefUtils.storeCloverToken(userCloverApiKey, RegistrationActivity.this);
        String password = (etUserPassword.getText().toString());

        if(password.length()>5 && !password.equals(password.toLowerCase()) &&
                !password.equals(password.toUpperCase()) &&
                password.matches(".*\\d+.*")  ){
            isPasswordValid = true;

        }else{

          isPasswordValid = false;
            etUserPassword.setError(getString(R.string.error_password_field));
        }

        if (isRegistrationValid() && isPasswordValid) {
            Log.e("abhi", "onClick: ......");
            getRegistrationDetails();
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean isRegistrationValid() {

            if (userFullName == null || userFullName.equals("") || userCobaltId == null || userCobaltId.equals("") ||
                userState == null || userState.equals("") || userState.equals("Select")|| userPassword == null || userPassword.equals("") ||
                userCloverId == null || userCloverId.equals("") || userCloverApiKey == null || userCloverApiKey.equals("") ||
                    userEmail == null || userEmail.equals("")  || !isValidEmail(userEmail) || !cbTerms.isChecked())

            {

                if (userFullName == null || userFullName.equals("") )
                   etUserFullName.setError(getString(R.string.error_compulsory_field));

                if (userCobaltId == null || userCobaltId.equals(""))
                    etUserCobaltId.setError(getString(R.string.error_compulsory_field));

                if ( userState == null || userState.equals("")  || userState.equals("Select"))
                    ((TextView)spUserState.getSelectedView()).setError(getString(R.string.error_spinner_field));

                if ( userPassword == null || userPassword.equals(""))
                    etUserPassword.setError(getString(R.string.error_compulsory_field));

                if ( userCloverId == null || userCloverId.equals(""))
                    etUserCloverId.setError(getString(R.string.error_compulsory_field));

                if (userCloverApiKey == null || userCloverApiKey.equals(""))
                    etUserCloverApiKey.setError(getString(R.string.error_compulsory_field));

                if (userEmail == null || userEmail.equals("") )
                    etUserEmailId.setError(getString(R.string.error_compulsory_field));

                if (!isValidEmail(userEmail) )
                    etUserEmailId.setError("Invalid Email");

                return false;
           } else
                return true;

    }
}
