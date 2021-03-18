package com.heuristify.mdu.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import static androidx.room.OnConflictStrategy.REPLACE;


@Entity(tableName = "stock_medicine")
public class StockMedicine {

    @PrimaryKey()
    @SerializedName("id")
    private int stock_medicine_id;
    @ColumnInfo
    @SerializedName("medicineId")
    private int stock_medicine_medicineId;
    @ColumnInfo
    @SerializedName("medicineName")
    private String stock_medicine_name;
    @ColumnInfo
    @SerializedName("quantity")
    private String stock_medicine_quantity;
    @ColumnInfo
    @SerializedName("total")
    private String stock_medicine_total;

    public int getStock_medicine_id() {
        return stock_medicine_id;
    }

    public void setStock_medicine_id(int stock_medicine_id) {
        this.stock_medicine_id = stock_medicine_id;
    }

    public int getStock_medicine_medicineId() {
        return stock_medicine_medicineId;
    }

    public void setStock_medicine_medicineId(int stock_medicine_medicineId) {
        this.stock_medicine_medicineId = stock_medicine_medicineId;
    }

    public String getStock_medicine_name() {
        return stock_medicine_name;
    }

    public void setStock_medicine_name(String stock_medicine_name) {
        this.stock_medicine_name = stock_medicine_name;
    }

    public String getStock_medicine_quantity() {
        return stock_medicine_quantity;
    }

    public void setStock_medicine_quantity(String stock_medicine_quantity) {
        this.stock_medicine_quantity = stock_medicine_quantity;
    }

    public String getStock_medicine_total() {
        return  stock_medicine_total;
    }

    public void setStock_medicine_total(String stock_medicine_total) {
        this.stock_medicine_total = stock_medicine_total;
    }
}
