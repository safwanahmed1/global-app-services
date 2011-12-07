package uno.com;

import java.io.IOException;
import java.util.ArrayList;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
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
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.util.Debug;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Display;

public class MainGameActivity extends BaseExample implements
		IAccelerometerListener, IOnSceneTouchListener, IOnAreaTouchListener {
	private Camera mCamera;
	public PhysicsHandler mPhysicsHandler;
	public static final byte BACK = 0;
	public static final byte NEW = 1;
	public static final byte X_MOVE = 0;
	public static final byte O_MOVE = 1;
	public static final byte WAVE_DOWN = 0;
	public static final byte WAVE_UP = 1;

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TextureRegion bgscreen3TextureRegion;
	private TextureRegion buttonNewTextureRegion;
	private TextureRegion buttonBackTextureRegion;

	private BitmapTextureAtlas waveAndBannerTA;
	private TextureRegion waveTR;
	private TextureRegion bannerTR;

	// sound
	private Sound xUpdate, oUpdate, waveSound, sunWinSound, sunFailedSound;
	// Sun wind and sun failed
	BitmapTextureAtlas sunWinTA, sunFailedTA;
	TextureRegion sunWinTR, sunFailedTR;
	int winPlayer;
	Sprite sunWin, sunFailed;

	BitmapTextureAtlas winColTA, winRowTA, winCross1TA, winCross2TA;
	TiledTextureRegion winColTR, winRowTR, winCross1TR, winCross2TR;
	Sprite wave;
	boolean isWaveDown, isWaveUp, isWaveMove, isWaveActive;
	byte wave_way;
	float y1, y2;
	boolean isPlay;

	Player player1, player2;
	Game myGame;
	Board board;
	public String endGameReport;

	private BitmapTextureAtlas mFontTexture;
	private Font mFont;
	// BitmapTextureAtlas mapTextureAtlas;
	// TiledTextureRegion mapTextureRegion;
	// AnimatedSprite map;

	TiledTextureRegion mapTR1;
	AnimatedSprite map1;
	TiledTextureRegion mapTR2;
	AnimatedSprite map2;
	TiledTextureRegion mapTR3;
	AnimatedSprite map3;

	// oMove
	BitmapTextureAtlas oMoveTA;
	TiledTextureRegion oMoveTR;
	int oAnimateStatus = 0;

	// xMove
	BitmapTextureAtlas xMoveTA;
	TiledTextureRegion xMoveTR;

	int xAnimateStatus = 0;
	byte sign, player;
	byte firstMove;
	float xBoard, yBoard;
	int size, size1;
	int sXWidth, sXHeight, sOWidth, sOHeight;
	int sWidth, sHeight;
	int result;
	int yWave;

	ChangeableText textScoreP1, textScoreP2, textScoreResult;
	Sprite buttonNew, buttonBack;
	ArrayList<AnimatedSprite> listSprite;

	public boolean isNew, isBack, isEnd;
	public boolean isUpdate, isChange, isCheckResult;
	private int w, h;
	private int timeWait = -1;
	private Scene mScene;

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
		engineOptions.setNeedsSound(true);
		return new Engine(engineOptions);
	}

	@Override
	public void onLoadResources() {
		// TODO Auto-generated method stub
		// this.mBitmapTextureAtlas = new BitmapTextureAtlas(1024, 1024,
		// TextureOptions.DEFAULT);
		// BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.setPathResource(w, h);
		this.bgscreen3TextureRegion = this.createTextureRegion("bgscreen3.jpg",
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// this.bgscreen3TextureRegion = BitmapTextureAtlasTextureRegionFactory
		// .createFromAsset(mBitmapTextureAtlas, this, "bgscreen3.jpg", 0,
		// 0);
		this.buttonNewTextureRegion = this.createTextureRegion(
				"sc3_new_on.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.buttonBackTextureRegion = this.createTextureRegion(
				"sc3_back_on.png", TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// this.buttonNewTextureRegion = BitmapTextureAtlasTextureRegionFactory
		// .createFromAsset(mBitmapTextureAtlas, this, "sc3_new_on.png",
		// this.bgscreen3TextureRegion.getWidth() + 1, 0);
		// this.buttonBackTextureRegion = BitmapTextureAtlasTextureRegionFactory
		// .createFromAsset(mBitmapTextureAtlas, this, "sc3_back_on.png",
		// this.bgscreen3TextureRegion.getWidth() + 1,
		// this.buttonNewTextureRegion.getHeight() + 1);
		this.mFontTexture = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.mFont = new Font(this.mFontTexture, Typeface.create(
				Typeface.DEFAULT, Typeface.BOLD), Config.sizeText, true,
				Color.BLACK);

		this.getFontManager().loadFont(this.mFont);
		mEngine.getTextureManager().loadTexture(mFontTexture);
		if (Config.modeLevel == Mode.LEVEL33) {
			// this.mapTextureAtlas = new BitmapTextureAtlas(2048, 256,
			// TextureOptions.DEFAULT);
			// this.mapTextureRegion = BitmapTextureAtlasTextureRegionFactory
			// .createTiledFromAsset(this.mapTextureAtlas, this,
			// "line3x3.png", 0, 0, 8, 1);
			// this.mapTextureRegion =
			// this.createTileTextureRegion("line3x3.png",
			// TextureOptions.BILINEAR_PREMULTIPLYALPHA, 8, 1);
			this.mapTR1 = this.createTileTextureRegion("line3x3_1.png",
					TextureOptions.BILINEAR_PREMULTIPLYALPHA, 2, 2);
			this.mapTR2 = this.createTileTextureRegion("line3x3_2.png",
					TextureOptions.BILINEAR_PREMULTIPLYALPHA, 2, 1);
			this.mapTR3 = this.createTileTextureRegion("line3x3_3.png",
					TextureOptions.BILINEAR_PREMULTIPLYALPHA, 2, 1);
		} else {
			// this.mapTextureAtlas = new BitmapTextureAtlas(4096, 512,
			// TextureOptions.DEFAULT);
			// this.mapTextureRegion = BitmapTextureAtlasTextureRegionFactory
			// .createTiledFromAsset(this.mapTextureAtlas, this,
			// "line6x6.png", 0, 0, 10, 1);
			// this.mapTextureRegion =
			// this.createTileTextureRegion("line6x6.png",
			// TextureOptions.BILINEAR_PREMULTIPLYALPHA, 10, 1);
			this.mapTR1 = this.createTileTextureRegion("line6x6_1.png",
					TextureOptions.BILINEAR_PREMULTIPLYALPHA, 2, 2);
			this.mapTR2 = this.createTileTextureRegion("line6x6_2.png",
					TextureOptions.BILINEAR_PREMULTIPLYALPHA, 2, 2);
			this.mapTR3 = this.createTileTextureRegion("line6x6_3.png",
					TextureOptions.BILINEAR_PREMULTIPLYALPHA, 2, 1);
		}
		// wave and banner
		// waveAndBannerTA = new BitmapTextureAtlas(512, 512,
		// TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// waveTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
		// waveAndBannerTA, this, "wave.png", 0, 0);
		this.waveTR = this.createTextureRegion("wave.png",
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.oMoveTR = this.createTileTextureRegion("o_animation.png",
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, 4, 3);
		// X and O image
		// oMoveTA = new BitmapTextureAtlas(256, 256,
		// TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// oMoveTR =
		// BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
		// oMoveTA, this, "o_animation.png", 0, 0, 4, 3);

		// xMoveTA = new BitmapTextureAtlas(256, 256,
		// TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// xMoveTR =
		// BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
		// xMoveTA, this, "x_animation.png", 0, 0, 4, 3);
		this.xMoveTR = this.createTileTextureRegion("x_animation.png",
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, 4, 3);
		// this.mEngine.getTextureManager().loadTextures(this.mBitmapTextureAtlas,
		// this.mapTextureAtlas, this.mFontTexture, oMoveTA, xMoveTA,
		// waveAndBannerTA);
		// winColTA = new BitmapTextureAtlas(32, 1024,
		// TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// winColTR =
		// BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
		// winColTA, this, "win_column.png", 0, 0, 1, 4);
		this.winColTR = this.createTileTextureRegion("win_column.png",
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, 1, 4);
		this.winRowTR = this.createTileTextureRegion("win_row.png",
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, 4, 1);
		this.winCross1TR = this.createTileTextureRegion("win_cross1.png",
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, 4, 1);
		this.winCross2TR = this.createTileTextureRegion("win_cross2.png",
				TextureOptions.BILINEAR_PREMULTIPLYALPHA, 4, 1);
		// winRowTA = new BitmapTextureAtlas(1024, 32,
		// TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// winRowTR =
		// BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
		// winRowTA, this, "win_row.png", 0, 0, 4, 1);

		// winCross1TA = new BitmapTextureAtlas(1024, 256,
		// TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// winCross1TR = BitmapTextureAtlasTextureRegionFactory
		// .createTiledFromAsset(winCross1TA, this, "win_cross1.png", 0,
		// 0, 4, 1);

		// winCross2TA = new BitmapTextureAtlas(1024, 256,
		// TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// winCross2TR = BitmapTextureAtlasTextureRegionFactory
		// .createTiledFromAsset(winCross2TA, this, "win_cross2.png", 0,
		// 0, 4, 1);
		// mEngine.getTextureManager().loadTextures(winColTA, winRowTA,
		// winCross1TA, winCross2TA);
		// /load re
		// sunFailedTA = new BitmapTextureAtlas(512, 512,
		// TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		// sunFailedTR =
		// BitmapTextureAtlasTextureRegionFactory.createFromAsset(sunFailedTA,this,"sun_failed.png",0,0);
		sunFailedTR = this.createTextureRegion("sun_failed.png",
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		sunWinTR = this.createTextureRegion("sun_win.png",
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		loadSound();
	}

	private void loadSound() {
		SoundFactory.setAssetBasePath("mfx/");
		try {
			xUpdate = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "x_updated.ogg");
			oUpdate = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "o_updated.ogg");
			waveSound = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "wave.ogg");
			sunWinSound = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "victory.ogg");
			sunFailedSound = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "failed.ogg");

		} catch (final IOException e) {
			Debug.e("Error", e);
		}

	}

	@Override
	public Scene onLoadScene() {
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mScene = new Scene();

		Sprite sprite = new Sprite(0, 0, this.bgscreen3TextureRegion);
		this.mScene.attachChild(sprite);
		listSprite = new ArrayList<AnimatedSprite>();
		final float xMap, yMap;

		if (Config.modeLevel == Mode.LEVEL33) {
			size = Config.sizeBoard33;
			size1 = Config.sizeCell33;
			xBoard = w / 2 - size / 2 - 12 + 4;
			yBoard = h / 6 - 7;
			sXWidth = Config.sXWidth33;
			sXHeight = Config.sXHeight33;
			sOWidth = Config.sOWidth33;
			sOHeight = Config.sOHeight33;
			xMap = w / 2 - size / 2 + 4;
			yMap = h / 6;
			// map.setPosition(w / 2 - map.getWidth() / 2 + 4, h / 6);
			// map.animate(500, 0);
		} else {
			size = Config.sizeBoard66;
			size1 = Config.sizeCell66;
			xBoard = w / 2 - size / 2 - 6 + 3;
			yBoard = h / 6 - 34;
			sXWidth = Config.sXWidth66;
			sXHeight = Config.sXHeight66;
			sOWidth = Config.sOWidth66;
			sOHeight = Config.sOHeight66;
			xMap = w / 2 - size / 2 + 3;
			yMap = h / 6 - 30;
			// map.setPosition(w / 2 - map.getWidth() / 2 + 3, h / 6 - 30);
			// map.animate(300, 0);
		}

		if (Config.modePlayers == Mode.ONE_PLAYER) {
			// Log.d("One ", "player");
			player1 = new Player(Config.namePlayer1, (byte) 1,
					Config.scorePlayer1, Config.yourSign, false);
			player2 = new Player(Config.namePlayer2, (byte) 2,
					Config.scorePlayer2, (byte) (1 - Config.yourSign), true);
			if (Config.firstMove == Config.yourSign) {
				player1.setIsFirstMove(true);
				player2.setIsFirstMove(false);
				sign = player1.sign;
			} else {
				player1.setIsFirstMove(false);
				player2.setIsFirstMove(true);
				sign = player2.sign;
			}
		} else {
			// Log.d("Two ", "player");
			player1 = new Player(Config.namePlayer1, (byte) 1,
					Config.scorePlayer1, Mode.X_SIGN, false);
			player2 = new Player(Config.namePlayer2, (byte) 2,
					Config.scorePlayer2, Mode.O_SIGN, false);
			if (Config.firstMove == Mode.X_SIGN) {
				player1.setIsFirstMove(true);
				player2.setIsFirstMove(false);
				sign = player1.sign;
			} else {
				player1.setIsFirstMove(false);
				player2.setIsFirstMove(true);
				sign = player2.sign;
			}
		}

		board = new Board(Config.modeLevel, player1.sign, player2.sign, xBoard,
				yBoard);
		if (Config.modePlayers == Mode.ONE_PLAYER) {
			// if (Config.modeLevel == Mode.LEVEL33){
			if (Config.firstMove == Config.yourSign) {
				board.setMoveSymbol((byte) 2);
			} else {
				board.setMoveSymbol((byte) 1);
			}
			// } else {
			// if (Config.firstMove == Mode.FIRST_X_MOVE) {
			// board.setMoveSymbol((byte) 1);
			// } else {
			// board.setMoveSymbol((byte) 2);
			// }
			// }
		} else {
			if (Config.firstMove == Mode.X_SIGN) {
				board.setMoveSymbol((byte) 2);
			} else {
				board.setMoveSymbol((byte) 1);
			}
		}
		myGame = new Game(player1, player2, board);

		buttonNew = new Sprite(-200f, 100f, this.buttonNewTextureRegion);
		buttonBack = new Sprite(-200f, 200f, this.buttonBackTextureRegion);
		mScene.attachChild(buttonBack);
		mScene.attachChild(buttonNew);

		String s1 = Config.namePlayer1 + " : " + Config.scorePlayer1;
		String s2 = Config.namePlayer2 + " : " + Config.scorePlayer2;
		textScoreP1 = new ChangeableText(Config.xText1, Config.yText1,
				this.mFont, s1, s1.length() + 2);
		textScoreP2 = new ChangeableText(Config.xText2, Config.yText2,
				this.mFont, s2, s2.length() + 2);
		textScoreResult = new ChangeableText(0, 0, this.mFont, "abcd",
				Math.max(s1.length(), s2.length()) + 5);
		mScene.attachChild(textScoreP1);
		mScene.attachChild(textScoreP2);

		wave = new Sprite(0, -this.waveTR.getHeight(), this.waveTR);
		this.mPhysicsHandler = new PhysicsHandler(wave);
		wave.registerUpdateHandler(this.mPhysicsHandler);
		// mPhysicsHandler.setVelocity(0, 100);
		// mScene.attachChild(wave);

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
				if (timeWait >= 0 && timeWait <= 10) {
					timeWait++;
				} else if (timeWait > 10) {
					timeWait = -1;
					if (isNew) {
						nextActivity(NEW);
						isNew = false;
					}
					if (isBack) {
						nextActivity(BACK);
						isBack = false;
					}
				}

				if (isWaveMove) {
					if (wave.getY() < -wave.getHeight() / 2 && !isWaveDown) {
						mPhysicsHandler.setAcceleration(0, wave.getHeight() / 2);
						isWaveDown = true;
					} else if (wave.getY() >= -wave.getHeight() / 2
							&& !isWaveUp) {
						wave.setPosition(0, -wave.getHeight() / 2);
						mPhysicsHandler.setAcceleration(0,
								-wave.getHeight() / 2);
						isWaveUp = true;

					} else if (wave.getY() <= -wave.getHeight() && isWaveDown
							&& isWaveUp) {
						isWaveMove = false;
						isWaveDown = false;
						isWaveUp = false;
						mPhysicsHandler.setAcceleration(0, 0);
						mPhysicsHandler.setVelocity(0, 0);
						isNew = false;
						mScene.detachChild(wave);
					}
					if (mPhysicsHandler.getVelocityY() < 0) {
						clearBoard();
						isEnd = false;
					}
				}
				if (myGame.player1.isActive) {
					if (myGame.player1.timeWait < Player.TIME_WAIT) {
						myGame.player1.timeWait++;
					} else {
						myGame.player1.setMove(true);
					}
				}
				if (myGame.player2.isActive) {
					if (myGame.player2.timeWait < Player.TIME_WAIT) {
						myGame.player2.timeWait++;
					} else if (myGame.player2.timeWait >= Player.TIME_WAIT) {
						if (myGame.player2.isAI && !isEnd && !isWaveMove
								&& isPlay) {

							int aiMove = myGame.player2.NextMove(myGame.gBoard);
							int x1 = (int) (aiMove % Config.modeLevel);
							int x2 = (int) (aiMove / Config.modeLevel);
							addSprite(myGame.player2.sign, x1, x2);
							int gCon = myGame.move(aiMove);
							switch (gCon) {

							case 2:
								winPlayer = 3;
								Config.scorePlayer2++;
								textScoreP2.setText(Config.namePlayer2 + " : "
										+ Config.scorePlayer2);
								endGameReport = "Too Bad!";
								myGame.gBoard.getWinArray();
								// myGame.gBoard.showWinArray();
								placeWinSign(0);
								endGame();
								break;
							case -1:
								endGameReport = "Tie!";
								placeTie();
								endGame();
								break;
							case 0:
								onMark(true);
								break;
							}

						} else
							myGame.player2.setMove(true);
					}
				}

			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub

			}
		});
		// if (Config.modeLevel == Mode.LEVEL33) {
		// map = new AnimatedSprite(xMap, yMap, mapTextureRegion);
		// map.animate(300);
		// mScene.attachChild(map);
		// } else {
		map1 = new AnimatedSprite(xMap, yMap, mapTR1);

		mScene.attachChild(map1);
		long[] dur = new long[] { 1000, 800, 600, 400 };
		map1.animate(dur, 0, new IAnimationListener() {

			@Override
			public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {
				Debug.d("End su kien 1 ");
				mScene.detachChild(map1);
				map2 = new AnimatedSprite(xMap, yMap, mapTR2);
				mScene.attachChild(map2);
				map2.animate(400, 0, new IAnimationListener() {

					@Override
					public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {
						Debug.d("End su kien 2");
						mScene.detachChild(map2);
						map3 = new AnimatedSprite(xMap, yMap, mapTR3);
						mScene.attachChild(map3);
						map3.animate(300, 0, new IAnimationListener() {

							@Override
							public void onAnimationEnd(
									AnimatedSprite pAnimatedSprite) {
								Debug.d("End su kien 2");
								isPlay = true;
							}
						});
					}
				});
			}
		});
		// }
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
		if (pSceneTouchEvent.isActionDown()) {
			float pX = pSceneTouchEvent.getX();
			float pY = pSceneTouchEvent.getY();
			if (buttonBack != null)
				if (pX >= Config.xBack2Button
						&& pX <= Config.xBack2Button + buttonBack.getWidth()) {
					if (pY >= Config.yBack2Button
							&& pY <= Config.yBack2Button
									+ buttonBack.getHeight()) {
						isBack = true;
						buttonBack.setPosition(Config.xBack2Button,
								Config.yBack2Button);
						createSpriteSpawnTimeHandler(BACK);
					}
				}
			if (buttonNew != null)
				if (pX >= Config.xNewButton
						&& pX <= Config.xNewButton + buttonNew.getWidth()) {
					if (pY >= Config.yNewButton
							&& pY <= Config.yNewButton + buttonNew.getHeight()) {
						if (!isNew) {
							isNew = true;
							buttonNew.setPosition(Config.xNewButton,
									Config.yNewButton);
							wave.setPosition(0, -wave.getHeight());
							createSpriteSpawnTimeHandler(NEW);
							if (Config.modeSound == Mode.SOUND_ON) {
								waveSound.play();
							}
							// ss
							if (sunFailed != null && sunFailed.hasParent())
								mScene.detachChild(sunFailed);
							if (sunWin != null && sunWin.hasParent())
								mScene.detachChild(sunWin);
							if (textScoreResult != null
									&& textScoreResult.hasParent()) {
								mScene.detachChild(textScoreResult);
							}
						}
					}
				}
			Log.d("player 1 ", "" + player1.isActive);
			Log.d("player 2 ", "" + player2.isActive);
			// boolean isMark = false;
			if (!isWaveMove && !isEnd && isPlay) {
				if (myGame.player1.isMove) {
					if (checkValidMove(pX, pY)) {
						byte p[] = getTilePosition(pX, pY);

						int pos = p[1] * Config.modeLevel + p[0];
						int moveCode = myGame.move(pos);
						if (moveCode != 3) {
							addSprite(myGame.player1.sign, p[0], p[1]);
						}
						switch (moveCode) {
						case 3:
							break;
						case 1:
							Config.scorePlayer1++;
							textScoreP1.setText(Config.namePlayer1 + " : "
									+ Config.scorePlayer1);
							winPlayer = 1;
							myGame.gBoard.getWinArray();
							// myGame.gBoard.showWinArray();
							if (Config.modePlayers == Mode.ONE_PLAYER) {
								endGameReport = "You Win!";
							} else {
								endGameReport = Config.namePlayer1 + " Win!";
							}
							placeWinSign(0);
							endGame();
							break;
						case -1:
							endGameReport = "Tie!";
							placeTie();
							endGame();
							break;
						case 0:
							onMark(true);
							break;
						}

					}
				} else if (myGame.player2.isMove && !myGame.player2.isAI) {
					if (checkValidMove(pX, pY)) {
						byte p[] = getTilePosition(pX, pY);

						int pos = p[1] * Config.modeLevel + p[0];
						System.out.println("pos player 2 " + pos);
						int moveCode = myGame.move(pos);
						if (moveCode != 3) {
							addSprite(myGame.player2.sign, p[0], p[1]);
						}
						switch (moveCode) {
						case 3:
							// "Nuoc di da trung");
							System.out.print("nuoc di da trung");
							break;
						case 2:
							// "Player 2 win");
							System.out.print("Player2 win");
							winPlayer = 2;
							Config.scorePlayer2++;
							textScoreP2.setText(Config.namePlayer2 + " : "
									+ Config.scorePlayer2);
							endGameReport = Config.namePlayer2 + " Win!";
							myGame.gBoard.getWinArray();
							// myGame.gBoard.showWinArray();

							placeWinSign(0);
							endGame();
							break;
						case -1:
							// "Ket qua Hoa`");
							endGameReport = "Tie!";
							placeTie();
							endGame();
							break;
						case 0:
							System.out.print("chua phan thang bai");
							onMark(true);
							break;
						}

					}
				}
			}

		}
		return true;
	}

	private boolean markOnboard(float pX, float pY) {
		if (pX >= xBoard && pX <= xBoard + size) {
			if (pY >= yBoard && pY <= yBoard + size) {
				byte[] p = getTilePosition(pX, pY);
				byte boxNo = (byte) (p[1] * Config.modeLevel + p[0]);
				Log.d("Index", "" + boxNo);
				if (board.setMove(boxNo)) {
					onMark(true);
					markOnBoard(p[0], p[1]);
					return true;
				} else {
					Log.d("danh roi", ":D");
					return false;
				}
			}
		}
		return false;
	}

	public boolean checkValidMove(float pX, float pY) {

		boolean isValid = false;
		if (pX >= xBoard && pX <= xBoard + size) {
			if (pY >= yBoard && pY <= yBoard + size) {
				// byte[] p = getTilePosition(pX, pY);
				// byte boxNo = (byte) (p[1] * Config.modeLevel + p[0]);
				// Log.d("Index", "" + boxNo);
				// if (boxNo < board.intBoard.length && board.intBoard[boxNo] ==
				// 0) {
				isValid = true;
				// }
			}
		}
		return isValid;
	}

	private byte[] getTilePosition(float pX, float pY) {
		byte[] p = new byte[2];
		p[0] = (byte) (((int) (pX - xBoard)) / size1);
		p[1] = (byte) (((int) (pY - yBoard)) / size1);
		return p;
	}

	private byte getIndex(float pX, float pY) {
		// byte[] p = new byte[2];
		int p1 = ((int) (pX - xBoard)) / size1;
		int p2 = ((int) (pY - yBoard)) / size1;
		return (byte) (p1 * Config.modeLevel + p2);
	}

	private void markOnBoard(byte pX, byte pY) {
		addSprite(sign, pX, pY);
	}

	private void markOnBoard(int index) {
		// Log.d("here", "sdfs");
		int x1 = (int) (index % Config.modeLevel);
		int x2 = (int) (index / Config.modeLevel);
		// byte boxNo = (byte) (x1 * Config.modeLevel + x2);
		// board.setMove(boxNo);
		addSprite(sign, x1, x2);
	}

	private void onMark(boolean isMark) {
		if (isMark) {
			if (myGame.player1.isActive()) {
				myGame.player1.setActive(false);
				myGame.player1.setMove(false);
				myGame.player1.timeWait = 0;
				myGame.player2.setActive(true);
				sign = myGame.player2.sign;
				// move = O_MOVE;
				// if (xAnimateStatus < 2) {
				// xAnimateStatus++;
				// } else {
				// xAnimateStatus = 0;
				// }
				myGame.player1.changeStatusSprite();
			} else {
				// move = X_MOVE;
				myGame.player1.setActive(true);
				myGame.player2.setActive(false);
				myGame.player2.setMove(false);
				myGame.player2.timeWait = 0;
				sign = myGame.player1.sign;
				// if (oAnimateStatus < 2) {
				// oAnimateStatus++;
				// } else {
				// oAnimateStatus = 0;
				// }
				myGame.player2.changeStatusSprite();
			}
			if (board.moveCount >= 1) {
				isCheckResult = true;
			}
		}
	}

	private void addSprite(byte type, int pX, int pY) {
		AnimatedSprite animatedSprite = null;
		byte status = 0;
		long dur[] = new long[] { 200, 200, 200, 200 };
		// Log.d("move ", "" + type);
		switch (type) {
		case X_MOVE:
			animatedSprite = new AnimatedSprite(xBoard + pX * size1
					+ (size1 - sXWidth) / 2, yBoard + pY * size1
					+ (size1 - sXHeight) / 2, xMoveTR.deepCopy());
			status = getPlayerMoving().getAnimateStatus();
			sWidth = sXWidth;
			sHeight = sXHeight;
			if (Config.modeSound == Mode.SOUND_ON) {
				xUpdate.play();
			}
			break;
		case O_MOVE:
			animatedSprite = new AnimatedSprite(xBoard + pX * size1
					+ (size1 - sOWidth) / 2, yBoard + pY * size1
					+ (size1 - sOHeight) / 2, oMoveTR.deepCopy());
			status = getPlayerMoving().getAnimateStatus();
			sWidth = sOWidth;
			sHeight = sOHeight;
			if (Config.modeSound == Mode.SOUND_ON) {
				oUpdate.play();
			}
			break;
		}

		if (animatedSprite != null) {
			animatedSprite.setSize(sWidth, sHeight);
			// animatedSprite.animate(dur, false, new IAnimationListener() {
			//
			// @Override
			// public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {
			// // TODO Auto-generated method stub
			//
			// }
			// });
			listSprite.add(animatedSprite);
			mScene.attachChild(animatedSprite);
			switch (status) {
			case 0:
				animatedSprite.animate(dur, 0, 3, 0);
				break;
			case 1:
				animatedSprite.animate(dur, 4, 7, 0);
				break;
			case 2:
				animatedSprite.animate(dur, 8, 11, 0);

				break;
			default:
				break;
			}
		}
	}

	public void loadMap6x6() {

	}

	public void placeWinSign(int testCode) {
		int[] tempArr = null;

		switch (testCode) {
		case 0:
			tempArr = new int[board.winArr.size()];

			for (int i = 0; i < board.winArr.size(); i++) {
				tempArr[i] = board.winArr.get(i).intValue();
			}
			break;
		case 1:// duong cheo cross 1
			tempArr = new int[3];
			tempArr[0] = 0;
			tempArr[1] = 4;
			tempArr[2] = 8;
			break;
		case 2:// duong cheo cross 2
			tempArr = new int[3];
			tempArr[0] = 2;
			tempArr[1] = 4;
			tempArr[2] = 6;
			break;
		case 3:// hang
			tempArr = new int[3];
			tempArr[0] = 6;
			tempArr[1] = 7;
			tempArr[2] = 8;
			break;
		case 4:// cot
			tempArr = new int[3];
			tempArr[0] = 2;
			tempArr[1] = 5;
			tempArr[2] = 8;
			break;

		}

		// Sap xep theo thu tu tang dan
		for (int i = 0; i < tempArr.length - 1; i++)
			for (int j = i + 1; j < tempArr.length; j++) {
				if (tempArr[j] < tempArr[i]) {
					int z = tempArr[i];
					tempArr[i] = tempArr[j];
					tempArr[j] = z;
				}
			}
		AnimatedSprite winSpr = null;
		// Chon huong trong truong tong quat
		for (int i = 0; i < tempArr.length; i++) {
			System.out.println("" + tempArr[i]);
		}
		int dx = tempArr[0] % Config.modeLevel - tempArr[1] % Config.modeLevel;
		int dy = tempArr[0] / Config.modeLevel - tempArr[1] / Config.modeLevel;
		// Xet truong hop
		if ((dx == -1) && (dy == -1)) {// cheo xuong trai -> phai, wincross 1
			if (Config.modeLevel == Mode.LEVEL33) {
				winSpr = new AnimatedSprite(xBoard + 13, yBoard + 30,
						winCross1TR);
			} else {
				winSpr = new AnimatedSprite(xBoard + (tempArr[0] % 6) * size1,
						yBoard + 14 + (tempArr[0] / 6) * size1, winCross1TR);
			}

		} else if ((dx == 1) && (dy == -1)) { // cheo xuong phai_> trai,wincross
			// 2

			if (Config.modeLevel == Mode.LEVEL33) {
				winSpr = new AnimatedSprite(xBoard + 19 + size1 / 2,
						yBoard + 30, winCross2TR);
			} else {
				int size = tempArr.length;
				winSpr = new AnimatedSprite(xBoard + (tempArr[size - 1] % 6)
						* size1 + size1 / 4, yBoard + 8 + (tempArr[0] / 6)
						* size1, winCross2TR);
			}

		}
		if ((dx == -1) && (dy == 0)) {// hang
			if (Config.modeLevel == Mode.LEVEL33) {
				winSpr = new AnimatedSprite(xBoard, yBoard
						+ ((tempArr[0] / 3) * size1) + 18 + size1 / 4, winRowTR);
			} else {
				winSpr = new AnimatedSprite(xBoard - 3 + (tempArr[0] % 6)
						* size1 - size1 / 2 + size1 / 2, yBoard
						+ ((tempArr[0] / 6) * size1) + 18, winRowTR);
				winSpr.setSize(winSpr.getWidth() * 4 / 5, winSpr.getHeight());
			}
		}
		if ((dx == 0) && (dy == -1)) { // cot
			if (Config.modeLevel == Mode.LEVEL33) {
				float v = xBoard + ((tempArr[0] % 3) * size1) + 18;
				int w1 = w;
				winSpr = new AnimatedSprite(xBoard + ((tempArr[0] % 3) * size1)
						+ 18, yBoard, winColTR);
			} else {
				winSpr = new AnimatedSprite(xBoard + ((tempArr[0] % 6) * size1)
						+ 12, yBoard + 26 + size1 / 2 + (tempArr[0] / 6)
						* size1 - size1, winColTR);
				winSpr.setSize(winSpr.getWidth(), (winSpr.getHeight() * 4) / 5);
			}
		}
		// /////////////////4
		long dur[] = new long[] { 350, 350, 350, 350 };
		// winSpr.animate(dur, 0, 3, 0);
		winSpr.animate(dur, false, new IAnimationListener() {

			@Override
			public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {
				// Debug.d("End su kien");
				if (winPlayer == 3) {
					if (sunFailed == null) {
						sunFailed = new Sprite(0, 0, sunFailedTR);
					}
					sunFailed.setPosition(w / 2 - sunFailed.getWidth() / 2,
							yBoard);
					textScoreResult.setText(endGameReport);
					float x1 = sunFailed.getX()
							+ (sunFailed.getWidth() - textScoreResult
									.getWidth()) / 2;
					float y1 = sunFailed.getY() + sunFailed.getHeight()
							- textScoreResult.getHeight() - 5;
					textScoreResult.setPosition(x1, y1);

					mScene.attachChild(sunFailed);
					mScene.attachChild(textScoreResult);
					if (Config.modeSound == Mode.SOUND_ON)
						sunFailedSound.play();
				} else {
					if (sunWin == null) {
						sunWin = new Sprite(0, 0, sunWinTR);
					}

					sunWin.setPosition(w / 2 - sunWin.getWidth() / 2, yBoard);
					textScoreResult.setText(endGameReport);
					float x1 = sunWin.getX()
							+ (sunWin.getWidth() - textScoreResult.getWidth())
							/ 2;
					float y1 = sunWin.getY() + sunWin.getHeight()
							- textScoreResult.getHeight() - 5;
					textScoreResult.setPosition(x1, y1);

					mScene.attachChild(sunWin);
					mScene.attachChild(textScoreResult);
					if (Config.modeSound == Mode.SOUND_ON)
						sunWinSound.play();
				}

			}
		});
		listSprite.add(winSpr);
		mScene.attachChild(winSpr);

	}

	public void placeTie() {
		if (sunWin == null) {
			sunWin = new Sprite(0, 0, sunWinTR);
		}

		sunWin.setPosition(w / 2 - sunWin.getWidth() / 2, yBoard);
		textScoreResult.setText(endGameReport);
		float x1 = sunWin.getX()
				+ (sunWin.getWidth() - textScoreResult.getWidth()) / 2;
		float y1 = sunWin.getY() + sunWin.getHeight()
				- textScoreResult.getHeight() - 5;
		textScoreResult.setPosition(x1, y1);

		mScene.attachChild(sunWin);
		mScene.attachChild(textScoreResult);
		if (Config.modeSound == Mode.SOUND_ON)
			sunWinSound.play();
	}

	private void clearBoard() {
		myGame.gBoard.clearBoard();
		for (int i = 0; i < listSprite.size(); i++) {
			AnimatedSprite animatedSprite = listSprite.get(i);
			mScene.detachChild(animatedSprite);
		}
		listSprite.clear();
		// mScene.detachChild(pEntity)
	}

	private void endGame() {
		isEnd = true;
		if (player1.isFirstMove) {
			myGame.player1.setIsFirstMove(false);
			myGame.player2.setIsFirstMove(true);
			myGame.player1.setActive(false);
			myGame.player1.setMove(false);
			myGame.player2.setActive(true);
			myGame.player2.setMove(true);
			Board.intFirstMove = 2;
			myGame.gBoard.setMoveSymbol(myGame.player1.symbol);
			myGame.gBoard.moveCount = 0;
			sign = myGame.player2.sign;
			myGame.player1.timeWait = 0;
			myGame.player2.timeWait = 0;
		} else {
			myGame.player1.setIsFirstMove(true);
			myGame.player2.setIsFirstMove(false);
			myGame.player1.setActive(true);
			myGame.player1.setMove(true);
			myGame.player2.setActive(false);
			myGame.player2.setMove(false);
			Board.intFirstMove = 1;
			myGame.gBoard.moveCount = 0;
			myGame.gBoard.setMoveSymbol(myGame.player2.symbol);
			sign = myGame.player1.sign;
			myGame.player1.timeWait = 0;
			myGame.player2.timeWait = 0;
		}
	}

	public Player getPlayerMoving() {
		Player player = null;
		if (myGame.player1.isActive()) {
			player = myGame.player1;
		} else {
			player = myGame.player2;
		}
		return player;
	}

	private void removeSprite(byte type) {
		switch (type) {
		case BACK:
			this.buttonBack.setPosition(-200, -100);
			break;
		case NEW:
			this.buttonNew.setPosition(-200, -100);
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
					MainGameActivity.this.getBaseContext(),
					NameAndChoseFirstMoveActivity.class);
			startActivityForResult(myIntent1, 3);
			finish();
			break;
		case NEW:
			// clearBoard();
			if (!wave.hasParent())
				mScene.attachChild(wave);
			isWaveMove = true;
			break;
		default:
			break;
		}
	}

	public void attachMap(final AnimatedSprite map1, final AnimatedSprite map2) {
		if (!map1.hasParent()) {
			mScene.attachChild(map1);
		}
		map1.animate(300, 0, new IAnimationListener() {

			@Override
			public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {

				if (map2 != null) {
					mScene.detachChild(map1);
					mScene.attachChild(map2);
				}

			}
		});
	}

	@Override
	public void onAccelerometerChanged(AccelerometerData pAccelerometerData) {
		// TODO Auto-generated method stub

	}

}
