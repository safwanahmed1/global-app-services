package com.sendme.android.slideshow.controller;

import android.net.Uri;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.Inject;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.R;
import com.sendme.android.slideshow.image.BackgroundImageLoader;
import com.sendme.android.slideshow.image.BackgroundImageLoaderListener;
import com.sendme.android.slideshow.image.ImageLoader;
import com.sendme.android.slideshow.image.ImageLoaderException;
import com.sendme.android.slideshow.image.ImageType;
import com.sendme.android.slideshow.image.LoadedImage;
import com.sendme.android.slideshow.manager.ORMException;
import com.sendme.android.slideshow.manager.PhotoManager;
import com.sendme.android.slideshow.manager.SettingsManager;
import com.sendme.android.slideshow.manager.SlideshowManager;
import com.sendme.android.slideshow.model.Photo;
import com.sendme.android.slideshow.model.SlideshowEntry;
import com.sendme.android.slideshow.runnable.AudioPlaybackUIAnimation;
import com.sendme.android.slideshow.runnable.ImageUpdater;
import com.sendme.android.slideshow.runnable.TextUpdater;
import com.sendme.android.slideshow.task.heartbeat.HeartbeatTask;
import com.sendme.android.slideshow.task.heartbeat.HeartbeatTaskEventListener;
import roboguice.inject.InjectView;

/**
 * 
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class SlideshowController extends Controller implements
		HeartbeatTaskEventListener, UIEventListener,
		BackgroundImageLoaderListener {
	private final static AndroidLogger log = AndroidLogger
			.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	@Inject
	private SettingsManager settingsManager = null;

	@Inject
	private SlideshowManager slideshowManager = null;

	@Inject
	private PhotoManager photoManager = null;

	@Inject
	private ImageLoader imageLoader = null;

	@InjectView(R.id.gallery)
	private ImageView gallery = null;

	@InjectView(R.id.slidetextLayout)
	private View textLayout = null;

	@InjectView(R.id.slidescreenText)
	private TextView slideMessage = null;

	private BackgroundImageLoader backgroundImageLoader = null;

	private Long lastPhotoUpdate = 0L;

	private Integer heartbeatIdentifier = null;

	private Integer uiEventListenerIdentifier = null;

	private Integer imageLoaderTaskEventListenerIdentifier = null;

	public SlideshowController() {
	}

	@Override
	public void initialize() {
		log.debug("SlideshowController initialized.");
	}

	@Override
	public void start() {
		UIController.addListener(this);

		HeartbeatTask.addListener(this);
		log.debug("SlideshowController started.");
	}

	@Override
	public void resume() {
		log.debug("SlideshowController resumed.");
	}

	@Override
	public void pause() {
		log.debug("SlideshowController paused.");
	}

	@Override
	public void stop() {
		HeartbeatTask.removeListener(this);

		UIController.removeListener(this);

		log.info("SlideshowController stopped.");
	}

	@Override
	public void destroy() {
		log.info("SlideshowController destroyed.");
	}

	public void onHeartbeatTaskEvent(Long time) {
		log.debug("SlideshowController got heartbeat: " + time);

		if ((time - lastPhotoUpdate) > settingsManager.getSlideshowInterval()) {
			// Ok, we haven't shown the next photo in a while, we should do that
			log.debug("We need a new photo....");

			if (backgroundImageLoader == null) {
				loadNextPhoto(time);
			} else {
				log.debug("ImageLoader is busy with a previous request.");
			}
		}
	}

	public void onBackgroundImageLoadingSuccess(LoadedImage image) {
		log.info("Got successful response from background image loader");

		updateImage(image);

		backgroundImageLoader = null;

		lastPhotoUpdate = System.currentTimeMillis();
	}

	public void onBackgroundImageLoadingFailure(ImageLoaderException e) {

		log.info("Got error response from background image loader", e);
	}

	public void onUIEvent(UIEvent event) {
		switch (event) {
		case PAUSE:
		case PLAY: {
			// TODO: We might want to pause/play the slideshow

			break;
		}

		case NEXT_PHOTO: {
			loadNextPhoto(System.currentTimeMillis());

			break;
		}

		case PREVIOUS_PHOTO: {
			loadPreviousPhoto(System.currentTimeMillis());

			break;
		}
		}
	}

	public void loadCurrentPhoto(Long time) {
		try {
			SlideshowEntry entry = null;

			Integer currentId = settingsManager.getCurrentSlideshowEntryId();

			if (currentId == null) {
				entry = slideshowManager.getNextEntry(currentId);
			} else {
				entry = slideshowManager.find(currentId);
			}

			if (entry != null) {

				if (textLayout.getVisibility() != View.GONE) {
					AudioPlaybackUIAnimation anim = new AudioPlaybackUIAnimation();
					anim.setView(textLayout);
					anim.setAnimation(AnimationUtils.loadAnimation(
							textLayout.getContext(), R.anim.slide_menu_hide));
					anim.setFinalVisibility(View.GONE);
					textLayout.post(anim);
				}
				if (entry.getPhotoId() != null) {
					Photo photo = photoManager.find(entry.getPhotoId());

					Uri uri = Uri.parse(photo.getURI());

					ImageType loadType = null;

					switch (photo.getPhotoSource()) {
					case FACEBOOK: {
						loadType = ImageType.REMOTE_PHOTO;

						break;
					}

					case FLICKR: {
						loadType = ImageType.REMOTE_PHOTO;

						break;
					}

					case LOCAL: {
						loadType = ImageType.LOCAL_PHOTO;

						break;
					}
					}

					loadImageInBackground(uri, loadType, photo.getText());
				} else {
					if (textLayout.getVisibility() != View.VISIBLE) {

						AudioPlaybackUIAnimation anim = new AudioPlaybackUIAnimation();
						anim.setView(textLayout);
						anim.setAnimation(AnimationUtils.loadAnimation(
								textLayout.getContext(), R.anim.slide_menu_show));
						anim.setFinalVisibility(View.VISIBLE);
						textLayout.post(anim);
					}

					log.debug("No image found!");
				}
			} else {

				if (textLayout.getVisibility() != View.VISIBLE) {

					AudioPlaybackUIAnimation anim = new AudioPlaybackUIAnimation();
					anim.setView(textLayout);
					anim.setAnimation(AnimationUtils.loadAnimation(
							textLayout.getContext(), R.anim.slide_menu_show));
					anim.setFinalVisibility(View.VISIBLE);
					textLayout.post(anim);
				}

				log.debug("No image found!");

			}
		} catch (ORMException e) {
			log.error("Error loading current photo.", e);
		}
	}

	private void loadImageInBackground(Uri uri, ImageType type, String text) {
		backgroundImageLoader = new BackgroundImageLoader();

		backgroundImageLoader.setListener(this);
		backgroundImageLoader.setImageLoader(imageLoader);
		backgroundImageLoader.setURI(uri);
		backgroundImageLoader.setType(type);
		backgroundImageLoader.setText(text);
		backgroundImageLoader.setTargetWidth(gallery.getWidth());
		backgroundImageLoader.setTargetHeight(gallery.getHeight());

		Thread thread = new Thread(backgroundImageLoader);

		thread.start();
	}

	public void loadNextPhoto(Long time) {
		// Ok, first we need to figure out which photo we're going to show.
		// Let's ask
		// the handy-dandy slideshow manager.

		try {
			Integer currentId = settingsManager.getCurrentSlideshowEntryId();

			SlideshowEntry nextEntry = slideshowManager.getNextEntry(currentId);

			log.info("About to try next image: " + nextEntry + " -- "
					+ currentId);

			// If it's not null, and its not the same entry we have, we can load
			// it
			if (nextEntry != null) {

				if (!nextEntry.getId().equals(currentId)) {
					settingsManager.setCurrentSlideshowEntryId(nextEntry
							.getId());

					loadCurrentPhoto(time);
				} else {
					log.debug("Same image returned!");
				}
			} else {
				TextUpdater textUpdater = new TextUpdater();

				textUpdater.setTextView(slideMessage);
				if (settingsManager.isUseFacebookImages()
						&& settingsManager.isUseLocalImages()) {
					textUpdater.setText(slideMessage.getContext().getString(
							R.string.slidescreenGettingImage));
				}
				if (!settingsManager.isUseFacebookImages()
						&& settingsManager.isUseLocalImages()) {
					textUpdater.setText(slideMessage.getContext().getString(
							R.string.slidescreenGettingLocalImage));
				}
				if (settingsManager.isUseFacebookImages()
						&& !settingsManager.isUseLocalImages()) {
					textUpdater.setText(slideMessage.getContext().getString(
							R.string.slidescreenGettingFacebookImage));
				}
				if (!settingsManager.isUseFacebookImages()
						&& !settingsManager.isUseLocalImages()) {
					textUpdater.setText(slideMessage.getContext().getString(
							R.string.slidescreenNoSourceImage));

				}
				slideMessage.post(textUpdater);
				log.debug("No entry returned???");
			}
		} catch (ORMException e) {
			log.error("Error showing next photo", e);
		}
	}

	public void loadPreviousPhoto(Long time) {
		// Ok, first we need to figure out which photo we're going to show.
		// Let's ask
		// the handy-dandy slideshow manager.
		try {
			Integer currentId = settingsManager.getCurrentSlideshowEntryId();

			SlideshowEntry previousEntry = slideshowManager
					.getPreviousEntry(currentId);

			log.info("About to try previous image: " + previousEntry + " -- "
					+ currentId);

			// If it's not null, and its not the same entry we have, we can load
			// it
			if (previousEntry != null) {

				if (!previousEntry.getId().equals(currentId)) {
					settingsManager.setCurrentSlideshowEntryId(previousEntry
							.getId());

					loadCurrentPhoto(time);
				} else {
					log.debug("Same image returned!");
				}
			} else {
				TextUpdater textUpdater = new TextUpdater();

				textUpdater.setTextView(slideMessage);
				if (settingsManager.isUseFacebookImages()
						&& settingsManager.isUseLocalImages()) {
					textUpdater.setText(slideMessage.getContext().getString(
							R.string.slidescreenGettingImage));
				}
				if (!settingsManager.isUseFacebookImages()
						&& settingsManager.isUseLocalImages()) {
					textUpdater.setText(slideMessage.getContext().getString(
							R.string.slidescreenGettingLocalImage));
				}
				if (settingsManager.isUseFacebookImages()
						&& !settingsManager.isUseLocalImages()) {
					textUpdater.setText(slideMessage.getContext().getString(
							R.string.slidescreenGettingFacebookImage));
				}
				if (!settingsManager.isUseFacebookImages()
						&& !settingsManager.isUseLocalImages()) {
					textUpdater.setText(slideMessage.getContext().getString(
							R.string.slidescreenNoSourceImage));

				}
				slideMessage.post(textUpdater);
				log.debug("No entry returned???");
			}
		} catch (ORMException e) {
			log.error("Error showing next photo", e);
		}
	}

	private void updateImage(LoadedImage image) {
		ImageUpdater imageUpdater = new ImageUpdater();

		imageUpdater.setImageView(gallery);

		imageUpdater.setImage(image);

		gallery.post(imageUpdater);
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}

	public SlideshowManager getSlideshowManager() {
		return slideshowManager;
	}

	public void setSlideshowManager(SlideshowManager slideshowManager) {
		this.slideshowManager = slideshowManager;
	}

	public PhotoManager getPhotoManager() {
		return photoManager;
	}

	public void setPhotoManager(PhotoManager photoManager) {
		this.photoManager = photoManager;
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public void setImageLoader(ImageLoader imageLoader) {
		this.imageLoader = imageLoader;
	}

	public ImageView getGallery() {
		return gallery;
	}

	public void setGallery(ImageView gallery) {
		this.gallery = gallery;
	}

	public Long getLastPhotoUpdate() {
		return lastPhotoUpdate;
	}

	public void setLastPhotoUpdate(Long lastPhotoUpdate) {
		this.lastPhotoUpdate = lastPhotoUpdate;
	}

	public Integer getHeartbeatIdentifier() {
		return heartbeatIdentifier;
	}

	public void setHeartbeatIdentifier(Integer heartbeatIdentifier) {
		this.heartbeatIdentifier = heartbeatIdentifier;
	}

	public Integer getUIEventListenerIdentifier() {
		return uiEventListenerIdentifier;
	}

	public void setUIEventListenerIdentifier(Integer uiEventListenerIdentifier) {
		this.uiEventListenerIdentifier = uiEventListenerIdentifier;
	}

	public Integer getImageLoaderTaskEventListenerIdentifier() {
		return imageLoaderTaskEventListenerIdentifier;
	}

	public void setImageLoaderTaskEventListenerIdentifier(
			Integer imageLoaderTaskEventListenerIdentifier) {
		this.imageLoaderTaskEventListenerIdentifier = imageLoaderTaskEventListenerIdentifier;
	}
}
