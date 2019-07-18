package com.parsclass.android.alltolearn.Utils;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {

//    @TypeConverter
//    public String fromSomeObjectList(Date data) {
//        if (data == null) {
//            return (null);
//        }
//        Gson gson = new Gson();
//        Type type = new TypeToken<Date>() {}.getType();
//        String json = gson.toJson(data, type);
//        return json;
//    }
//
//    @TypeConverter
//    public Date toSomeObjectList(String SomeObjectListString) {
//        if (SomeObjectListString == null) {
//            return (null);
//        }
//        Gson gson = new Gson();
//        Type type = new TypeToken<String[]>() {}.getType();
//        Date SomeObjectList = gson.fromJson(SomeObjectListString, type);
//        return SomeObjectList;
//    }

    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }
}
