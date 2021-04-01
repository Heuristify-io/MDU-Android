package com.heuristify.mdu.pojo;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class ConsultationHistory extends BaseObservable {

    private int id;
    private String patientDiagnosis;
    private String fullName;
    private String created_date;

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getPatientDiagnosis() {
        return patientDiagnosis;
    }

    public void setPatientDiagnosis(String patientDiagnosis) {
        this.patientDiagnosis = patientDiagnosis;
        notifyPropertyChanged(BR.patientDiagnosis);
    }

    @Bindable
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        notifyPropertyChanged(BR.fullName);
    }

    @Bindable
    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
        notifyPropertyChanged(BR.created_date);
    }
}
