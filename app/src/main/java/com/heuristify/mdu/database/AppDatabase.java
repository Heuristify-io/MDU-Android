package com.heuristify.mdu.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.heuristify.mdu.database.dao.TaskDao;
import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.database.typeConverter.MedicineEntity;
import com.heuristify.mdu.pojo.Patient;
import com.heuristify.mdu.pojo.StockMedicine;

@Database(version = 9, entities = {MedicineEntity.class, StockMedicine.class, Patient.class, DiagnosisAndMedicine.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
