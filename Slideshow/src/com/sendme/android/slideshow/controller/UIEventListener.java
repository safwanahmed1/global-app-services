package com.sendme.android.slideshow.controller;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public interface UIEventListener
{
	public abstract void onUIEvent(UIEvent event);

	public abstract void setUIEventListenerIdentifier(Integer identfier);

	public abstract Integer getUIEventListenerIdentifier();
}
