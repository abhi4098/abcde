package com.CobaltConnect1.api;


import com.CobaltConnect1.generated.model.CategoryListResponse;
import com.CobaltConnect1.generated.model.DashboardData;
import com.CobaltConnect1.generated.model.DashboardDataResponse;
import com.CobaltConnect1.generated.model.DefaultMarginUpdate;
import com.CobaltConnect1.generated.model.ForgotPassword;
import com.CobaltConnect1.generated.model.ForgotPasswordResponse;
import com.CobaltConnect1.generated.model.MarginUpdate;
import com.CobaltConnect1.generated.model.MarginUpdateAll;
import com.CobaltConnect1.generated.model.MarginUpdateAllResponse;
import com.CobaltConnect1.generated.model.MarginUpdateResponse;
import com.CobaltConnect1.generated.model.MinimumStockUpdate;
import com.CobaltConnect1.generated.model.MyCloverProduct;
import com.CobaltConnect1.generated.model.MyCloverProductResponse;
import com.CobaltConnect1.generated.model.OauthVerification;
import com.CobaltConnect1.generated.model.OauthVerificationResponse;
import com.CobaltConnect1.generated.model.ProductUpdate;
import com.CobaltConnect1.generated.model.ProductUpdateResponse;
import com.CobaltConnect1.generated.model.Profile;
import com.CobaltConnect1.generated.model.ProfileResponse;
import com.CobaltConnect1.generated.model.RegisterRequest;
import com.CobaltConnect1.generated.model.RegisterRequestResponse;
import com.CobaltConnect1.generated.model.Registration;
import com.CobaltConnect1.generated.model.RegistrationResponse;
import com.CobaltConnect1.generated.model.SignIn;
import com.CobaltConnect1.generated.model.SignInResponse;
import com.CobaltConnect1.generated.model.StateList;
import com.CobaltConnect1.generated.model.StateListResponse;
import com.CobaltConnect1.generated.model.UpdateProfile;
import com.CobaltConnect1.generated.model.UpdateProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class RetrofitInterface {

    public interface MerchantLoginClient {

        @POST("api/")
        Call<SignInResponse> merchantSignIn(@Body SignIn signinDetails);

    }

    public interface MerchantRegisterClient {

        @POST("api/")
        Call<RegistrationResponse> merchantSignUp(@Body Registration registrationDetails);

    }

    public interface MerchantOauthClient {

        @POST("api/")
        Call<OauthVerificationResponse> merchantOauth(@Body OauthVerification oauthVerification);

    }

    public interface MerchantRegisterRequestClient {

        @POST("api/")
        Call<RegisterRequestResponse> merchantRegisterRequest(@Body RegisterRequest registerRequest);

    }

    public interface MerchantForgotPasswordClient {

        @POST("api/")
        Call<ForgotPasswordResponse> merchantForgotPassword(@Body ForgotPassword forgotPasswordDetails);

    }

    public interface MerchantStatesClient {

        @POST("api/")
        Call<StateListResponse> merchantState(@Body StateList stateList);

    }

    public interface MerchantProfileClient {

        @POST("api/")
        Call<ProfileResponse> merchantProfile(@Body Profile profile);

    }

    public interface MerchantUpdateProfileClient {

        @POST("api/")
        Call<UpdateProfileResponse> merchantUpdateProfile(@Body UpdateProfile updateProfile);

    }

    public interface MerchantUpdateProductClient {

        @POST("api/")
        Call<ProductUpdateResponse> merchantUpdateProduct(@Body ProductUpdate productUpdate);

    }

    public interface MerchantMyProductClient {

        @POST("api/")
        Call<MyCloverProductResponse> merchantMyProduct(@Body MyCloverProduct myCloverProduct);

    }

    //This api is used for updating margin ,minimum stock and default margin
    public interface MerchantMarginUpdateClient {

        @POST("api/")
        Call<MarginUpdateResponse> merchantMarginUpdate(@Body MarginUpdate marginUpdate);

    }

    public interface MerchantDefaultMarginUpdateClient {

        @POST("api/")
        Call<MarginUpdateResponse> merchantDefaultMarginUpdate(@Body DefaultMarginUpdate marginUpdate);

    }

    public interface MerchantMinStockUpdateClient {

        @POST("api/")
        Call<MarginUpdateResponse> merchantMinStockUpdate(@Body MinimumStockUpdate marginUpdate);

    }

    public interface MerchantUpdateToCloverClient {

        @POST("api/")
        Call<MarginUpdateAllResponse> merchantUpdateToClover(@Body MarginUpdateAll marginUpdate);

    }

    public interface MerchantFetchFromCloverClient {

        @POST("api/")
        Call<MarginUpdateAllResponse> merchantFetchFromClover(@Body MarginUpdateAll marginUpdate);

    }

    public interface DashboardDataClient {

        @POST("api/")
        Call<DashboardDataResponse> merchantDashboardData(@Body DashboardData dashboardData);

    }

    public interface MerchantMyProductCategoryClient {

        @POST("api/")
        Call<CategoryListResponse> merchantMyProductCategory(@Body MyCloverProduct myCloverProduct);

    }
}
