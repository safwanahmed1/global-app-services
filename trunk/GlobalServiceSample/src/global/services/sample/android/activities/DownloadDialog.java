package global.services.sample.android.activities;

import global.services.sample.android.R;
import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;

public class DownloadDialog extends Dialog {

	public DownloadDialog(Context context, String fileName) {
		super(context);
		// TODO Auto-generated constructor stub
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.download_dialog);
		this.setTitle("Download file");
		LayoutParams params = getWindow().getAttributes(); 
        params.width = LayoutParams.FILL_PARENT; 
         getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
         EditText txtFileName = (EditText) findViewById(R.id.editFileName);
         txtFileName.setText(fileName);
	}

}
