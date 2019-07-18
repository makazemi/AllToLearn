package com.parsclass.android.alltolearn.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.parsclass.android.alltolearn.model.CategoryItem;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CustomView> {
    private List<CategoryItem> values;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnItemClickListener listener;

    public static final String TAG="CategoryAdapter";

    public CategoryAdapter( Context context,List<CategoryItem> values) {
        this.values = values;
        this.context = context;
        Log.e(TAG,"in cousntru");
    }

    public CategoryAdapter(Context context) {
        this.context = context;
    }

    public void setValues(List<CategoryItem> values){
        this.values=values;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_cateogory_recycler_item,viewGroup,false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {

        CategoryItem item=values.get(position);
        holder.txtTitle.setText(item.getTitle());
        GlideApp.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.img_itm_1_start_viewpager)
                .error(R.drawable.img_itm_1_start_viewpager)
                .into(holder.imgCategory);

        //holder.imgCategory.setImageResource(item.getImage());
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

//    @NonNull
//    @Override
//    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
////        if(layoutInflater==null){
////            layoutInflater=layoutInflater.from(parent.getContext());
////        }
//        layoutInflater=layoutInflater.from(parent.getContext());
//
//        CatDataBinding categoryBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.show_cateogory_recycler_item,parent,false);
//        return new CustomView(categoryBinding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CustomView holder, int position) {
//        CategoryItem categoryItem=values.get(position);
//        //holder.getCategoryBinding().imgItemCategory.layout(0,0,0,0);
//        holder.bind(categoryItem);
//        holder.getCategoryBinding().getRoot().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(listener!=null && position!=RecyclerView.NO_POSITION){
//                    listener.onItemInstructorClick(categoryItem);
//                }
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return values.size();
//    }
//
//    public class CustomView extends RecyclerView.ViewHolder{
//
//        private CatDataBinding categoryBinding;
//
//        public CustomView(CatDataBinding categoryBinding) {
//            super(categoryBinding.getRoot());
//            this.categoryBinding=categoryBinding;
//        }
//
//        public void bind(CategoryItem categoryItem){
//            this.categoryBinding.setCategoryItem(categoryItem);
//            categoryBinding.executePendingBindings();
//        }
//
//        public CatDataBinding getCategoryBinding(){
//            return categoryBinding;
//        }
//    }

    public interface OnItemClickListener{
        void onItemInstructorClick(CategoryItem categoryItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
