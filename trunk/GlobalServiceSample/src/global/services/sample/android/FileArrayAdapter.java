package global.services.sample.android;

import java.util.ArrayList;
import global.services.lib.android.FileInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
			if (objFile.getFileType().contains("excel")) {
				iconApp.setImageResource(R.drawable.ic_excel_file);
			} else if (objFile.getFileType().contains("word")) {
				iconApp.setImageResource(R.drawable.ic_word_file);
			}else if (objFile.getFileType().contains("text")) {
				iconApp.setImageResource(R.drawable.ic_text_file);
			}else if (objFile.getFileType().contains("pdf")) {
				iconApp.setImageResource(R.drawable.ic_pdf_file);
			}else if (objFile.getFileType().contains("flash")) {
				iconApp.setImageResource(R.drawable.ic_flash_file);
			}else if (objFile.getFileType().contains("zip")) {
				iconApp.setImageResource(R.drawable.ic_zip_file);
			}else if (objFile.getFileType().contains("image")) {
				iconApp.setImageResource(R.drawable.ic_image_file);
			}else if (objFile.getFileType().contains("media")) {
				iconApp.setImageResource(R.drawable.ic_music_file);
			}
			


			TextView txtName = (TextView) convertView
					.findViewById(R.id.file_name);
			txtName.setText(objFile.getFileName());
			TextView txtSize = (TextView) convertView
					.findViewById(R.id.file_size);
			txtSize.setText(objFile.getFileSize());
		}
		return convertView;

	}

}
