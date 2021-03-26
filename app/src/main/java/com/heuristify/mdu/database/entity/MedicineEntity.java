package com.heuristify.mdu.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.heuristify.mdu.database.typeConverter.MedicineConverter;
import com.heuristify.mdu.pojo.Medicine;

import java.util.List;

@TypeConverters(MedicineConverter.class)
@Entity(tableName = "medicine_list_entity")
public class MedicineEntity {

    @PrimaryKey
    private int medicine_list_id;
    @ColumnInfo(name = "medicine_list_column")
    private List<Medicine> medicineList;

    public int getMedicine_list_id() {
        return medicine_list_id;
    }

    public void setMedicine_list_id(int medicine_list_id) {
        this.medicine_list_id = medicine_list_id;
    }

    public List<Medicine> getMedicineList() {
        return medicineList;
    }

    public void setMedicineList(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }
}
