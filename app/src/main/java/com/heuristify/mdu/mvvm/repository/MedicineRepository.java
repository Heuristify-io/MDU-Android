package com.heuristify.mdu.mvvm.repository;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.pojo.MedicineList;
import com.heuristify.mdu.database.entity.StockMedicine;
import com.heuristify.mdu.pojo.StockMedicineList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicineRepository {

    MutableLiveData<Response<MedicineList>> searchMedicineResponse = new MutableLiveData<>();
    MutableLiveData<Response<StockMedicineList>> createMedicineResponse = new MutableLiveData<>();
    MutableLiveData<Response<StockMedicineList>> getMedicineList = new MutableLiveData<>();
    MutableLiveData<String> error_msg = new MutableLiveData<>();
    MutableLiveData<String> get_medicine_error_msg = new MutableLiveData<>();


    public MutableLiveData<Response<MedicineList>> getSearchMedicine() {
        return searchMedicineResponse;
    }

    public LiveData<List<StockMedicine>> getRemainingStockMedicineList() {
        return MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().stockMedicineDoa().getRemainingStockMedicine();
    }

    public LiveData<List<StockMedicine>> getAllMedicinesForInventoryFragment() {

        return MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().stockMedicineDoa().getStockMedicinesUsingLiveData();
    }

    public MutableLiveData<Response<StockMedicineList>> createMedicineInventory() {
        return createMedicineResponse;
    }


    private void updateMedicine(Response<StockMedicineList> response, String medicineName, int quantity) {
        new Thread(() -> {

            StockMedicine stockMedicine = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().stockMedicineDoa().getMedicineQuantityAndTotal(medicineName);
            if (stockMedicine != null) {

                String q1 = stockMedicine.getStock_medicine_quantity();
                String t1 = stockMedicine.getStock_medicine_total();
                int add_quantity = Integer.parseInt(q1) + quantity;
                int add_total = Integer.parseInt(t1) + quantity;
                MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().stockMedicineDoa().update(add_quantity, add_total, stockMedicine.getStock_medicine_medicineId());
            }
            createMedicineResponse.postValue(response);

        }).start();
    }

    private void addNewMedicine(Response<StockMedicineList> response, int medicine_id, String medicineName, int quantity) {
        new Thread(() -> {
            StockMedicine stockMedicine = new StockMedicine();
            //for primary key
            stockMedicine.setStock_medicine_medicineId(medicine_id);
            //actual medicine id need for data sync
            stockMedicine.setMedicineId(medicine_id);
            stockMedicine.setStock_medicine_name(medicineName);
            stockMedicine.setStock_medicine_quantity(String.valueOf(quantity));
            stockMedicine.setStock_medicine_total(String.valueOf(quantity));
            storeInToDb(stockMedicine);
            createMedicineResponse.postValue(response);

        }).start();
    }


    public MutableLiveData<String> getGet_medicine_error_msg() {
        return get_medicine_error_msg;
    }

    public MutableLiveData<String> getError_msg() {
        return error_msg;
    }


    public MutableLiveData<Response<StockMedicineList>> getStockMedicineList() {
        return getMedicineList;
    }


    private void storeInToDb(StockMedicine stockMedicine) {
        MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().stockMedicineDoa().insertStockMedicine(stockMedicine);
    }

    private void deleteOldListAndStoreNewListFromDb(List<StockMedicine> stockMedicineListList) {
        new Thread(() -> {
            List<StockMedicine> stockMedicines = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().stockMedicineDoa().getStockMedicines();
            if (stockMedicines != null) {
                MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().stockMedicineDoa().deleteStockMedicines();
            }
            for (int i = 0; i < stockMedicineListList.size(); i++) {
                storeInToDb(stockMedicineListList.get(i));
            }
        }).start();
    }

    public void getSuggestionFromServer(String suggestion) {
        getSearchMedicineList(suggestion);
    }

    public void getMedicineForPinView() {
        callGetMedicineList();
    }

    public void createMedicines(String medicineName, String from, String strength, String units, int quantity) {

        createMedicine(medicineName, from, strength, units, quantity);

    }

    private void callGetMedicineList() {

        Call<StockMedicineList> stockMedicineListCall = MyApplication.getInstance().getRetrofitServicesWithToken().getMedicineStock();
        stockMedicineListCall.enqueue(new Callback<StockMedicineList>() {
            public Call<StockMedicineList> call;
            public Response<StockMedicineList> response;

            public void onResponse(@NonNull Call<StockMedicineList> call, @NonNull Response<StockMedicineList> response) {
                this.call = call;
                this.response = response;
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        if (response.body().getStockMedicineListList() != null) {
                            deleteOldListAndStoreNewListFromDb(response.body().getStockMedicineListList());
                        }
                    }
                }
                getMedicineList.setValue(response);
            }

            @Override
            public void onFailure(@NonNull Call<StockMedicineList> call, @NonNull Throwable t) {
                get_medicine_error_msg.setValue(t.getMessage());
            }
        });
    }

    private void createMedicine(String medicineName, String from, String strength, String units, int quantity) {
        Call<StockMedicineList> responseBodyCall = MyApplication.getInstance().getRetrofitServicesWithToken().createMedicine(medicineName, from, strength, units, quantity);
        responseBodyCall.enqueue(new Callback<StockMedicineList>() {
            @Override
            public void onResponse(@NonNull Call<StockMedicineList> call, @NonNull Response<StockMedicineList> response) {
                try {

                    if (response.code() == 200) {
                        //update medicine fields
                        DisplayLog.showLog("medicine_code1", "" + response.code());
                        updateMedicine(response, medicineName, quantity);

                    } else if (response.code() == 201) {
                        //add new medicine
                        DisplayLog.showLog("medicine_code2", "" + response.code());
                        assert response.body() != null;
                        addNewMedicine(response, response.body().getMedicine().getMedicine_id(), medicineName, quantity);
                    } else {
                        createMedicineResponse.setValue(response);
                    }

                } catch (Exception e) {
                    createMedicineResponse.setValue(response);
                }

            }

            @Override
            public void onFailure(@NonNull Call<StockMedicineList> call, @NonNull Throwable t) {
                error_msg.setValue(t.getMessage());
            }
        });


    }

    private void getSearchMedicineList(String medicine) {
        Call<MedicineList> call = MyApplication.getInstance().getRetrofitServicesWithToken().getSearchMedicine(medicine);
        call.enqueue(new Callback<MedicineList>() {
            public Call<MedicineList> call;
            public Response<MedicineList> response;

            @Override
            public void onResponse(@NonNull Call<MedicineList> call, @NonNull Response<MedicineList> response) {
                this.call = call;
                this.response = response;
                searchMedicineResponse.setValue(response);
            }

            @Override
            public void onFailure(@NonNull Call<MedicineList> call, @NonNull Throwable t) {
                error_msg.setValue(t.getMessage());
            }
        });
    }
}
