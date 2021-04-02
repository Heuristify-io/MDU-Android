package com.heuristify.mdu.mvvm.repository;

import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.helper.DisplayLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class DataSyncRepository {

    MutableLiveData<List<Patient>> getPatientListMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ResponseBody> patientImageMutableLiveData = new MutableLiveData<>();
    List<Patient> patientList = new ArrayList<>();

    public MutableLiveData<List<Patient>> getAllPatientMutableLiveData() {
        return getPatientListMutableLiveData;
    }

    public MutableLiveData<ResponseBody> getPatientImageMutableLiveData() {
        return patientImageMutableLiveData;
    }

    public void getPatientList(){
        new Thread(() -> {
            List<Patient> patientList = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().getAllPatient();

            for(int i =0; i<patientList.size(); i++){
                Patient patient = patientList.get(i);

                if(patient.getImage_path() != null){

                    File file = new File(patient.getImage_path());

//                    File file = new File(patient.getImage_path());
                    if(file.exists()){
                        DisplayLog.showLog("imagePath","pathExist "+file.getName() +" "+file.getPath());
                    }else{
                        DisplayLog.showLog("imagePath","pathNotExist "+file.getPath());
                    }

                }else{
                    DisplayLog.showLog("imagePath","imagePathEmpty "+patient.getImage_path());
                }
            }
//            getPatientListMutableLiveData.postValue(patientList);

        }).start();
    }

    private void checkPatientImagePath(List<Patient> patientList) {

    }

    public void uploadPatientImages(List<Patient> patientList){

        for(int i =0; i< patientList.size(); i++){

        }
    }
}
