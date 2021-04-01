package com.heuristify.mdu.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.heuristify.mdu.database.dao.TaskDao;
import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.database.entity.MedicineEntity;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.database.entity.PrescribedMedicine;
import com.heuristify.mdu.database.entity.StockMedicine;
import com.heuristify.mdu.database.typeConverter.DateConverter;

@Database(version = 5, entities = {MedicineEntity.class, StockMedicine.class, Patient.class, DiagnosisAndMedicine.class, PrescribedMedicine.class})
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
