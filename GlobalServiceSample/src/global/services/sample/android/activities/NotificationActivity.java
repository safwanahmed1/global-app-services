package global.services.sample.android.activities;

import global.services.lib.android.factories.NotificationFactory;
import global.services.lib.android.objects.Notification;
import global.services.sample.android.R;
import global.services.sample.android.R.id;
import global.services.sample.android.R.layout;
import global.services.sample.android.R.menu;
import global.services.sample.android.R.string;
import global.services.sample.android.adapters.NoteArrayAdapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class NotificationActivity extends ListActivity {
	private static final String NOTIFICATION_FILE = "notification.xml";
	private NoteArrayAdapter adapter;
	List<Notification> noteList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_list);

		noteList = LoadNoteFromFileToListView();
		if (noteList != null) {
			adapter = new NoteArrayAdapter(getApplicationContext(),
					R.layout.note_list, (ArrayList<Notification>) noteList);

			setListAdapter(adapter);

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.get_note_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.refresh_note:
			GetNoteToLocalFile();
			noteList = LoadNoteFromFileToListView();
			if (adapter == null) {
				adapter = new NoteArrayAdapter(getApplicationContext(),
						R.layout.note_list, (ArrayList<Notification>) noteList);

				setListAdapter(adapter);
			}
			adapter.notifyDataSetChanged();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* Download score from server and save to XML find in internal memory */
	private void GetNoteToLocalFile() {
		NotificationFactory noteFactory = new NotificationFactory(
				getResources().getString(R.string.userid), Long
						.parseLong(getResources().getString(R.string.appid)));
		String notesXML = noteFactory.GetNotesXMLContent();
		FileOutputStream fos;

		try {
			fos = openFileOutput(NOTIFICATION_FILE, Context.MODE_PRIVATE);
			fos.write(notesXML.getBytes());
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* Refresh score from xml file to listview */
	private List<Notification> LoadNoteFromFileToListView() {
		List<Notification> scoreList = null;
		FileInputStream fis;
		StringBuffer fileContent = new StringBuffer("");
		try {

			byte[] buffer = new byte[1024];
			fis = openFileInput(NOTIFICATION_FILE);

			int length;
			while ((length = fis.read(buffer)) != -1) {
				fileContent.append(new String(buffer), 0, length);
			}
			scoreList = GetNoteListFromXML(fileContent.toString());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scoreList;
	}

	public List<Notification> GetNoteListFromXML(String notesXMLContent) {
		List<Notification> noteList = new ArrayList<Notification>();
		String strElemName;
		String id;
		String userId;
		String appId;
		String title;
		String content;

		notesXMLContent = notesXMLContent.replace("\n", "");
		XmlPullParser notes;
		try {

			notes = XmlPullParserFactory.newInstance().newPullParser();
			notes.setInput(new StringReader(notesXMLContent));
		} catch (XmlPullParserException e) {
			notes = null;
		}
		if (notes != null) {
			int eventType = -1;
			// boolean bFoundScores = false;

			// Find Score records from XML
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {

					// Get the name of the tag (eg scores or score)
					strElemName = notes.getName();

					if (strElemName.equals("note")) {
						// bFoundScores = true;
						Notification noteObj = new Notification();
						id = notes.getAttributeValue(null, "id");
						if (id != null)
							noteObj.setId(Long.parseLong(id));
						userId = notes.getAttributeValue(null, "useid");
						if (userId != null)
							noteObj.setUserId(userId);
						appId = notes.getAttributeValue(null, "appid");
						if (appId != null)
							noteObj.setAppId(Long.parseLong(appId));

						title = notes.getAttributeValue(null, "title");
						if (title != null)
							noteObj.setTitle(title);
						content = notes.getAttributeValue(null, "content");
						if (content != null)
							noteObj.setContent(content);

						noteList.add(noteObj);

					}
				}
				try {
					eventType = notes.next();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return noteList;
	}
}
