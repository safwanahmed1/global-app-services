package global.services.sample.android;

import java.util.List;

import global.services.lib.android.Highscore;
import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;


public class ScoreArrayAdapter extends ArrayAdapter<Highscore> {
	private final Activity context;
	private final List<Highscore> scoreList;
	
	public ScoreArrayAdapter(Activity context, List<Highscore> objects) {
	
		// TODO Auto-generated constructor stub
		
		this.context =  context;
	}

}
