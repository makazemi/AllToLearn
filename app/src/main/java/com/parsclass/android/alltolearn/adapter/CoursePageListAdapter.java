package com.parsclass.android.alltolearn.adapter;

import androidx.paging.PagedListAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.databinding.CourseBinding;
import com.parsclass.android.alltolearn.model.Course;

import java.util.List;

public class CoursePageListAdapter extends PagedListAdapter<Course, CoursePageListAdapter.MyViewHolder> {

    private List<Course> values;
    private LayoutInflater layoutInflater;
    private OnItemClickListener listener;

    public CoursePageListAdapter() {
        super(Course.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

//        if(layoutInflater==null){
//            layoutInflater=layoutInflater.from(parent.getContext());
//        }
        CourseBinding binding=  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.course_list_recycler_item, parent, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Course item=getItem(position);
        holder.bind(item);
        holder.getBinding().getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null && position!=RecyclerView.NO_POSITION){
                    listener.onItemCourseClick(item);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CourseBinding binding;

        MyViewHolder(CourseBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void bind(Course item ){
            this.binding.setModel(item);
            binding.executePendingBindings();
        }

        public CourseBinding getBinding(){
            return binding;
        }

    }

    public interface OnItemClickListener{
        void onItemCourseClick(Course course);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
