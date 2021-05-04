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


    public MedicineViewModel(@NonNull Application application) {
        super(application);
        medicineRepository = new MedicineRepository();
    }

    public MutableLiveData<Response<StockMedicineList>> getStockMedicineList() {
        return medicineRepository.getStockMedicineList();
    }

    public MutableLiveData<Response<MedicineList>> getSearchStockMutableLiveData() {
        return medicineRepository.getSearchMedicine();
    }

    public MutableLiveData<Response<StockMedicineList>> createMedicine() {
        return medicineRepository.createMedicineInventory();
    }


    public MutableLiveData<String> getError_msg() {
        return medicineRepository.getError_msg();
    }

    public MutableLiveData<String> get_medicine_error_msg() {
        return medicineRepository.getGet_medicine_error_msg();
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

    public void GetMedicineListForPinView() {
        medicineRepository.getMedicineForPinView();
    }

    public void CreateMedicines(String medicineName, String from, String strength, String units, int quantity) {
        medicineRepository.createMedicines(medicineName, from, strength, units, quantity);
    }

}
