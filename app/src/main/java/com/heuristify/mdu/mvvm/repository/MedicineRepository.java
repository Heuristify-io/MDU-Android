package com.heuristify.mdu.mvvm.repository;

import android.database.Cursor;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.MedicineEntity;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.pojo.MedicineList;
import com.heuristify.mdu.pojo.StockMedicine;
import com.heuristify.mdu.pojo.StockMedicineList;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicineRepository {

    MutableLiveData<Response<MedicineList>> searchMedicineResponse = new MutableLiveData<>();
    MutableLiveData<Response<StockMedicineList>> createMedicineResponse = new MutableLiveData<>();
    MutableLiveData<String> error_msg = new MutableLiveData<>();
    MutableLiveData<Boolean> isSuggestion = new MutableLiveData<>();


    public void getMedicine() {

        Call<MedicineList> call = MyApplication.getInstance().getRetrofitServicesWithToken().getMedicine();
        call.enqueue(new Callback<MedicineList>() {
            @Override
            public void onResponse(Call<MedicineList> call, Response<MedicineList> response) {
                if (response.isSuccessful()) {
                    Log.e("medicine_response", "" + response.body().getMedicineList().size());
                    if (response.body().getMedicineList().size() > 0) {
                        storeMedicineIntoDb(response);
                    }
                }
                Log.e("medicine_response2", "" + response.code());
            }

            @Override
            public void onFailure(Call<MedicineList> call, Throwable t) {

                Log.e("medicine_responseFail", "" + t.getMessage());

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

    public MutableLiveData<Response<StockMedicineList>> createMedicineInventory(String medicineName, String from, String strength, String units, int quantity) {
        createMedicine(medicineName, from, strength, units, quantity);
        return createMedicineResponse;
    }

    private void createMedicine(String medicineName, String from, String strength, String units, int quantity) {
        Call<StockMedicineList> responseBodyCall = MyApplication.getInstance().getRetrofitServicesWithToken().createMedicine(medicineName, from, strength, units, quantity);
        responseBodyCall.enqueue(new Callback<StockMedicineList>() {
            @Override
            public void onResponse(Call<StockMedicineList> call, Response<StockMedicineList> response) {
                try {

                    if (response.code() == 201) {

                        if (response.body().getMsg().equals("Successfully created medicine.")) {
                            //add new medicine
                            DisplayLog.showLog("add_new_med", "" + response.code());
                            addNewMedicine(response, response.body().getMedicine().getMedicine_id(), medicineName, quantity);

                        } else if (response.body().getMsg().equals("Successfully bought medicine.")) {

                            //update medicine fields
                            updateMedicine(response, medicineName, quantity);

                        } else {
                            createMedicineResponse.setValue(response);
                        }

                    } else {
                        createMedicineResponse.setValue(response);
                    }

                } catch (Exception e) {
                    createMedicineResponse.setValue(response);
                }

            }

            @Override
            public void onFailure(Call<StockMedicineList> call, Throwable t) {
                error_msg.setValue(t.getMessage());
            }
        });


    }

    private void updateMedicine(Response<StockMedicineList> response, String medicineName, int quantity) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                StockMedicine stockMedicine = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().getMedicineQuantityAndTotal(medicineName);
                if (stockMedicine != null) {

                    String q1 = stockMedicine.getStock_medicine_quantity();
                    String t1 = stockMedicine.getStock_medicine_total();
                    int add_quantity = Integer.parseInt(q1) + quantity;
                    int add_total = Integer.parseInt(t1) + quantity;
                    MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().update(add_quantity, add_total, medicineName);
                }

                MyApplication.getInstance().getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createMedicineResponse.setValue(response);
                    }
                });

            }
        }).start();
    }

    private void addNewMedicine(Response<StockMedicineList> response, int medicine_id, String medicineName, int quantity) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                StockMedicine stockMedicine = new StockMedicine();
                stockMedicine.setStock_medicine_medicineId(medicine_id);
                stockMedicine.setStock_medicine_name(medicineName);
                stockMedicine.setStock_medicine_quantity(String.valueOf(quantity));
                stockMedicine.setStock_medicine_total(String.valueOf(quantity));
                storeInToDb(stockMedicine);

                MyApplication.getInstance().getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        createMedicineResponse.setValue(response);
                    }
                });

            }
        }).start();
    }

    public MutableLiveData<String> getError_msg() {
        return error_msg;
    }

    public MutableLiveData<Boolean> getBooleanMutableLiveData() {
        return isSuggestion;
    }

    public void getStockMedicineList() {
        Call<StockMedicineList> stockMedicineListCall = MyApplication.getInstance().getRetrofitServicesWithToken().getMedicineStock();
        stockMedicineListCall.enqueue(new Callback<StockMedicineList>() {
            @Override
            public void onResponse(Call<StockMedicineList> call, Response<StockMedicineList> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {

                        //do not response to activity
                        if (response.body().getStockMedicineListList() != null) {
                            deleteOldListAndStoreNewListFromDb(response, response.body().getStockMedicineListList(), "getStockMedicineList");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<StockMedicineList> call, Throwable t) {

            }
        });
    }

    private void storeInToDb(StockMedicine stockMedicine) {
        MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().insertStockMedicine(stockMedicine);
    }

    private void deleteOldListAndStoreNewListFromDb(Response<StockMedicineList> response, List<StockMedicine> stockMedicineListList, String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<StockMedicine> stockMedicines = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().getStockMedicines();
                if (stockMedicines != null) {
                    MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().deleteStockMedicines();
                }
                for (int i = 0; i < stockMedicineListList.size(); i++) {
                    StockMedicine stockMedicine = new StockMedicine();
                    stockMedicine.setStock_medicine_medicineId(stockMedicineListList.get(i).getStock_medicine_medicineId());
                    stockMedicine.setStock_medicine_name(stockMedicineListList.get(i).getStock_medicine_name());
                    stockMedicine.setStock_medicine_quantity(stockMedicineListList.get(i).getStock_medicine_quantity());
                    stockMedicine.setStock_medicine_total(stockMedicineListList.get(i).getStock_medicine_total());
                    storeInToDb(stockMedicine);
                }
                if (msg.equals("createMedicine")) {
                    MyApplication.getInstance().getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            createMedicineResponse.setValue(response);
                        }
                    });
                }
            }
        }).start();
    }
}
