package com.heuristify.mdu.database.typeConverter;
import androidx.room.TypeConverter;

import com.heuristify.mdu.helper.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateConverter {

    static DateFormat df = new SimpleDateFormat(Constant.DOB_FORMAT);
    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String dateToTimestamp(Date value) {

        return value == null ? null : df.format(value);
    }

}
