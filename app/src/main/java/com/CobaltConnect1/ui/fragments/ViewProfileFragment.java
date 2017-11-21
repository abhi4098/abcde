package com.CobaltConnect1.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.CobaltConnect1.R;
import com.CobaltConnect1.api.ApiAdapter;
import com.CobaltConnect1.api.RetrofitInterface;
import com.CobaltConnect1.generated.model.Profile;
import com.CobaltConnect1.generated.model.ProfileResponse;
import com.CobaltConnect1.ui.activities.LoadingDialog;
import com.CobaltConnect1.utils.NetworkUtils;
import com.CobaltConnect1.utils.PrefUtils;
import com.CobaltConnect1.utils.SnakBarUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.CobaltConnect1.api.ApiEndPoints.BASE_URL;


public class ViewProfileFragment extends Fragment {

    private RetrofitInterface.MerchantProfileClient ProfileAdapter;
    TextView tvFullName, tvTokenId, tvEmailId, tvCloverId ,tvCloverToken, tvState;

    public ViewProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_view_profile, container, false);
        tvFullName = (TextView) rootView.findViewById(R.id.full_name);
        tvTokenId = (TextView) rootView.findViewById(R.id.token_id);
        tvEmailId = (TextView) rootView.findViewById(R.id.email_id);
        tvCloverId = (TextView) rootView.findViewById(R.id.clover_id);
        tvCloverToken = (TextView) rootView.findViewById(R.id.clover_token);
        tvState = (TextView) rootView.findViewById(R.id.state_id);
        LoadingDialog.showLoadingDialog(getActivity(),"Loading...");
        setUpRestAdapter();
        getProfileDetails();


        return rootView;
    }

    private void getProfileDetails() {
        Call<ProfileResponse> call = ProfileAdapter.merchantProfile(new Profile(PrefUtils.getAuthToken(getActivity()),"profile"));
        if (NetworkUtils.isNetworkConnected(getActivity())) {
            call.enqueue(new Callback<ProfileResponse>() {

                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(getActivity(),"profile Details",Toast.LENGTH_SHORT).show();
                        PrefUtils.storeUserName(response.body().getFullName(), getActivity());
                        Log.e("abhi", "onResponse: username------ "+response.body().getFullName() );
                        PrefUtils.storeStateId(response.body().getStateid(), getActivity());
                        Log.e("abhi", "onResponse: stateid------ "+response.body().getStateid() );
                        PrefUtils.storeEmail(response.body().getEmailId(), getActivity());
                        Log.e("abhi", "onResponse: email------ "+response.body().getEmailId() );
                        PrefUtils.storeAuthToken(response.body().getTokenid(), getActivity());
                        Log.e("abhi", "onResponse: auth token------ "+response.body().getTokenid() );
                        PrefUtils.storeCloverId(response.body().getCloverId(), getActivity());
                        Log.e("abhi", "onResponse: cloverid------ "+response.body().getCloverId() );
                        PrefUtils.storeCloverToken(response.body().getCloverToken(), getActivity());
                        Log.e("abhi", "onResponse: clovertoken------ "+response.body().getCloverToken() );
                        setProfileDetails();


                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {

                }


            });

        } else {
            SnakBarUtils.networkConnected(getActivity());
        }
    }

    private void setProfileDetails() {
        tvFullName.setText(PrefUtils.getUserName(getActivity()));
        tvTokenId.setText(PrefUtils.getAuthToken(getActivity()));
        tvEmailId.setText(PrefUtils.getEmail(getActivity()));
        tvCloverId.setText(PrefUtils.getCloverId(getActivity()));
        tvCloverToken.setText(PrefUtils.getCloverToken(getActivity()));
        tvState.setText(PrefUtils.getStateId(getActivity()));
        LoadingDialog.cancelLoading();
    }

    private void setUpRestAdapter() {
        ProfileAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantProfileClient.class, BASE_URL, getActivity());
    }






}
