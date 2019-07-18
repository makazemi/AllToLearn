package com.parsclass.android.alltolearn.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.model.Article;

import java.util.ArrayList;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.CustomView> {

    Context context;
    ArrayList<Article> values;

    public AdapterSearch(Context context, ArrayList<Article> values) {
        this.context = context;
        this.values = values;
    }

    public AdapterSearch(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_item,viewGroup,false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        holder.textView.setText(values.get(position).getTitle());
    }

    public void setValues(ArrayList<Article> values){
        this.values=values;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class CustomView extends RecyclerView.ViewHolder{

        private TextView textView;
        public CustomView(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);
        }
    }
}
