package com.heuristify.mdu.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.database.entity.MedicineEntity;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.database.entity.PrescribedMedicine;
import com.heuristify.mdu.database.entity.StockMedicine;
import com.heuristify.mdu.pojo.ConsultationHistory;
import com.heuristify.mdu.pojo.PatientPrescribedMedicine;

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
    long insertPatientDiagnosis(DiagnosisAndMedicine diagnosisAndMedicine);

    @Insert()
    long[] insertPrescribedMedicine(List<PrescribedMedicine> prescribedMedicines);


    // select query

    @Query("SELECT * FROM medicine_list_entity")
    MedicineEntity getMedicine();

    @Query("SELECT * FROM doctor_med_stocks WHERE " + "medicineName =:medicine_name")
    StockMedicine getMedicineQuantityAndTotal(String medicine_name);

    @Query("SELECT * FROM doctor_med_stocks")
    List<StockMedicine> getStockMedicines();

    @Query("SELECT quantity FROM doctor_med_stocks WHERE " + "id =:id")
    String getStockMedicinesQuantity(int id);

    @Query("SELECT * FROM patients WHERE fullName =:name AND cnicFirst2Digits =:firstTwoDigit AND cnicLast2Digits =:lastFourDigit AND age =:age")
    Patient getPatient(String name, int firstTwoDigit, int lastFourDigit, int age);


    @Query("SELECT * FROM doctor_med_stocks WHERE medicineName LIKE :name || '%'")
    List<StockMedicine> getStockMedicine(String name);

    @Query("SELECT prescribed_medicine.id,prescribed_medicine.frequency,prescribed_medicine.days,doctor_med_stocks.medicineName FROM prescribed_medicine  " +
            "JOIN doctor_med_stocks ON doctor_med_stocks.id = prescribed_medicine.medicineId WHERE prescribed_medicine.consultationId =:consultation_id")
    List<PatientPrescribedMedicine> getPatientPrescribedMedicine(int  consultation_id);

    @Query("SELECT * FROM consultations WHERE " + "id =:consultation_id")
    DiagnosisAndMedicine getDiagnosisAndMedicine(int consultation_id);


    @Query("SELECT c1.id,c1.patientDiagnosis,p1.fullName FROM consultations c1 INNER JOIN patients p1 ON p1.id = c1.patientId")
    List<ConsultationHistory> getPatientAndConsultation();


    // delete query

    @Query("DELETE FROM doctor_med_stocks")
    void deleteStockMedicines();

    // update query

    @Query("UPDATE doctor_med_stocks SET quantity = :medicine_quantity, quantity = :total WHERE id =:stock_medicine_medicineId")
    void update(int medicine_quantity, int total, int stock_medicine_medicineId);


}
