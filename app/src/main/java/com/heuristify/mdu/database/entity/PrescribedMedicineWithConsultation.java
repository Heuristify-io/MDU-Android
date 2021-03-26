package com.heuristify.mdu.database.entity;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class PrescribedMedicineWithConsultation {
    @Embedded DiagnosisAndMedicine diagnosisAndMedicine;
    @Relation(parentColumn = "id", entityColumn = "consultationId")
    public List<PrescribedMedicine> prescribedMedicineList;

    public PrescribedMedicineWithConsultation(DiagnosisAndMedicine diagnosisAndMedicine, List<PrescribedMedicine> prescribedMedicineList) {
        this.diagnosisAndMedicine = diagnosisAndMedicine;
        this.prescribedMedicineList = prescribedMedicineList;
    }
}
