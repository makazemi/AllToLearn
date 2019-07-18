package com.parsclass.android.alltolearn.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.adapter.InstructorAdapter;
import com.parsclass.android.alltolearn.base.BaseActivity;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.model.Instructor;
import com.parsclass.android.alltolearn.viewmodel.InstructorViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.parsclass.android.alltolearn.adapter.InstructorAdapter.ALL_LIST;
import static com.parsclass.android.alltolearn.view.HomeFragment.KEY_DETAIL_COURSE_ACTIVITY;

public class AboutCourseActivity extends BaseActivity {

    @BindView(R.id.txtTitle)
    TextView txtTitle;
    @BindView(R.id.txtIntroParagraph)
    TextView txtIntroParagraph;
    @BindView(R.id.instructorRcy)
    RecyclerView instructorRcy;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Course currentCourse;
    private InstructorViewModel instructorViewModel;
    private InstructorAdapter instructorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_course);

        ButterKnife.bind(this);
        init();
        setInstructorRcy();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        currentCourse = (Course) getIntent().getSerializableExtra(KEY_DETAIL_COURSE_ACTIVITY);
        instructorViewModel = ViewModelProviders.of(this).get(InstructorViewModel.class);
        txtTitle.setText(currentCourse.getShortDescription());
        txtIntroParagraph.setText(currentCourse.getLongDescription());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        instructorRcy.setLayoutManager(layoutManager);
        instructorAdapter = new InstructorAdapter(this, ALL_LIST);
        instructorRcy.setAdapter(instructorAdapter);

    }

    private void setInstructorRcy() {
        List<Instructor> list = new ArrayList<>();
        instructorViewModel.getBriefInstructor(currentCourse.getId()).observe(this, new Observer<List<Instructor>>() {
            @Override
            public void onChanged(@Nullable List<Instructor> instructors) {
                list.clear();
                list.addAll(instructors);
                instructorAdapter.setValues(list);
            }
        });
    }

    @OnClick(R.id.imgBack)
    public void onImgBackClick(){
        super.onBackPressed();
    }
}
