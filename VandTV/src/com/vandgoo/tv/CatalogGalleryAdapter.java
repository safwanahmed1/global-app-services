package com.vandgoo.tv;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CatalogGalleryAdapter extends BaseAdapter {
	private ArrayList<Catalog> catalogs_;
	private Context mContext;

	// Gets the context so it can be used later
	public CatalogGalleryAdapter(Context c, ArrayList<Catalog> catalogList) {
		mContext = c;
		catalogs_ = catalogList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Catalog objCatalog = catalogs_.get(position);
		TextView catalogItem = new TextView(mContext);
		catalogItem.setText(objCatalog.getName() + " (" + objCatalog.GetCount()
				+ ")");

		Gallery.LayoutParams layoutPara = new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//layoutPara.
		catalogItem.setLayoutParams(layoutPara);
		catalogItem.setBackgroundResource(android.R.drawable.btn_default);
		catalogItem.setTextColor(R.color.green);
		return catalogItem;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return catalogs_.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		Catalog chn = catalogs_.get(position);
		return chn;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		// Catalog chn = catalogs_.get(position);
		return (long) position;
	}
}
