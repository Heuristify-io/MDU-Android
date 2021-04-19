package com.heuristify.mdu.mvvm.repository;


import android.util.Patterns;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.database.entity.PrescribedMedicine;
import com.heuristify.mdu.helper.Constant;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.network.BaseUrl;
import com.heuristify.mdu.pojo.SyncApiResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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
    MutableLiveData<List<Patient>> patientImageListMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> patientImageSynFailMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Response<SyncApiResponse>> syncRecordMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> patientImageListMutableLiveDataError = new MutableLiveData<>();
    MutableLiveData<String> syncMutableLiveDataError = new MutableLiveData<>();
    List<Patient> patientList = new ArrayList<>();
    List<String> attendanceList = new ArrayList<>();
    List<DiagnosisAndMedicine> diagnosisAndMedicineList = new ArrayList<>();
    List<PrescribedMedicine> prescribedMedicineList = new ArrayList<>();


    int count = -1;

    public MutableLiveData<List<Patient>> getAllPatientMutableLiveData() {
        return patientImageListMutableLiveData;
    }

    public MutableLiveData<String> getPatientImageListMutableLiveDataError() {
        return patientImageListMutableLiveDataError;
    }

    public MutableLiveData<Response<SyncApiResponse>> getSyncRecordMutableLiveData() {
        return syncRecordMutableLiveData;
    }

    public MutableLiveData<String> getSyncMutableLiveDataError() {
        return syncMutableLiveDataError;
    }

    public MutableLiveData<String> getPatientImageSynFailMutableLiveData() {
        return patientImageSynFailMutableLiveData;
    }

    public void getPatientImageList(int sync) {
        new Thread(() -> {
            List<Patient> patientList2 = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().getAllPatientWithImages(sync);
            if (patientList2.size() > 0) {
                for (int i = 0; i < patientList2.size(); i++) {
                    if (patientList2.get(i).getImage_path() != null) {

                        File file = new File(patientList2.get(i).getImage_path());
                        if (file.exists()) {
                            patientList.add(patientList2.get(i));
                        }else{
                            //remove image path update sync to 0
                            MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().updatePatient(patientList2.get(i).getId(),
                                    "",sync);
                        }
                    }
                }

                if (patientList.size() > 0) {
                    uploadImagesToServer();
                } else {
                    DisplayLog.showLog("DataSyncRepository ", "localDbEmpty");
                    patientImageListMutableLiveData.postValue(patientList2);
                }

            } else {
                DisplayLog.showLog("DataSyncRepository ", "localDbEmpty2");
                patientImageListMutableLiveData.postValue(patientList2);
            }

        }).start();
    }

    public void getRecords(){

        Call<SyncApiResponse> apiResponseCall = MyApplication.getInstance().getRetrofitServicesWithToken().getRecords();
        apiResponseCall.enqueue(new Callback<SyncApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<SyncApiResponse> call, @NonNull Response<SyncApiResponse> response) {
                if (response.code() == 200) {
                    //delete table anc recreate table with new receive data
                    deleteAndRecreateTableAgain(response);
                } else {
                    syncRecordMutableLiveData.setValue(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SyncApiResponse> call, @NonNull Throwable t) {
                syncMutableLiveDataError.setValue(t.getMessage() + " " + t.getStackTrace() + " " + call.toString() + " " + t.toString());
            }
        });
    }

    private boolean IsValidUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches();
        } catch (MalformedURLException ignored) {
        }
        return false;
    }

    public void uploadRecord(int patient_sync, int consultation_sync, int prescribed_medicine_syn) {
        patientList.clear();
        new Thread(() -> {
            attendanceList = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().doctorAttendanceDao().getDoctorAttendance();
            patientList = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().getAllPatients(patient_sync);
            diagnosisAndMedicineList = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().diagnosisAndMedicineDao().getDiagnosis(consultation_sync);
            prescribedMedicineList = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().prescribedMedicineDao().getPrescribed(prescribed_medicine_syn);

            JSONObject jsonObject;
            JSONArray attendanceJsonArray = new JSONArray();
            JSONArray patientsJsonArray = new JSONArray();
            JSONArray diagnosisAndMedicineJsonArray = new JSONArray();
            JSONArray prescribedMedicineJsonArray = new JSONArray();

            try {

                for (int i = 0; i < attendanceList.size(); i++) {
                    attendanceJsonArray.put(new JSONObject().put("attendanceDate", attendanceList.get(i)));
                }

                for (int j = 0; j < patientList.size(); j++) {
                    patientsJsonArray.put(new JSONObject().put("id", patientList.get(j).getId()).put("fullName", patientList.get(j).getFullName())
                            .put("gender", patientList.get(j).getGender()).put("age", patientList.get(j).getAge())
                            .put("cnicFirst2Digits", patientList.get(j).getCnicFirst2Digits())
                            .put("cnicLast4Digits", patientList.get(j).getCnicLast4Digits())
                            .put("imageUrl", patientList.get(j).getImage_path()));
                }

                for (int k = 0; k < diagnosisAndMedicineList.size(); k++) {
                    diagnosisAndMedicineJsonArray.put(new JSONObject().put("id", diagnosisAndMedicineList.get(k).getId()).put("patientId", diagnosisAndMedicineList.get(k).getPatientId())
                            .put("patientDiagnosis", diagnosisAndMedicineList.get(k).getPatientDiagnosis())
                            .put("description", diagnosisAndMedicineList.get(k).getDescription())
                            .put("lat", diagnosisAndMedicineList.get(k).getLat())
                            .put("lng", diagnosisAndMedicineList.get(k).getLng()));
                }

                for (int l = 0; l < prescribedMedicineList.size(); l++) {
                    prescribedMedicineJsonArray.put(new JSONObject().put("id", prescribedMedicineList.get(l).getId())

                            .put("medicineId", prescribedMedicineList.get(l).getActualMedicineId())
//                            .put("actualMedicineId", prescribedMedicineList.get(l).getActualMedicineId())
                            .put("consultationId", prescribedMedicineList.get(l).getConsultationId())
                            .put("days", prescribedMedicineList.get(l).getDays())
                            .put("frequencyNum", prescribedMedicineList.get(l).getFrequencyNum())
                            .put("frequency", prescribedMedicineList.get(l).getFrequency()));

                }

                jsonObject = new JSONObject();
                jsonObject.put("attendance", attendanceJsonArray);
                jsonObject.put("patients", patientsJsonArray);
                jsonObject.put("consultations", diagnosisAndMedicineJsonArray);
                jsonObject.put("prescribed_medicines", prescribedMedicineJsonArray);
                DisplayLog.showLog("JsonArray", "" + jsonObject.toString());
                JsonObject jsonRequest = new JsonObject();
                jsonRequest.addProperty("data", String.valueOf(jsonObject));
                uploadRecordApiCall(jsonRequest);


            } catch (Exception e) {
                e.printStackTrace();
                syncMutableLiveDataError.postValue("exception " + e.getMessage());
            }


        }).start();

    }

    private void uploadRecordApiCall(JsonObject jsonRequest) {

        Call<SyncApiResponse> apiResponseCall = MyApplication.getInstance().getRetrofitServicesWithToken().uploadSyncData(jsonRequest);
        apiResponseCall.enqueue(new Callback<SyncApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<SyncApiResponse> call, @NonNull Response<SyncApiResponse> response) {
                if (response.code() == 200) {
                    //delete table anc recreate table with new receive data
                    deleteAndRecreateTableAgain(response);
                } else {
                    syncRecordMutableLiveData.setValue(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SyncApiResponse> call, @NonNull Throwable t) {
                syncMutableLiveDataError.setValue(t.getMessage() + " " + t.getStackTrace() + " " + call.toString() + " " + t.toString());
            }
        });
    }

    private void deleteAndRecreateTableAgain(Response<SyncApiResponse> response) {

        try {

            new Thread(() -> {

                MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().stockMedicineDoa().deleteStockMedicines();
                MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().doctorAttendanceDao().deleteDoctorAttendance();
                MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().deletePatients();
                MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().diagnosisAndMedicineDao().deleteDiagnosisAndMedicines();
                MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().prescribedMedicineDao().deletePrescribedMedicines();

                for (int i = 0; i < response.body().getStockMedicineList().size(); i++) {
                    MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().stockMedicineDoa().insertStockMedicine(response.body().getStockMedicineList().get(i));
                }

                for (int j = 0; j < response.body().getDoctorAttendanceList().size(); j++) {
                    MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().doctorAttendanceDao().insertDoctorAttendDance(response.body().getDoctorAttendanceList().get(j));
                }

                for (int k = 0; k < response.body().getPatientList().size(); k++) {
                    MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().insertPatient(response.body().getPatientList().get(k));
                }

                for (int m = 0; m < response.body().getDiagnosisAndMedicineList().size(); m++) {
                    MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().diagnosisAndMedicineDao().insertPatientDiagnosis(response.body().getDiagnosisAndMedicineList().get(m));
                }
                List<PrescribedMedicine> prescribedMedicineList = response.body().getPrescribedMedicineList();
                MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().prescribedMedicineDao().insertPrescribedMedicine(prescribedMedicineList);
                syncRecordMutableLiveData.postValue(response);


            }).start();

        } catch (Exception e) {
            e.getMessage();
            syncRecordMutableLiveData.setValue(response);
        }

    }


    private void uploadImagesToServer() {
        count++;
        File file = new File(patientList.get(count).getImage_path());
        RequestBody mFile = RequestBody.create(file,MediaType.parse("image/*"));
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), mFile);
        RequestBody filename = RequestBody.create(file.getName(),MediaType.parse("text/plain"));
        Call<ResponseBody> call = MyApplication.getInstance().getRetrofitServicesWithToken().uploadImages(fileToUpload, filename);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                DisplayLog.showLog("upload_image", "response22 " + response.code());
                try {

                    if (response.code() == 200) {
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.optBoolean("success")) {
                            updatePatientImageLink(patientList.get(count).getId(), jsonObject.optString("imageUrl"), Constant.patient_sync_one);

                            if (count < patientList.size() - 1) {
                                uploadImagesToServer();
                            } else {

                                clearCountAndImageList();
                                patientImageListMutableLiveData.setValue(patientList);
                            }

                        }else{

                            //image upload fail
                            clearCountAndImageList();
                            patientImageListMutableLiveDataError.setValue(response.message());
                        }

                    } else {

                        clearCountAndImageList();
                        patientImageListMutableLiveDataError.setValue(response.message());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    clearCountAndImageList();
                    patientImageListMutableLiveDataError.setValue(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                DisplayLog.showLog("upload_image", "response222 " + t.getMessage());
                clearCountAndImageList();
                patientImageListMutableLiveDataError.setValue(t.getMessage());

            }
        });
    }

    private void updatePatientImageLink(int id, String imageUrl, int sync) {
        BaseUrl urlHelper = new BaseUrl();
        new Thread(() -> MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().updatePatient(id, (urlHelper.BASE_URL + imageUrl), sync)).start();
    }


    private void clearCountAndImageList() {
        count = -1;
        patientList.clear();
    }

}
