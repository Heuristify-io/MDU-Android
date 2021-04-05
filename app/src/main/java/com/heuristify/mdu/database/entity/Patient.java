package com.heuristify.mdu.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "patients")
public class Patient implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "fullName")
    private String fullName;
    @ColumnInfo(name = "cnicFirst2Digits")
    private int cnicFirst2Digits;
    @ColumnInfo(name = "cnicLast2Digits")
    private int cnicLast4Digits;
    @ColumnInfo(name = "gender")
    private String gender;
    @ColumnInfo(name = "age")
    private int age;
    @ColumnInfo(name = "imageURL")
    private String image_path;
    @ColumnInfo(name = "isSync")
    private int image_sync = 0;

    public Patient() {
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

    public int getImage_sync() {
        return image_sync;
    }

    public void setImage_sync(int image_sync) {
        this.image_sync = image_sync;
    }
}
