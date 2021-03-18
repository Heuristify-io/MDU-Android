package com.heuristify.mdu.database.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.heuristify.mdu.database.entity.MedicineEntity;
import com.heuristify.mdu.pojo.StockMedicine;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TaskDao {

    @Insert(onConflict = REPLACE)
    void insertMedicineList(MedicineEntity medicine);

    @Query("SELECT * FROM medicine_list_entity")
    MedicineEntity getMedicine();

    @Query("SELECT * FROM stock_medicine WHERE stock_medicine_name =:medicine_name")
    StockMedicine getMedicineQuantityAndTotal(String medicine_name);


    @Insert()
    void insertStockMedicine(StockMedicine stockMedicine);

    @Query("SELECT * FROM stock_medicine")
    List<StockMedicine> getStockMedicines();

    @Query("DELETE FROM stock_medicine")
    void deleteStockMedicines();

    @Query("UPDATE stock_medicine SET stock_medicine_quantity = :medicine_quantity, stock_medicine_total = :total WHERE stock_medicine_name =:medicine_name")
    void update(int medicine_quantity, int total, String  medicine_name);


}
