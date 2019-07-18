package com.parsclass.android.alltolearn.Utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parsclass.android.alltolearn.model.Lecture;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LectureConverter {
    @TypeConverter
    public String fromSomeObjectList(ArrayList<Lecture> data) {
        if (data == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Lecture>>() {}.getType();
        String json = gson.toJson(data, type);
        return json;
    }

    @TypeConverter
    public ArrayList<Lecture> toSomeObjectList(String SomeObjectListString) {
        if (SomeObjectListString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<String[]>() {}.getType();
        ArrayList<Lecture> SomeObjectList = gson.fromJson(SomeObjectListString, type);
        return SomeObjectList;
    }
}
