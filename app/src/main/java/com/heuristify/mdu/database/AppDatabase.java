package com.heuristify.mdu.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.heuristify.mdu.database.dao.TaskDao;
import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.database.entity.MedicineEntity;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.database.entity.StockMedicine;

@Database(version = 4, entities = {MedicineEntity.class, StockMedicine.class, Patient.class, DiagnosisAndMedicine.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
