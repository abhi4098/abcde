package com.CobaltConnect1.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    TextView tvFullName, tvPhoneNum, tvEmailId, tvAdd1,tvAdd2,tvAdd3 ,tvCity, tvState,tvcountry;
    LinearLayout llAdd3,llAdd2;

    public ViewProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_view_profile, container, false);
        tvFullName = (TextView) rootView.findViewById(R.id.full_name);
        tvPhoneNum = (TextView) rootView.findViewById(R.id.phone_num);
        tvEmailId = (TextView) rootView.findViewById(R.id.email_id);
        tvCity = (TextView) rootView.findViewById(R.id.city);
        tvcountry = (TextView) rootView.findViewById(R.id.country);
        tvState = (TextView) rootView.findViewById(R.id.state);
        tvAdd1 = (TextView) rootView.findViewById(R.id.address1);
        tvAdd2 = (TextView) rootView.findViewById(R.id.address2);
        tvAdd3 = (TextView) rootView.findViewById(R.id.address3);

        llAdd2 = (LinearLayout) rootView.findViewById(R.id.ll_add2);
        llAdd3 = (LinearLayout) rootView.findViewById(R.id.ll_add3);
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
                        PrefUtils.storeStateId(response.body().getStateid(), getActivity());
                        PrefUtils.storeEmail(response.body().getEmailId(), getActivity());
                        PrefUtils.storeAuthToken(response.body().getTokenid(), getActivity());
                        PrefUtils.storeCloverId(response.body().getCloverId(), getActivity());
                        PrefUtils.storeCloverToken(response.body().getCloverToken(), getActivity());
                        setProfileDetails(response);


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

    private void setProfileDetails(Response<ProfileResponse> response) {
        tvFullName.setText(PrefUtils.getUserName(getActivity()));
        tvPhoneNum.setText(response.body().getPhoneNumber());
        tvEmailId.setText(PrefUtils.getEmail(getActivity()));
        tvCity.setText(response.body().getCity());
        tvcountry.setText(response.body().getCountry());
        tvState.setText(response.body().getState());
        tvAdd1.setText(response.body().getAddress1());
        if (response.body().getAddress2() !=null)
        {
            llAdd2.setVisibility(View.VISIBLE);
            tvAdd2.setText(response.body().getAddress2());
        }

        if (response.body().getAddress3() !=null)
        {
            llAdd3.setVisibility(View.VISIBLE);
            tvAdd3.setText(response.body().getAddress3());
        }



        LoadingDialog.cancelLoading();
    }

    private void setUpRestAdapter() {
        ProfileAdapter = ApiAdapter.createRestAdapter(RetrofitInterface.MerchantProfileClient.class, BASE_URL, getActivity());
    }






}
