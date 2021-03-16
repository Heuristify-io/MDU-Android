package com.heuristify.mdu.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.heuristify.mdu.database.entity.MedicineEntity;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TaskDao {

    @Insert(onConflict = REPLACE)
    void insertMedicineList(MedicineEntity medicine);

    @Query("SELECT * FROM medicine_list_entity")
    MedicineEntity getMedicine();

}
