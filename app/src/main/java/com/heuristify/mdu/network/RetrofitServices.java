package com.heuristify.mdu.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitServices {

    @FormUrlEncoded
    @POST("/auth/app/login")
    Call<ResponseBody> loginPin(@Field("loginPin") int loginPin);
}
