package uno.com;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
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
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class NameAndChoseFirstMoveActivity extends BaseExample implements
		IAccelerometerListener, IOnSceneTouchListener, IOnAreaTouchListener {
	private Camera mCamera;
	public static final byte BACK = 0;
	public static final byte PLAY = 1;
	private BitmapTextureAtlas mBitmapTextureAtlas;

	private TextureRegion bgscreen2TextureRegion;
	private TextureRegion subBgscreenTextureRegion;

	private TextureRegion radioSetOOnTextureRegion;
	private TextureRegion radioSetOOffTextureRegion;
	private TextureRegion radioSetXOffTextureRegion;
	private TextureRegion radioSetXOnTextureRegion;

	private TextureRegion buttonPlayTextureRegion;
	private TextureRegion buttonBackTextureRegion;

	Sprite radioSetOOn, radioSetOOff, radioSetXOn, radioSetXOff;
	Sprite radioChoseXOn,radioChoseXOff,radioChoseOOn,radioChoseOOff;
	Sprite buttonPlay, buttonBack;

	public boolean isPlay, isBack;
	public boolean isUpdate;
	private int w, h;
	private int timeWait = -1;
	private TableLayout tableLayout;
	private ArrayList<EditText> arrayList;
	Text text;
	private Scene mScene;

	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.main);
	//
	// }
	@Override
	public Engine onLoadEngine() {
		// TODO Auto-generated method stub
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

	@Override
	public void onLoadResources() {
		// TODO Auto-generated method stub
//		this.mBitmapTextureAtlas = new BitmapTextureAtlas(1024, 1024,
//				TextureOptions.DEFAULT);
		setPathResource(w, h);
//		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
//		this.bgscreen2TextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(mBitmapTextureAtlas, this, "bgscreen2.jpg", 0,
//						0);
		this.bgscreen2TextureRegion = this.createTextureRegion( "bgscreen2.jpg", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		if (Config.modePlayers == Mode.TWO_PLAYERS) {
//			this.subBgscreenTextureRegion = BitmapTextureAtlasTextureRegionFactory
//					.createFromAsset(mBitmapTextureAtlas, this,
//							"setup2pl_bg.jpg",
//							this.bgscreen2TextureRegion.getWidth(), 0);
			this.subBgscreenTextureRegion = this.createTextureRegion( "setup2pl_bg.jpg", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		}
//		this.radioSetOOnTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(mBitmapTextureAtlas, this, "setup_0_on.png",
//						0, this.bgscreen2TextureRegion.getHeight() + 1);
		this.radioSetOOnTextureRegion = this.createTextureRegion( "setup_0_on.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.radioSetOOffTextureRegion = this.createTextureRegion( "setup_0_off.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//		this.radioSetOOffTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(mBitmapTextureAtlas, this, "setup_0_off.png",
//						this.radioSetOOnTextureRegion.getWidth() + 1,
//						this.bgscreen2TextureRegion.getHeight() + 1);
//		this.radioSetXOnTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(
//						mBitmapTextureAtlas,
//						this,
//						"setup_x_on.png",
//						this.radioSetOOnTextureRegion.getWidth()
//								+ this.radioSetOOffTextureRegion.getWidth() + 2,
//						this.bgscreen2TextureRegion.getHeight() + 1);
		this.radioSetXOnTextureRegion = this.createTextureRegion( "setup_x_on.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.radioSetXOffTextureRegion = this.createTextureRegion( "setup_x_off.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//		this.radioSetXOffTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(mBitmapTextureAtlas, this, "setup_x_off.png",
//						this.radioSetOOnTextureRegion.getWidth()
//								+ this.radioSetOOffTextureRegion.getWidth()
//								+ this.radioSetXOnTextureRegion.getWidth() + 3,
//						this.bgscreen2TextureRegion.getHeight() + 1);
//		this.buttonPlayTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(mBitmapTextureAtlas, this, "sc2_play_on.png",
//						2 * this.bgscreen2TextureRegion.getWidth() + 1, 0);
		this.buttonPlayTextureRegion = this.createTextureRegion( "sc2_play_on.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.buttonBackTextureRegion = this.createTextureRegion( "sc2_back_on.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//		this.buttonBackTextureRegion = BitmapTextureAtlasTextureRegionFactory
//				.createFromAsset(mBitmapTextureAtlas, this, "sc2_back_on.png",
//						2 * this.bgscreen2TextureRegion.getWidth() + 1,
//						this.buttonPlayTextureRegion.getHeight() + 1);
//		this.mEngine.getTextureManager().loadTexture(this.mBitmapTextureAtlas);
	}

	@Override
	public Scene onLoadScene() {
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mScene = new Scene();

		Sprite sprite = new Sprite(0, 0, this.bgscreen2TextureRegion);
		this.mScene.attachChild(sprite);
		// this.getBaseContext().
		// this.mScene.
		arrayList = new ArrayList<EditText>();
//		tableLayout = createTableLayout(108, 132, 40, h - 132 - 50);
		if (Config.modePlayers == Mode.ONE_PLAYER) {
			EditText editText = createEditText(true, 1, "","Player", Config.heightText,Color.TRANSPARENT);
			arrayList.add(editText);
			tableLayout = createTableLayout(Config.nameCorP1For1P,editText);
			this.addContentView(tableLayout, new ViewGroup.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			radioChoseXOn = new Sprite(Config.xSignXRadio, Config.ySignXRadio, this.radioSetXOnTextureRegion);
			radioChoseXOff = new Sprite(Config.xSignXRadio, Config.ySignXRadio,
					this.radioSetXOffTextureRegion) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
						final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					if (Config.yourSign == Mode.O_SIGN) {
						this.setVisible(false);
						radioChoseXOn.setVisible(true);
						radioChoseOOn.setVisible(false);
						radioChoseOOff.setVisible(true);
						Config.yourSign = Mode.X_SIGN;
					}
					return true;
				}
			};
			radioChoseOOn = new Sprite(Config.xSignORadio, Config.ySignORadio, this.radioSetOOnTextureRegion);
			radioChoseOOff = new Sprite(Config.xSignORadio, Config.ySignORadio,
					this.radioSetOOffTextureRegion) {
				@Override
				public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
						final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					if (Config.yourSign == Mode.X_SIGN) {
						this.setVisible(false);
						radioChoseOOn.setVisible(true);
						radioChoseXOn.setVisible(false);
						radioChoseXOff.setVisible(true);
						Config.yourSign = Mode.O_SIGN;
					}
					return true;
				}
			};
			if(Config.yourSign == Mode.X_SIGN){
				radioChoseXOn.setVisible(true);
				radioChoseXOff.setVisible(false);
				radioChoseOOn.setVisible(false);
				radioChoseOOff.setVisible(true);
			} else {
				radioChoseXOn.setVisible(false);
				radioChoseXOff.setVisible(true);
				radioChoseOOn.setVisible(true);
				radioChoseOOff.setVisible(false);
			}
			 mScene.attachChild(radioChoseXOn);
			 mScene.attachChild(radioChoseXOff);
			 mScene.attachChild(radioChoseOOn);
			 mScene.attachChild(radioChoseOOff);
			 mScene.registerTouchArea(radioChoseXOff);
			 mScene.registerTouchArea(radioChoseOOff);
		} else {
			Sprite sprite2 = new Sprite(Config.xSubBg, Config.ySubBg, this.subBgscreenTextureRegion);
//			{
//				 @Override
//				 public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final
//				 float pTouchAreaLocalX, final float pTouchAreaLocalY) {
//				 this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2,
//				 pSceneTouchEvent.getY() - this.getHeight() / 2);
//				 Log.d(""+this.getX(), ""+this.getY());
//				 return true;
//				 }
//				 };
//				 mScene.registerTouchArea(sprite2);
//			 {
//			 @Override
//			 public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
//			 final
//			 float pTouchAreaLocalX, final float pTouchAreaLocalY) {
//			 this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2,
//			 pSceneTouchEvent.getY() - this.getHeight() / 2);
//			 Log.d(""+this.getX(), ""+this.getY());
//			 return true;
//			 }
//			 };
			this.mScene.attachChild(sprite2);
//			TableLayout tableLayout = createTableLayout(144, 131, 40, h - 131 - 50);
//			TableLayout tableLayout2 = createTableLayout(144, 192, 40, h - 192 - 50);
//			this.addContentView(tableLayout, new ViewGroup.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//			this.addContentView(tableLayout2, new ViewGroup.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//			arrayList.add(tableLayout);
//			arrayList.add(tableLayout2);
			EditText editText1 = createEditText(true, 1, "","Player 1", Config.heightText,Color.TRANSPARENT);
			EditText editText2 = createEditText(true, 2, "","Player 2", Config.heightText,Color.TRANSPARENT);
			arrayList.add(editText1);
			arrayList.add(editText2);
			TableLayout tableLayout = createTableLayout(Config.nameCorP1For2P,editText1);
			TableLayout tableLayout2 = createTableLayout(Config.nameCorP2,editText2);
			this.addContentView(tableLayout, new ViewGroup.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			this.addContentView(tableLayout2, new ViewGroup.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
		radioSetOOn = new Sprite(Config.xMoveORadio, Config.yMoveORadio, this.radioSetOOnTextureRegion);
		radioSetOOff = new Sprite(Config.xMoveORadio, Config.yMoveORadio,
				this.radioSetOOffTextureRegion) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (Config.firstMove == Mode.FIRST_X_MOVE) {
					this.setVisible(false);
					radioSetOOn.setVisible(true);
					radioSetXOn.setVisible(false);
					radioSetXOff.setVisible(true);
					Config.firstMove = Mode.FIRST_O_MOVE;
				}
				return true;
			}
		};
		radioSetXOn = new Sprite(Config.xMoveXRadio, Config.yMoveXRadio, this.radioSetXOnTextureRegion);
		radioSetXOff = new Sprite(Config.xMoveXRadio, Config.yMoveXRadio,
				this.radioSetXOffTextureRegion) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (Config.firstMove == Mode.FIRST_O_MOVE) {
					this.setVisible(false);
					radioSetXOn.setVisible(true);
					radioSetOOn.setVisible(false);
					radioSetOOff.setVisible(true);
					Config.firstMove = Mode.FIRST_X_MOVE;
				}
				return true;
			}
		};
		if (Config.firstMove == Mode.FIRST_O_MOVE) {
			radioSetOOn.setVisible(true);
			radioSetOOff.setVisible(false);
			radioSetXOn.setVisible(false);
			radioSetXOff.setVisible(true);
		} else {
			radioSetOOn.setVisible(false);
			radioSetOOff.setVisible(true);
			radioSetXOn.setVisible(true);
			radioSetXOff.setVisible(false);
		}
		buttonPlay = new Sprite(-200f, -100f, this.buttonPlayTextureRegion);
		buttonBack = new Sprite(-200f, -100f, this.buttonBackTextureRegion);
		mScene.attachChild(buttonBack);
		mScene.attachChild(buttonPlay);
		this.mScene.attachChild(radioSetOOn);
		this.mScene.attachChild(radioSetOOff);
		mScene.registerTouchArea(radioSetOOff);
		this.mScene.attachChild(radioSetXOn);
		this.mScene.attachChild(radioSetXOff);
		mScene.registerTouchArea(radioSetXOff);
		this.mScene.setOnSceneTouchListener(this);
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
				//
				if (timeWait >= 0 && timeWait <= 10) {
					timeWait++;
				} else if (timeWait > 10) {
					timeWait = -1;
					if (isPlay) {
						nextActivity(PLAY);
						isPlay = false;
					}
					if(isBack){
						nextActivity(BACK);
						isBack = false;
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
		if (pSceneTouchEvent.isActionDown()) {
			float x = pSceneTouchEvent.getX();
			float y = pSceneTouchEvent.getY();
			if (buttonBack != null)
				if (x >= Config.xBack1Button && x <= Config.xBack1Button + buttonBack.getWidth()) {
					if (y >= Config.yBack1Button && y <= Config.yBack1Button + buttonBack.getHeight()) {
						isBack = true;
						buttonBack.setPosition(Config.xBack1Button, Config.yBack1Button);
						createSpriteSpawnTimeHandler(BACK);
					}
				}
			if (buttonPlay != null)
				if (x >= Config.xPlayButton && x <= Config.xPlayButton + buttonPlay.getWidth()) {
					if (y >= Config.yPlayButton && y <= Config.yPlayButton + buttonPlay.getHeight()) {
						isPlay = true;
						buttonPlay.setPosition(Config.xPlayButton, Config.yPlayButton);
						createSpriteSpawnTimeHandler(PLAY);
					}
				}
			

		}
		return false;
	}

	private void removeSprite(byte type) {
		switch (type) {
		case BACK:
			this.buttonBack.setPosition(-200, -100);
			break;
		case PLAY:
			this.buttonPlay.setPosition(-200, -100);
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
		case BACK:
			Intent myIntent1 = new Intent(
					NameAndChoseFirstMoveActivity.this.getBaseContext(),
					ConfigActivity.class);
			startActivityForResult(myIntent1, 3);
			break;
		case PLAY:
			switch (Config.modePlayers) {
			case Mode.ONE_PLAYER:
				EditText editText = arrayList.get(0);
				Editable e = editText.getText();
				Config.namePlayer1 = e.toString();
				Config.namePlayer2 = "Phone";
				break;
			case Mode.TWO_PLAYERS:
				EditText editText1 = arrayList.get(0);
				Editable e1 = editText1.getText();
				Config.namePlayer1 = e1.toString();
				EditText editText2 = arrayList.get(1);
				Editable e2 = editText2.getText();
				Config.namePlayer2 = e2.toString();
				break;
			default:
				break;
			}
			Intent myIntent2 = new Intent(
					NameAndChoseFirstMoveActivity.this.getBaseContext(),
					MainGameActivity.class);
			startActivityForResult(myIntent2, 3);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onAccelerometerChanged(AccelerometerData pAccelerometerData) {
		// TODO Auto-generated method stub

	}

	private TableLayout createTableLayout(int[] cor,View view) {
		TableLayout tableLayout = new TableLayout(this);
		tableLayout.setVerticalGravity(Gravity.BOTTOM);
		tableLayout.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		tableLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		tableLayout.setPadding(cor[0], cor[1], cor[2], cor[3]);
		tableLayout.addView(view);
		return tableLayout;
	}

	private EditText createEditText(boolean isSingleLine,int id,String hint,String text,int height,int color) {
		EditText editText = new EditText(this);
		editText.setSingleLine(isSingleLine);
		editText.setId(id);
		editText.setHint(hint);
		editText.setText(text);
		editText.setHeight(height);
		editText.setBackgroundColor(color);
		return editText;
	}
	private TextView createText(String text){
		TextView textView = new TextView(this);
		textView.setText(text);
		
		return textView;
	}
	
}
