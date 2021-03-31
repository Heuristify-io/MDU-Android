package com.heuristify.mdu.mvvm.repository;

import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.pojo.ConsultationHistory;
import com.heuristify.mdu.pojo.MedicineName;
import com.heuristify.mdu.pojo.PatientPrescribedMedicine;
import com.heuristify.mdu.pojo.PatientPrescribedMedicineAndDiagnosis;

import java.util.List;

public class ConsultationRepository {

    MutableLiveData<PatientPrescribedMedicineAndDiagnosis> patientPrescribedMedicineList = new MutableLiveData<>();
    MutableLiveData<List<ConsultationHistory>> consultationHistoryMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<MedicineName>> patientMedicineList = new MutableLiveData<>();

    public MutableLiveData<PatientPrescribedMedicineAndDiagnosis> getPatientPrescribedMedicineList(int consultation_id) {
        getPatientPrescribedMedicine(consultation_id);
        return patientPrescribedMedicineList;
    }

    public MutableLiveData<List<ConsultationHistory>> getConsultationHistoryMutableLiveData() {
        getConsultationAndPatient();
        return consultationHistoryMutableLiveData;
    }

    public MutableLiveData<List<MedicineName>> getPatientMedicineList(int consultation_id) {
        getPatientMedicines(consultation_id);
        return patientMedicineList;
    }

    private void getPatientMedicines(int consultation_id) {
        new Thread(() -> {
            List<MedicineName> medicineNameList = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().getPatientMedicines(consultation_id);
            MyApplication.getInstance().getActivity().runOnUiThread(() -> {
                patientMedicineList.setValue(medicineNameList);
            });

        }).start();
    }

    private void getConsultationAndPatient() {
        new Thread(() -> {
            List<ConsultationHistory> consultationHistory = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().getPatientAndConsultation();
            MyApplication.getInstance().getActivity().runOnUiThread(() -> {
                consultationHistoryMutableLiveData.setValue(consultationHistory);
            });

        }).start();
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
