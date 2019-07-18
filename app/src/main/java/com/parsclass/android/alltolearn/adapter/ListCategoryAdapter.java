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
import com.parsclass.android.alltolearn.model.CategoryItem;

import java.util.List;


public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.CustomView> {

    private Context context;
    private List<CategoryItem> values;
    private OnItemClickListener listener;

    public ListCategoryAdapter(Context context, List<CategoryItem> values) {
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
        CategoryItem item=values.get(position);
        holder.txtTitle.setText(item.getTitle());
        GlideApp.with(context)
                .load(item.getImageDrawable())
                .placeholder(R.drawable.ic_development_category)
                .error(R.drawable.ic_development_category)
                .into(holder.imgIcon);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null && position!=RecyclerView.NO_POSITION) {
                    listener.onItemCategoryClick(item);
                }
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

    public interface OnItemClickListener{
        void onItemCategoryClick(CategoryItem categoryItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
