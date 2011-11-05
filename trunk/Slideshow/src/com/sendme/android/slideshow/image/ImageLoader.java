package com.sendme.android.slideshow.image;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.google.inject.Inject;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.manager.SettingsManager;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class ImageLoader
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private final static ReentrantLock imageCacheLock = new ReentrantLock(true);

	private static Map<Uri, SoftReference<LoadedImage>> imageCache = new HashMap<Uri, SoftReference<LoadedImage>>();

	@Inject
	private AndroidSlideshow ass = null;

	@Inject
	private ContentResolver contentResolver = null;

	public ImageLoader()
	{
	}

	public LoadedImage loadImage(ImageType type, Uri uri, int targetWidth, int targetHeight)
	throws ImageLoaderException
	{
		return loadImage(type, uri, null, targetWidth, targetHeight);
	}

	public LoadedImage loadImage(ImageType type, Uri uri, String text, int targetWidth, int targetHeight)
	throws ImageLoaderException
	{
		LoadedImage output = getImageFromCache(uri);

		if (output == null)
		{
			output = new LoadedImage();

			output.setURI(uri);

			output.setText(text);

			boolean cache = false;

			Uri finalURI = null;

			try
			{
				switch (type)
				{
					case AD:
					{
						finalURI = cacheRemoteImage(uri);

						break;
					}

					case ALBUM_ART:
					{
						finalURI = uri;

						break;
					}

					case LOCAL_PHOTO:
					{
						cache = true;

						finalURI = uri;

						break;
					}

					case REMOTE_PHOTO:
					{
						cache = true;

						finalURI = cacheRemoteImage(uri);

						break;
					}
				}

				Bitmap bm = decodeBitmap(finalURI, targetWidth, targetHeight);

				output.setBitmap(bm);
			}
			catch (FileNotFoundException e)
			{
				log.error("Could not load image for URI: " + finalURI, e);
			}
			catch (IOException e)
			{
				log.error("Could not load image for URI: " + finalURI, e);
			}

			if (cache)
			{
				addImageToCache(output);
			}
		}

		return output;
	}

	private Uri cacheRemoteImage(Uri uri)
	throws IOException
	{
		Uri output = null;

		File cacheFile = File.createTempFile("IMAGE_FILE_" + System.currentTimeMillis(), "tmpfile");

		// TODO:  Is this really going to work?  Should we delete some other way?
		cacheFile.deleteOnExit();

		FileOutputStream os = new FileOutputStream(cacheFile);

		HttpClient httpClient = new DefaultHttpClient();

		HttpGet get = new HttpGet(uri.toString());

		HttpResponse httpResponse = httpClient.execute(get);

		InputStream is = httpResponse.getEntity().getContent();

		byte[] tmp = new byte[10240];

		while (true)
		{
			int test = is.read(tmp);

			if (test == -1)
			{
				break;
			}

			os.write(tmp, 0, test);
		}

		os.close();

		is.close();

		output = Uri.parse(cacheFile.toURI().toString());

		return output;
	}

	private Bitmap decodeBitmap(Uri uri, int maxWidth, int maxHeight)
	throws FileNotFoundException, IOException
	{
		Bitmap output = null;

		log.info("About to load image " + uri + " for dimensions: " + maxWidth + "x" + maxHeight);

		//Decode the image size
		BitmapFactory.Options dimensionOptions = new BitmapFactory.Options();

		dimensionOptions.inJustDecodeBounds = true;

		InputStream is = contentResolver.openInputStream(uri);

		BitmapFactory.decodeStream(is, null, dimensionOptions);

		is.close();

		log.info("Image dimensions: " + dimensionOptions.outWidth + "x" + dimensionOptions.outHeight);

		int scale = 1;

		double maxSize = Math.max(maxWidth, maxHeight);

		if (dimensionOptions.outWidth > maxWidth || dimensionOptions.outHeight > maxHeight)
		{
			double imageMaxSize = Math.max(dimensionOptions.outHeight, dimensionOptions.outWidth);

			scale = (int) Math.round(Math.pow(2D, (double) (Math.round(Math.log(maxSize / imageMaxSize) / Math.log(0.5D)))));

			log.info("Image Scale: " + scale);
		}

		// Now decode it scaled, so we can reserve memory
		BitmapFactory.Options decodeOptions = new BitmapFactory.Options();

		decodeOptions.inSampleSize = scale;

		is = contentResolver.openInputStream(uri);

		output = BitmapFactory.decodeStream(is, null, decodeOptions);

		is.close();

		log.info("Final image dimensions: " + decodeOptions.outWidth + "x" + decodeOptions.outHeight);

		return output;
	}

	public static void addImageToCache(LoadedImage image)
	{
		imageCacheLock.lock();

		try
		{
			image.setAddedToCache(System.currentTimeMillis());

			imageCache.put(image.getURI(), new SoftReference<LoadedImage>(image));
		}
		finally
		{
			imageCacheLock.unlock();
		}
	}

	public static LoadedImage getImageFromCache(Uri uri)
	{
		LoadedImage output = null;

		imageCacheLock.lock();

		try
		{
			SoftReference<LoadedImage> reference = imageCache.get(uri);

			if (reference != null)
			{
				output = reference.get();
			}
		}
		finally
		{
			imageCacheLock.unlock();
		}

		return output;
	}

	public static void cleanCache()
	{
		imageCacheLock.lock();

		try
		{
			Long now = System.currentTimeMillis();

			Long lifetime = SettingsManager.getImageCacheLifetime();

			Map<Uri, SoftReference<LoadedImage>> newCache = new HashMap<Uri, SoftReference<LoadedImage>>();

			for (Entry<Uri, SoftReference<LoadedImage>> entry : imageCache.entrySet())
			{
				if (entry.getValue().get().getAddedToCache() + lifetime > now)
				{
					newCache.put(entry.getKey(), entry.getValue());
				}
			}

			imageCache = newCache;
		}
		finally
		{
			imageCacheLock.unlock();
		}
	}

	public static void clearCache()
	{
		imageCacheLock.lock();

		try
		{
			imageCache.clear();
		}
		finally
		{
			imageCacheLock.unlock();
		}
	}
}
