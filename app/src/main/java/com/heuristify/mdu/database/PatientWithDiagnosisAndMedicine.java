package com.heuristify.mdu.database;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.pojo.Patient;

import java.util.List;

public class PatientWithDiagnosisAndMedicine {

    @Embedded
    public Patient patient;
    @Relation(parentColumn = "id", entityColumn = "id")
    public List<DiagnosisAndMedicine> diagnosisAndMedicineList;

    public PatientWithDiagnosisAndMedicine(Patient patient, List<DiagnosisAndMedicine> diagnosisAndMedicineList) {
        this.patient = patient;
        this.diagnosisAndMedicineList = diagnosisAndMedicineList;
    }
}
