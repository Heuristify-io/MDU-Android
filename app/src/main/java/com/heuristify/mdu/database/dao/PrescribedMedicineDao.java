package com.heuristify.mdu.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.heuristify.mdu.database.entity.PrescribedMedicine;

import java.util.List;

@Dao
public interface PrescribedMedicineDao {

    @Insert()
    long[] insertPrescribedMedicine(List<PrescribedMedicine> prescribedMedicines);

    @Query("SELECT * FROM prescribed_medicine WHERE  isSync =:sync")
    List<PrescribedMedicine> getPrescribed(int sync);

    @Query("DELETE FROM prescribed_medicine")
    void deletePrescribedMedicines();
}
