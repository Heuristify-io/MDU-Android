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

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = new LoginRepository();
    }

    public MutableLiveData<Response<ResponseBody>> getPinResponse(){
        return loginRepository.getResponseBodyMutableLiveData();
    }

    public MutableLiveData<String> getPinCodeError_msg(){
        return loginRepository.getError_msg();
    }


    public void sendPins(int pin_code){
        loginRepository.sendUserPinToServer(pin_code);
    }

}
