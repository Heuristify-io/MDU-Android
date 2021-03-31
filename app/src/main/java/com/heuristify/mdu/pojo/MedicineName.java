package com.heuristify.mdu.pojo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.room.ColumnInfo;



public class MedicineName extends BaseObservable {

    @ColumnInfo(name = "medicineName")
    private String medicine_name;

    @Bindable
    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
        notifyPropertyChanged(BR.medicine_name);
    }
}
