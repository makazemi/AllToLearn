package com.parsclass.android.alltolearn.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(tableName = "course_instructor_join_table",
        primaryKeys = { "courseId", "instructorId" },
        foreignKeys = {
                @ForeignKey(entity = Course.class,
                        parentColumns = "id",
                        childColumns = "courseId"),
                @ForeignKey(entity = Instructor.class,
                        parentColumns = "id",
                        childColumns = "instructorId")
        }
        ,indices = {@Index("instructorId")})
public class CourseInstructorJoin {
    @ColumnInfo(name = "courseId")
    public final int courseId;
    @ColumnInfo(name = "instructorId")
    public final int instructorId;

    public CourseInstructorJoin(int courseId, int instructorId) {
        this.courseId = courseId;
        this.instructorId = instructorId;
    }
}
