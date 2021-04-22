package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.database.entity.StockMedicine;
import com.heuristify.mdu.mvvm.repository.MedicineRepository;
import com.heuristify.mdu.pojo.MedicineList;
import com.heuristify.mdu.pojo.StockMedicineList;


import java.util.List;

import retrofit2.Response;

public class MedicineViewModel extends AndroidViewModel {
    MedicineRepository medicineRepository;
    MutableLiveData<Response<StockMedicineList>> createMedicineResponse = new MutableLiveData<>();
    MutableLiveData<Response<StockMedicineList>> getMedicineList = new MutableLiveData<>();
    MutableLiveData<String> error_msg = new MutableLiveData<>();
    MutableLiveData<String> get_medicine_error_msg = new MutableLiveData<>();


    public MedicineViewModel(@NonNull Application application) {
        super(application);
        medicineRepository = new MedicineRepository();
    }

    public MutableLiveData<Response<StockMedicineList>> getStockMedicineList() {
        this.getMedicineList = medicineRepository.getStockMedicineList();
        return getMedicineList;
    }

    public MutableLiveData<Response<MedicineList>> getSearchStockMutableLiveData() {
        return medicineRepository.getSearchMedicine();
    }

    public MutableLiveData<Response<StockMedicineList>> createMedicine(String medicineName, String from, String strength, String units, int quantity) {
        this.createMedicineResponse = medicineRepository.createMedicineInventory(medicineName, from, strength, units, quantity);
        return createMedicineResponse;
    }


    public MutableLiveData<String> getError_msg() {
        this.error_msg = medicineRepository.getError_msg();
        return error_msg;
    }

    public MutableLiveData<String> get_medicine_error_msg() {
        this.get_medicine_error_msg = medicineRepository.getGet_medicine_error_msg();
        return get_medicine_error_msg;
    }

    public LiveData<List<StockMedicine>> getRemainingStockMedicine() {
        return medicineRepository.getRemainingStockMedicineList();
    }

    public LiveData<List<StockMedicine>> getMedicineList() {
        return medicineRepository.getAllMedicinesForInventoryFragment();
    }

    public void getSuggestion(String suggestion) {
        medicineRepository.getSuggestionFromServer(suggestion);
    }


}
