package com.parsclass.android.alltolearn.adapter;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
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

public class MainSliderAdapter extends PagerAdapter {

    private List<Course> values;
    //private Resource<List<Course>> values;
    private OnItemClickListener listener;

    public MainSliderAdapter(List<Course> values) {
        this.values = values;

    }

    public MainSliderAdapter() {
    }

    public void setValues(List<Course> values){
        this.values=values;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater=LayoutInflater.from(container.getContext());
        View view=inflater.inflate(R.layout.main_view_pager_item,container,false);
        container.addView(view);

        Course course=values.get(position);
        TextView txtTitle= view.findViewById(R.id.txtTitle);
        TextView txtCount= view.findViewById(R.id.txtCount);
        RatingBar ratingBar=view.findViewById(R.id.ratingBar);
        ImageView imgVp=view.findViewById(R.id.img_view_pager);
        txtTitle.setText(values.get(position).getTitle());
        txtCount.setText(values.get(position).getNumberPersonRate());
        ratingBar.setRating(values.get(position).getRatingAvg());

        GlideApp.with(container.getContext())
                .load(values.get(position).getImagePath())
                .placeholder(R.drawable.img_itm_1_start_viewpager)
                .error(R.drawable.img_itm_1_start_viewpager)
                .into(imgVp);

        final int index=position;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(listener!=null) {
                    listener.onItemClick(course);
                }
            }
        });
        return view;
    }


    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return ((Object)view).equals(o);
    }

    public interface OnItemClickListener{
        void onItemClick(Course course);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
