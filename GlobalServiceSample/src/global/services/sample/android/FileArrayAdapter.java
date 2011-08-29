package global.services.sample.android;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import global.services.lib.android.Advertisement;
import global.services.lib.android.FileDownloader;
import global.services.lib.android.FileInfo;
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

public class FileArrayAdapter extends ArrayAdapter<FileInfo> {
	private ArrayList<FileInfo> items_;

	public FileArrayAdapter(Context context, int textViewResourceId,
			ArrayList<FileInfo> items) {
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
			convertView = vi.inflate(R.layout.file_list_item, null);
		}
		FileInfo objFile = items_.get(position);
		if (objFile != null) {

			ImageView iconApp = (ImageView) convertView
					.findViewById(R.id.file_icon);
			if (objFile.getFileType().equals("excel")) {
				iconApp.setImageResource(R.drawable.ic_excel_file);
			} else if (objFile.getFileType().equals("word")) {
				iconApp.setImageResource(R.drawable.ic_word_file);
			}

			TextView txtName = (TextView) convertView
					.findViewById(R.id.file_name);
			TextView txtSize = (TextView) convertView
					.findViewById(R.id.file_size);
		}
		return convertView;

	}

}
