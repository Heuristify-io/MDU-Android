package com.heuristify.mdu.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MedicineList {

    @SerializedName("data")
    List<Medicine> medicineList;

    public List<Medicine> getMedicineList() {
        return medicineList;
    }
}
