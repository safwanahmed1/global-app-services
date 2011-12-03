package com.sendme.android.slideshow.runnable;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;
import com.sendme.android.slideshow.image.LoadedImage;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class SharingNotifier
implements Runnable
{
	private Context context = null;
	private String message = null;

	private Toast currentText = null;

	public SharingNotifier(Context context, String message)
	{
		this.message = message;
		this.context = context;
	}

	public void run()
	{

		currentText = Toast.makeText(context, message, Toast.LENGTH_SHORT);

		currentText.show();
	}

}
