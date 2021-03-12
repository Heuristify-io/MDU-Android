package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.mvvm.repository.LoginRepository;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    LoginRepository loginRepository;
    MutableLiveData<Response<ResponseBody>> responseBodyMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> error_msg = new MutableLiveData<>();
    MutableLiveData<Boolean> progress = new MutableLiveData<>();


    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = new LoginRepository();
    }

    public MutableLiveData<Response<ResponseBody>> getLoginRepository(int pin_code) {
        this.responseBodyMutableLiveData = loginRepository.getResponseBodyMutableLiveData(pin_code);
        return responseBodyMutableLiveData;
    }

    public MutableLiveData<String> getError_msg(){
        this.error_msg = loginRepository.getError_msg();
        return error_msg;
    }

    public MutableLiveData<Boolean> getProgress(){
        this.progress = loginRepository.getProgress();
        return progress;
    }

}
