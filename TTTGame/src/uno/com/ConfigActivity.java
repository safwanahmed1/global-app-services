package uno.com;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.SmoothCamera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
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

import android.content.Intent;
import android.util.Log;
import android.view.Display;

public class ConfigActivity extends BaseExample implements
		IAccelerometerListener, IOnSceneTouchListener, IOnAreaTouchListener {
	// private static final int CAMERA_WIDTH = 720;
	// private static final int CAMERA_HEIGHT = 480;
	public static final byte QUIT = 0;
	public static final byte NEXT = 1;
	private PhysicsHandler mPhysicsHandler;
	public boolean isNext, isQuit;
	public boolean isUpdate;

	public byte timeWait = -1;
	private Camera mCamera;

	private BitmapTextureAtlas mBitmapTextureAtlas;

	private TextureRegion bgscreen1TextureRegion;
	private TextureRegion radioOnTextureRegion;
	private TextureRegion radioOffTextureRegion;

	private TextureRegion buttonNextTextureRegion;
	private TextureRegion buttonQuitTextureRegion;

	private int w, h;

	private Scene mScene;

	private Sprite radioPlayerOn, radioPlayerOff;
	private Sprite radioLevelOn, radioLevelOff;
	private Sprite radioSoundOn, radioSoundOff;

	private Sprite buttonNext, buttonQuit;

	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		return false;
	}

	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {
			float x = pSceneTouchEvent.getX();
			float y = pSceneTouchEvent.getY();
			if (buttonQuit != null)
				if (x >= Config.xQuitButton && x <= Config.xQuitButton + buttonQuit.getWidth()) {
					if (y >= Config.yQuitButton && y <= Config.yQuitButton + buttonQuit.getHeight()) {
						isQuit = true;
						buttonQuit.setPosition(Config.xQuitButton, Config.yQuitButton);
						createSpriteSpawnTimeHandler(QUIT);
					}
				}
			if (buttonNext != null)
				if (x >= Config.xNextButton && x <= Config.xNextButton + buttonNext.getWidth()) {
					if (y >= Config.yNextButton && y <= Config.yNextButton + buttonNext.getHeight()) {
						isNext = true;
						buttonNext.setPosition(Config.xNextButton, Config.yNextButton);
						createSpriteSpawnTimeHandler(NEXT);
					}
				}

		}
		return false;
	}

	public void onAccelerometerChanged(AccelerometerData pAccelerometerData) {
		// TODO Auto-generated method stub

	}

	/** Called when the activity is first created. */

	public Engine onLoadEngine() {
		Display d = getWindowManager().getDefaultDisplay();
		w = d.getWidth();
		h = d.getHeight();
		this.mCamera = new Camera(0, 0, w, h);
		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(w, h),
				this.mCamera);
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		return new Engine(engineOptions);
	}

	public void onLoadResources() {
		// TODO Auto-generated method stub
//		this.mBitmapTextureAtlas = new BitmapTextureAtlas(1024, 1024,
//				TextureOptions.DEFAULT);
		setPathResource(w, h);
//		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
//		this.bgscreen1TextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(mBitmapTextureAtlas, this, "bgscreen1.jpg", 0,
//						0);
		this.bgscreen1TextureRegion = this.createTextureRegion("bgscreen1.jpg", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//		this.radioOnTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(mBitmapTextureAtlas, this, "radio_on.png",
//						this.bgscreen1TextureRegion.getWidth(), 0);
		this.radioOnTextureRegion = this.createTextureRegion("radio_on.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//		this.radioOffTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(mBitmapTextureAtlas, this, "radio_off.png",
//						this.bgscreen1TextureRegion.getWidth()
//								+ this.radioOnTextureRegion.getWidth(), 0);
		this.radioOffTextureRegion = this.createTextureRegion("radio_off.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//		this.buttonNextTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(mBitmapTextureAtlas, this, "sc1_next_on.png",
//						0, this.bgscreen1TextureRegion.getHeight());
		this.buttonNextTextureRegion = this.createTextureRegion("sc1_next_on.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.buttonQuitTextureRegion = this.createTextureRegion("sc1_quit_on.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//		this.buttonQuitTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(mBitmapTextureAtlas, this, "sc1_quit_on.png",
//						0, this.bgscreen1TextureRegion.getHeight()
//								+ this.buttonNextTextureRegion.getHeight());
//		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
	}

	public Scene onLoadScene() {
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mScene = new Scene();

		// final AutoParallaxBackground autoParallaxBackground = new
		// AutoParallaxBackground(0, 0, 0, 5);
		// autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f,
		// new Sprite(0,this.bgscreen1TextureRegion.getHeight(),
		// this.bgscreen1TextureRegion)));
		//
		// this.mScene.setBackground(autoParallaxBackground);
		Sprite sprite = new Sprite(0, 0, this.bgscreen1TextureRegion);
		buttonQuit = new Sprite(-200, -100, this.buttonQuitTextureRegion);
		buttonNext = new Sprite(-200, -100, this.buttonNextTextureRegion);

//		 {
//		 @Override
//		 public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final
//		 float pTouchAreaLocalX, final float pTouchAreaLocalY) {
//		 this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2,
//		 pSceneTouchEvent.getY() - this.getHeight() / 2);
//		 Log.d(""+this.getX(), ""+this.getY());
//		 return true;
//		 }
//		 };
//		 buttonQuit = new Sprite(300,400,this.buttonQuitTextureRegion);

		radioPlayerOn = new Sprite(Config.xP1Radio, Config.yP1Radio, this.radioOnTextureRegion);
		radioLevelOn = new Sprite(Config.xL33Radio, Config.yL33Radio, this.radioOnTextureRegion);
		radioSoundOn = new Sprite(Config.xSOnRadio, Config.ySOnRadio, this.radioOnTextureRegion);
		radioPlayerOff = new Sprite(Config.xP2Radio, Config.yP2Radio, this.radioOffTextureRegion) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (Config.modePlayers == Mode.TWO_PLAYERS) {
					Config.modePlayers = Mode.ONE_PLAYER;
				} else {
					Config.modePlayers = Mode.TWO_PLAYERS;
				}
//				if (Config.modeLevel != Mode.LEVEL66 ) {
					float x = this.getX();
					float y = this.getY();
					this.setPosition(radioPlayerOn.getX(), radioPlayerOn.getY());
					radioPlayerOn.setPosition(x, y);
//				}
				return true;
			}
		};
		radioLevelOff = new Sprite(Config.xL66Radio, Config.yL66Radio, this.radioOffTextureRegion) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (Config.modeLevel == Mode.LEVEL33) {
					Config.modeLevel = Mode.LEVEL66;
//					if (Config.modePlayers == Mode.ONE_PLAYER) {
//						float x1 = radioPlayerOff.getX();
//						float y1 = radioPlayerOff.getY();
//						radioPlayerOff.setPosition(radioPlayerOn.getX(),
//								radioPlayerOn.getY());
//						radioPlayerOn.setPosition(x1, y1);
//						Config.modePlayers = Mode.TWO_PLAYERS;
//					}
				} else {
					Config.modeLevel = Mode.LEVEL33;

				}
				float x = this.getX();
				float y = this.getY();
				this.setPosition(radioLevelOn.getX(), radioLevelOn.getY());
				radioLevelOn.setPosition(x, y);

				return true;
			}
		};
		radioSoundOff = new Sprite(Config.xSOffRadio, Config.ySOffRadio, this.radioOffTextureRegion) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (Config.modeSound == Mode.SOUND_OFF) {
					Config.modeSound = Mode.SOUND_ON;
				} else {
					Config.modeSound = Mode.SOUND_OFF;
				}
				float x = this.getX();
				float y = this.getY();
				this.setPosition(radioSoundOn.getX(), radioSoundOn.getY());
				radioSoundOn.setPosition(x, y);
				return true;
			}
		};
		if (Config.modePlayers == Mode.ONE_PLAYER) {
			radioPlayerOn.setPosition(Config.xP1Radio, Config.yP1Radio);
			radioPlayerOff.setPosition(Config.xP2Radio, Config.yP2Radio);
		} else {
			radioPlayerOff.setPosition(Config.xP1Radio, Config.yP1Radio);
			radioPlayerOn.setPosition(Config.xP2Radio, Config.yP2Radio);
		}
		if (Config.modeLevel == Mode.LEVEL33) {
			radioLevelOn.setPosition(Config.xL33Radio, Config.yL33Radio);
			radioLevelOff.setPosition(Config.xL66Radio, Config.yL66Radio);
		} else {
			radioLevelOff.setPosition(Config.xL33Radio, Config.yL33Radio);
			radioLevelOn.setPosition(Config.xL66Radio, Config.yL66Radio);
		}
		if (Config.modeSound == Mode.SOUND_ON) {
			radioSoundOn.setPosition(Config.xSOnRadio, Config.ySOnRadio);
			radioSoundOff.setPosition(Config.xSOffRadio, Config.ySOffRadio);
		} else {
			radioSoundOff.setPosition(Config.xSOnRadio, Config.ySOnRadio);
			radioSoundOn.setPosition(Config.xSOffRadio, Config.ySOffRadio);
		}
		this.mScene.registerTouchArea(radioPlayerOff);
		this.mScene.registerTouchArea(radioLevelOff);
		this.mScene.registerTouchArea(radioSoundOff);
		this.mScene.attachChild(sprite);

		// this.mScene.registerTouchArea(buttonNext);
		this.mScene.attachChild(buttonQuit);
		this.mScene.attachChild(buttonNext);

		this.mScene.attachChild(radioPlayerOff);
		this.mScene.attachChild(radioPlayerOn);
		this.mScene.attachChild(radioLevelOn);
		this.mScene.attachChild(radioLevelOff);
		this.mScene.attachChild(radioSoundOn);
		this.mScene.attachChild(radioSoundOff);
		this.mScene.setOnSceneTouchListener(this);
		this.mScene.setOnAreaTouchListener(this);

		this.mScene.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				// TODO Auto-generated method stub
				// Your code to run here!
				if (isUpdate) {
					// nextActivity(NEXT);
					if (timeWait < 0)
						timeWait = 0;
					isUpdate = false;
				}
				if (timeWait >= 0 && timeWait <= 10) {
					timeWait++;
				} else if (timeWait > 10) {
					timeWait = -1;
					if (isNext) {
						nextActivity(NEXT);
						isNext = false;
					}
				}

			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub

			}
		});
		return this.mScene;
	}

	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}

	private void createSprite(byte type, float pX, float pY) {
		switch (type) {
		case QUIT:
			buttonQuit = new Sprite(pX, pY, this.buttonQuitTextureRegion);
			this.mScene.attachChild(buttonQuit);
			break;
		case NEXT:
			buttonNext = new Sprite(pX, pY, this.buttonNextTextureRegion);
			this.mScene.attachChild(buttonNext);
			break;
		default:
			break;
		}

	}

	private void removeSprite(byte type) {
		switch (type) {
		case QUIT:
			this.buttonQuit.setPosition(-200, -100);
			break;
		case NEXT:
			this.buttonNext.setPosition(-200, -100);
			break;
		default:
			break;
		}
	}

	private void createSpriteSpawnTimeHandler(final byte type) {
		TimerHandler spriteTimerHandler;
		this.getEngine().registerUpdateHandler(

		spriteTimerHandler = new TimerHandler(0.1f, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				removeSprite(type);
				isUpdate = true;
			}
		}));
	}

	public void nextActivity(byte type) {
		switch (type) {
		case QUIT:
			
//			Intent intent = new Intent(Intent.ACTION_MAIN);
//			intent.addCategory(Intent.CATEGORY_HOME);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
			super.onDestroy();
			finish();
			System.exit(0);
			break;
		case NEXT:
			Intent myIntent = new Intent(ConfigActivity.this.getBaseContext(),
					NameAndChoseFirstMoveActivity.class);
			startActivityForResult(myIntent, 2);
			finish();
			break;
		default:
			break;
		}
	}
}