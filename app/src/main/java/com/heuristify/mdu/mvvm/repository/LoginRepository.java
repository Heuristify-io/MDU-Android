package com.heuristify.mdu.mvvm.repository;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.heuristify.mdu.base.MyApplication;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    MutableLiveData<Response<ResponseBody>> responseBodyMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> error_msg = new MutableLiveData<>();

    public MutableLiveData<Response<ResponseBody>> getResponseBodyMutableLiveData() {
        return responseBodyMutableLiveData;
    }

    public MutableLiveData<String> getError_msg() {
        return error_msg;
    }

    public void sendUserPinToServer(int pin_code) {

        Call<ResponseBody> call = MyApplication.getInstance().getRetrofitServices().loginPin(pin_code);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                responseBodyMutableLiveData.postValue(response);
                Log.e("respoinse", "" + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                error_msg.postValue(t.getMessage());
                Log.e("respoinse2", "" + t.getMessage());
            }
        });

    }


}
