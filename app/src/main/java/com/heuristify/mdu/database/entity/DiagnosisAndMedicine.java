package com.heuristify.mdu.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.heuristify.mdu.database.typeConverter.DateConverter;

import java.util.Date;

@Entity(tableName = "consultations")
public class DiagnosisAndMedicine {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "patientDiagnosis")
    private String patientDiagnosis;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "lat")
    private double lat;
    @ColumnInfo(name = "lng")
    private double lng;
    @ColumnInfo(name = "isSync")
    private boolean isSync = false;
    @ColumnInfo(name = "patientId")
    private int patientId;
    @ColumnInfo(name = "created_date")
    @TypeConverters(DateConverter.class)
    private Date created_date;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientDiagnosis() {
        return patientDiagnosis;
    }

    public void setPatientDiagnosis(String patientDiagnosis) {
        this.patientDiagnosis = patientDiagnosis;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }
}
