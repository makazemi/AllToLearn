package com.parsclass.android.alltolearn.Utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parsclass.android.alltolearn.model.Comment;
import com.parsclass.android.alltolearn.model.Question;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class QandAConverter {

    @TypeConverter
    public String fromSomeObjectList(ArrayList<Question> data) {
        if (data == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Comment>>() {}.getType();
        String json = gson.toJson(data, type);
        return json;
    }

    @TypeConverter
    public ArrayList<Question> toSomeObjectList(String SomeObjectListString) {
        if (SomeObjectListString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<String[]>() {}.getType();
        ArrayList<Question> SomeObjectList = gson.fromJson(SomeObjectListString, type);
        return SomeObjectList;
    }
}
