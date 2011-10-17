package com.vandgoo.tv;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class ChannelMenuAdapter extends BaseAdapter {
	private Context mContext;

	// Gets the context so it can be used later
	public ChannelMenuAdapter(Context c) {
		mContext = c;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater vi = LayoutInflater.from(mContext);
			// LayoutInflater li = (LayoutInflater)
			// context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
			convertView = vi.inflate(R.layout.menu_list_item, null);
		}
		ImageView menuIcon = (ImageView) convertView
				.findViewById(R.id.icon_menu_item);
		TextView menuText = (TextView) convertView
				.findViewById(R.id.text_menu_item);

		switch (position) {
		case 0:

			menuIcon.setImageResource(R.drawable.play_channel_ic);
			menuText.setText("Play");
			break;
		case 1:

			menuIcon.setImageResource(R.drawable.add_favourite_ic);
			menuText.setText("Add to favourite");
			break;
		case 2:

			menuIcon.setImageResource(R.drawable.view_schedule_ic);
			menuText.setText("View schedule");
			break;
		case 3:

			menuIcon.setImageResource(R.drawable.update_channel_ic);
			menuText.setText("Update channel");
			break;
		case 4:

			menuIcon.setImageResource(R.drawable.bad_chnnel_ic);
			menuText.setText("Bad channel");
			break;
		}
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		// Catalog chn = catalogs_.get(position);
		return (long) position;
	}
}
