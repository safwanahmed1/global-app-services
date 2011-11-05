/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sendme.android.slideshow.task.heartbeat;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public interface HeartbeatTaskEventListener
{
	public abstract void setHeartbeatIdentifier(Integer identifier);
	
	public abstract Integer getHeartbeatIdentifier();
	
	public abstract void onHeartbeatTaskEvent(Long time);
}
