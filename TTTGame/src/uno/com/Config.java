package uno.com;

import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;

public class Config {
	public static byte modePlayers ;
	public static byte modeLevel;
	public static byte modeSound ;
	public static byte firstMove ;
	public static byte yourSign ;
	
	public static String namePlayer1 ;
	public static String namePlayer2 ;
	
	public static byte scorePlayer1 ;
	public static byte scorePlayer2 ;
	// Radio coordinates
	public static float xP1Radio,yP1Radio;
	public static float xP2Radio,yP2Radio;
	public static float xL33Radio,yL33Radio;
	public static float xL66Radio,yL66Radio;
	public static float xSOnRadio,ySOnRadio;
	public static float xSOffRadio,ySOffRadio;
	public static float xSignXRadio,ySignXRadio;
	public static float xSignORadio,ySignORadio;
	public static float xMoveXRadio,yMoveXRadio;
	public static float xMoveORadio,yMoveORadio;
	//Button coodinates
	public static float xQuitButton,yQuitButton;
	public static float xNextButton,yNextButton;
	public static float xBack1Button,yBack1Button;
	public static float xBack2Button,yBack2Button;
	public static float xPlayButton,yPlayButton;
	public static float xNewButton,yNewButton;
	//Load coordinates
	public static float xLoad,yLoad;
	//SubBG coordinates
	public static float xSubBg,ySubBg;
	//EditText coordinates
	public static int[] nameCorP1For1P;
	public static int[] nameCorP1For2P;
	public static int[] nameCorP2;
	//size edit text
	public static int heightText;
	//coordinates and size font text
	public static float xText1,yText1;
	public static float xText2,yText2;
	public static float sizeText;
	//size main board
	public static int sizeBoard33;
	public static int sizeBoard66;
	public static int sizeCell33;
	public static int sizeCell66;
	//size x,o sprite
	public static int sXWidth33;
	public static int sXHeight33;
	public static int sOHeight33;
	public static int sOWidth33;
	public static int sXWidth66;
	public static int sXHeight66;
	public static int sOHeight66;
	public static int sOWidth66;
	
	//Timehandler
	public static float timePass;
	//Time wait
	public static byte timeWait;
	public static void config(){
		modePlayers = Mode.ONE_PLAYER;
		modeLevel = Mode.LEVEL33;
		modeSound = Mode.SOUND_ON;
		firstMove = Mode.FIRST_X_MOVE;
		yourSign = Mode.X_SIGN;
		namePlayer1 = "Player 1";
		namePlayer2 = "Player 2";
		scorePlayer1 = 0;
		scorePlayer2 = 0;
	}
	public static void config(int w,int h){
		modePlayers = Mode.ONE_PLAYER;
		modeLevel = Mode.LEVEL33;
		modeSound = Mode.SOUND_ON;
		firstMove = Mode.FIRST_X_MOVE;
		yourSign = Mode.X_SIGN;
		namePlayer1 = "Player 1";
		namePlayer2 = "Player 2";
		scorePlayer1 = 0;
		scorePlayer2 = 0;
		switch (w) {
		case 240:
			switch (h) {
			case 320:
				break;
			case 400:
				break;
			case 432:
				break;
			}
			break;
		case 320:
			xP1Radio = 152.5f;
			yP1Radio = 191.5f;
			xP2Radio = 230.5f;
			yP2Radio = 191.5f;
			xL33Radio = 152.5f;
			yL33Radio = 251.5f;
			xL66Radio = 230.5f;
			yL66Radio = 251.5f;
			xSOnRadio = 152.5f;
			ySOnRadio = 311.5f;
			xSOffRadio = 230.5f;
			ySOffRadio = 311.5f;
			xSignXRadio = 159.5f;
			ySignXRadio = 190.5f;
			xSignORadio = 237.5f;
			ySignORadio = 190.5f;
			xMoveXRadio = 159.5f;
			yMoveXRadio = 252.5f;
			xMoveORadio = 237.5f;
			yMoveORadio = 252.5f;
			
			xQuitButton = 0.5f;
			yQuitButton = 400f;
			xNextButton = 156.50002f;
			yNextButton = 406f;
			xBack1Button = 0f;
			yBack1Button = 368f;
			xPlayButton = 159f;
			yPlayButton = 396.5f;
			xBack2Button = 0.5f;
			yBack2Button = 383f;
			xNewButton = 161f;
			yNewButton = 395f;
			
			xLoad = 218f;
			yLoad = 382f;
			
			xSubBg = 0;
			ySubBg = 132;
			
			heightText = 40;
			nameCorP1For1P = new int[]{108, 142, 40, h - 142 - heightText};
			nameCorP1For2P = new int[]{146, 131, 33, h - 131 - heightText};
			nameCorP2 = new int[]{146, 192, 33, h - 192 - heightText};
			xText1 = 40;
			yText1 = 20;
			xText2 = 200;
			yText2 = 20;
			sizeText = 16;
			
			sizeBoard33 = 243;
			sizeCell33 = 78;
			sizeBoard66 = 267;
			sizeCell66 = 46;
			
			sXWidth33 = 49;
			sXHeight33 = 54;
			sOWidth33 = 55;
			sOHeight33 = 52;
			sXWidth66 = 30;
			sXHeight66 = 35;
			sOWidth66 = 35;
			sOHeight66 = 32;
			
			timePass = 0.1f;
			
			timeWait = 25;
			break;
		case 480:
			switch (h) {
			case 800:
				break;
			case 854:
				xP1Radio = 225.0f;
				yP1Radio = 366.0573f;
				xP2Radio = 342.0f;
				yP2Radio = 366.0573f;
				xL33Radio = 225.0f;
				yL33Radio = 447.86548f;
				xL66Radio = 342.0f;
				yL66Radio = 447.86548f;
				xSOnRadio = 225.0f;
				ySOnRadio = 530.6714f;
				xSOffRadio = 342.0f;
				ySOffRadio = 530.6714f;
				xSignXRadio = 238.0f;
				ySignXRadio = 369.0503f;
				xSignORadio = 356.0f;
				ySignORadio = 369.0503f;
				xMoveXRadio = 238.0f;
				yMoveXRadio = 448.86316f;
				xMoveORadio = 356.0f;
				yMoveORadio = 448.86316f;
				
				xQuitButton = 0.5f;
				yQuitButton = 635.75494f;
				xNextButton = 244.5f;
				yNextButton = 658.248f;
				xBack1Button = 0.5f;
				yBack1Button = 614.719f;
				xPlayButton = 250.5f;
				yPlayButton = 658.7643f;
				xBack2Button = 1.0f;
				yBack2Button = 645.32275f;
				xNewButton = 260.0f;
				yNewButton = 651.2994f;
				
				xLoad = 320f;
				yLoad = 698.8185f;
				
				xSubBg = 0;
				ySubBg = 283;
				
				heightText = 60;
				nameCorP1For1P = new int[]{163, 298, 60, h - 298 - heightText};
				nameCorP1For2P = new int[]{220, 291, 50, h - 291 - heightText};
				nameCorP2 = new int[]{220, 374, 50, h - 374 - heightText};
				xText1 = 60;
				yText1 = 30;
				xText2 = 300;
				yText2 = 30;
				sizeText = 24;
				
				sizeBoard33 = 330;
				sizeCell33 = 118;
				sizeBoard66 = 402;
				sizeCell66 = 68;
				
				sXWidth33 = 66;
				sXHeight33 = 76;
				sOWidth33 = 84;
				sOHeight33 = 78;
				sXWidth66 = 42;
				sXHeight66 = 46;
				sOWidth66 = 49;
				sOHeight66 = 48;
				
				timePass = 0.2f;
				timeWait = 15;
				
				break;
			}
			break;
		}
	}
	
}
