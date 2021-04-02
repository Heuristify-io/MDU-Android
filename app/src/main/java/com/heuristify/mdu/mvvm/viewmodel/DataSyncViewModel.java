package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.mvvm.repository.DataSyncRepository;

import java.util.List;

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

}
