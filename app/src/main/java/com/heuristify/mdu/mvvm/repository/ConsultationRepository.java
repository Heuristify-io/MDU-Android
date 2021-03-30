package com.heuristify.mdu.mvvm.repository;

import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.pojo.PatientPrescribedMedicine;
import com.heuristify.mdu.pojo.PatientPrescribedMedicineAndDiagnosis;

import java.util.List;

public class ConsultationRepository {

    MutableLiveData<PatientPrescribedMedicineAndDiagnosis> patientPrescribedMedicineList = new MutableLiveData<>();

    public MutableLiveData<PatientPrescribedMedicineAndDiagnosis> getPatientPrescribedMedicineList(int consultation_id) {
        getPatientPrescribedMedicine(consultation_id);
        return patientPrescribedMedicineList;
    }

    private void getPatientPrescribedMedicine(int consultation_id) {

        new Thread(() -> {
            DiagnosisAndMedicine diagnosisAndMedicine = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().getDiagnosisAndMedicine(consultation_id);
            List<PatientPrescribedMedicine> prescribedMedicines = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().getPatientPrescribedMedicine(consultation_id);
            PatientPrescribedMedicineAndDiagnosis patientPrescribedMedicineAndDiagnosis = new PatientPrescribedMedicineAndDiagnosis();
            patientPrescribedMedicineAndDiagnosis.setDiagnosisAndMedicine(diagnosisAndMedicine);
            patientPrescribedMedicineAndDiagnosis.setList(prescribedMedicines);
            MyApplication.getInstance().getActivity().runOnUiThread(() -> {
                patientPrescribedMedicineList.setValue(patientPrescribedMedicineAndDiagnosis);
            });

        }).start();


    }
}
