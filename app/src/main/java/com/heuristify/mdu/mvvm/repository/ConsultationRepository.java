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

    MutableLiveData<Integer> totalConsultationMutableLiveDate = new MutableLiveData<>();
    MutableLiveData<Integer> updatedConsultationMutableLiveDate = new MutableLiveData<>();
    MutableLiveData<Integer> pendingConsultationMutableLiveDate = new MutableLiveData<>();
    MutableLiveData<String> patientImageMutableLiveDate = new MutableLiveData<>();

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
            DiagnosisAndMedicine diagnosisAndMedicine = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().diagnosisAndMedicineDao().getDiagnosisAndMedicine(consultation_id);
            List<PatientPrescribedMedicine> prescribedMedicines = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().getPatientPrescribedMedicine(consultation_id);
            PatientPrescribedMedicineAndDiagnosis patientPrescribedMedicineAndDiagnosis = new PatientPrescribedMedicineAndDiagnosis();
            patientPrescribedMedicineAndDiagnosis.setDiagnosisAndMedicine(diagnosisAndMedicine);
            patientPrescribedMedicineAndDiagnosis.setList(prescribedMedicines);
            MyApplication.getInstance().getActivity().runOnUiThread(() -> {
                patientPrescribedMedicineList.setValue(patientPrescribedMedicineAndDiagnosis);
            });

        }).start();
    }


    public MutableLiveData<Integer> getTotalConsultationMutableLiveDate() {
        return totalConsultationMutableLiveDate;
    }

    public MutableLiveData<Integer> getUpdatedConsultationMutableLiveDate() {
        return updatedConsultationMutableLiveDate;
    }

    public MutableLiveData<Integer> getPendingConsultationMutableLiveDate() {
        return pendingConsultationMutableLiveDate;
    }

    public MutableLiveData<String> getPatientImageMutableLiveDate() {
        return patientImageMutableLiveDate;
    }

    public void getTotalConsultation() {
        new Thread(() -> {
            int count = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().diagnosisAndMedicineDao().getTotalCount();
            totalConsultationMutableLiveDate.postValue(count);
        }).start();
    }

    public void getUpdatedConsultation() {
        new Thread(() -> {
            int count = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().diagnosisAndMedicineDao().getUpdateCount();
            updatedConsultationMutableLiveDate.postValue(count);
        }).start();
    }

    public void getPendingConsultation() {
        new Thread(() -> {
            int count = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().diagnosisAndMedicineDao().getPendingCount();
            pendingConsultationMutableLiveDate.postValue(count);
        }).start();
    }

    public void getPatientImage(int patient_id){
        new Thread(() -> {
            String image = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().patientDao().getPatientImage(patient_id);
            patientImageMutableLiveDate.postValue(image);
        }).start();
    }
}
