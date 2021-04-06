package com.heuristify.mdu.network;

import com.google.gson.JsonObject;
import com.heuristify.mdu.pojo.MedicineList;
import com.heuristify.mdu.pojo.PatientHistoryList;
import com.heuristify.mdu.pojo.StockMedicineList;
import com.heuristify.mdu.pojo.SyncApiResponse;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RetrofitServices {

    @FormUrlEncoded
    @POST("auth/app/login")
    Call<ResponseBody> loginPin(@Field("loginPin") int loginPin);

    @GET("medicines/")
    Call<MedicineList>  getMedicine();

    @GET("medicines/stocks")
    Call<StockMedicineList>  getMedicineStock();

    @GET("medicines/search/{name}")
    Call<MedicineList>  getSearchMedicine(@Path("name") String name);

    @GET("consultations/patienthistory/{patient_id}")
    Call<PatientHistoryList>  getPatientHistory(@Path("patient_id") int patient_id);

    @FormUrlEncoded
    @POST("medicines/create")
    Call<StockMedicineList> createMedicine(@Field("medicineName") String medicineName,@Field("form") String form,@Field("strength") String strength,@Field("units") String units,@Field("quantity") int quantity);

    @Multipart
    @POST("patients/upload-image")
    Call<ResponseBody> uploadImages(@Part MultipartBody.Part file, @Part("image") RequestBody name);

    @POST("doctors/uploadrecords")
    Call<SyncApiResponse> uploadSyncData(@Body JsonObject jsonObject);
}
