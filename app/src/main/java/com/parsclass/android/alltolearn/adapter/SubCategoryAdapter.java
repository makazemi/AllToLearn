package com.parsclass.android.alltolearn.adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.parsclass.android.alltolearn.model.SubCategory;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.CustomView> {

    private Context context;
    private ArrayList<SubCategory> values;

    public SubCategoryAdapter(Context context, ArrayList<SubCategory> values) {
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_search_recycler_item,parent,false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        SubCategory item=values.get(position);
        holder.txtTitle.setText(item.getTitle());
        GlideApp.with(context)
                .load(item.getImagePath())
                .placeholder(R.drawable.ic_development_category)
                .error(R.drawable.ic_development_category)
                .into(holder.imgIcon);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConstantUtil.KEY_SUB_CATEGORY,item);
//                SubCategoryFragment fragment = new SubCategoryFragment();
//                fragment.setArguments(bundle);
//                loadFragment(R.id.flContent, fragment);
            }
        });
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

    private void loadFragment(int layoutId, Fragment fragment){
        FragmentManager manager =((FragmentActivity)context).getSupportFragmentManager();
        manager.beginTransaction().addToBackStack("f").replace(layoutId, fragment).commit();
    }


}
