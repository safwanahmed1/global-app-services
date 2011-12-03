package com.sendme.android.slideshow.runnable;

import android.widget.TextView;
import android.widget.Toast;
import com.sendme.android.slideshow.image.LoadedImage;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class TextUpdater
implements Runnable
{
	private TextView textView = null;
	private String text = null;



	public TextUpdater()
	{
		
	}

	public void run()
	{
		textView.setText(text);
	}

	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public TextView getTextView()
	{
		return textView;
	}

	public void setTextView(TextView textView)
	{
		this.textView = textView;
	}
}
