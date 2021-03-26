package com.heuristify.mdu.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

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
                        onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})


public class PrescribedMedicine {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "medicineId")
    private int medicineId;
    @ColumnInfo(name = "consultationId")
    private int consultationId;
    @ColumnInfo(name = "days")
    private int days;
    @ColumnInfo(name = "frequency")
    private String frequency;
    @ColumnInfo(name = "frequencyNum")
    private int frequencyNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public int getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(int consultationId) {
        this.consultationId = consultationId;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public int getFrequencyNum() {
        return frequencyNum;
    }

    public void setFrequencyNum(int frequencyNum) {
        this.frequencyNum = frequencyNum;
    }
}
