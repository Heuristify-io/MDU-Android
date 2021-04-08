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
    List<Patient> getAllPatientWithImages(int sync);

    @Query("SELECT * from patients WHERE isSync =:sync")
    List<Patient> getAllPatients(int sync);

    @Query("SELECT id from patients WHERE id =:patient_id AND isSync =:syn")
    int checkPatient(int patient_id,int syn);

    @Query("SELECT * FROM patients WHERE fullName =:name AND cnicFirst2Digits =:firstTwoDigit AND cnicLast4Digits =:lastFourDigit AND age =:age AND gender =:gen")
    Patient getPatient(String name, int firstTwoDigit, int lastFourDigit, int age,String gen);

    @Query("SELECT * FROM patients WHERE fullName =:name AND age =:age AND gender =:gen AND imageUrl =:image")
    Patient getPatientWithImage(String name, int age,String gen,String image);

    @Query("UPDATE patients SET imageURL = :imageUrl,isSync =:sync  WHERE id =:id")
    void updatePatient(int id, String imageUrl, int sync);

    @Query("DELETE FROM patients")
    void deletePatients();




}
