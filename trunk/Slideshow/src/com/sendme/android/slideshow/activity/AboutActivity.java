package com.sendme.android.slideshow.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import com.sendme.android.slideshow.R;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class AboutActivity
extends Activity
{
	private TextView textView = null;

	public AboutActivity()
	{

	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about);

		textView = (TextView) findViewById(R.id.aboutText);

		textView.setText(Html.fromHtml(getString(R.string.aboutText)));
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	protected void onStop()
	{
		super.onStop();
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
