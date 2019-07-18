package com.parsclass.android.alltolearn.Utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListStringConverter {

    @TypeConverter
    public String fromSomeObjectList(ArrayList<String> data) {
        if (data == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        String json = gson.toJson(data, type);
        return json;
    }

    @TypeConverter
    public ArrayList<String> toSomeObjectList(String SomeObjectListString) {
        if (SomeObjectListString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<String[]>() {}.getType();
        ArrayList<String> SomeObjectList = gson.fromJson(SomeObjectListString, type);
        return SomeObjectList;
    }
}
