package com.vandgoo.tv;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private Integer[] mImageIds = {
            R.drawable.icon,
            R.drawable.logo,
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.logo,
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.logo,
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.logo,
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.logo,
            R.drawable.icon,
            R.drawable.icon,
            R.drawable.logo,
            R.drawable.icon
            
    };

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mImageIds.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mImageIds[position]);
        return imageView;
    }

	
}