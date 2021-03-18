package com.heuristify.mdu.database.dao;

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

    @Insert()
    void insertStockMedicine(StockMedicine stockMedicine);

    @Query("SELECT * FROM stock_medicine")
    List<StockMedicine> getStockMedicines();

    @Query("DELETE FROM stock_medicine")
    void deleteStockMedicines();

}
