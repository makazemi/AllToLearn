package com.parsclass.android.alltolearn.local;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;

import com.parsclass.android.alltolearn.Utils.ListCourseConverter;
import com.parsclass.android.alltolearn.model.CategoryItem;
import com.parsclass.android.alltolearn.model.Comment;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.model.CourseInstructorJoin;
import com.parsclass.android.alltolearn.model.Instructor;
import com.parsclass.android.alltolearn.model.LectureList;
import com.parsclass.android.alltolearn.model.Question;
import com.parsclass.android.alltolearn.model.QResponse;
import com.parsclass.android.alltolearn.model.Section;
import com.parsclass.android.alltolearn.model.User;

@Database(entities = {Course.class,CategoryItem.class, Instructor.class,
        Question.class, QResponse.class,
        CourseInstructorJoin.class, Comment.class,Section.class, User.class,
LectureList.class},
        version = 1)
@TypeConverters({ListCourseConverter.class})
public abstract class LearnDatabase extends RoomDatabase {

    private static LearnDatabase instance;
    public abstract CourseDao courseDao();
    public abstract CategoryDao categoryDao();
    public abstract CommentDao commentDao();
    public abstract QuestionDao questionDao();
    public abstract ResponseDao responseDao();
    public abstract SectionDao sectionDao();
    public abstract CourseInstructorJoinDao courseInstructorJoinDao();
    public abstract UserDao userDao();
    public abstract LectureListDao lectureDao();

    public static synchronized LearnDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    LearnDatabase.class,"learn_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {

        private CourseDao courseDao;
        private CategoryDao categoryDao;
        private CommentDao commentDao;
        private QuestionDao questionDao;
        private ResponseDao responseDao;
        private SectionDao sectionDao;
        private CourseInstructorJoinDao courseInstructorJoinDao;
        private UserDao userDao;

        private LectureListDao lectureDao;

        private PopulateDbAsyncTask(LearnDatabase db){
            courseDao=db.courseDao();
            categoryDao=db.categoryDao();
            commentDao=db.commentDao();
            questionDao=db.questionDao();
            responseDao=db.responseDao();
            sectionDao=db.sectionDao();
            courseInstructorJoinDao=db.courseInstructorJoinDao();
            userDao=db.userDao();
           // testmodelDao=db.testmodelDao();
            lectureDao=db.lectureDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
