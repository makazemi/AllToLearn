package com.parsclass.android.alltolearn.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.hardware.camera2.CameraManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.parsclass.android.alltolearn.model.Lecture;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.wang.avi.AVLoadingIndicatorView;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_ARTICLE;

public class LectureViewHolder extends ChildViewHolder {
    public static final String TAG="LectureViewHolder";

    public static final int TYPE_LIST_MY_COURSE=1;
    public static final int TYPE_LIST_PUBLIC_COURSE=2;
    public TextView txtNumber,txtTitle,txtTypeMedia,txtDuration,txtPreview;
    private ImageView imgDownload,imgCompleted;
    private AVLoadingIndicatorView progressBar;
    private View rootView;

    public LectureViewHolder(View itemView) {
        super(itemView);
        rootView=itemView;
        txtNumber=itemView.findViewById(R.id.txtNumber);
        txtTitle=itemView.findViewById(R.id.txtTitleLecture);
        txtTypeMedia=itemView.findViewById(R.id.txtTypeMedia);
        txtDuration=itemView.findViewById(R.id.txtDuration);
        txtPreview=itemView.findViewById(R.id.txtPreview);
        imgDownload=itemView.findViewById(R.id.imgDownload);
        imgCompleted=itemView.findViewById(R.id.imgCompleted);
        progressBar=itemView.findViewById(R.id.progressBar);
    }

    public void setAttribute(Lecture lecture, int typeList, Context context) {
        txtNumber.setText(lecture.getNumber());
        txtTitle.setText(lecture.getTitle_lecture());
        txtTypeMedia.setText(lecture.getTypeMedia());
        if(!TextUtils.isEmpty(lecture.getLduration()))
            txtDuration.setText(lecture.getLduration()+" "+"دقیقه");
        imgDownload.setVisibility(View.GONE);
        imgCompleted.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        if(lecture.isHasPreview())
            txtPreview.setVisibility(View.VISIBLE);
        else
            txtPreview.setVisibility(View.GONE);
        if(lecture.getTypeMedia().equals(TYPE_ARTICLE))
            txtDuration.setVisibility(View.GONE);
        else
            txtDuration.setVisibility(View.VISIBLE);

    }


    public TextView getTxtPreview() {
        return txtPreview;
    }

    public View getRootView() {
        return rootView;
    }

    public ImageView getImgDownload(){return imgDownload;}


}
