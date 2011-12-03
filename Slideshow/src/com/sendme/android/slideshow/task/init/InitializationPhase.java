package com.sendme.android.slideshow.task.init;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public enum InitializationPhase
{
	START,
	DATABASE,
	LOCAL_IMAGE_SOURCE,
	FACEBOOK_IMAGE_SOURCE,
	LOCAL_AUDIO_TRACKS_SOURCE,
	SLIDESHOW,
	PLAYLIST,
	COMPLETE
}
