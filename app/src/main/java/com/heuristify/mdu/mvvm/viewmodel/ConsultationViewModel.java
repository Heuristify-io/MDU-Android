package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.mvvm.repository.ConsultationRepository;
import com.heuristify.mdu.pojo.ConsultationHistory;
import com.heuristify.mdu.pojo.MedicineName;
import com.heuristify.mdu.pojo.PatientPrescribedMedicineAndDiagnosis;

import java.util.List;


public class ConsultationViewModel extends AndroidViewModel {
    ConsultationRepository consultationRepository;
    MutableLiveData<PatientPrescribedMedicineAndDiagnosis> patientPrescribedMedicineList = new MutableLiveData<>();
    MutableLiveData<List<ConsultationHistory>> consultationHistoryMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<MedicineName>> patientMedicineList = new MutableLiveData<>();

    public ConsultationViewModel(@NonNull Application application) {
        super(application);
        consultationRepository = new ConsultationRepository();
    }

    public MutableLiveData<PatientPrescribedMedicineAndDiagnosis> getPatientPrescribedMedicineList(int consultation_id) {
        this.patientPrescribedMedicineList = consultationRepository.getPatientPrescribedMedicineList(consultation_id);
        return patientPrescribedMedicineList;
    }


    public MutableLiveData<List<ConsultationHistory>> getConsultationHistoryMutableLiveData() {
        this.consultationHistoryMutableLiveData = consultationRepository.getConsultationHistoryMutableLiveData();
        return consultationHistoryMutableLiveData;
    }

    public MutableLiveData<List<MedicineName>> getPatientMedicineList(int consultation_id) {
        this.patientMedicineList = consultationRepository.getPatientMedicineList(consultation_id);
        return patientMedicineList;
    }

    public MutableLiveData<Integer> totalConsultationObserver() {
        return consultationRepository.getTotalConsultationMutableLiveDate();
    }

    public MutableLiveData<Integer> updatedConsultationObserver() {
        return consultationRepository.getUpdatedConsultationMutableLiveDate();
    }

    public MutableLiveData<Integer> pendingConsultationObserver() {
        return consultationRepository.getPendingConsultationMutableLiveDate();
    }

    public void getAllTotalConsultation() {
        consultationRepository.getTotalConsultation();
    }

    public void getAllUpdatedConsultation() {
        consultationRepository.getUpdatedConsultation();
    }

    public void getAllPendingConsultation() {
        consultationRepository.getPendingConsultation();
    }
}
