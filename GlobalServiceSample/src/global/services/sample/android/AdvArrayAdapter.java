package global.services.sample.android;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.util.ByteArrayBuffer;

import global.services.lib.android.Advertisement;
import global.services.lib.android.FileInfoFactory;
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
				InputStream is = new FileInfoFactory(objAdv.getUserId())
						.Download(objAdv.getIconFileId());
				BufferedInputStream bis = new BufferedInputStream(is);

				/*
				 * Read bytes to the Buffer until there is nothing more to
				 * read(-1).
				 */
				FileOutputStream fos = null;
				try {
					fos = getContext().openFileOutput(imgFile.getName(), Context.MODE_PRIVATE);
					//fos = new FileOutputStream(imgFile);

					ByteArrayBuffer baf = new ByteArrayBuffer(1024);
					byte[] buffer = new byte[1024];

					int current = 0;
					while ((current = is.read(buffer)) != -1) {
						fos.write(buffer);
					}
					
					/*
					while ((current = bis.read()) != -1) {
						baf.append((byte) current);
						fos.write(baf.toByteArray());
						baf.clear();
					}
					*/
					fos.close();

					is.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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
