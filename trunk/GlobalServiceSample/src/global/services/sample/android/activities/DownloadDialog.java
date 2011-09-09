package global.services.sample.android.activities;

import org.apache.http.auth.NTCredentials;

import global.services.sample.android.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DownloadDialog extends Dialog implements OnClickListener{

	private Button btnDownload;
	private Button btnCancel;
	private EditText txtLocation;
	private EditText txtFileName;
		
	public DownloadDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.download_dialog);
		
		txtLocation = (EditText) findViewById(R.id.editLocation);
		txtFileName = (EditText) findViewById(R.id.editFileName);
		btnDownload = (Button) findViewById(R.id.btnDownload);
		btnCancel = (Button) findViewById(R.id.btnCancel);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == btnCancel) 
		{
			dismiss();
		}
	}
	

}
