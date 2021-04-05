package com.heuristify.mdu.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.heuristify.mdu.database.entity.DoctorAttendance;

import java.util.List;

@Dao
public interface DoctorAttendanceDao {

    @Insert()
    long insertDoctorAttendDance(DoctorAttendance doctorAttendance);

    @Query("SELECT * FROM doctor_attendance")
    List<DoctorAttendance> getDoctorAttendance();

    @Query("SELECT attendanceDate FROM doctor_attendance WHERE attendanceDate =:date")
    String checkAttendance(String date);


}
