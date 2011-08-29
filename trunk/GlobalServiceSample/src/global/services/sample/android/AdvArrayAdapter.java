package global.services.sample.android;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import global.services.lib.android.Advertisement;
import global.services.lib.android.FileDownloader;
import global.services.lib.android.Highscore;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AdvArrayAdapter extends ArrayAdapter<Advertisement> {
	private ArrayList<Advertisement> items_;

	public AdvArrayAdapter(Context context, int textViewResourceId,
			ArrayList<Advertisement> items) {
		super(context, textViewResourceId, items);
		items_ = items;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setNotifyOnChange(boolean notifyOnChange) {
		// TODO Auto-generated method stub
		super.setNotifyOnChange(notifyOnChange);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			LayoutInflater vi = LayoutInflater.from(getContext());
			// LayoutInflater li = (LayoutInflater)
			// context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
			convertView = vi.inflate(R.layout.adv_list_item, null);
		}
		Advertisement objAdv = items_.get(position);
		if (objAdv != null) {

			File imgFile = new File(String.valueOf(objAdv.getIconFileId()));
			if (!imgFile.exists()) {
				imgFile = new FileDownloader(objAdv.getUserId(),
						objAdv.getIconFileId()).Download(String.valueOf(objAdv
						.getIconFileId()));
			}

			Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(objAdv
					.getIconFileId()));

			ImageView iconApp = (ImageView) convertView
					.findViewById(R.id.adv_app_icon);
			iconApp.setImageBitmap(myBitmap);

			TextView txtName = (TextView) convertView
					.findViewById(R.id.adv_app_name);
			TextView txtTitle = (TextView) convertView
					.findViewById(R.id.adv_title);
			TextView txtContent = (TextView) convertView
					.findViewById(R.id.adv_content);

			if (txtName != null)
				txtName.setText(objAdv.getAppName());
			if (txtTitle != null)
				txtTitle.setText(objAdv.getTittle());
			if (txtContent != null)
				txtContent.setText(objAdv.getContent());

		}
		return convertView;

	}

}
