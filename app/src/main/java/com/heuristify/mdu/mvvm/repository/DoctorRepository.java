package com.heuristify.mdu.mvvm.repository;

import androidx.lifecycle.MutableLiveData;

import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.DoctorAttendance;

public class DoctorRepository {

    private final MutableLiveData<Long> doctorAttendanceMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> checkDoctorAttendanceMutableLiveData1 = new MutableLiveData<>();

    public MutableLiveData<Long> insertDoctorAttendanceGetter () {
        return doctorAttendanceMutableLiveData;
    }

    public MutableLiveData<String> getCheckDoctorAttendanceMutableLiveData1() {
        return checkDoctorAttendanceMutableLiveData1;
    }


    public void insertDoctorAttendance(DoctorAttendance doctorAttendance){
        new Thread(() -> {
            long doctorAttendance1 = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().doctorAttendanceDao().insertDoctorAttendDance(doctorAttendance);
            doctorAttendanceMutableLiveData.postValue(doctorAttendance1);

        }).start();
    }

    public void checkAttendance(String date) {
        new Thread(() -> {
            String date2 = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().doctorAttendanceDao().checkAttendance(date);
            checkDoctorAttendanceMutableLiveData1.postValue(date2);
        }).start();
    }
}
