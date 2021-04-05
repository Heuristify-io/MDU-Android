package com.heuristify.mdu.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.heuristify.mdu.database.entity.StockMedicine;

import java.util.List;

@Dao
public interface StockMedicineDoa {

    @Insert()
    void insertStockMedicine(StockMedicine stockMedicine);

    @Query("SELECT * FROM doctor_med_stocks WHERE " + "medicineName =:medicine_name")
    StockMedicine getMedicineQuantityAndTotal(String medicine_name);

    @Query("SELECT * FROM doctor_med_stocks")
    List<StockMedicine> getStockMedicines();

    @Query("SELECT quantity FROM doctor_med_stocks WHERE " + "id =:id")
    String getStockMedicinesQuantity(int id);

    @Query("SELECT * FROM doctor_med_stocks WHERE medicineName LIKE :name || '%'")
    List<StockMedicine> getStockMedicine(String name);

    @Query("UPDATE doctor_med_stocks SET quantity = :medicine_quantity, quantity = :total WHERE id =:stock_medicine_medicineId")
    void update(int medicine_quantity, int total, int stock_medicine_medicineId);

    @Query("DELETE FROM doctor_med_stocks")
    void deleteStockMedicines();

}
