package com.heuristify.mdu.pojo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.heuristify.mdu.BR;

public class PatientHistory extends BaseObservable {

    @SerializedName("id")
    private int patient_history_id;
    @SerializedName("medicineName")
    private String patient_history_medicine_name;
    @SerializedName("date")
    private String patient_history_medicine_date;
    @SerializedName("quantity")
    private String patient_history_medicine_quantity;

    @Bindable
    public int getPatient_history_id() {
        return patient_history_id;
    }

    public void setPatient_history_id(int patient_history_id) {
        this.patient_history_id = patient_history_id;
        notifyPropertyChanged(BR.patient_history_id);
    }

    @Bindable
    public String getPatient_history_medicine_name() {
        return patient_history_medicine_name;
    }

    public void setPatient_history_medicine_name(String patient_history_medicine_name) {
        this.patient_history_medicine_name = patient_history_medicine_name;
        notifyPropertyChanged(BR.patient_history_medicine_name);
    }

    @Bindable
    public String getPatient_history_medicine_date() {
        return patient_history_medicine_date;
    }

    public void setPatient_history_medicine_date(String patient_history_medicine_date) {
        this.patient_history_medicine_date = patient_history_medicine_date;
        notifyPropertyChanged(BR.patient_history_medicine_date);
    }

    @Bindable
    public String getPatient_history_medicine_quantity() {
        return patient_history_medicine_quantity;
    }

    public void setPatient_history_medicine_quantity(String patient_history_medicine_quantity) {
        this.patient_history_medicine_quantity = patient_history_medicine_quantity;
        notifyPropertyChanged(BR.patient_history_medicine_quantity);
    }
}
