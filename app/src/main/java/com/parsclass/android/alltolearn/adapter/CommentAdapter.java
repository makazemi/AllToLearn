package com.parsclass.android.alltolearn.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.model.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CustomView> {
    private Context context;
    private List<Comment> values;
    private int typeList;
    public static final int LIMIT_SIZE_TYPE=1;
    public static final int ALL_LIST=2;

    public CommentAdapter(Context context, List<Comment> values, int typeList) {
        this.context = context;
        this.values = values;
        this.typeList = typeList;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_recycler_item, parent, false);
        return new CustomView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        Comment item=values.get(position);
        holder.txtAuthor.setText(item.getAuthor());
        holder.txtComment.setText(item.getContent());
        holder.txtDate.setText(item.getStringDate());
        holder.ratingBar.setRating(item.getRate());
    }

    @Override
    public int getItemCount() {
        if(typeList==LIMIT_SIZE_TYPE && values.size()>=3)
            return 3;
        else
            return values.size();
    }

    public class CustomView extends RecyclerView.ViewHolder {

        private View rootView;
        private TextView txtComment,txtAuthor,txtDate;
        private RatingBar ratingBar;
        public CustomView(@NonNull View itemView) {
            super(itemView);
            rootView=itemView;
            ratingBar=itemView.findViewById(R.id.ratingBar);
            txtComment=itemView.findViewById(R.id.txtComment);
            txtAuthor=itemView.findViewById(R.id.txtAuthor);
            txtDate=itemView.findViewById(R.id.txtDate);
        }
    }

    public void setValues(List<Comment> commentList){
        this.values=commentList;
    }
}
