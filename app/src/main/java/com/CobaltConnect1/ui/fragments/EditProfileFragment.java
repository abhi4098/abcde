package com.CobaltConnect1.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.CobaltConnect1.R;
import com.CobaltConnect1.api.ApiAdapter;
import com.CobaltConnect1.api.RetrofitInterface;
import com.CobaltConnect1.generated.model.UpdateProfile;
import com.CobaltConnect1.generated.model.UpdateProfileResponse;
import com.CobaltConnect1.ui.activities.NavigationalDrawerActivity;
import com.CobaltConnect1.utils.NetworkUtils;
import com.CobaltConnect1.utils.PrefUtils;
import com.CobaltConnect1.utils.SnakBarUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.CobaltConnect1.api.ApiEndPoints.BASE_URL;


public class EditProfileFragment extends Fragment implements View.OnClickListener {
    private RetrofitInterface.MerchantUpdateProfileClient UpdateProfileAdapter;
    EditText etUserFullName;
    EditText etUserEmailId;
    EditText etUserToken;
    EditText etUserCloverId;
    EditText etUserCloverApiKey;
    EditText etUserState;
    Button btUpdate;
    String userTokenId,userFullName,userEmail,userCloverId,userCloverApiKey,userState;

    public EditProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        etUserFullName = (EditText) rootView.findViewById(R.id.full_name);
        etUserToken = (EditText) rootView.findViewById(R.id.token_id);
        etUserEmailId = (EditText) rootView.findViewById(R.id.email_id);
        etUserCloverId = (EditText) rootView.findViewById(R.id.clover_id);
        etUserCloverApiKey = (EditText) rootView.findViewById(R.id.clover_token);
        etUserState = (EditText) rootView.findViewById(R.id.state_id);
        btUpdate = (Button) rootView.findViewById(R.id.update_button);
        etUserFullName.setText(PrefUtils.getUserName(getActivity()));
        etUserToken.setText(PrefUtils.getAuthToken(getActivity()));
        etUserEmailId.setText(PrefUtils.getEmail(getActivity()));
        etUserCloverId.setText(PrefUtils.getCloverId(getActivity()));
        etUserCloverApiKey.setText(PrefUtils.getCloverToken(getActivity()));
        etUserState.setText(PrefUtils.getStateId(getActivity()));

        btUpdate.setOnClickListener(this);
        setUpRestAdapter();



        return rootView;
    }

    private void updateProfileDetails() {
        Call<UpdateProfileResponse> call = UpdateProfileAdapter.merchantUpdateProfile(new UpdateProfile(userFullName, userTokenId, userEmail, userCloverId ,userCloverApiKey, userState,"updateProfile"));
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            call.enqueue(new Callback<UpdateProfileResponse>() {

                @Override
                public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {

                    if (response.isSuccessful()) {
                        //getActivity().finish();
                        Intent intent = new Intent(getActivity(), NavigationalDrawerActivity.class);
                        startActivity(intent);

                    }
                }

                @Override
                public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {

                }


            });

        } else {
            SnakBarUtils.networkConnected(getActivity());
        }
    }

    private void setUpRestAdapter() {
        UpdateProfileAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantUpdateProfileClient.class, BASE_URL, getActivity());
    }


    @Override
    public void onClick(View view) {
        userFullName = etUserFullName.getText().toString();
        userEmail = etUserEmailId.getText().toString();
        userTokenId = etUserToken.getText().toString();
        userState = etUserState.getText().toString();
        userCloverId = etUserCloverId.getText().toString();
        userCloverApiKey = etUserCloverApiKey.getText().toString();


        if (isProfileDetailsValid()) {
            updateProfileDetails();
        }

    }

    private boolean isProfileDetailsValid() {
        if (userFullName == null || userFullName.equals("") || userTokenId == null || userTokenId.equals("") ||
                userState == null || userState.equals("") || userCloverId == null || userCloverId.equals("") || userCloverApiKey == null || userCloverApiKey.equals("") ||
                userEmail == null || userEmail.equals("")  || !isValidEmail(userEmail))

        {

            if (userFullName == null || userFullName.equals("") )
                etUserFullName.setError(getString(R.string.error_compulsory_field));

            if (userTokenId == null || userTokenId.equals(""))
                etUserToken.setError(getString(R.string.error_compulsory_field));

            if ( userState == null || userState.equals("") )
                etUserState.setError(getString(R.string.error_compulsory_field));

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

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


}
