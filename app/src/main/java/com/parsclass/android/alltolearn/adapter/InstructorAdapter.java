package com.parsclass.android.alltolearn.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.parsclass.android.alltolearn.model.Instructor;

import java.util.List;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_BRIEF_ITEM_INSTRUCTOR;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_DETAIL_ITEM_INSTRUCTOR;

public class InstructorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Instructor> values;
    private int typeList;
    public static final int LIMIT_SIZE_TYPE = 1;
    public static final int ALL_LIST = 2;
    private OnItemClickListener listener;

    public InstructorAdapter(Context context, List<Instructor> values, int typeList) {
        this.context = context;
        this.values = values;
        this.typeList = typeList;
    }

    public InstructorAdapter(Context context, int typeList) {
        this.context = context;
        this.typeList = typeList;
    }

    public void setValues(List<Instructor> values) {
        this.values = values;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instructor_recycler_item, parent, false);
                return new DetailView(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.instructor_brief_recycler_item, parent, false);
                return new BriefView(view);
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1:
                DetailView detailView = (DetailView) holder;
                bindDetailView(detailView,position);
                break;

            case 2:
                BriefView briefView = (BriefView) holder;
                bindBriefView(briefView,position);
                break;
        }

    }

    public void bindDetailView(DetailView holder, int position) {
        Instructor item = values.get(position);
        holder.txtName.setText(item.getName());
        holder.txtNumberCourse.setText(item.getNumberCourses() + " " +context.getString(R.string.label_course));
        holder.txtNumberStudent.setText(item.getNumberStudents() + " " + context.getString(R.string.label_student));
        holder.txtAvgRate.setText(context.getString(R.string.label_avg_rate) + " " + String.valueOf(item.getRateAvg()));
        GlideApp.with(context)
                .load(item.getImagePath())
                .placeholder(R.drawable.avator_placeholder)
                .error(R.drawable.avator_placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imgProfile);

        holder.txtShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemInstructorClick(item);
                }
            }
        });
    }

    public void bindBriefView(BriefView holder,int position){
        Instructor item = values.get(position);
        holder.txtName.setText(item.getName());
        GlideApp.with(context)
                .load(item.getImagePath())
                .placeholder(R.drawable.ic_avatar_placehoder)
                .error(R.drawable.ic_avatar_placehoder)
                .apply(RequestOptions.circleCropTransform())
                //.apply(new RequestOptions().override(40, 40))
                .into(holder.imgProfile);
    }

    @Override
    public int getItemCount() {
        if (typeList == LIMIT_SIZE_TYPE && values.size() >= 3)
            return 3;
        else
            return values.size();
    }

    public class DetailView extends RecyclerView.ViewHolder {

        private View rootView;
        private TextView txtName, txtNumberStudent, txtNumberCourse, txtShowProfile, txtAvgRate;
        private ImageView imgProfile;

        public DetailView(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            txtName = itemView.findViewById(R.id.txtNameInstructor);
            txtNumberStudent = itemView.findViewById(R.id.txtNumberStudent);
            txtNumberCourse = itemView.findViewById(R.id.txtNumberCourse);
            imgProfile = rootView.findViewById(R.id.imgInstructor);
            txtAvgRate = rootView.findViewById(R.id.txtAvgRate);
            txtShowProfile = itemView.findViewById(R.id.txtViewProfile);
        }
    }

    public class BriefView extends RecyclerView.ViewHolder{

        private View rootView;
        private TextView txtName;
        private ImageView imgProfile;
        public BriefView(@NonNull View itemView) {
            super(itemView);
            rootView=itemView;
            txtName=itemView.findViewById(R.id.txtItem);
            imgProfile=itemView.findViewById(R.id.imgIcon);

        }
    }

    public interface OnItemClickListener {
        void onItemInstructorClick(Instructor instructor);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        Instructor item = values.get(position);
        switch (item.getType_instructor()) {
            case TYPE_DETAIL_ITEM_INSTRUCTOR:
                return TYPE_DETAIL_ITEM_INSTRUCTOR;
            case TYPE_BRIEF_ITEM_INSTRUCTOR:
                return TYPE_BRIEF_ITEM_INSTRUCTOR;
            default:
                return -1;
        }
    }
}

