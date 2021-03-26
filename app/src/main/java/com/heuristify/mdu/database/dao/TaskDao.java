package com.heuristify.mdu.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.database.entity.MedicineEntity;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.database.entity.StockMedicine;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TaskDao {

    // insert query

    @Insert(onConflict = REPLACE)
    void insertMedicineList(MedicineEntity medicine);

    @Insert()
    void insertStockMedicine(StockMedicine stockMedicine);

    @Insert()
    long insertPatient(Patient patient);

    @Insert()
    void insertPatientDiagnosis(List<DiagnosisAndMedicine> diagnosisAndMedicine);


    // select query

    @Query("SELECT * FROM medicine_list_entity")
    MedicineEntity getMedicine();

    @Query("SELECT * FROM doctor_med_stocks WHERE " + "stock_medicine_name =:medicine_name")
    StockMedicine getMedicineQuantityAndTotal(String medicine_name);

    @Query("SELECT * FROM doctor_med_stocks")
    List<StockMedicine> getStockMedicines();

    @Query("SELECT * FROM patients WHERE fullName =:name AND cnicFirst2Digits =:firstTwoDigit AND cnicLast2Digits =:lastFourDigit AND age =:age")
    Patient getPatient(String name, int firstTwoDigit, int lastFourDigit, int age);


    @Query("SELECT * FROM doctor_med_stocks WHERE stock_medicine_name LIKE :name || '%'")
    List<StockMedicine> getStockMedicine(String name);


    // delete query

    @Query("DELETE FROM doctor_med_stocks")
    void deleteStockMedicines();

    // update query

    @Query("UPDATE doctor_med_stocks SET stock_medicine_quantity = :medicine_quantity, stock_medicine_total = :total WHERE stock_medicine_medicineId =:stock_medicine_medicineId")
    void update(int medicine_quantity, int total, int stock_medicine_medicineId);


}
