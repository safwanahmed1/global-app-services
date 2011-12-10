
package com.seta.android.game.wordsearch.view;

import android.content.Context; 
import android.media.MediaPlayer; 
import android.test.IsolatedContext;
 
public class Sound { 
    private MediaPlayer mPlayer; 
    private String name; 
 
    private boolean mPlaying = false; 
    private boolean mLoop = false; 
    public static boolean muteEffect =false;
    private Context context;
    private WordSearchPreferences setttingScreen;
 
    public Sound(Context ctx, int resID)  
    { 
    	this.context  = ctx;
        // clip name 
        name = ctx.getResources().getResourceName(resID); 
 
        // Create a media player 
        mPlayer = MediaPlayer.create(ctx, resID); 
 
        // Listen for  completion events 
        mPlayer.setOnCompletionListener( 
             new MediaPlayer.OnCompletionListener() {  
 
                    @Override 
                    public void onCompletion(MediaPlayer mp) { 
                        mPlaying = false; 
 
                        if (mLoop) { 
                            mp.start(); 
                        } 
                    } 
 
             }); 
    } 
 
     
    public synchronized void play() { 
	if(!WordSearchPreferences.isSoundEffect)
     {
        if (mPlaying) 
            return; 
 
        if (mPlayer != null) { 
            mPlaying = true; 
            mPlayer.start(); 
        } 
    } 
    }
    public synchronized void pause() { 
        try {              
            if (mPlaying) { 
                mPlaying = false; 
                mPlayer.pause(); 
            } 
 
        } catch (Exception e) { 
            System.err.println("AduioClip::pause " + name + " " 
                    + e.toString()); 
        } 
    } 
 
    public void stop()
    {
    	try
    	{
    		if(mPlaying)
    		{
		    	mLoop=false;
		    	mPlaying=false;
		    	mPlayer.pause();
		    	
		    	mPlayer.seekTo(0);
    		}
    	}
    	
    	 catch (Exception e) { 
             System.err.println("AduioClip::stop " + name + " " 
                     + e.toString()); 
         }
    }
    
    public synchronized void loop() { 
        mLoop = true; 
        mPlaying = true; 
        mPlayer.start(); 
    } 
 
    public void release() { 
        if (mPlayer != null) { 
            mPlayer.release(); 
            mPlayer = null; 
        } 
    } 
    
    public boolean getIsPlaying()
    {
    	return this.mPlaying;
    	
    }
} 