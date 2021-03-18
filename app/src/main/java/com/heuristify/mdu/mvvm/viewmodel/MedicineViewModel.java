package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.mvvm.repository.MedicineRepository;
import com.heuristify.mdu.pojo.MedicineList;
import com.heuristify.mdu.pojo.StockMedicineList;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class MedicineViewModel extends AndroidViewModel {
    MedicineRepository medicineRepository;
    MutableLiveData<Response<MedicineList>> responseBodyMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Response<StockMedicineList>> createMedicineResponse = new MutableLiveData<>();
    MutableLiveData<Response<StockMedicineList>> stockMedicineListResponse = new MutableLiveData<>();
    MutableLiveData<String> error_msg = new MutableLiveData<>();
    MutableLiveData<Boolean> isSuggestion = new MutableLiveData<>();


    public MedicineViewModel(@NonNull Application application) {
        super(application);
        medicineRepository = new MedicineRepository();
    }

    public void getMedicineList(){
        medicineRepository.getMedicine();
    }

    public void getStockMedicineList(){
        medicineRepository.getStockMedicineList();
    }

    public MutableLiveData<Response<MedicineList>> getSearchStockMutableLiveData(String medicine) {
        this.responseBodyMutableLiveData = medicineRepository.getSearchMedicine(medicine);
        return responseBodyMutableLiveData;
    }

    public MutableLiveData<Response<StockMedicineList>> createMedicine(String medicineName,String from,String strength,String units,int quantity){
        this.createMedicineResponse = medicineRepository.createMedicineInventory(medicineName,from,strength,units,quantity);
        return createMedicineResponse;
    }

    public MutableLiveData<String> getError_msg() {
        this.error_msg = medicineRepository.getError_msg();
        return error_msg;
    }

    public MutableLiveData<Boolean> getBooleanMutableLiveData() {
        this.isSuggestion = medicineRepository.getBooleanMutableLiveData();
        return isSuggestion;
    }




}
