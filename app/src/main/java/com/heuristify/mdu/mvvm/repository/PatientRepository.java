package com.heuristify.mdu.mvvm.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.pojo.PatientHistoryList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PatientRepository {

    MutableLiveData<Response<PatientHistoryList>> responseMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> error_msg = new MutableLiveData<>();
    MutableLiveData<Integer> checkPatientMutableLiveData = new MutableLiveData<>();


    public MutableLiveData<Response<PatientHistoryList>> getPatientHistory(int patient_id) {
        callPatientHistoryApi(patient_id);
        return responseMutableLiveData;
    }

    public MutableLiveData<String> getError_msg() {
        return error_msg;
    }

    public MutableLiveData<Integer> getCheckPatientMutableLiveData() {
        return checkPatientMutableLiveData;
    }

    private void callPatientHistoryApi(int patient_id) {
        Call<PatientHistoryList> patientHistoryListCall = MyApplication.getInstance().getRetrofitServicesWithToken().getPatientHistory(patient_id);
        patientHistoryListCall.enqueue(new Callback<PatientHistoryList>() {
            @Override
            public void onResponse(@NonNull Call<PatientHistoryList> call, @NonNull Response<PatientHistoryList> response) {
                responseMutableLiveData.setValue(response);
            }

            @Override
            public void onFailure(@NonNull Call<PatientHistoryList> call, @NonNull Throwable t) {
                error_msg.setValue(t.getMessage());
            }
        });
    }

    public void checkPatient(int patient_id,int sync){
        new Thread(() -> {
            int id = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().checkPatient(patient_id,sync);
            checkPatientMutableLiveData.postValue(id);
        }).start();
    }
}
