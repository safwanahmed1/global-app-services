package com.vandgoo.tv;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    private Integer[] mImageIds = {
            R.drawable.brt,
            R.drawable.vl1,
            R.drawable.vl2,
            R.drawable.vtc1,
            R.drawable.vtc2,
            R.drawable.vtc3,
            R.drawable.vtc14,
            R.drawable.vtc16,
            R.drawable.vtv1,
            R.drawable.vtv2,
            R.drawable.vtv3,
            R.drawable.vtv4,
            R.drawable.vtv6,
            R.drawable.sctv1,
            R.drawable.yantv,
            R.drawable.sctv3,
            R.drawable.yeah1,
            R.drawable.sctv6,
            R.drawable.sctv7,
            R.drawable.sctv8,
            R.drawable.sctv9,
            R.drawable.sctv10,
            R.drawable.sctv11,
            R.drawable.sctv12,
            R.drawable.sctv13,
            R.drawable.sctv14,
            R.drawable.sctv15,
            R.drawable.sctv16,
            R.drawable.sctv17
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
            imageView.setLayoutParams(new GridView.LayoutParams(85, 35));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mImageIds[position]);
        return imageView;
    }

	
}