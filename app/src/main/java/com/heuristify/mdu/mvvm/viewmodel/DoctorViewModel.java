package com.heuristify.mdu.mvvm.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.database.entity.DoctorAttendance;
import com.heuristify.mdu.mvvm.repository.DoctorRepository;

public class DoctorViewModel extends AndroidViewModel {

    DoctorRepository doctorRepository;

    public DoctorViewModel(@NonNull Application application) {
        super(application);
        doctorRepository = new DoctorRepository();
    }


    public MutableLiveData<Long> insertDoctorAttendanceMutableLiveDataObserver() {
        return doctorRepository.insertDoctorAttendanceGetter();
    }

    public MutableLiveData<String> getDoctorAttendanceMutableLiveData1() {
        return doctorRepository.getCheckDoctorAttendanceMutableLiveData1();
    }

    public void checkPatientAttendance(String date){
        doctorRepository.checkAttendance(date);
    }

    public void insertDoctorAttendance(DoctorAttendance doctorAttendance){
        doctorRepository.insertDoctorAttendance(doctorAttendance);
    }
}
