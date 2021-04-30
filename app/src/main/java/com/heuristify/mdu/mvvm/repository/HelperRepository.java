package com.heuristify.mdu.mvvm.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.base.MyApplication;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelperRepository {
    MutableLiveData<Response<ResponseBody>> tokenResponseMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> tokenErrorMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Response<ResponseBody>> getTokenResponseMutableLiveData() {
        return tokenResponseMutableLiveData;
    }

    public MutableLiveData<String> getTokenErrorMutableLiveData() {
        return tokenErrorMutableLiveData;
    }

    public void verifyToken() {
        Call<ResponseBody> call = MyApplication.getInstance().getRetrofitServicesWithToken().verifyToken();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                tokenResponseMutableLiveData.postValue(response);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                tokenErrorMutableLiveData.postValue(t.getMessage());
            }
        });
    }
}
