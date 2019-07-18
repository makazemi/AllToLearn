package com.parsclass.android.alltolearn.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.base.BaseFragment;
import com.parsclass.android.alltolearn.model.Course;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AboutCourseFragment extends BaseFragment {
    private View rootView;
    @BindView(R.id.txtAboutCourse)
    TextView txtAboutCourse;
    @BindView(R.id.txtShareCourse)
    TextView txtShareCourse;
    //@BindView(R.id.txtResourceCourse)
//    TextView txtResourceCourse;
//    @BindView(R.id.txtFavoriteCourse)
//    TextView txtAddToFavorite;
    private Course currentCourse;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentCourse=(Course) getArguments().getSerializable(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_about_course, container, false);
        }
        ButterKnife.bind(this,rootView);
        return rootView;
    }

    @OnClick(R.id.txtAboutCourse)
    public void aboutCourse(){
//        Bundle bundle=new Bundle();
//        bundle.putSerializable(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY,currentCourse);
//        DetailCourseFragment fragment=new DetailCourseFragment();
//        fragment.setArguments(bundle);
//        loadFragment(R.id.flContent,fragment,TAG_MAIN_ACTIVITY);
        Intent intent=new Intent(getActivity(),AboutCourseActivity.class);
        intent.putExtra(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY,currentCourse);
        startActivity(intent);
    }

    @OnClick(R.id.txtShareCourse)
    public void shareCourse(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String content=getString(R.string.content_share_text_course)+"\n"+currentCourse.getLinkWebSite();
        shareIntent.putExtra(Intent.EXTRA_TEXT,content);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_share_text_course));
        startActivity(Intent.createChooser(shareIntent, "Share..."));
    }
//    @OnClick(R.id.txtResourceCourse)
//    public void resourceCourse(View view){
//
//    }
//    @OnClick(R.id.txtFavoriteCourse)
//    public void addFavoriteCourse(View view){
//
//    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}
