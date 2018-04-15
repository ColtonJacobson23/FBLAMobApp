package com.example.coltonjacobson.fblamobapp.Database;

import android.arch.persistence.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class DateConverter {


    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

    @TypeConverter
    public Date storedStringToDate(String s) {
        Date date = null;
        try {
            date = dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    @TypeConverter
    public String dateToStoredString(Date date) {

        String dateString = dateFormat.format(date);
        return dateString;

    }
}
