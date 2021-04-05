package com.heuristify.mdu.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.heuristify.mdu.database.entity.Patient;

import java.util.List;

@Dao
public interface PatientDao {

    @Insert()
    long insertPatient(Patient patient);

    @Query("SELECT * from patients WHERE isSync =:sync AND imageURL IS NOT NULL AND imageURL != ''")
    List<Patient> getAllPatient(int sync);

    @Query("SELECT * FROM patients WHERE fullName =:name AND cnicFirst2Digits =:firstTwoDigit AND cnicLast4Digits =:lastFourDigit AND age =:age")
    Patient getPatient(String name, int firstTwoDigit, int lastFourDigit, int age);

    @Query("UPDATE patients SET imageURL = :imageUrl,isSync =:sync  WHERE id =:id")
    void updatePatient(int id, String imageUrl, int sync);


}
