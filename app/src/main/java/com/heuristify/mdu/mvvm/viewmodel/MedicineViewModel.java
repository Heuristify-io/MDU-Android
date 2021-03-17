package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.mvvm.repository.MedicineRepository;
import com.heuristify.mdu.pojo.MedicineList;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class MedicineViewModel extends AndroidViewModel {
    MedicineRepository medicineRepositroy;

    MutableLiveData<Response<MedicineList>> responseBodyMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> error_msg = new MutableLiveData<>();
    MutableLiveData<Boolean> isSuggestion = new MutableLiveData<>();

    public MedicineViewModel(@NonNull Application application) {
        super(application);
        medicineRepositroy = new MedicineRepository();
    }

    public void getMedicineList(){
        medicineRepositroy.getMedicine();
    }

    public MutableLiveData<Response<MedicineList>> getSearchStockMutableLiveData(String medicine) {
        this.responseBodyMutableLiveData = medicineRepositroy.getSearchMedicine(medicine);
        return responseBodyMutableLiveData;
    }

    public MutableLiveData<String> getError_msg() {
        this.error_msg = medicineRepositroy.getError_msg();
        return error_msg;
    }

    public MutableLiveData<Boolean> getBooleanMutableLiveData() {
        this.isSuggestion = medicineRepositroy.getBooleanMutableLiveData();
        return isSuggestion;
    }




}
