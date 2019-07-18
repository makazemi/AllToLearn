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
import com.parsclass.android.alltolearn.Utils.GlideApp;

import java.util.ArrayList;

public class mySimpleAdapter extends RecyclerView.Adapter<mySimpleAdapter.CustomView> {

    private Context context;
    private ArrayList<String> values;
    private int type;
    public static final int COURSE_INFO_TYPE=1;
    public static final int SUB_CATEGORY_LIST_TYPE=2;
    public static final int CHECK_LIST_TYPE =3;

    public mySimpleAdapter(Context context, ArrayList<String> values, int type) {
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
        String item=values.get(position);
        holder.txtTitle.setText(item);
        if(this.type==COURSE_INFO_TYPE){
            if(position==0) {
                GlideApp.with(context)
                        .load(R.drawable.ic_video_cinfo)
                        .placeholder(R.drawable.ic_video_cinfo)
                        .error(R.drawable.ic_video_cinfo)
                        .into(holder.imgIcon);
            }else if(position==1){
                GlideApp.with(context)
                        .load(R.drawable.ic_quiz_cinfo)
                        .placeholder(R.drawable.ic_quiz_cinfo)
                        .error(R.drawable.ic_quiz_cinfo)
                        .into(holder.imgIcon);
            }
            else if (position==2){
                GlideApp.with(context)
                        .load(R.drawable.ic_support_file_cinfo)
                        .placeholder(R.drawable.ic_support_file_cinfo)
                        .error(R.drawable.ic_support_file_cinfo)
                        .into(holder.imgIcon);
            }
            else if(position==3){
                GlideApp.with(context)
                        .load(R.drawable.ic_article_cinfo)
                        .placeholder(R.drawable.ic_article_cinfo)
                        .error(R.drawable.ic_article_cinfo)
                        .into(holder.imgIcon);
            }
            else if(position==4){
                GlideApp.with(context)
                        .load(R.drawable.ic_infinity_cinfo)
                        .placeholder(R.drawable.ic_infinity_cinfo)
                        .error(R.drawable.ic_infinity_cinfo)
                        .into(holder.imgIcon);
            }
            else if(position==5){
                GlideApp.with(context)
                        .load(R.drawable.ic_mobile_cinfo)
                        .placeholder(R.drawable.ic_mobile_cinfo)
                        .error(R.drawable.ic_mobile_cinfo)
                        .into(holder.imgIcon);
            }
            else if(position==6){
                GlideApp.with(context)
                        .load(R.drawable.ic_certificate_cinfo)
                        .placeholder(R.drawable.ic_certificate_cinfo)
                        .error(R.drawable.ic_certificate_cinfo)
                        .into(holder.imgIcon);
            }
        }
        else if(type== CHECK_LIST_TYPE){
            GlideApp.with(context)
                    .load(R.drawable.ic_check)
                    .placeholder(R.drawable.ic_check)
                    .error(R.drawable.ic_check)
                    .into(holder.imgIcon);
        }
        else if(type==SUB_CATEGORY_LIST_TYPE){


        }


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
