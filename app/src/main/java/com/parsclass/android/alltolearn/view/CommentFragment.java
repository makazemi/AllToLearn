package com.parsclass.android.alltolearn.view;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.adapter.CommentAdapter;
import com.parsclass.android.alltolearn.base.BaseHideBottomNavFragment;
import com.parsclass.android.alltolearn.model.Comment;
import com.parsclass.android.alltolearn.remote.Status;
import com.parsclass.android.alltolearn.viewmodel.CommentViewModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.AVG_RATING_COURSE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.ID_COURSE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.NUMBER_RATING_COURSE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.RATING_COURSE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TITLE_COURSE;


public class CommentFragment extends BaseHideBottomNavFragment {

    @BindView(R.id.txtAvgRate)
    TextView txtAvgRate;
    @BindView(R.id.txtNumberRate)
    TextView txtNumberRate;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.commentRcy)
    RecyclerView commentRcy;
    @BindView(R.id.toolbar_title)
    TextView txtTitleToolbar;
    @BindView(R.id.progressBar)
    AVLoadingIndicatorView progressBar;
    private View view;
    private CommentViewModel commentViewModel;
    private CommentAdapter commentAdapter;
    private ArrayList<Comment> commentArrayList = new ArrayList<>();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String title_course;
    private String AvgRate_course;
    private String numberRate_course;
    private float rating;
    private int course_id;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null) {
            view = inflater.inflate(R.layout.fragment_comment, container, false);
        }
        ButterKnife.bind(this, view);
        initView();
        setCommentRcy();
        return view;
    }

    private void initView() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);

        collapsingToolbarLayout.setTitleEnabled(false);
        ViewCompat.setNestedScrollingEnabled(commentRcy, false);

        commentViewModel= ViewModelProviders.of(this).get(CommentViewModel.class);
        title_course=getArguments().getString(TITLE_COURSE);
        AvgRate_course=getArguments().getString(AVG_RATING_COURSE);
        numberRate_course=getArguments().getString(NUMBER_RATING_COURSE);
        rating=getArguments().getFloat(RATING_COURSE);
        course_id=getArguments().getInt(ID_COURSE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        commentRcy.setLayoutManager(layoutManager);
        commentAdapter = new CommentAdapter(getContext(), commentArrayList, CommentAdapter.ALL_LIST);
        commentRcy.setAdapter(commentAdapter);

        txtTitleToolbar.setText(title_course);
        txtAvgRate.setText(AvgRate_course);
        txtNumberRate.setText(numberRate_course);
        ratingBar.setRating(rating);
    }

    private void setCommentRcy(){
        commentViewModel.getAllComments(course_id).observe(this, new Observer<List<Comment>>() {
            @Override
            public void onChanged(@Nullable List<Comment> comments) {
                commentArrayList.addAll(comments);
                commentAdapter.setValues(commentArrayList);
                commentAdapter.notifyDataSetChanged();
            }
        });

        commentViewModel.getStatus().observe(this, new Observer<Status>() {
            @Override
            public void onChanged(@Nullable Status status) {
                if(status.equals(Status.SUCCESS) || status.equals(Status.ERROR)){
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick(R.id.imgBack)
    public void onBackClick(){
        getFragmentManager().popBackStack();
    }
}
