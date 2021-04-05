package com.heuristify.mdu.database.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.heuristify.mdu.database.typeConverter.DateConverter;

import java.util.Date;

@Entity(tableName = "doctor_attendance")
public class DoctorAttendance extends BaseObservable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "attendanceDate")
    @TypeConverters(DateConverter.class)
    private Date attendanceDate;


    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
        notifyPropertyChanged(BR.attendanceDate);
    }
}
