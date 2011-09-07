package global.services.sample.android.tasks;

import global.services.lib.android.factories.AdvertisementFactory;
import global.services.lib.android.objects.Highscore;
import global.services.sample.android.activities.GetScoreActivity;
import global.services.sample.android.adapters.ScoreArrayAdapter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DownloadNoteToLocal extends AsyncTask<String, Integer, Void> {
	private String ADVERTISEMENT_FILE = "advertisement.xml";
	private Context context;
	private ProgressDialog dialog;

	public DownloadNoteToLocal(Context ctx) {
		context = ctx;
		dialog = new ProgressDialog(context);

	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog.setMessage("Downloading data...");
		dialog.show();
	}

	@Override
	protected Void doInBackground(String... params) {
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

		return null;
	}

}
