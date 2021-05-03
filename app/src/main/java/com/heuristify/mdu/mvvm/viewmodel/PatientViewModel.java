package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.mvvm.repository.PatientRepository;
import com.heuristify.mdu.pojo.PatientHistoryList;

import retrofit2.Response;

public class PatientViewModel extends AndroidViewModel {
    PatientRepository patientRepository;

    public PatientViewModel(@NonNull Application application) {
        super(application);
        patientRepository = new PatientRepository();
    }

    public MutableLiveData<Response<PatientHistoryList>> getPatientResponseMutableLiveData() {
        return patientRepository.getPatientHistory();
    }

    public MutableLiveData<String> getError_msg() {
        return patientRepository.getError_msg();
    }

    public LiveData<Integer> checkPatientSync(int patient_id, int sync) {
        return patientRepository.checkPatient(patient_id, sync);
    }

    public MutableLiveData<Patient> getPatient() {
        return patientRepository.getPatientMutableLiveData();
    }

    public void createPatientWithOutImage(String name, int cnicFirstTwoDigit, int cnicLastFourDigit, int age, String gender) {
        patientRepository.patientWithOutImage(name, cnicFirstTwoDigit, cnicLastFourDigit, age, gender);
    }

    public void createPatientWithImage(String name, int age, String gender, String image_page) {
        patientRepository.patientWithImage(name, age, gender, image_page);
    }


    public void createPatientWithImageAndCnicDetails(String name, int cnicFirstTwoDigit, int cnicLastFourDigit, int age, String gender, String image_page) {
        patientRepository.patientWithImageAndCnicDetails(name, cnicFirstTwoDigit, cnicLastFourDigit, age, gender, image_page);
    }

    public void callPatientHistoryApi(int patient_id){
        patientRepository.callPatientHistoryApi(patient_id);
    }
}
