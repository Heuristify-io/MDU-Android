package com.heuristify.mdu.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.heuristify.mdu.database.dao.TaskDao;
import com.heuristify.mdu.database.entity.MedicineEntity;

@Database(version = 1, entities = {MedicineEntity.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}