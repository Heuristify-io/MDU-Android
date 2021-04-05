package com.heuristify.mdu.mvvm.repository;


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
    MutableLiveData<Response<SyncApiResponse>> syncAttendancePatientsConsultationsAndPrescribedMedicineMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> getPatientListMutableLiveDataError = new MutableLiveData<>();
    MutableLiveData<String> syncMutableLiveDataError = new MutableLiveData<>();
    List<Patient> patientList = new ArrayList<>();
    List<String> attendanceList = new ArrayList<>();
    List<DiagnosisAndMedicine> diagnosisAndMedicineList = new ArrayList<>();
    List<PrescribedMedicine> prescribedMedicineList = new ArrayList<>();
    int count = -1;

    public MutableLiveData<List<Patient>> getAllPatientMutableLiveData() {
        return getPatientListMutableLiveData;
    }

    public MutableLiveData<String> getGetPatientListMutableLiveDataError() {
        return getPatientListMutableLiveDataError;
    }

    public MutableLiveData<Response<SyncApiResponse>> getSyncAttendancePatientsConsultationsAndPrescribedMedicineMutableLiveData() {
        return syncAttendancePatientsConsultationsAndPrescribedMedicineMutableLiveData;
    }

    public MutableLiveData<String> getSyncMutableLiveDataError() {
        return syncMutableLiveDataError;
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

                if (patientList.size() > 0) {
                    uploadImagesToServer();
                } else {
                    getPatientListMutableLiveData.postValue(patientList2);
                }

            } else {
                getPatientListMutableLiveData.postValue(patientList2);
            }

        }).start();
    }

    public void getDoctorAttendancePatientsConsultationsAndPrescribedMedicine(int patient_sync, int consultation_sync, int prescribed_medicine_syn) {
        patientList.clear();
        new Thread(() -> {
            attendanceList = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().doctorAttendanceDao().getDoctorAttendance();
            patientList = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().getAllPatient(patient_sync);
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
                            .put("gender", patientList.get(j).getGender())
                            .put("age", patientList.get(j).getAge())
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
                    prescribedMedicineJsonArray.put(new JSONObject().put("id", prescribedMedicineList.get(l).getId()).put("medicineId", prescribedMedicineList.get(l).getMedicineId())
                            .put("consultationId", prescribedMedicineList.get(l).getConsultationId())
                            .put("days", prescribedMedicineList.get(l).getDays())
                            .put("frequency", prescribedMedicineList.get(l).getFrequency()));
                }

                jsonObject = new JSONObject();
                jsonObject.put("attendance", attendanceJsonArray);
                jsonObject.put("patients", patientsJsonArray);
                jsonObject.put("consultations", diagnosisAndMedicineJsonArray);
                jsonObject.put("prescribed_medicines", prescribedMedicineJsonArray);
                DisplayLog.showLog("JsonArray", "" + jsonObject.toString());

                Call<SyncApiResponse> apiResponseCall = MyApplication.getInstance().getRetrofitServicesWithToken().uploadSyncData(jsonObject);
                apiResponseCall.enqueue(new Callback<SyncApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<SyncApiResponse> call, @NonNull Response<SyncApiResponse> response) {
                        syncAttendancePatientsConsultationsAndPrescribedMedicineMutableLiveData.setValue(response);
                    }

                    @Override
                    public void onFailure(@NonNull Call<SyncApiResponse> call, @NonNull Throwable t) {
                        syncMutableLiveDataError.setValue(t.getMessage());
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                syncMutableLiveDataError.postValue("exception "+e.getMessage());
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
                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        if (jsonObject.optBoolean("success")) {
                            updatePatientImageLink(patientList.get(count).getId(), jsonObject.optString("imageUrl"), Constant.patient_sync_one);
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
        new Thread(() -> MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().updatePatient(id, (urlHelper.BASE_URL + imageUrl), sync)).start();
    }


    private void clearCountAndImageList() {
        count = -1;
        patientList.clear();
    }

}
