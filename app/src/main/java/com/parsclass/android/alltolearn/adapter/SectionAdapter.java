package com.parsclass.android.alltolearn.adapter;

import android.content.Context;
import androidx.core.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.model.Lecture;
import com.parsclass.android.alltolearn.model.SectionExpandable;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends ExpandableRecyclerViewAdapter<SectionViewHolder, LectureViewHolder> {

    public static final String TAG="SectionAdapter";
    private OnItemClickListener listener;
    private int typeList;
    private int child_index =-1;
    private int group_index =-1;
    private Context context;
    public SectionAdapter(List<? extends ExpandableGroup> groups,int typeList,Context context) {
        super(groups);
        this.typeList=typeList;
        this.context=context;

    }

    @Override
    public List<? extends ExpandableGroup> getGroups() {

        return super.getGroups();
    }

    public void gj(int flatPosition,int childIndex){
        ExpandableGroup group=getGroups().get(flatPosition);
        SectionExpandable s=((SectionExpandable) group);
        Lecture l=new Lecture();
        notifyItemChanged(3,l);

    }

    public void updatePlayingLecture(int flatPosition,int childIndex){
        child_index=childIndex;
        group_index=flatPosition;
        notifyDataSetChanged();
//        ExpandableGroup group=getGroups().get(flatPosition);
//        SectionExpandable s=((SectionExpandable) group);
//        final Lecture oldLecture = ((SectionExpandable) group).getItems().get(childIndex);
//        ArrayList<Lecture> lectures=s.getLectures();
//        int index=lectures.indexOf(oldLecture);
//        for (int i=0;i<lectures.size();i++){
//            lectures.get(i).setSelected(false);
//        }
//        oldLecture.setSelected(true);
//        lectures.set(index,oldLecture);
//         s.setLectures(lectures);
//        notifyItemChanged(flatPosition,s);

    }

    @Override
    public SectionViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.section_expanded_recycler_item, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public LectureViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lecture_recycler_item, parent, false);
        return new LectureViewHolder(view);
    }


    @Override
    public void onBindChildViewHolder(LectureViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {

        final Lecture item = ((SectionExpandable) group).getItems().get(childIndex);

        final int sectionId=((SectionExpandable) group).getId();
        ArrayList<Lecture> lectureList=((SectionExpandable) group).getLectures();
        holder.setAttribute(item,typeList,context);
        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG,"onLectureClick");
                if(listener!=null) {
                    listener.onLectureClick(item);
                    Log.e(TAG,"onLectureClick if");
                }
            }
        });


        holder.getTxtPreview().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listener!=null) {
                    listener.onPreviewClick(item);
                }

            }
        });


        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(listener!=null) {
                    listener.onLectureClick(item);

                }
                child_index =childIndex;
                group_index=flatPosition;
                notifyDataSetChanged();
            }
        });

        if(child_index ==childIndex && group_index==flatPosition){
            holder.txtTitle.setTextColor(
                    ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null));
            holder.txtNumber.setTextColor(
                    ResourcesCompat.getColor(context.getResources(), R.color.colorPrimary, null));
        }
        else {
            holder.txtTitle.setTextColor(
                    ResourcesCompat.getColor(context.getResources(), R.color.colorSormei, null));
            holder.txtNumber.setTextColor(
                    ResourcesCompat.getColor(context.getResources(), R.color.colorSormei, null));
        }

        holder.getImgDownload().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.onDownloadClick(item,sectionId,childIndex,lectureList);

                }
            }
        });



    }


    @Override
    public void onBindGroupViewHolder(SectionViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {

        holder.setSectionTitle(group);
    }
    public interface OnItemClickListener{
        void onLectureClick(Lecture lecture);
        void onPreviewClick(Lecture lecture);
        void onDownloadClick(Lecture lecture, int sectionId, int lectureId, ArrayList<Lecture> lectureList);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }



}
