package global.services.sample.android;
import global.services.lib.android.FileInfo;
import global.services.lib.android.FileInfoFactory;

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


public class FilesActivity extends ListActivity {
	private List<FileInfo> fileList ;
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
		case R.id.refresh_adv:
			GetFileInfiToLocalFile();
			fileList = LoadFileInfoFromFileToListView();
			adapter.notifyDataSetChanged();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	/* Download score from server and save to XML find in internal memory */
	private void GetFileInfiToLocalFile() {
		FileInfoFactory fileFactory = new FileInfoFactory(getResources()
				.getString(R.string.userid));
		String advsXML = fileFactory.GetFilesXMLContent();
		FileOutputStream fos;

		try {
			fos = openFileOutput(FILEINFO_FILE, Context.MODE_PRIVATE);
			fos.write(advsXML.getBytes());
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				fileContent.append(new String(buffer));
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


}
