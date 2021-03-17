package com.heuristify.mdu.mvvm.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.MedicineEntity;
import com.heuristify.mdu.pojo.MedicineList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicineRepository {

    MutableLiveData<Response<MedicineList>> searchMedicineResponse = new MutableLiveData<>();
    MutableLiveData<String> error_msg = new MutableLiveData<>();
    MutableLiveData<Boolean> isSuggestion = new MutableLiveData<>();
    

    public void getMedicine() {

        Call<MedicineList> call = MyApplication.getInstance().getRetrofitServicesWithToken().getMedicine();
        call.enqueue(new Callback<MedicineList>() {
            @Override
            public void onResponse(Call<MedicineList> call, Response<MedicineList> response) {
                if (response.isSuccessful()) {
                    Log.e("medicine_response",""+response.body().getMedicineList().size());
                    if (response.body().getMedicineList().size() > 0) {
                        storeMedicineIntoDb(response);
                    }
                }
                Log.e("medicine_response2",""+response.code());
            }

            @Override
            public void onFailure(Call<MedicineList> call, Throwable t) {

                Log.e("medicine_responseFail",""+t.getMessage());

            }
        });


    }

    private void storeMedicineIntoDb(Response<MedicineList> response) {
        MedicineEntity medicineEntity = new MedicineEntity();
        medicineEntity.setMedicineList(response.body().getMedicineList());
        new Thread(new Runnable() {
            @Override
            public void run() {
                MedicineEntity medicineEntity = new MedicineEntity();
                medicineEntity.setMedicineList(response.body().getMedicineList());
                MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().insertMedicineList(medicineEntity);
            }
        }).start();

    }

    public MutableLiveData<Response<MedicineList>> getSearchMedicine(String stock) {
        isSuggestion.setValue(false);
        getSearchMedicineList(stock);
        return searchMedicineResponse;
    }

    private void getSearchMedicineList(String medicine) {
        Call<MedicineList> call = MyApplication.getInstance().getRetrofitServicesWithToken().getSearchMedicine(medicine);
        call.enqueue(new Callback<MedicineList>() {
            @Override
            public void onResponse(Call<MedicineList> call, Response<MedicineList> response) {
                isSuggestion.setValue(true);
                searchMedicineResponse.setValue(response);
            }

            @Override
            public void onFailure(Call<MedicineList> call, Throwable t) {
                isSuggestion.setValue(false);
                error_msg.setValue(t.getMessage());
            }
        });
    }

    public MutableLiveData<String> getError_msg() {
        return error_msg;
    }

    public MutableLiveData<Boolean> getBooleanMutableLiveData() {
        return isSuggestion;
    }
}
