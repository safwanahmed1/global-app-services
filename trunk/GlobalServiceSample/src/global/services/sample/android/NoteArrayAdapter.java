package global.services.sample.android;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import global.services.lib.android.Notification;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NoteArrayAdapter extends ArrayAdapter<Notification> {
	private ArrayList<Notification> items_;

	public NoteArrayAdapter(Context context, int textViewResourceId,
			ArrayList<Notification> items) {
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
			//LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
			convertView = vi.inflate(R.layout.note_list_item, null);
		}
		Notification objNote = items_.get(position);
		if (objNote != null) {
			TextView txtTitle = (TextView) convertView
					.findViewById(R.id.note_title);
			TextView txtContent = (TextView) convertView
					.findViewById(R.id.note_content);
			
			if (txtTitle != null)
				txtTitle.setText(objNote.getTitle());
			if (txtContent != null)
				txtContent.setText(objNote.getContent());
			
		}
		return convertView;

	}

}
