package com.heuristify.mdu.database.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(
        tableName = "prescribed_medicine",
        foreignKeys = {
                @ForeignKey(
                        entity = DiagnosisAndMedicine.class,
                        parentColumns = {"id"},
                        childColumns = {"consultationId"},
                        onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),

                @ForeignKey(
                        entity = StockMedicine.class,
                        parentColumns = {"id"},
                        childColumns = {"medicineId"},
                        onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        })


public class PrescribedMedicine extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "medicineId")
    @SerializedName("medicineId")
    private int medicineId;

    @ColumnInfo(name = "consultationId")
    @SerializedName("consultationId")
    private int consultationId;

    @ColumnInfo(name = "days")
    @SerializedName("days")
    private int days;

    @ColumnInfo(name = "frequency")
    @SerializedName("frequency")
    private String frequency;

    @ColumnInfo(name = "frequencyNum")
    @SerializedName("frequencyNum")
    private int frequencyNum;

    @ColumnInfo(name = "isSync")
    @SerializedName("isSync")
    private int isSync = 0;

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
        notifyPropertyChanged(BR.medicineId);
    }

    @Bindable
    public int getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(int consultationId) {
        this.consultationId = consultationId;
        notifyPropertyChanged(BR.consultationId);
    }

    @Bindable
    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
        notifyPropertyChanged(BR.days);
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
    public int getFrequencyNum() {
        return frequencyNum;
    }

    public void setFrequencyNum(int frequencyNum) {
        this.frequencyNum = frequencyNum;
        notifyPropertyChanged(BR.frequencyNum);
    }

    @Bindable
    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
        notifyPropertyChanged(BR.isSync);
    }
}
