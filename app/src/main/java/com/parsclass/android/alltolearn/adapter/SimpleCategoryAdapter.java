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
import com.parsclass.android.alltolearn.model.CategoryItem;

import java.util.List;

public class SimpleCategoryAdapter extends RecyclerView.Adapter<SimpleCategoryAdapter.CustomView> {


    private Context context;
    private List<CategoryItem> values;
    private OnItemClickListener listener;

    public SimpleCategoryAdapter(Context context, List<CategoryItem> values) {
        this.context = context;
        this.values = values;
    }

    public SimpleCategoryAdapter(Context context) {
        this.context = context;
    }

    public void setValues(List<CategoryItem> values){
        this.values=values;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_category_recycler_item_simple,viewGroup,false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {

        CategoryItem item=values.get(position);
        holder.txtTitle.setText(item.getTitle());
     //   holder.imgCategory.layout(0,0,0,0);

//        GlideApp.with(context)
//                .load(item.getImage())
//                .placeholder(R.drawable.img_itm_1_start_viewpager)
//                .error(R.drawable.img_itm_1_start_viewpager)
//                .into(holder.imgCategory);

        holder.imgCategory.setImageResource(item.getImage());
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null && position!=RecyclerView.NO_POSITION){
                    listener.onItemInstructorClick(item);
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
        private ImageView imgCategory;
        private TextView txtTitle;
        public CustomView(@NonNull View itemView) {
            super(itemView);
            rootView=itemView;
            imgCategory=itemView.findViewById(R.id.imgItemCategory);
            txtTitle=itemView.findViewById(R.id.txtTitleItemCategory);
        }
    }

    public interface OnItemClickListener{
        void onItemInstructorClick(CategoryItem categoryItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
