package global.services.sample.android;

import java.util.List;

import global.services.lib.android.Highscore;
import global.services.lib.android.HighscoreFactory;
import global.services.sample.android.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GetScoreActivity extends ListActivity {
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.get_score);
	        /*
	        HighscoreFactory scoreFactory = new HighscoreFactory(
					getResources().getString(R.string.userid), Long
							.parseLong(getResources().getString(
									R.string.appid)));
			List<Highscore> scoreList =  scoreFactory.GetScores();
			ListView scoreView = getListView();
			scoreView.setAdapter(new ArrayAdapter<Highscore>(this, R.layout.menu_item,
					scoreList){
					
				
			});
			*/
	 }
}
