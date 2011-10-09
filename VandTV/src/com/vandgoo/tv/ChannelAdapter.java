package com.vandgoo.tv;

import java.io.File;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ChannelAdapter extends BaseAdapter {
	private ArrayList<TVChannel> items_;
	private Context mContext;  
	  
	 // Gets the context so it can be used later  
	 public ChannelAdapter(Context c, ArrayList<TVChannel> channelList) {  
	  mContext = c;  
	  items_ = channelList;
	 }  
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 ImageView imageView;
	        if (convertView == null) {  // if it's not recycled, initialize some attributes
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(85, 35));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	        } else {
	            imageView = (ImageView) convertView;
	        }

		TVChannel objCh = items_.get(position);
		if (objCh != null) {

			File imgFile = new File(mContext.getFilesDir(), String
					.valueOf(objCh.getName()));

			String pathName = imgFile.getAbsolutePath();
			Bitmap myBitmap = BitmapFactory.decodeFile(pathName);

			imageView.setImageBitmap(myBitmap);

		}
		return imageView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items_.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		TVChannel chn = items_.get(position);
		return chn;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		TVChannel chn = items_.get(position);
		return Long.parseLong(chn.getId());
	}
}
