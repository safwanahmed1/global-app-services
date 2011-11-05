package com.sendme.android.slideshow.runnable;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.R;
import com.sendme.android.slideshow.controller.UIController;
import com.sendme.android.slideshow.image.ImageLoader;
import com.sendme.android.slideshow.image.ImageLoaderException;
import com.sendme.android.slideshow.image.ImageType;
import com.sendme.android.slideshow.image.LoadedImage;
import com.sendme.android.slideshow.model.AudioTrack;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class AudioPlaybackUIUpdater
implements Runnable
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private UIController uiController = null;

	private AudioTrack audioTrack = null;

	public AudioPlaybackUIUpdater()
	{
	}

	public void run()
	{
		ImageLoader imageLoader = uiController.getImageLoader();
		
		ImageView albumArtView = uiController.getAlbumArtView();

		TextView nowPlayingView = uiController.getCurrentlyPlayingLabel();

		if (audioTrack != null)
		{
			String nowPlaying = getNowPlayingString();

			if (nowPlaying != null)
			{
				nowPlayingView.setText(nowPlaying);
			}
			else
			{
				nowPlayingView.setText(null);
			}

			String albumArtURIString = audioTrack.getAlbumArtURI();

			LoadedImage image = null;
			
			if (albumArtURIString != null)
			{
				Uri uri = Uri.parse(albumArtURIString);

				try
				{
					image = imageLoader.loadImage(ImageType.ALBUM_ART, uri, albumArtView.getWidth(), albumArtView.getHeight());
				}
				catch (ImageLoaderException e)
				{
					log.error("Error loading album art for track: " + audioTrack.getId(), e);
				}
			}

			if (image != null)
			{
				albumArtView.setImageBitmap(image.getBitmap());
			}
			else
			{
				albumArtView.setImageBitmap(null);
			}
		}
		else
		{
			nowPlayingView.setText(null);
			
			albumArtView.setImageBitmap(null);
		}

		uiController.showControls();

		UIController.registerVanishEvent();
	}

	public String getNowPlayingString()
	{
		AndroidSlideshow ass = uiController.getAndroidSlideshow();

		String output = ass.getString(R.string.audioInformationString);

		Map<String, String> replacements = new HashMap<String, String>();

		replacements.put("artist", audioTrack.getArtist());
		replacements.put("album", audioTrack.getAlbum());
		replacements.put("trackNumber", audioTrack.getTrackNumber());
		replacements.put("trackTitle", audioTrack.getTrackTitle());

		for (Entry<String, String> entry : replacements.entrySet())
		{
			String key = entry.getKey();

			String value = entry.getValue();

			Pattern pattern = Pattern.compile("\\{" + key + "\\}");

			Matcher matcher = pattern.matcher(output);

			output = matcher.replaceAll(value);
		}

		return output;
	}

	public UIController getUIController()
	{
		return uiController;
	}

	public void setUIController(UIController uiController)
	{
		this.uiController = uiController;
	}

	public AudioTrack getAudioTrack()
	{
		return audioTrack;
	}

	public void setAudioTrack(AudioTrack audioTrack)
	{
		this.audioTrack = audioTrack;
	}
}
