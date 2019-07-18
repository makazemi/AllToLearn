package com.parsclass.android.alltolearn.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;

import java.util.ArrayList;

public class WhatLearnAdapter extends RecyclerView.Adapter<WhatLearnAdapter.CustomView> {
    private Context context;
    private ArrayList<String> values;
    private int type;
    public static final int COURSE_INFO_TYPE=1;
    public static final int CATEGORY_LIST_TYPE=2;
    public static final int WHAT_WE_LEARN_TYPE=3;

    public WhatLearnAdapter(Context context, ArrayList<String> values, int type) {
        this.context = context;
        this.values = values;
        this.type = type;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_search_recycler_item,parent,false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {

    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class CustomView extends RecyclerView.ViewHolder {

        private View rootView;
        private TextView txtTitle;
        private ImageView imgIcon;
        public CustomView(@NonNull View itemView) {
            super(itemView);
            rootView=itemView;
            txtTitle=itemView.findViewById(R.id.txtItem);
            imgIcon=itemView.findViewById(R.id.imgIcon);
        }
    }
}
