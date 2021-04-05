package com.heuristify.mdu.mvvm.repository;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.network.BaseUrl;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSyncRepository {
    MutableLiveData<List<Patient>> getPatientListMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> getPatientListMutableLiveDataError = new MutableLiveData<>();
    List<Patient> patientList = new ArrayList<>();
    int count = -1;

    public MutableLiveData<List<Patient>> getAllPatientMutableLiveData() {
        return getPatientListMutableLiveData;
    }

    public MutableLiveData<String> getGetPatientListMutableLiveDataError() {
        return getPatientListMutableLiveDataError;
    }

    public void getPatientList(int sync) {
        new Thread(() -> {
            List<Patient> patientList2 = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().getAllPatient(sync);
            if (patientList2.size() > 0) {
                for (int i = 0; i < patientList2.size(); i++) {
                    if (patientList2.get(i).getImage_path() != null) {
                        File file = new File(patientList2.get(i).getImage_path());
                        if (file.exists()) {
                            patientList.add(patientList2.get(i));
                        }
                    }
                }

                if(patientList.size() > 0){
                    uploadImagesToServer();
                }else{
                    getPatientListMutableLiveData.postValue(patientList2);
                }

            } else {
                getPatientListMutableLiveData.postValue(patientList2);
            }

        }).start();
    }


    private void uploadImagesToServer() {
        count++;
        File file = new File(patientList.get(count).getImage_path());
        RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        Call<ResponseBody> call = MyApplication.getInstance().getRetrofitServicesWithToken().uploadImages(fileToUpload, filename);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                try {

                    if (response.code() == 200) {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.optBoolean("success") == true) {
                            updatePatientImageLink(patientList.get(count).getId(), jsonObject.optString("imageUrl"), 1);
                        }

                        if (count < patientList.size() - 1) {
                            uploadImagesToServer();
                        } else {
                            clearCountAndImageList();
                            getPatientListMutableLiveData.setValue(patientList);
                        }
                    } else {
                        if (count < patientList.size() - 1) {
                            uploadImagesToServer();
                        } else {
                            clearCountAndImageList();
                            getPatientListMutableLiveDataError.setValue(response.message());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    getPatientListMutableLiveDataError.setValue(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                if (count < patientList.size() - 1) {
                    uploadImagesToServer();
                } else {
                    clearCountAndImageList();
                    getPatientListMutableLiveDataError.setValue(t.getMessage());
                }
            }
        });
    }

    private void updatePatientImageLink(int id, String imageUrl, int sync) {
        BaseUrl urlHelper = new BaseUrl();
        new Thread(() -> {
            MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().updatePatient(id, (urlHelper.BASE_URL + imageUrl), sync);


        }).start();
    }


    private void clearCountAndImageList() {
        count = -1;
        patientList.clear();
    }

}
