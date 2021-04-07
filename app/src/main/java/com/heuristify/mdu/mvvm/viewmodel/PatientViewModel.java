package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.mvvm.repository.PatientRepository;
import com.heuristify.mdu.pojo.PatientHistoryList;


import retrofit2.Response;

public class PatientViewModel extends AndroidViewModel {
    PatientRepository patientRepository;
    MutableLiveData<Response<PatientHistoryList>> patientResponseMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> error_msg = new MutableLiveData<>();

    public PatientViewModel(@NonNull Application application) {
        super(application);
        patientRepository = new PatientRepository();
    }

    public MutableLiveData<Response<PatientHistoryList>> getPatientResponseMutableLiveData(int patient_id) {
        this.patientResponseMutableLiveData = patientRepository.getPatientHistory(patient_id);
        return patientResponseMutableLiveData;
    }

    public MutableLiveData<Integer> checkPatient() {
        return patientRepository.getCheckPatientMutableLiveData();
    }

    public MutableLiveData<String> getError_msg() {
        this.error_msg = patientRepository.getError_msg();
        return error_msg;
    }

    public void checkPatientSync(int patient_id,int sync){
        patientRepository.checkPatient(patient_id,sync);
    }
}
