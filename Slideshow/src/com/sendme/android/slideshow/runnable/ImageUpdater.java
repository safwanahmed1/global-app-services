package com.sendme.android.slideshow.runnable;

import android.widget.ImageView;
import android.widget.Toast;
import com.sendme.android.slideshow.image.LoadedImage;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class ImageUpdater
implements Runnable
{
	private ImageView imageView = null;

	private LoadedImage image = null;

	private Toast currentText = null;

	public ImageUpdater()
	{
	}

	public void run()
	{
		imageView.setImageBitmap(image.getBitmap());

		if (currentText != null)
		{
			currentText.cancel();
		}

		currentText = Toast.makeText(imageView.getContext(), image.getText(), Toast.LENGTH_SHORT);

		currentText.show();
	}

	public LoadedImage getImage()
	{
		return image;
	}

	public void setImage(LoadedImage image)
	{
		this.image = image;
	}

	public ImageView getImageView()
	{
		return imageView;
	}

	public void setImageView(ImageView imageView)
	{
		this.imageView = imageView;
	}
}
