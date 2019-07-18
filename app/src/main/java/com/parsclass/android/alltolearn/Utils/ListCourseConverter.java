package com.parsclass.android.alltolearn.Utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parsclass.android.alltolearn.model.Course;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ListCourseConverter {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<Course> stringToSomeObjectList(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Course>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String someObjectListToString(List<Course> someObjects) {
        return gson.toJson(someObjects);
    }
}
