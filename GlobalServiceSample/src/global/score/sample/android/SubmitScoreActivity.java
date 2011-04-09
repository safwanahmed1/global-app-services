package global.score.sample.android;

import global.sample.android.R;
import global.score.lib.android.GlobalScore;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SubmitScoreActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_score);

		Button button = (Button) findViewById(R.id.btnSubmit);
		// Register the onClick listener with the implementation above
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				GlobalScore gScore = new GlobalScore("Global");
				
				TextView txtPlayer = (TextView) findViewById(R.id.editPlayer);
				gScore.setLevel(txtPlayer.getText().toString());
				
				TextView txtLevel = (TextView) findViewById(R.id.editLevel);
				gScore.setPlayer(txtLevel.getText().toString());
				
				TextView txtScore = (TextView) findViewById(R.id.editScore);
				gScore.setScore(txtScore.getText().toString());
				
				TextView txtDuring = (TextView) findViewById(R.id.editDuring);
				gScore.setDuring(txtDuring.getText().toString());
				
				TextView txtDate = (TextView) findViewById(R.id.editDate);
				gScore.setDate(txtDate.getText().toString());
				
				TextView txtLocation = (TextView) findViewById(R.id.editLocation);
				gScore.setLocation(txtLocation.getText().toString());
				
				TextView txtComment = (TextView) findViewById(R.id.editComment);
				gScore.setComment(txtComment.getText().toString());
				
				gScore.SubmitScore();

			}

		}

		);

	}

}
