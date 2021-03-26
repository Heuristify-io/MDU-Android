package com.heuristify.mdu.database.entity;

import androidx.room.Embedded;
import androidx.room.Relation;


public class PatientWithDiagnosisAndMedicine {

    @Embedded
    public Patient patient;
    @Relation(parentColumn = "id", entityColumn = "patientId")
    public DiagnosisAndMedicine diagnosisAndMedicine;

    public PatientWithDiagnosisAndMedicine(Patient patient, DiagnosisAndMedicine diagnosisAndMedicineList) {
        this.patient = patient;
        this.diagnosisAndMedicine = diagnosisAndMedicineList;
    }
}
