package com.heuristify.mdu.database.databasehelper;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heuristify.mdu.pojo.Medicine;

import java.lang.reflect.Type;
import java.util.List;

public class MedicineConverter {

    @TypeConverter
    public static List<Medicine> stringToMeasurements(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Medicine>>() {}.getType();
        List<Medicine> measurements = gson.fromJson(json, type);
        return measurements;
    }

    @TypeConverter
    public static String measurementsToString(List<Medicine> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Medicine>>() {}.getType();
        String json = gson.toJson(list, type);
        return json;
    }
}
