package com.vandgoo.tv;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CatalogAdapter extends BaseAdapter {
	private ArrayList<Catalog> catalogs_;
	private Context mContext;

	// Gets the context so it can be used later
	public CatalogAdapter(Context c, ArrayList<Catalog> catalogList) {
		mContext = c;
		catalogs_ = catalogList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater vi = LayoutInflater.from(mContext);
			// LayoutInflater li = (LayoutInflater)
			// context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
			convertView = vi.inflate(R.layout.catalog_list_item, null);
		}

		Catalog objCatalog = catalogs_.get(position);
		if (objCatalog != null) {

			TextView catalogItem = (TextView) convertView
					.findViewById(R.id.catalog_item);
			catalogItem.setText(objCatalog.getName() + "("
					+ objCatalog.GetCount() + ")");

		}
		return convertView;
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
