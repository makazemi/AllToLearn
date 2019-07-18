package com.parsclass.android.alltolearn.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.parsclass.android.alltolearn.model.Course;
import com.parsclass.android.alltolearn.view.DetailMyCourseActivity;
import com.parsclass.android.alltolearn.view.HomeFragment;

import java.util.List;

public class MyListCourseAdapter extends RecyclerView.Adapter<MyListCourseAdapter.CustomView> {

    private Context context;
    private List<Course> values;

    public MyListCourseAdapter(Context context, List<Course> values) {
        this.context = context;
        this.values = values;
    }

    public MyListCourseAdapter(Context context){
        this.context=context;
    }

    public void setCourse(List<Course> courses){
        this.values=courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.course_my_recycler_item,parent,false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        Course item=values.get(position);

            holder.txtTitle.setText(item.getTitle());
            holder.txtInstructor.setText(item.getInstructorName());
            GlideApp.with(context)
                    .load(item.getImagePath())
                    .placeholder(R.drawable.img_category_music)
                    .error(R.drawable.img_category_music)
                    .into(holder.imgCourse);
            if(!item.isStarted())
                holder.txtProgress.setText(context.getString(R.string.start));
            else {
                holder.txtProgress.setText("۲ درصد تکمیل شده");
            }

            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DetailMyCourseActivity.class);
                    intent.putExtra(HomeFragment.KEY_DETAIL_COURSE_ACTIVITY,item);
                    context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class CustomView extends RecyclerView.ViewHolder {

        private View rootView;
        private ImageView imgCourse;
        private TextView txtTitle,txtInstructor,txtProgress;
        private ProgressBar progressBarComplete;
        public CustomView(@NonNull View itemView) {
            super(itemView);
            rootView=itemView;
            imgCourse=itemView.findViewById(R.id.imgCourse);
            txtTitle=itemView.findViewById(R.id.txtTitleCourse);
            txtInstructor=itemView.findViewById(R.id.txtInstructor);
            txtProgress=itemView.findViewById(R.id.txtProgress);
            progressBarComplete=itemView.findViewById(R.id.progressBarComplete);
        }
    }
}
