package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.mvvm.repository.DataSyncRepository;
import com.heuristify.mdu.pojo.SyncApiResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class DataSyncViewModel extends AndroidViewModel {
    DataSyncRepository dataSyncRepository;

    public DataSyncViewModel(@NonNull Application application) {
        super(application);
        dataSyncRepository = new DataSyncRepository();
    }

    public MutableLiveData<List<Patient>> getPatientList(){
        return dataSyncRepository.getAllPatientMutableLiveData();
    }

    public MutableLiveData<String> errorMsg(){
        return dataSyncRepository.getGetPatientListMutableLiveDataError();
    }

    public void callGetAllPatientMethod(int sync){
        dataSyncRepository.getPatientList(sync);
    }

    public void callGetDoctorAttendancePatientsConsultationsAndPrescribedMedicine(int patient_sync, int consultation_sync, int prescribed_medicine_syn){
        dataSyncRepository.getDoctorAttendancePatientsConsultationsAndPrescribedMedicine(patient_sync,consultation_sync,prescribed_medicine_syn);
    }

    public MutableLiveData<Response<SyncApiResponse>> getDoctorAttendancePatientsConsultationsAndPrescribedMedicineMutableResponsive(){
        return dataSyncRepository.getSyncAttendancePatientsConsultationsAndPrescribedMedicineMutableLiveData();
    }

    public MutableLiveData<String> getSyncMutableLiveDataErrorResponse(){
        return dataSyncRepository.getSyncMutableLiveDataError();
    }



}
