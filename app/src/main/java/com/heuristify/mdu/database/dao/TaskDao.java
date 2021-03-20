package com.heuristify.mdu.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.heuristify.mdu.database.entity.MedicineEntity;
import com.heuristify.mdu.pojo.Patient;
import com.heuristify.mdu.pojo.StockMedicine;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TaskDao {

    /**********insert query**********/

    @Insert(onConflict = REPLACE)
    void insertMedicineList(MedicineEntity medicine);

    @Insert()
    void insertStockMedicine(StockMedicine stockMedicine);

    @Insert()
    long insertPatient(Patient patient);


    /**********select query**********/

    @Query("SELECT * FROM medicine_list_entity")
    MedicineEntity getMedicine();

    @Query("SELECT * FROM stock_medicine WHERE " + "stock_medicine_name =:medicine_name")
    StockMedicine getMedicineQuantityAndTotal(String medicine_name);

    @Query("SELECT * FROM stock_medicine")
    List<StockMedicine> getStockMedicines();

    @Query("SELECT * FROM patients WHERE fullName =:name AND cnicFirst2Digits =:firstTwoDigit AND cnicLast4Digits =:lastFourDigit AND age =:age")
    Patient getPatient(String name,int firstTwoDigit,int lastFourDigit,int age);


    /**********delete query**********/

    @Query("DELETE FROM stock_medicine")
    void deleteStockMedicines();

    /**********update query**********/

    @Query("UPDATE stock_medicine SET stock_medicine_quantity = :medicine_quantity, stock_medicine_total = :total WHERE stock_medicine_name =:medicine_name")
    void update(int medicine_quantity, int total, String  medicine_name);


}
