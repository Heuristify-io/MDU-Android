package com.heuristify.mdu.database.entity;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;
import com.heuristify.mdu.database.typeConverter.DateConverter;

import java.util.Date;

@Entity(tableName = "consultations")
public class DiagnosisAndMedicine extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "patientDiagnosis")
    @SerializedName("patientDiagnosis")
    private String patientDiagnosis;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String description;

    @ColumnInfo(name = "lat")
    @SerializedName("lat")
    private double lat;

    @ColumnInfo(name = "lng")
    @SerializedName("lng")
    private double lng;

    @ColumnInfo(name = "isSync")
    @SerializedName("isSync")
    private byte sync = 0;

    @ColumnInfo(name = "patientId")
    @SerializedName("patientId")
    private int patientId;

    @ColumnInfo(name = "createdAt")
    @SerializedName("createdAt")
    @TypeConverters(DateConverter.class)
    private Date created_date;


    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Bindable
    public String getPatientDiagnosis() {
        return patientDiagnosis;
    }

    public void setPatientDiagnosis(String patientDiagnosis) {
        this.patientDiagnosis = patientDiagnosis;
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
        notifyPropertyChanged(BR.lat);
    }

    @Bindable
    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
        notifyPropertyChanged(BR.lng);
    }

    @Bindable
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
        notifyPropertyChanged(BR.patientId);
    }

    public byte getSync() {
        return sync;
    }

    public void setSync(byte sync) {
        this.sync = sync;
        notifyPropertyChanged(BR.isSync);
    }

    @Bindable
    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
        notifyPropertyChanged(BR.created_date);
    }
}
