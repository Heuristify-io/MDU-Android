package com.heuristify.mdu.pojo;

import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;

import java.util.List;

public class PatientPrescribedMedicineAndDiagnosis {

    private DiagnosisAndMedicine diagnosisAndMedicine;
    private List<PatientPrescribedMedicine> list;

    public DiagnosisAndMedicine getDiagnosisAndMedicine() {
        return diagnosisAndMedicine;
    }

    public void setDiagnosisAndMedicine(DiagnosisAndMedicine diagnosisAndMedicine) {
        this.diagnosisAndMedicine = diagnosisAndMedicine;
    }

    public List<PatientPrescribedMedicine> getList() {
        return list;
    }

    public void setList(List<PatientPrescribedMedicine> list) {
        this.list = list;
    }
}
