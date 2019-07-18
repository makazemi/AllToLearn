package com.parsclass.android.alltolearn.adapter;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.parsclass.android.alltolearn.model.VPItem;

import java.util.ArrayList;

public class StartVPAdapter extends PagerAdapter {

    private ArrayList<VPItem> values;

    public StartVPAdapter(ArrayList<VPItem> values) {
        this.values = values;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater=LayoutInflater.from(container.getContext());
        View view=inflater.inflate(R.layout.start_view_pager_item,container,false);
        container.addView(view);
        TextView txtTitle= view.findViewById(R.id.txtTitle);
        TextView txtDesc= view.findViewById(R.id.txtDesc);
        ImageView imgVp=view.findViewById(R.id.img_view_pager);
//        RelativeLayout lLayout =view.findViewById(R.id.relativeRoot);
//        lLayout.setBackgroundResource(values.get(position).getImage());
        txtTitle.setText(values.get(position).getTitle());
        txtDesc.setText(values.get(position).getDescription());


        GlideApp.with(container.getContext())
                .load(values.get(position).getImage())
                .placeholder(R.drawable.img_background_intro1)
                .error(R.drawable.img_background_intro1)
                .into(imgVp);
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
}
