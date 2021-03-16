package com.heuristify.mdu.mvvm.repository;

import android.util.Log;

import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.MedicineEntity;
import com.heuristify.mdu.pojo.MedicineList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicineRepository {

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
}
