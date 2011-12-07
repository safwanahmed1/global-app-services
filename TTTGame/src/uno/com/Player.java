package uno.com;

import android.util.Log;

public class Player {
	public static byte TIME_WAIT = Config.timeWait;
	public String name;
	public byte symbol;
	public int playerWin;
	public int playerLoss;
	public byte score;
	public byte sign;
	public boolean isFirstMove;
	public boolean isActive;
	public boolean isMove;
	public boolean isAI;
	public byte animateStatus;
	public byte indexMove;
	public byte timeWait;

	public Player(String playerName, byte symbol, byte sign,
			boolean isFirstMove, byte score, boolean isAI) {
		this.name = playerName;
		this.score = score;
		this.sign = sign;
		this.isFirstMove = isFirstMove;
		this.isActive = isFirstMove;
		this.symbol = symbol;
		this.isAI = isAI;
	}

	public Player(String playerName, byte symbol, byte score, byte sign,
			boolean isAI) {
		this.name = playerName;
		this.score = score;
		this.sign = sign;
		this.symbol = symbol;
		this.isAI = isAI;
	}

	public Player(byte symbol) {

	}

	public void PlayerWins() {
		playerWin++;
	}

	public void PlayerLoses() {
		playerLoss++;
	}

	public int PlayerWinCount() {
		return playerWin;
	}

	public int PlayerLossCount() {
		return playerLoss;
	}

	public void setScore(byte score) {
		this.score = score;
	}

	public void setSign(byte sign) {
		this.sign = sign;
	}

	public void setSymbol(byte symbol) {
		this.symbol = symbol;
	}

	public void setIsFirstMove(boolean isFirstMove) {
		this.isFirstMove = isFirstMove;
		this.isActive = isFirstMove;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void changeStatusSprite() {
		if (animateStatus >= 0 && animateStatus < 2) {
			animateStatus++;
		} else if (animateStatus == 2) {
			animateStatus = 0;
		}
	}

	public byte getAnimateStatus() {
		return animateStatus;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}

	public void move(byte x) {
		this.indexMove = x;
	}

	// public int nextMove(Board board) {
	// // if (board.moveCount >= 2) {
	// Inteligence intel = new Inteligence(board);
	// this.indexMove = (byte) intel.Move(board.moveCount);
	// return this.indexMove;
	// // } else {
	// //
	// // for (int i = 0; i < board.intBoard.length; i++) {
	// // if (board.intBoard[i] == 0) {
	// // this.indexMove = (byte) i;
	// // Log.d("random", ""+i);
	// // return i;
	// // }
	// // }
	// // return -1;
	// // }
	// }
	public int NextMove(Board gBoard) {
		if (Config.modeLevel == Mode.LEVEL33) {
			Inteligence33 intel = new Inteligence33(gBoard);
			return intel.Move(gBoard.moveCount, gBoard);
		} else {
			Inteligence66 inteligence66 = new Inteligence66(gBoard);
			return inteligence66.move();
		}
	}
}
