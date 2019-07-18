package com.parsclass.android.alltolearn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.parsclass.android.alltolearn.model.CategoryItem;

import java.util.List;

public class CategoryGridAdapter extends BaseAdapter {


    private Context context;
    private List<CategoryItem> values;

    public CategoryGridAdapter(Context context, List<CategoryItem> values) {
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CategoryItem item = values.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.show_cateogory_recycler_item, null);
        }

        final ImageView imageView = convertView.findViewById(R.id.imgItemCategory);
        final TextView nameTextView = convertView.findViewById(R.id.txtTitleItemCategory);

        GlideApp.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.img_itm_1_start_viewpager)
                // .apply(new RequestOptions().override(200, 200))
                .error(R.drawable.img_itm_1_start_viewpager)
                .into(imageView);

        nameTextView.setText(item.getTitle());

        return convertView;
    }
}
