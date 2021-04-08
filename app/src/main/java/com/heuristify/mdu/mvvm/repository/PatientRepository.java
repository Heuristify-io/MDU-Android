package com.heuristify.mdu.mvvm.repository;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.helper.Constant;
import com.heuristify.mdu.pojo.PatientHistoryList;
import com.heuristify.mdu.view.activities.AddDiagnosisAndMedicineActivity;
import com.heuristify.mdu.view.activities.AddNewConsultationActivity;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PatientRepository {

    MutableLiveData<Response<PatientHistoryList>> responseMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> error_msg = new MutableLiveData<>();
    MutableLiveData<Integer> checkPatientMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Patient> patientMutableLiveData = new MutableLiveData<>();


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

    public MutableLiveData<Patient> getPatientMutableLiveData() {
        return patientMutableLiveData;
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

    public void checkPatient(int patient_id, int sync) {
        new Thread(() -> {
            int id = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().checkPatient(patient_id, sync);
            checkPatientMutableLiveData.postValue(id);
        }).start();
    }

    public void patientWithOutImage(String name, int cnicFirstTwoDigit, int cnicLastFourDigit, int age, String gender) {

        new Thread(() -> {
            Patient patient = null;
            patient = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().getPatient(name, cnicFirstTwoDigit, cnicLastFourDigit, age, gender);
            if (patient != null) {
                //patient already exist
                patientMutableLiveData.postValue(patient);
            } else {
                //create new patient
                patient = new Patient();
                patient.setFullName(name);
                patient.setCnicFirst2Digits(cnicFirstTwoDigit);
                patient.setCnicLast4Digits(cnicLastFourDigit);
                patient.setAge(age);
                patient.setGender(gender);
                patient.setImage_path("");
                int insert = (int) MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().insertPatient(patient);
                Patient patient2 = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().getPatient(name, cnicFirstTwoDigit, cnicLastFourDigit, age, gender);
                patientMutableLiveData.postValue(patient2);
            }

        }).start();

    }

    public void patientWithImage(String name, int age, String gender, String image) {
        new Thread(() -> {
            Patient patient = new Patient();
            patient.setFullName(name);
            patient.setCnicFirst2Digits(0);
            patient.setCnicLast4Digits(0);
            patient.setAge(age);
            patient.setGender(gender);
            patient.setImage_path(image);
            int insert = (int) MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().insertPatient(patient);
            Patient patient2 = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().getPatientWithImage(name, age, gender,image);
            patientMutableLiveData.postValue(patient2);
        }).start();

    }


    public void patientWithImageAndCnicDetails(String name, int cnicFirstTwoDigit, int cnicLastFourDigit, int age, String gender, String image) {

        new Thread(() -> {
            Patient patient = null;
            patient = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().getPatient(name, cnicFirstTwoDigit, cnicLastFourDigit, age, gender);
            if (patient != null) {

                //patient already exist update patient image and sync value
                MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().updatePatient(patient.getId(), image, Constant.patient_sync_zero);
                patientMutableLiveData.postValue(patient);

            } else {
                //create new patient
                patient = new Patient();
                patient.setFullName(name);
                patient.setCnicFirst2Digits(cnicFirstTwoDigit);
                patient.setCnicLast4Digits(cnicLastFourDigit);
                patient.setAge(age);
                patient.setGender(gender);
                patient.setImage_path(image);
                int insert = (int) MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().insertPatient(patient);
                Patient patient2 = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().getPatient(name, cnicFirstTwoDigit, cnicLastFourDigit, age, gender);
                patientMutableLiveData.postValue(patient2);
            }

        }).start();

    }
}
