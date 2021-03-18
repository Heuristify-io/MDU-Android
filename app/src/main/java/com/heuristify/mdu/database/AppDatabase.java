package com.heuristify.mdu.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.heuristify.mdu.database.dao.TaskDao;
import com.heuristify.mdu.database.entity.MedicineEntity;
import com.heuristify.mdu.pojo.StockMedicine;

@Database(version = 2, entities = {MedicineEntity.class, StockMedicine.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}
