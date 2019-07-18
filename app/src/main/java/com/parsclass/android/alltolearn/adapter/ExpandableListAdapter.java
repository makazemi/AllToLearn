package com.parsclass.android.alltolearn.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.model.Lecture;
import com.parsclass.android.alltolearn.model.Section;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    public static final String TAG="ExpandableListAdapter";
    private Context _context;
    private List<Section> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Section, List<Lecture>> _listDataChild;

    public ExpandableListAdapter(Context _context, List<Section> _listDataHeader, HashMap<Section, List<Lecture>> _listDataChild) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
    }

    public ExpandableListAdapter(Context _context) {
        this._context = _context;
    }

    public void setValues(List<Section> _listDataHeader, HashMap<Section, List<Lecture>> _listDataChild){
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        //final String childText = (String) getChild(groupPosition, childPosition);
        final Lecture child = (Lecture) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView=LayoutInflater.from(_context).inflate(R.layout.lecture_recycler_item, parent, false);
//            LayoutInflater infalInflater = (LayoutInflater) this._context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = infalInflater.inflate(R.layout.lecture_recycler_item, null);
        }


        TextView txtNumber=convertView.findViewById(R.id.txtNumber);
        TextView txtTitleLecture=convertView.findViewById(R.id.txtTitleLecture);
        TextView txtTypeMedia=convertView.findViewById(R.id.txtTypeMedia);
        TextView txtDuration=convertView.findViewById(R.id.txtDuration);
        TextView txtPreview=convertView.findViewById(R.id.txtPreview);

        txtNumber.setText(child.getNumber());
        txtTitleLecture.setText(child.getTitle_lecture());
        txtTypeMedia.setText(child.getTypeMedia());
        txtDuration.setText(child.getLduration());
        if(child.isHasPreview())
            txtPreview.setVisibility(View.VISIBLE);
        else
            txtPreview.setVisibility(View.GONE);
        Log.e(TAG,"in bind child");

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
       // String headerTitle = (String) getGroup(groupPosition);
        Section group = (Section) getGroup(groupPosition);
        if (convertView == null) {
            convertView=LayoutInflater.from(_context).inflate(R.layout.section_recycler_item, parent, false);
//            LayoutInflater infalInflater = (LayoutInflater) this._context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = infalInflater.inflate(R.layout.section_recycler_item, null);
        }

        TextView txtNumber=convertView.findViewById(R.id.txtNumber);
        TextView txtTitle=convertView.findViewById(R.id.txtTitleSection);

        txtNumber.setText(group.getNumber_section());
        txtTitle.setText(group.getTitle_section());

        Log.e(TAG,"in bind group");

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}