package com.heuristify.mdu.database.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.database.entity.DoctorAttendance;
import com.heuristify.mdu.database.entity.MedicineEntity;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.database.entity.PrescribedMedicine;
import com.heuristify.mdu.database.entity.StockMedicine;
import com.heuristify.mdu.pojo.ConsultationHistory;
import com.heuristify.mdu.pojo.MedicineName;
import com.heuristify.mdu.pojo.PatientPrescribedMedicine;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TaskDao {

    // insert query

    @Insert(onConflict = REPLACE)
    void insertMedicineList(MedicineEntity medicine);

    // select query

    @Query("SELECT * FROM medicine_list_entity")
    MedicineEntity getMedicine();


    @Query("SELECT prescribed_medicine.id,prescribed_medicine.frequency,prescribed_medicine.days,doctor_med_stocks.medicineName FROM prescribed_medicine  " +
            "JOIN doctor_med_stocks ON doctor_med_stocks.id = prescribed_medicine.medicineId WHERE prescribed_medicine.consultationId =:consultation_id")
    List<PatientPrescribedMedicine> getPatientPrescribedMedicine(int consultation_id);


    @Query("SELECT c1.id,c1.patientDiagnosis,c1.createdAt,p1.fullName,p1.gender,p1.age FROM consultations c1 INNER JOIN patients p1 ON p1.id = c1.patientId ORDER BY p1.id desc")
    List<ConsultationHistory> getPatientAndConsultation();

    @Query("SELECT med.medicineName,p1.frequency,p1.days From prescribed_medicine p1 INNER JOIN doctor_med_stocks med ON med.id = p1.medicineId WHERE p1.consultationId =:consultation_id")
    List<MedicineName> getPatientMedicines(int consultation_id);






}
