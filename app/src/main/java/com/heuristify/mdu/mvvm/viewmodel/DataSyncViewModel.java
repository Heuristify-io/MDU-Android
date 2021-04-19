package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.mvvm.repository.DataSyncRepository;
import com.heuristify.mdu.pojo.SyncApiResponse;

import java.util.List;

import retrofit2.Response;

public class DataSyncViewModel extends AndroidViewModel {
    DataSyncRepository dataSyncRepository;

    public DataSyncViewModel(@NonNull Application application) {
        super(application);
        dataSyncRepository = new DataSyncRepository();
    }

    public MutableLiveData<List<Patient>> getPatientImagesList(){
        return dataSyncRepository.getAllPatientMutableLiveData();
    }

    public MutableLiveData<String> errorMsgPatientImages(){
        return dataSyncRepository.getPatientImageListMutableLiveDataError();
    }

    public void callGetAllPatientImagesMethod(int sync){
        dataSyncRepository.getPatientImageList(sync);
    }

    public void getAllRecords(){
        dataSyncRepository.getRecords();
    }

    public void uploadRecords(int patient_sync, int consultation_sync, int prescribed_medicine_syn){
        dataSyncRepository.uploadRecord(patient_sync,consultation_sync,prescribed_medicine_syn);
    }

    public MutableLiveData<Response<SyncApiResponse>> observeUploadRecordMutableResponsive(){
        return dataSyncRepository.getSyncRecordMutableLiveData();
    }

    public MutableLiveData<String> getSyncMutableLiveDataErrorResponse(){
        return dataSyncRepository.getSyncMutableLiveDataError();
    }




}
