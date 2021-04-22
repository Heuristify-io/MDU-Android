package com.heuristify.mdu.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.heuristify.mdu.database.entity.StockMedicine;
import com.heuristify.mdu.pojo.Medicine;

import java.util.List;

@Dao
public interface StockMedicineDoa {

    @Insert()
    void insertStockMedicine(StockMedicine stockMedicine);

    @Query("SELECT * FROM doctor_med_stocks WHERE " + "medicineName =:medicine_name")
    StockMedicine getMedicineQuantityAndTotal(String medicine_name);

    @Query("SELECT * FROM doctor_med_stocks")
    List<StockMedicine> getStockMedicines();

    @Query("SELECT * FROM doctor_med_stocks")
    LiveData<List<StockMedicine>> getStockMedicinesUsingLiveData();

    @Query("SELECT * FROM doctor_med_stocks WHERE CAST(quantity AS INT) < 20 LIMIT 25")
    List<StockMedicine> getRemainingStockMedicine();

    @Query("SELECT quantity FROM doctor_med_stocks WHERE " + "id =:id")
    String getStockMedicinesQuantity(int id);

    @Query("SELECT * FROM doctor_med_stocks WHERE medicineName LIKE :name || '%'")
    List<StockMedicine> getStockMedicine(String name);

    @Query("UPDATE doctor_med_stocks SET quantity = :medicine_quantity, total = :total WHERE id =:stock_medicine_medicineId")
    void update(int medicine_quantity, int total, int stock_medicine_medicineId);

    @Query("UPDATE doctor_med_stocks SET quantity = :medicine_quantity WHERE id =:stock_medicine_medicineId")
    void updateQuantity(int medicine_quantity, int stock_medicine_medicineId);

    @Query("DELETE FROM doctor_med_stocks")
    void deleteStockMedicines();

}
