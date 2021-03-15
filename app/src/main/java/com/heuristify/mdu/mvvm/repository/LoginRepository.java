package com.heuristify.mdu.mvvm.repository;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.base.MyApplication;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    MutableLiveData<Response<ResponseBody>> responseBodyMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> error_msg = new MutableLiveData<>();
    MutableLiveData<Boolean> progress = new MutableLiveData<>();

    public MutableLiveData<Response<ResponseBody>> getResponseBodyMutableLiveData(int pin_code) {
        progress.setValue(true);
        sendLoginPinCode(pin_code);
        return responseBodyMutableLiveData;
    }

    private void sendLoginPinCode(int pin_code) {

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                Call<ResponseBody> call = MyApplication.getInstance().getRetrofitServices().loginPin(pin_code);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        progress.setValue(false);
                        responseBodyMutableLiveData.postValue(response);
                        Log.e("respoinse",""+response.code());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        progress.setValue(false);
                        error_msg.postValue(t.getMessage());
                        Log.e("respoinse2",""+t.getMessage());
                    }
                });

            }
        }, 300);


    }

    public MutableLiveData<String> getError_msg() {
        return error_msg;
    }

    public MutableLiveData<Boolean> getProgress() {
        return progress;
    }


}
