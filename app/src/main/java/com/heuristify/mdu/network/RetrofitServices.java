package com.heuristify.mdu.network;

import com.heuristify.mdu.pojo.MedicineList;
import com.heuristify.mdu.pojo.PatientHistoryList;
import com.heuristify.mdu.pojo.StockMedicineList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitServices {

    @FormUrlEncoded
    @POST("/auth/app/login")
    Call<ResponseBody> loginPin(@Field("loginPin") int loginPin);

    @GET("/medicines/")
    Call<MedicineList>  getMedicine();

    @GET("/medicines/stocks")
    Call<StockMedicineList>  getMedicineStock();

    @GET("/medicines/search/{name}")
    Call<MedicineList>  getSearchMedicine(@Path("name") String name);

    @GET("/consultations/patienthistory/{patient_id}")
    Call<PatientHistoryList>  getPatientHistory(@Path("patient_id") int patient_id);

    @FormUrlEncoded
    @POST("/medicines/create")
    Call<StockMedicineList> createMedicine(@Field("medicineName") String medicineName,@Field("form") String form,@Field("strength") String strength,@Field("units") String units,@Field("quantity") int quantity);
}
