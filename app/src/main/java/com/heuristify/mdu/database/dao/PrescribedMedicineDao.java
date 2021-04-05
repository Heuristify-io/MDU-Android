package com.heuristify.mdu.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.heuristify.mdu.database.entity.PrescribedMedicine;

import java.util.List;

@Dao
public interface PrescribedMedicineDao {

    @Insert()
    long[] insertPrescribedMedicine(List<PrescribedMedicine> prescribedMedicines);
}
