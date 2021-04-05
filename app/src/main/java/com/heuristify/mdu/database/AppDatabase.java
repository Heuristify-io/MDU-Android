package com.heuristify.mdu.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.heuristify.mdu.database.dao.DiagnosisAndMedicineDao;
import com.heuristify.mdu.database.dao.DoctorAttendanceDao;
import com.heuristify.mdu.database.dao.PatientDao;
import com.heuristify.mdu.database.dao.PrescribedMedicineDao;
import com.heuristify.mdu.database.dao.StockMedicineDoa;
import com.heuristify.mdu.database.dao.TaskDao;
import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.database.entity.DoctorAttendance;
import com.heuristify.mdu.database.entity.MedicineEntity;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.database.entity.PrescribedMedicine;
import com.heuristify.mdu.database.entity.StockMedicine;

@Database(version = 7, entities = {MedicineEntity.class, StockMedicine.class, Patient.class, DiagnosisAndMedicine.class, PrescribedMedicine.class,
        DoctorAttendance.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();

    public abstract PatientDao patientDao();

    public abstract DoctorAttendanceDao doctorAttendanceDao();

    public abstract StockMedicineDoa stockMedicineDoa();

    public abstract DiagnosisAndMedicineDao diagnosisAndMedicineDao();

    public abstract PrescribedMedicineDao prescribedMedicineDao();
}
