package com.heuristify.mdu.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;

import java.util.List;

@Dao
public interface DiagnosisAndMedicineDao {

    @Insert()
    long insertPatientDiagnosis(DiagnosisAndMedicine diagnosisAndMedicine);

    @Query("SELECT * FROM consultations WHERE " + "id =:consultation_id")
    DiagnosisAndMedicine getDiagnosisAndMedicine(int consultation_id);

    @Query("SELECT * FROM consultations WHERE  isSync =:sync")
    List<DiagnosisAndMedicine> getDiagnosis(int sync);

    @Query("DELETE FROM consultations")
    void deleteDiagnosisAndMedicines();

    @Query("SELECT COUNT(*) FROM consultations")
    int getTotalCount();

    @Query("SELECT COUNT(id) FROM consultations WHERE isSync = 1")
    int getUpdateCount();

    @Query("SELECT COUNT(id) FROM consultations WHERE isSync = 0")
    int getPendingCount();
}
