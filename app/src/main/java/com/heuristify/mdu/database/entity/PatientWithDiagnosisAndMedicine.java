package com.heuristify.mdu.database.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

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
