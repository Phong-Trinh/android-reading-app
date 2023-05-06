package com.example.oneread.data.db;

import androidx.room.TypeConverter;
import com.example.oneread.utils.AppConstants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Converters {
    @TypeConverter
    public static List<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    @TypeConverter
    public static Date fromTimestamp(String time) {
        SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @TypeConverter
    public static String dateToTimestamp(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.ENGLISH);
        return formatter.format(date);
    }
}