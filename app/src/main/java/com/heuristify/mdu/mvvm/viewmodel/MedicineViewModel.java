package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.heuristify.mdu.mvvm.repository.MedicineRepository;

public class MedicineViewModel extends AndroidViewModel {
    MedicineRepository medicineRepositroy;

    public MedicineViewModel(@NonNull Application application) {
        super(application);
        medicineRepositroy = new MedicineRepository();
    }

    public void getMedicineList(){
        medicineRepositroy.getMedicine();
    }
}
