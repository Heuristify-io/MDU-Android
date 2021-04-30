package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.mvvm.repository.HelperRepository;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class HelperViewModel extends AndroidViewModel {
    HelperRepository helperRepository;

    public HelperViewModel(@NonNull Application application) {
        super(application);
        helperRepository = new HelperRepository();
    }

    public MutableLiveData<Response<ResponseBody>> getResponseMutableLiveData() {
        return helperRepository.getTokenResponseMutableLiveData();
    }

    public MutableLiveData<String> getErrorMutableLiveData() {
        return helperRepository.getTokenErrorMutableLiveData();
    }

    public void verifyToken(){
        helperRepository.verifyToken();
    }
}
