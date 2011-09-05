package global.services.sample.android;

import java.util.ArrayList;
import java.util.Date;

import global.services.lib.android.Highscore;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ScoreArrayAdapter extends ArrayAdapter<Highscore> {
	private ArrayList<Highscore> items_;

	public ScoreArrayAdapter(Context context, int textViewResourceId,
			ArrayList<Highscore> items) {
		super(context, textViewResourceId, items);
		items_ = items;
		// TODO Auto-generated constructor stub
	}


	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
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
			convertView = vi.inflate(R.layout.score_list_item, null);
		}
		Highscore objScore = items_.get(position);
		if (objScore != null) {
			TextView txtLevel = (TextView) convertView
					.findViewById(R.id.score_level);
			TextView txtPlayer = (TextView) convertView
					.findViewById(R.id.score_player);
			TextView txtLocation = (TextView) convertView
					.findViewById(R.id.score_location);
			TextView txtComment = (TextView) convertView
					.findViewById(R.id.score_comment);
			TextView txtScore = (TextView) convertView
					.findViewById(R.id.score_score);
			TextView txtDuring = (TextView) convertView
					.findViewById(R.id.score_during);
			TextView txtDate = (TextView) convertView
					.findViewById(R.id.score_date);
			if (txtLevel != null)
				txtLevel.setText(objScore.getSubBoard());
			if (txtPlayer != null)
				txtPlayer.setText(objScore.getPlayer());
			if (txtLocation != null)
				txtLocation.setText(objScore.getLocation());
			if (txtComment != null)
				txtComment.setText(objScore.getComment());
			if (txtScore != null)
				txtScore.setText(String.valueOf(objScore.getHighScore()));
			if (txtDuring != null)
				txtDuring.setText(TimeParser(objScore.getDuring()));
			if (txtDate != null)
				txtDate.setText(DateParser(objScore.getDate()));

		}
		return convertView;

	}

	private String TimeParser(Long time) {
		// TODO Auto-generated method stub

		StringBuilder sb = new StringBuilder();
		int hour = (int) (time / 3600000);
		if (hour > 1) {
			sb.append(hour + ":");
			time = time % 3600000;
		}
		int minute = (int) (time / 60000);
		if (minute > 1) {
			if (minute < 10)
				sb.append("0");
			sb.append(minute + ":");
			time = time % 60000;
		} else {
			sb.append("00:");
		}
		int second = (int) (time / 1000);
		if (second > 1) {
			if (second < 10)
				sb.append("0");
			sb.append(second);
		} else {
			sb.append("00");
		}
		return sb.toString();
	}

	private String DateParser(Long lDate) {
		// TODO Auto-generated method stub

		Date dDate = new Date(lDate);
		java.text.DateFormat dateFormat = DateFormat
				.getDateFormat(getContext());
		String dateString = dateFormat.format(dDate);
		return dateString;
	}
}
