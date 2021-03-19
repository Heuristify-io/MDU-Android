package com.heuristify.mdu.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "patients")
public class Patient {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private int id;
    @ColumnInfo
    private String fullName;
    @ColumnInfo
    private int cnicFirst2Digits;
    @ColumnInfo
    private int cnicLast4Digits;
    @ColumnInfo
    private String gender;
    @ColumnInfo
    private int age;
    @ColumnInfo
    private String image_path;

    public Patient() {
    }

    public Patient(int id, String fullName, int cnicFirst2Digits, int cnicLast4Digits, String gender, int age, String image_path) {
        this.id = id;
        this.fullName = fullName;
        this.cnicFirst2Digits = cnicFirst2Digits;
        this.cnicLast4Digits = cnicLast4Digits;
        this.gender = gender;
        this.age = age;
        this.image_path = image_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getCnicFirst2Digits() {
        return cnicFirst2Digits;
    }

    public void setCnicFirst2Digits(int cnicFirst2Digits) {
        this.cnicFirst2Digits = cnicFirst2Digits;
    }

    public int getCnicLast4Digits() {
        return cnicLast4Digits;
    }

    public void setCnicLast4Digits(int cnicLast4Digits) {
        this.cnicLast4Digits = cnicLast4Digits;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
