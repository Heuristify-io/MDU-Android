package com.heuristify.mdu.database.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "patients")
public class Patient extends BaseObservable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "fullName")
    @SerializedName("fullName")
    private String fullName;

    @ColumnInfo(name = "cnicFirst2Digits")
    @SerializedName("cnicFirst2Digits")
    private int cnicFirst2Digits;

    @ColumnInfo(name = "cnicLast4Digits")
    @SerializedName("cnicLast4Digits")
    private int cnicLast4Digits;

    @ColumnInfo(name = "gender")
    @SerializedName("gender")
    private String gender;

    @ColumnInfo(name = "age")
    @SerializedName("age")
    private int age;

    @ColumnInfo(name = "imageUrl")
    @SerializedName("imageUrl")
    private String image_path;

    @ColumnInfo(name = "isSync")
    @SerializedName("isSync")
    private int image_sync = 0;

    @ColumnInfo(name = "isDataSync")
    @SerializedName("isDataSync")
    private int isDataSync = 0;

    public Patient() {
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
        notifyPropertyChanged(BR.fullName);
    }

    @Bindable
    public int getCnicFirst2Digits() {
        return cnicFirst2Digits;
    }

    public void setCnicFirst2Digits(int cnicFirst2Digits) {
        this.cnicFirst2Digits = cnicFirst2Digits;
        notifyPropertyChanged(BR.cnicFirst2Digits);
    }

    @Bindable
    public int getCnicLast4Digits() {
        return cnicLast4Digits;
    }

    public void setCnicLast4Digits(int cnicLast4Digits) {
        this.cnicLast4Digits = cnicLast4Digits;
        notifyPropertyChanged(BR.cnicLast4Digits);
    }

    @Bindable
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        notifyPropertyChanged(BR.gender);
    }

    @Bindable
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }

    @Bindable
    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
        notifyPropertyChanged(BR.image_path);
    }

    @Bindable
    public int getImage_sync() {
        return image_sync;
    }

    public void setImage_sync(int image_sync) {
        this.image_sync = image_sync;
        notifyPropertyChanged(BR.image_sync);
    }

    @Bindable
    public int getIsDataSync() {
        return isDataSync;
    }

    public void setIsDataSync(int isDataSync) {
        this.isDataSync = isDataSync;
        notifyPropertyChanged(BR.isDataSync);
    }
}
