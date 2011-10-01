package com.vandgoo.tv;
import java.io.File;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.vandgoo.tv.TaskListener.OnTaskFinishedListener;

public class VandTVActivity extends Activity {
    /** Called when the activity is first created. */
	private String CHANNEL_LIST_FILE = "channels.xml";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        File channelFile = new File(this.getFilesDir(),CHANNEL_LIST_FILE);
        if (!channelFile.exists()) {
        	UpdateChannelTask channelTask = new UpdateChannelTask(this);
        	channelTask.setOnTaskFinishedListener(mOnTaskFinishedListener);
        	channelTask.execute(null);
        	
        	
        }

        GridView gridChannel = (GridView) findViewById(R.id.gridChannel);
        gridChannel.setAdapter(new ImageAdapter(this));
        gridChannel.setOnItemClickListener(new OnItemClickListener() {
        	@Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(VandTVActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
	
        });
    }
    private OnTaskFinishedListener mOnTaskFinishedListener = new OnTaskFinishedListener() {

		@Override
		public void onTaskFinished(boolean successful) {
			

		}
	};
}