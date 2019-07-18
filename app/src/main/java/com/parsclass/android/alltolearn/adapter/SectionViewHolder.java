package com.parsclass.android.alltolearn.adapter;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.parsclass.android.alltolearn.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class SectionViewHolder extends GroupViewHolder {

    public static final String TAG="SectionViewHolder";
    private TextView txtTitle;
    private ImageView arrow;

    public SectionViewHolder(View itemView) {
        super(itemView);
        txtTitle =itemView.findViewById(R.id.txtTitleSection);
        arrow = itemView.findViewById(R.id.img_arrow);
    }

    public void setSectionTitle(ExpandableGroup genre) {
            txtTitle.setText(genre.getTitle());
    }

    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }
}
