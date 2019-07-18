package com.parsclass.android.alltolearn.adapter;

import android.content.Context;
import androidx.databinding.BindingAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;

import com.parsclass.android.alltolearn.R;
import com.parsclass.android.alltolearn.Utils.GlideApp;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by win10 on 09/01/2019.
 */

public class GlideBindingAdapters {
    @BindingAdapter(value ={"imageUrl","activity"},requireAll = false)
    public static void setImageResource(ImageView view, int imageUrl, AppCompatActivity activity){
        Context context=view.getContext();

        GlideApp.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.img_itm_1_start_viewpager)
               // .apply(new RequestOptions().override(200, 200))
                .error(R.drawable.img_category_art)
//                .dontAnimate()
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        activity.supportStartPostponedEnterTransition();
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        activity.supportStartPostponedEnterTransition();
//                        return false;
//                    }
//                })
                .into(view);
    }

    @BindingAdapter(value ={"imageUrl","activity"},requireAll = false)
    public static void setImageResource(ImageView view, String imageUrl,AppCompatActivity activity){
        Context context=view.getContext();
        GlideApp.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.img_itm_1_start_viewpager)
                .error(R.drawable.img_category_art)
//                .dontAnimate()
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        activity.supportStartPostponedEnterTransition();
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        activity.supportStartPostponedEnterTransition();
//                        return false;
//                    }
//                })
                .into(view);
    }

    @BindingAdapter("imagePath")
    public static void setImage(ImageView view, String imageUrl){
        Context context=view.getContext();
        Picasso.with(context)
                .load(imageUrl)
                .centerCrop()
                .error(R.drawable.img_category_art)
                .placeholder(R.drawable.img_category_art)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(view);
    }

    @BindingAdapter("imagePath")
    public static void setImage(ImageView view, int imageUrl){
        Context context=view.getContext();
        Picasso.with(context)
                .load(imageUrl)
                .error(R.drawable.img_category_art)
                .placeholder(R.drawable.img_category_art)
                .into(view);
    }
}
