package com.parsclass.android.alltolearn.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.parsclass.android.alltolearn.model.Course;

import java.util.List;

public class SimpleCourseAdapter extends RecyclerView.Adapter<SimpleCourseAdapter.CustomView> {

    private Context context;
    private List<Course> values;
    int typeList;
    public static final int LIMIT_LIST = 1;
    public static final int ALL_LIST = 2;
    public static final int ALSO_VIEWED = 3;
    public static final int INSTRUCTOR_COURSE= 4;
    public static final int FAVORITE_COURSE= 5;
    public static final String TAG="SimpleCourseAdapter";

    private OnItemClickListener listener;

    public SimpleCourseAdapter(Context context, List<Course> values, int typeList) {
        this.context = context;
        this.values = values;
        this.typeList = typeList;
    }

    public SimpleCourseAdapter(Context context, int typeList) {
        this.context = context;
        this.typeList = typeList;
    }

    public void setValues(List<Course> values){
        this.values=values;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(typeList==ALSO_VIEWED || typeList==FAVORITE_COURSE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_recycler_item, parent, false);
           // Log.e(TAG,"if simple adpter "+typeList);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_recycler_item, parent, false);
           // Log.e(TAG,"else simple adpter "+typeList);
        }
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        Course item = values.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.txtCount.setText(item.getNumberPersonRate());
        holder.txtPrice.setText(String.valueOf(item.getPrice()));
        holder.ratingBar.setRating(item.getRatingAvg());

        GlideApp.with(context)
                .load(item.getImagePath())
                .placeholder(R.drawable.img_itm_1_start_viewpager)
                .error(R.drawable.img_itm_1_start_viewpager)
                .into(holder.imgCourse);

        ViewCompat.setTransitionName(holder.imgCourse, String.valueOf(item.getId()));

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listener!=null && position!=RecyclerView.NO_POSITION) {
                    listener.onItemCourseClick(item);

                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if ((typeList == LIMIT_LIST) && values.size()>=11)
            return 11;
        else if((typeList==INSTRUCTOR_COURSE || typeList==ALSO_VIEWED) && values.size()>=3)
            return 3;
        else
            return values.size();
    }

    public class CustomView extends RecyclerView.ViewHolder {

        public View rootView;
        public ImageView imgCourse;
        private TextView txtTitle, txtCount, txtPrice;
        private RatingBar ratingBar;

        public CustomView(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            imgCourse = rootView.findViewById(R.id.imgCourse);
            txtTitle = rootView.findViewById(R.id.txtTitleCourse);
            txtCount = rootView.findViewById(R.id.txtCount);
            txtPrice = rootView.findViewById(R.id.txtPrice);
            ratingBar = rootView.findViewById(R.id.ratingBar);
        }
    }

    public interface OnItemClickListener{
        void onItemCourseClick(Course item);

    }


    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }


}