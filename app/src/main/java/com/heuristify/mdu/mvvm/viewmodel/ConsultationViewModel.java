package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.mvvm.repository.ConsultationRepository;
import com.heuristify.mdu.pojo.PatientPrescribedMedicineAndDiagnosis;


public class ConsultationViewModel extends AndroidViewModel {
    ConsultationRepository consultationRepository;
    MutableLiveData<PatientPrescribedMedicineAndDiagnosis> patientPrescribedMedicineList = new MutableLiveData<>();

    public ConsultationViewModel(@NonNull Application application) {
        super(application);
        consultationRepository = new ConsultationRepository();
    }

    public MutableLiveData<PatientPrescribedMedicineAndDiagnosis> getPatientPrescribedMedicineList(int consultation_id) {
        this.patientPrescribedMedicineList = consultationRepository.getPatientPrescribedMedicineList(consultation_id);
        return patientPrescribedMedicineList;
    }
}
