package com.project.luismendez.popularmovies;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.MainThread;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PosterAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int mLayoutId;
    private ArrayList<String> mPosterUrls;

    public PosterAdapter(Context context, int layoutId, ArrayList<String> posterUrls) {
        super(context, layoutId, posterUrls);
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mPosterUrls = posterUrls;
    }

    public int getCount() {
        return mPosterUrls.size();
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(urls[position]);
*/
        ImageView imageView2;
        if (convertView == null) {
            imageView2 = new ImageView(mContext);
            //imageView2.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //imageView2.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //imageView2.setPadding(8, 8, 8, 8);
        } else {
            imageView2 = (ImageView) convertView;
        }

        int columnWidth = mContext.getResources().getDisplayMetrics().widthPixels;

        int posterWidth;
        int posterHeight;
        if (Configuration.ORIENTATION_PORTRAIT == mContext.getResources().getConfiguration().orientation) {
            posterWidth = columnWidth / 2; //2 columns portrait, 3 columns landscape
            posterHeight = (3 * columnWidth / 4); //1 * 1.5 poster aspect ratio
        } else {
            posterWidth = columnWidth / 3;
            posterHeight = columnWidth / 2;
        }

        Picasso.with(mContext)
                .load(mPosterUrls.get(position))
                .centerCrop().resize(posterWidth, posterHeight)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView2);
        return imageView2;
    }
}
