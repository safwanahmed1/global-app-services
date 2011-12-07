package uno.com;

import java.io.IOException;
import java.io.InputStream;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.util.MathUtils;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Display;

public class StartActivity extends BaseExample implements
		IAccelerometerListener, IOnSceneTouchListener, IOnAreaTouchListener {
	private Camera mCamera;
	private int w, h;
	private int timeWait = 0;
	private Scene mScene;
	private BitmapTextureAtlas mBitmapTextureAtlas;

	private TextureRegion startScreenTextureRegion;
	private TextureRegion loadingTextureRegion;
	Sprite loading;

	@Override
	public Engine onLoadEngine() {
		// TODO Auto-generated method stub
		Display d = getWindowManager().getDefaultDisplay();
		w = d.getWidth();
		h = d.getHeight();
		System.out.println("width "+w + " | height "+h);
		
		this.mCamera = new Camera(0, 0, w, h);
		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(w, h),
				this.mCamera);
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		return new Engine(engineOptions);
	}

	

	@Override
	public void onLoadResources() {
		// TODO Auto-generated method stub
//		this.mBitmapTextureAtlas = new BitmapTextureAtlas(512, 512,
//				TextureOptions.DEFAULT);
		
        
//		byte b[] = (this.getResources().getAssets().open("gfx/mdpi/sc3_new_on.pmg")).;
		setPathResource(w, h);
//		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/mdpi/");
//		this.startScreenTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(mBitmapTextureAtlas, this,
//						"splash_screen.jpg", 0, 0);
		this.startScreenTextureRegion = this.createTextureRegion("splash_screen.jpg", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//		this.loadingTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(mBitmapTextureAtlas, this, "loading_roll.png",
//						this.startScreenTextureRegion.getWidth() + 1, 0);
//		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
		this.loadingTextureRegion = this.createTextureRegion("loading_roll.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	}

	@Override
	public Scene onLoadScene() {
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mScene = new Scene();
		Config.config(w, h);
		Sprite sprite = new Sprite(0, 0, this.startScreenTextureRegion);
		this.mScene.attachChild(sprite);
		loading = new Sprite(Config.xLoad, Config.yLoad, loadingTextureRegion);
		mScene.attachChild(loading);
		this.mScene.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				// TODO Auto-generated method stub
				// Your code to run here!
				if (timeWait >= 0 && timeWait <= 50) {
					timeWait += 1;
					loading.setRotation(MathUtils
							.radToDeg((float) ((timeWait * 20) * (Math.PI / 180))));
//					if (timeWait == 20) {
//
//						Config.config();
//						// Log.d("Config ", "ok"+Config.modePlayers);
//					}
				} else if (timeWait > 50) {
					Intent myIntent2 = new Intent(StartActivity.this
							.getBaseContext(), ConfigActivity.class);
					startActivityForResult(myIntent2, 1);
					finish();
				}
			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub

			}
		});
		return mScene;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onAccelerometerChanged(AccelerometerData pAccelerometerData) {
		// TODO Auto-generated method stub

	}

}
