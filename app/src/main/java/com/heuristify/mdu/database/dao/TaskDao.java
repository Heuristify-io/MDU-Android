package com.heuristify.mdu.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.heuristify.mdu.database.entity.MedicineEntity;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TaskDao {

    @Insert(onConflict = REPLACE)
    void insertMedicineList(MedicineEntity medicine);

}
