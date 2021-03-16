package com.heuristify.mdu.pojo;

import com.google.gson.annotations.SerializedName;

public class Medicine {

    @SerializedName("id")
    private int medicine_id;
    @SerializedName("medicineName")
    private String medicine_name;
    @SerializedName("form")
    private String medicine_form;
    @SerializedName("strength")
    private String medicine_strength;
    @SerializedName("units")
    private String medicine_units;
    @SerializedName("createdAt")
    private String medicine_CreatedAt;
    @SerializedName("updatedAt")
    private String medicine_updatedAt;

    public int getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(int medicine_id) {
        this.medicine_id = medicine_id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getMedicine_form() {
        return medicine_form;
    }

    public void setMedicine_form(String medicine_form) {
        this.medicine_form = medicine_form;
    }

    public String getMedicine_strength() {
        return medicine_strength;
    }

    public void setMedicine_strength(String medicine_strength) {
        this.medicine_strength = medicine_strength;
    }

    public String getMedicine_units() {
        return medicine_units;
    }

    public void setMedicine_units(String medicine_units) {
        this.medicine_units = medicine_units;
    }

    public String getMedicine_CreatedAt() {
        return medicine_CreatedAt;
    }

    public void setMedicine_CreatedAt(String medicine_CreatedAt) {
        this.medicine_CreatedAt = medicine_CreatedAt;
    }

    public String getMedicine_updatedAt() {
        return medicine_updatedAt;
    }

    public void setMedicine_updatedAt(String medicine_updatedAt) {
        this.medicine_updatedAt = medicine_updatedAt;
    }
}
