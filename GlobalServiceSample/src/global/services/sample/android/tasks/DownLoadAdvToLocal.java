package global.services.sample.android.tasks;

import global.services.lib.android.factories.AdvertisementFactory;
import global.services.sample.android.R;
import global.services.sample.android.activities.AdvertisementActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;

public class DownLoadAdvToLocal extends AsyncTask<String, Integer, Void> {
	private String ADVERTISEMENT_FILE = "advertisement.xml";
	private Context context;

	public void SetContext(Context ctx) {
		context = ctx;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void doInBackground(String... params) {
		// TODO Auto-generated method stub
		AdvertisementFactory advFactory = new AdvertisementFactory(params[0]);
		String advsXML = advFactory.GetAdvsXMLContent();
		FileOutputStream fos;
		try {
			if (context != null) {
				fos = context.openFileOutput(ADVERTISEMENT_FILE,
						Context.MODE_PRIVATE);
				fos.write(advsXML.getBytes());
				fos.close();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
