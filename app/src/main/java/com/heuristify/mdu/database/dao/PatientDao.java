package com.heuristify.mdu.database.dao;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface PatientDao {

    @Query("UPDATE patients SET imageURL = :imageUrl,sync =:sync  WHERE id =:id")
    void updatePatient(int id, String imageUrl,int sync);
}
