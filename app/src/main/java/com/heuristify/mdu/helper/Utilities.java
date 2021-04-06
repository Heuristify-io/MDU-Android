package com.heuristify.mdu.helper;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utilities {

    public static String currentDate() {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(Constant.DOB_FORMAT, Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }
}
