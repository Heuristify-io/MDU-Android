package com.heuristify.mdu.pojo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.room.ColumnInfo;



public class MedicineName extends BaseObservable {

    @ColumnInfo(name = "medicineName")
    private String medicine_name;
    @ColumnInfo(name = "frequency")
    private String frequency;
    @ColumnInfo(name = "days")
    private String days;

    @Bindable
    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
        notifyPropertyChanged(BR.medicine_name);
    }

    @Bindable
    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
        notifyPropertyChanged(BR.frequency);
    }

    @Bindable
    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
        notifyPropertyChanged(BR.days);
    }
}
