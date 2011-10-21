package com.vandgoo.tv;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ChannelMenuDialog extends Dialog {
	private Context mContext;
	private TVChannel mChannel;

	// step 1 - to return values from dialog
	public interface OnMenuEventListener {
		public void menuEvent(int menuItem, TVChannel channel);
	}

	// Step 2 – Declare the above interface as a field member
	// step 2 - to return values from dialog
	private OnMenuEventListener mMenuEventListener;

	// Step 3 – Add it to the custom dialog constructor
	// step 3 - to return values from dialog

	public ChannelMenuDialog(Context context, TVChannel channel, OnMenuEventListener onMenuEventListener) {
		super(context);
		mContext = context;
		mChannel = channel;
		mMenuEventListener = onMenuEventListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_CONTEXT_MENU);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		requestWindowFeature(Window.FEATURE_OPTIONS_PANEL);
		
		setTitle(mChannel.getName());
		
		/** Design the dialog in main.xml file */
		setContentView(R.layout.channel_menu_list);
		ListView channelMenu = (ListView) findViewById(R.id.channel_menu_list);

		ChannelMenuAdapter menuAdapter = new ChannelMenuAdapter(mContext);
		// ListView listCatalog = (ListView)
		// findViewById(R.id.channel_menu_list);
		channelMenu.setAdapter(menuAdapter);
		channelMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mMenuEventListener.menuEvent(arg2, mChannel);
				dismiss();
			}

		});
		getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
	}

}
