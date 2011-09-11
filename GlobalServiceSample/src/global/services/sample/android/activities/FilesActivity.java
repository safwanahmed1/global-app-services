package global.services.sample.android.activities;

import global.services.lib.android.objects.FileInfo;
import global.services.sample.android.R;
import global.services.sample.android.adapters.FileArrayAdapter;
import global.services.sample.android.tasks.DownloadFileInfoToLocal;
import global.services.sample.android.tasks.TaskListener.OnTaskFinishedListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class FilesActivity extends ListActivity {
	private List<FileInfo> fileList;
	private FileArrayAdapter adapter;
	private String FILEINFO_FILE = "fileinfo.xml";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adv_list);

		fileList = LoadFileInfoFromFileToListView();
		if (fileList != null) {
			adapter = new FileArrayAdapter(getApplicationContext(),
					R.layout.file_list, (ArrayList<FileInfo>) fileList);

			setListAdapter(adapter);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.get_file_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.refresh_file:
			DownloadFileInfoToLocal downFileInfo = new DownloadFileInfoToLocal(
					this);
			downFileInfo.setOnTaskFinishedListener(mOnTaskFinishedListener);
			downFileInfo.execute(getResources().getString(R.string.userid));

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		FileInfo fileItem = (FileInfo) this.getListAdapter().getItem(position);
		final DownloadDialog dial = new DownloadDialog(this, fileItem.getFileName());

		EditText txtLocation = (EditText) dial.findViewById(R.id.editLocation);
		EditText txtFileName = (EditText) dial.findViewById(R.id.editFileName);
		Button btnDownload = (Button) dial.findViewById(R.id.btnDownload);
		Button btnCancel = (Button) dial.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dial.dismiss();
			}
		});
		btnDownload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dial.dismiss();
				DownloadFileInfoToLocal downFileInfo = new DownloadFileInfoToLocal();
				downFileInfo.setOnTaskFinishedListener(mOnTaskFinishedListener);
				downFileInfo.execute(getResources().getString(R.string.userid));
				
			}
		});
		dial.show();

	}

	private List<FileInfo> LoadFileInfoFromFileToListView() {
		// TODO Auto-generated method stub
		List<FileInfo> fileList = null;
		FileInputStream fis;
		StringBuffer fileContent = new StringBuffer("");
		try {

			byte[] buffer = new byte[1024];
			fis = openFileInput(FILEINFO_FILE);

			int length;
			while ((length = fis.read(buffer)) != -1) {
				fileContent.append(new String(buffer), 0, length);
			}
			fileList = GetFileListFromXML(fileContent.toString());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileList;
	}

	private List<FileInfo> GetFileListFromXML(String filesXMLContent) {
		// TODO Auto-generated method stub
		List<FileInfo> fileList = new ArrayList<FileInfo>();
		String strElemName;
		String id;
		String userId;
		String name;
		String size;
		String type;

		filesXMLContent = filesXMLContent.replace("\n", "");
		XmlPullParser files;
		try {

			files = XmlPullParserFactory.newInstance().newPullParser();
			files.setInput(new StringReader(filesXMLContent));
		} catch (XmlPullParserException e) {
			files = null;
		}
		if (files != null) {
			int eventType = -1;
			// boolean bFoundScores = false;

			// Find Score records from XML
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {

					// Get the name of the tag (eg scores or score)
					strElemName = files.getName();

					if (strElemName.equals("file")) {
						// bFoundScores = true;
						FileInfo fileObj = new FileInfo();
						id = files.getAttributeValue(null, "id");
						if (id != null)
							fileObj.setId(Long.parseLong(id));
						userId = files.getAttributeValue(null, "useid");
						if (userId != null)
							fileObj.setUserId(userId);
						name = files.getAttributeValue(null, "name");
						if (name != null)
							fileObj.setFileName(name);

						size = files.getAttributeValue(null, "size");
						if (size != null)
							fileObj.setFileSize(size);
						type = files.getAttributeValue(null, "type");
						if (size != null)
							fileObj.setFileType(type);

						fileList.add(fileObj);

					}
				}
				try {
					eventType = files.next();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return fileList;
	}

	private OnTaskFinishedListener mOnTaskFinishedListener = new OnTaskFinishedListener() {

		@Override
		public void onTaskFinished(boolean successful) {
			fileList = LoadFileInfoFromFileToListView();
			if (fileList != null) {
				adapter = new FileArrayAdapter(getApplicationContext(),
						R.layout.file_list, (ArrayList<FileInfo>) fileList);

				setListAdapter(adapter);

			}

		}
	};
}
