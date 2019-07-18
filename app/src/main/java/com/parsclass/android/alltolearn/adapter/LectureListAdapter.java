package com.parsclass.android.alltolearn.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.exoplayer2.offline.Download;
import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.ConstantUtil;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.parsclass.android.alltolearn.model.LectureList;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import static com.parsclass.android.alltolearn.Utils.ConstantUtil.DOWNLOAD_COMPLETED_STATUS;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.DOWNLOAD_NOT_START_STATUS;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.DOWNLOAD_RUNNING_STATUS;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_ARTICLE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_LECTURE;
import static com.parsclass.android.alltolearn.Utils.ConstantUtil.TYPE_SECTION;

public class LectureListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG="LectureListAdapter";
    private Context context;
    private List<LectureList> values;
    private OnItemClickListener listener;
    private int child_index;


    public LectureListAdapter(Context context) {
        this.context = context;

    }

    public void setValues(List<LectureList> values){
        this.values=values;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecture_recycler_item, parent, false);
                return new LectureView(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_recycler_item, parent, false);
                return new SectionView(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                LectureView lectureView = (LectureView)holder;
                bindLecture(lectureView,position);
                break;

            case 1:
                SectionView sectionView = (SectionView)holder;
                bindSection(sectionView,position);
                break;
        }
    }

    private void bindLecture(LectureView holder,int position){
        LectureList item=values.get(position);
        holder.txtNumber.setText(item.getNumber_lectureList());
        holder.txtTitle.setText(item.getTitle_lectureList());
        holder.txtTypeMedia.setText(item.getTypeMedia_lectureList());
        if(!TextUtils.isEmpty(item.getLduration_lectureList()))
            holder.txtDuration.setText(item.getLduration_lectureList()+" "+"دقیقه");
        if(item.getTypeMedia_lectureList().equals(TYPE_ARTICLE)) {
            holder.txtDuration.setVisibility(View.GONE);
            holder.imgDownload.setVisibility(View.GONE);
           // Log.e(TAG,"type media is: "+TYPE_ARTICLE);
        }
        else {
            holder.txtDuration.setVisibility(View.VISIBLE);
        }
        holder.txtPreview.setVisibility(View.GONE);
        if(item.isHasCompleted_lectureList()){
            holder.imgCompleted.setVisibility(View.VISIBLE);
           // Log.e(TAG,"in adapter");
          //  holder.progressBar.setVisibility(View.GONE);
           // DrawableCompat.setTint(holder.imgDownload.getDrawable(), ContextCompat.getColor(context, R.color.colorCompleteDownload));
        }
        holder.progressBar.setVisibility(View.GONE);
        holder.progressBar.hide();
        if(!item.getTypeMedia_lectureList().equals(TYPE_ARTICLE)){

            if(item.getDownloadStatus_lectureList()== DOWNLOAD_RUNNING_STATUS){
                holder.imgDownload.setVisibility(View.GONE);
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.progressBar.show();
                //holder.progressBar.setProgress(item.getProgressDownload_lectureList());
                Log.e(TAG,"in running viewholder postion= "+position);
            }
            else if(item.getDownloadStatus_lectureList()==ConstantUtil.DOWNLOAD_NOT_START_STATUS){
                GlideApp.with(context)
                        .load(R.drawable.ic_download)
                        .placeholder(R.drawable.ic_download)
                        .error(R.drawable.ic_download)
                        .into(holder.imgDownload);
                holder.imgDownload.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);
                holder.progressBar.hide();
               // Log.e(TAG,"in not start viewholder postion= "+position);
            }
           else if(item.getDownloadStatus_lectureList()==ConstantUtil.DOWNLOAD_COMPLETED_STATUS){
                holder.progressBar.setVisibility(View.GONE);
                GlideApp.with(context)
                        .load(R.drawable.ic_download_complete)
                        .placeholder(R.drawable.ic_download_complete)
                        .error(R.drawable.ic_download_complete)
                        .into(holder.imgDownload);
                holder.imgDownload.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.GONE);
                holder.progressBar.hide();
                  Log.e(TAG,"in completed viewholder postion= "+position);
            }
        }
        else {
            if(item.getDownloadStatus_lectureList()== DOWNLOAD_NOT_START_STATUS){
                holder.progressBar.setVisibility(View.GONE);
                holder.progressBar.hide();
            }

            else if(item.getDownloadStatus_lectureList()== DOWNLOAD_RUNNING_STATUS){
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.progressBar.show();
                //holder.progressBar.setProgress(item.getProgressDownload_lectureList());
                //Log.e(TAG,"in running viewholder");
            }
            else if(item.getDownloadStatus_lectureList()==ConstantUtil.DOWNLOAD_COMPLETED_STATUS){
                holder.progressBar.setVisibility(View.GONE);
                holder.progressBar.hide();
            }
        }



//        if(item.isPlaying()){
//            holder.animationView.setVisibility(View.VISIBLE);
//        }else{
//            holder.animationView.setVisibility(View.GONE);
//        }

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onLectureClick(item,position);
                }
//                child_index=position;
//                notifyDataSetChanged();
            }
        });

        holder.imgDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onDownloadClick(item);
                }
            }
        });

       // if(child_index ==position)
        if(item.isPlaying())
        {
            holder.txtTitle.setTextColor(
                    ResourcesCompat.getColor(context.getResources(), R.color.colorMarjani, null));
            holder.txtNumber.setTextColor(
                    ResourcesCompat.getColor(context.getResources(), R.color.colorMarjani, null));
        }else {
            holder.txtTitle.setTextColor(
                    ResourcesCompat.getColor(context.getResources(), R.color.colorPublicText, null));
            holder.txtNumber.setTextColor(
                    ResourcesCompat.getColor(context.getResources(), R.color.colorPublicText, null));
        }
    }

    private void bindSection(SectionView holder,int position){
        LectureList item=values.get(position);
        holder.txtNumber.setText(item.getNumber_lectureList());
        holder.txtTitle.setText(item.getTitle_lectureList());
    }

    public void updatePlayingLecture(int position) {
        child_index = position;
        notifyDataSetChanged();
    }
    public void updateProgressDownload(int position,int progress){
        LectureList item=values.get(position);
        //item.setProgressDownload_lectureList(progress);
        if(progress==100)
            item.setDownloadStatus_lectureList(DOWNLOAD_COMPLETED_STATUS);
        else
            item.setDownloadStatus_lectureList(DOWNLOAD_RUNNING_STATUS);
        values.set(position,item);
        notifyItemChanged(position);
    }

    public void updateProgressDownloadMedia(int position,int progress,int download_state){
        LectureList item=values.get(position);
        if(progress>=0 && progress<100 && download_state==Download.STATE_DOWNLOADING){
            item.setDownloadStatus_lectureList(DOWNLOAD_RUNNING_STATUS);
            item.setProgressDownload_lectureList(progress);
            Log.e(TAG,"Download.STATE_DOWNLOADING progress= "+progress+" positn= "+position+
                    " state: "+download_state);
        }
        if(download_state== Download.STATE_COMPLETED || progress==100){
            item.setDownloadStatus_lectureList(DOWNLOAD_COMPLETED_STATUS);
            Log.e(TAG,"Download.STATE_COMPLETED progress= "+progress+" positn= "+position+
                    " state: "+download_state);
        }
        if(download_state==Download.STATE_FAILED){
            item.setDownloadStatus_lectureList(DOWNLOAD_NOT_START_STATUS);
            Log.e(TAG,"Download.STATE_FAILED progress= "+progress+" positn= "+position+
                    " state: "+download_state);
        }
        values.set(position,item);
        notifyItemChanged(position);
    }

    public void updateIsPlaying(int position){
        for(int i=0;i<values.size();i++){
            LectureList lecture=values.get(i);
            if(i==position)
            lecture.setPlaying(true);
            else
            lecture.setPlaying(false);
            values.set(i,lecture);
        }
        notifyDataSetChanged();
    }

    public void updateCompleteLecture(int position){
        LectureList item=values.get(position);
        item.setHasCompleted_lectureList(true);
        values.set(position,item);
        notifyItemChanged(position);
    }

    @Override
    public int getItemViewType(int position) {
        switch (values.get(position).getTypeLecture_lectureList()) {
            case 0:
                return TYPE_LECTURE;
            case 1:
                return TYPE_SECTION;
            default:
                return -1;
        }
    }


    @Override
    public int getItemCount() {
        return values.size();
    }

    public class LectureView extends RecyclerView.ViewHolder {

        private TextView txtNumber,txtTitle,txtTypeMedia,txtDuration,txtPreview;
        private ImageView imgDownload,imgCompleted;
       // private ProgressBar progressBar;
       // private DonutProgress progressBar;
        private AVLoadingIndicatorView progressBar;
        private LottieAnimationView animationView;
        private View rootView;
        public LectureView(@NonNull View itemView) {
            super(itemView);
            rootView=itemView;
            txtNumber=itemView.findViewById(R.id.txtNumber);
            txtTitle=itemView.findViewById(R.id.txtTitleLecture);
            txtTypeMedia=itemView.findViewById(R.id.txtTypeMedia);
            txtDuration=itemView.findViewById(R.id.txtDuration);
            imgDownload=itemView.findViewById(R.id.imgDownload);
            imgCompleted=itemView.findViewById(R.id.imgCompleted);
            progressBar=itemView.findViewById(R.id.progressBar);
            txtPreview=itemView.findViewById(R.id.txtPreview);
//            animationView=itemView.findViewById(R.id.animation_playing);
//            animationView.setScale(3f);
        }
    }

    public class SectionView extends RecyclerView.ViewHolder {

        private TextView txtNumber,txtTitle;
        public SectionView(@NonNull View itemView) {
            super(itemView);
            txtNumber=itemView.findViewById(R.id.txtNumber);
            txtTitle=itemView.findViewById(R.id.txtTitleSection);
        }
    }


    public interface OnItemClickListener{
        void onLectureClick(LectureList lecture,int position);
        void onDownloadClick(LectureList lecture);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }
}
