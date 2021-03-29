package com.heuristify.mdu.database.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.heuristify.mdu.BR;


@Entity(tableName = "doctor_med_stocks")
public class StockMedicine extends BaseObservable {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int stock_medicine_medicineId;
    @ColumnInfo(name = "medicineName")
    @SerializedName("medicineName")
    private String stock_medicine_name;
    @ColumnInfo(name = "quantity")
    @SerializedName("quantity")
    private String stock_medicine_quantity;
    @ColumnInfo(name = "total")
    @SerializedName("total")
    private String stock_medicine_total;

    @Bindable
    public int getStock_medicine_medicineId() {
        return stock_medicine_medicineId;
    }

    public void setStock_medicine_medicineId(int stock_medicine_medicineId) {
        notifyPropertyChanged(BR.stock_medicine_medicineId);
        this.stock_medicine_medicineId = stock_medicine_medicineId;
    }

    @Bindable
    public String getStock_medicine_name() {
        return stock_medicine_name;
    }

    public void setStock_medicine_name(String stock_medicine_name) {
        notifyPropertyChanged(BR.stock_medicine_name);
        this.stock_medicine_name = stock_medicine_name;
    }

    @Bindable
    public String getStock_medicine_quantity() {
        return stock_medicine_quantity;
    }

    public void setStock_medicine_quantity(String stock_medicine_quantity) {
        notifyPropertyChanged(BR.stock_medicine_quantity);
        this.stock_medicine_quantity = stock_medicine_quantity;
    }

    @Bindable
    public String getStock_medicine_total() {
        return  stock_medicine_total;
    }

    public void setStock_medicine_total(String stock_medicine_total) {
        notifyPropertyChanged(BR.stock_medicine_total);
        this.stock_medicine_total = stock_medicine_total;
    }
}
