package uno.com;

import java.util.ArrayList;

import android.util.Log;

public class Board {
	public static final byte PLAYER1 = 1;
	public static final byte PLAYER2 = 2;
	public static final byte EMPTY = 0;
	public static final byte DRAW = -1;
	public static final byte NO_CONDITION = 0;
	public int intBoard[];
	public byte intMoveSymbol; // 0=empty;1=Player1;2=Player2
	public static byte intFirstMove;
	public int moveCount = 0;
	float x, y;
	public byte winCondition;
	public ArrayList<Box> listBox, listScoreBox;
	// Player player1, player2;
	byte signPlayer1, signPlayer2;
	ArrayList<Integer> winArr = new ArrayList<Integer>();

	public Board(byte mode, byte signPlayer1, byte signPlayer2, float pX,
			float pY) {
		switch (mode) {
		case Mode.LEVEL33:
			intBoard = new int[9];
			winCondition = 3;
			break;
		case Mode.LEVEL66:
			intBoard = new int[36];
			winCondition = 4;
			break;
		default:
			break;
		}
		// listBox = new ArrayList<Box>();
		if (intBoard != null)
			for (byte x = 0; x < intBoard.length; x++) {
				intBoard[x] = EMPTY;
			}
		this.signPlayer1 = signPlayer1;
		this.signPlayer2 = signPlayer2;
		this.x = pX;
		this.y = pY;
		intFirstMove = 1;
		intMoveSymbol = 2;
	}

	// public Board(byte mode) {
	// switch (mode) {
	// case Mode.LEVEL33:
	// intBoard = new byte[9];
	// winCondition = 3;
	// break;
	// case Mode.LEVEL66:
	// intBoard = new byte[36];
	// winCondition = 4;
	// break;
	// default:
	// break;
	// }
	//
	// listBox = new ArrayList<Box>();
	// for (byte x = 0; x < intBoard.length; x++) {
	// intBoard[x] = EMPTY;
	// }
	// }

	// public Board(byte[] intBoard) {
	// for (int x = 0; x < intBoard.length; x++) {
	// this.intBoard[x] = intBoard[x];
	// }
	// }

	public Board(int firstMove) {
		if (firstMove == PLAYER1) {
			intMoveSymbol = PLAYER2;
		} else {
			intMoveSymbol = PLAYER1;
		}
		if (Config.modeLevel == Mode.LEVEL33) {
			intBoard = new int[9];
			for (int x = 0; x <= 8; x++) {
				intBoard[x] = 0;
			}
			this.winCondition = 3;
		} else {
			intBoard = new int[36];
			for (int x = 0; x <= 35; x++) {
				intBoard[x] = 0;
			}
			this.winCondition = 4;
		}
	}

	public Board(Board board) {
		this.moveCount = board.moveCount;
		this.intMoveSymbol = board.intMoveSymbol;
		this.winCondition = board.winCondition;
		this.intBoard = new int[board.intBoard.length];
		System.arraycopy(board.intBoard, 0, this.intBoard, 0, 9);
	}

	// public Board(Board board, byte mode) {
	// this.moveCount = board.moveCount;
	// this.intMoveSymbol = board.intMoveSymbol;
	// switch (mode) {
	// case Mode.LEVEL33:
	// intBoard = new byte[9];
	// winCondition = 3;
	// break;
	// case Mode.LEVEL66:
	// intBoard = new byte[36];
	// winCondition = 4;
	// break;
	// default:
	// break;
	// }
	// listBox = new ArrayList<Box>();
	// for (int i = 0; i < board.listBox.size(); i++) {
	// Box b = board.listBox.get(i);
	// Box box = new Box(b.index, b.symbol, b.sign, b.board);
	// this.listBox.add(box);
	// }
	// for (byte x = 0; x < intBoard.length; x++) {
	// intBoard[x] = EMPTY;
	// }
	// // Log.d(""+board.intBoard, ""+this.intBoard);
	// System.arraycopy(board.intBoard, 0, this.intBoard, 0,
	// board.intBoard.length);
	// this.signPlayer1 = board.signPlayer1;
	// this.signPlayer2 = board.signPlayer2;
	// }

	public void copy(Board board) {
		this.moveCount = board.moveCount;
		this.intMoveSymbol = board.intMoveSymbol;

		System.arraycopy(board.intBoard, 0, this.intBoard, 0, 9);
		// this.listBox = board.listBox;
		// this.signPlayer1 = board.signPlayer1;
		// this.signPlayer2 = board.signPlayer2;
		// this.listBox = new ArrayList<Box>();
		// for (int i = 0; i < board.listBox.size(); i++) {
		// Box b = board.listBox.get(i);
		// Box box = new Box(b.index, b.symbol, b.sign, this);
		// this.listBox.add(box);
		// }

	}

	public void setMoveSymbol(byte moveSymbol) {
		this.intMoveSymbol = moveSymbol;
	}

	public boolean setMove(int boxNo) {
		if (intBoard[boxNo] != EMPTY) {
			return false;
		}

		// Log.d("symbol", "" + intMoveSymbol);
		// Box box = null;

		if (intMoveSymbol == PLAYER1) {
			// if (Config.modeLevel == Mode.LEVEL66)
			// box = new Box(boxNo, intMoveSymbol, signPlayer1, this);
			intMoveSymbol = PLAYER2;
		} else {
			// if (Config.modeLevel == Mode.LEVEL66)
			// box = new Box(boxNo, intMoveSymbol, signPlayer2, this);
			intMoveSymbol = PLAYER1;
		}
		intBoard[boxNo] = intMoveSymbol;
		// if (box != null)
		// listBox.add(box);
		moveCount++;
		return true;

	}

	/*
	 * [1=Player1],[2=Player2],[-1=draw],[0=no condition]
	 */
	public int checkCondition()// [1=Player1],[2=Player2],[-1=draw],[0=no
								// condition]
	{
	
		int count = 0, value = 0;
		int max = Config.modeLevel;
		// check hang ngang
		for (int j = 0; j < max; j++) {
			value = 0;
			count = 1;
			for (int i = 0; i < max - 1; i++) {
				if (intBoard[j * max + i] == intBoard[j * max + i + 1]
						&& intBoard[j * max + i] != 0) {
//					addToWinArray(j*max+i);
//					addToWinArray(j*max+i+1);
					value = intBoard[j * max + i];
					count++;
				} else {
//					winArr.clear();
					count = 1;
				}
				if (count == winCondition) {
					return value;
				}
			}
		}
		// check hang doc
		for (int j = 0; j < max; j++) {
			value = 0;
			count = 1;
			for (int i = 0; i < max - 1; i++) {

				if (intBoard[j + i * max] == intBoard[j + i * max + max]
						&& intBoard[j + i * max] != 0) {
//					addToWinArray(j+i*max);
//					addToWinArray(j+i*max+max);
					value = intBoard[j + i * max];
					count++;
				} else {
//					winArr.clear();
					count = 1;
				}
				if (count == winCondition) {
					return value;
				}
			}
		}
		// check duong cheo1
		for (int i = 0; i < max * 2 - 1; i++) {
			value = 0;
			count = 1;
			int max1 = getNumberCross(i, max);
			int startIndex = getStartIndexCross(i, max);
			int endIndex = getEndIndexCross(i, max);
			if (max1 >= winCondition) {
				for (int j = startIndex; j < endIndex; j++) {
					int in1 = getIndex1(i, j, max1, max);
					int in2 = getIndex1(i, j + 1, max1, max);
					if (intBoard[in1] == intBoard[in2] && intBoard[in1] != 0) {
//						addToWinArray(in1);
//						addToWinArray(in2);
						value = intBoard[max * (i - j) + j];
						count++;
					} else {
//						winArr.clear();
						count = 1;
					}
					if (count == winCondition) {
						return value;
					}
				}
			}
		}
		// check duong cheo 2
		for (int i = 0; i < max * 2 - 1; i++) {
			value = 0;
			count = 1;
			int max1 = getNumberCross(i, max);
			int startIndex = getStartIndexCross(i, max);
			int endIndex = getEndIndexCross(i, max);
			if (max1 >= winCondition) {
				for (int j = startIndex; j < endIndex; j++) {
					int in1 = getIndex2(i, j, max1, max);
					int in2 = getIndex2(i, j + 1, max1, max);
					if (intBoard[in1] == intBoard[in2] && intBoard[in1] != 0) {
//						addToWinArray(in1);
//						addToWinArray(in2);
						value = intBoard[in1];
						count++;
					} else {
//						winArr.clear();
						count = 1;
					}
					
					if (count == winCondition) {
						return value;
					}
				}
			}
		}
		for (int i = 0; i < intBoard.length; i++) {
			if (intBoard[i] == 0) {
				return 0;
			}
		}
		
		return -1;
	}
	public void getWinArray(){
		for(int i = 0;i<intBoard.length;i++){
			System.out.print(""+intBoard[i]);
			if(i % 3 == 2){
				System.out.println();
			}
		}
		int count = 0, value = 0;
		int max = Config.modeLevel;
		// check hang ngang
		for (int j = 0; j < max; j++) {
			value = 0;
			count = 1;
			for (int i = 0; i < max - 1; i++) {
				int x1 = j*max + i;
				int y1 = x1+1;
				winArr.clear();
				if (intBoard[j * max + i] == intBoard[j * max + i + 1]
						&& intBoard[j * max + i] != 0) {
					addToWinArray(j*max+i);
					addToWinArray(j*max+i+1);
					value = intBoard[j * max + i];
					count++;
				} else {
					winArr.clear();
					count = 1;
				}
				if (count == winCondition) {
					return ;
				}
			}
		}
		// check hang doc
		for (int j = 0; j < max; j++) {
			value = 0;
			count = 1;
			winArr.clear();
			for (int i = 0; i < max - 1; i++) {

				if (intBoard[j + i * max] == intBoard[j + i * max + max]
						&& intBoard[j + i * max] != 0) {
					addToWinArray(j+i*max);
					addToWinArray(j+i*max+max);
					value = intBoard[j + i * max];
					count++;
				} else {
					winArr.clear();
					count = 1;
				}
				if (count == winCondition) {
					return ;
				}
			}
		}
		// check duong cheo1
		for (int i = 0; i < max * 2 - 1; i++) {
			value = 0;
			count = 1;
			winArr.clear();
			int max1 = getNumberCross(i, max);
			int startIndex = getStartIndexCross(i, max);
			int endIndex = getEndIndexCross(i, max);
			if (max1 >= winCondition) {
				for (int j = startIndex; j < endIndex; j++) {
					int in1 = getIndex1(i, j, max1, max);
					int in2 = getIndex1(i, j + 1, max1, max);
					if (intBoard[in1] == intBoard[in2] && intBoard[in1] != 0) {
						addToWinArray(in1);
						addToWinArray(in2);
						value = intBoard[max * (i - j) + j];
						count++;
					} else {
						winArr.clear();
						count = 1;
					}
					if (count == winCondition) {
						return ;
					}
				}
			}
		}
		// check duong cheo 2
		for (int i = 0; i < max * 2 - 1; i++) {
			value = 0;
			count = 1;
			winArr.clear();
			int max1 = getNumberCross(i, max);
			int startIndex = getStartIndexCross(i, max);
			int endIndex = getEndIndexCross(i, max);
			if (max1 >= winCondition) {
				for (int j = startIndex; j < endIndex; j++) {
					int in1 = getIndex2(i, j, max1, max);
					int in2 = getIndex2(i, j + 1, max1, max);
					if (intBoard[in1] == intBoard[in2] && intBoard[in1] != 0) {
						addToWinArray(in1);
						addToWinArray(in2);
						value = intBoard[in1];
						count++;
					} else {
						winArr.clear();
						count = 1;
					}
					
					if (count == winCondition) {
						return ;
					}
				}
			}
		}
	}
	// public void clearBoxCheck() {
	// if (listBox != null && !listBox.isEmpty()) {
	// for (int i = 0; i < listBox.size(); i++) {
	// Box box = listBox.get(i);
	// box.clearCheck();
	// }
	// }
	// }
	public void addToWinArray(int value) {
		boolean isContain = false;
		for (Integer t : winArr) {
			if (t.intValue() == value)
				isContain = true;
		}
		if (isContain == false) {
			Integer t = new Integer(value);
			winArr.add(t);
		}

	}
	public String showWinArray() {
		  String result="";
		  for (Integer t:winArr) {
		   result+=" "+t.toString();
		  }
		  return result;
		 }
	public void clearBoard() {
		for (int i = 0; i < intBoard.length; i++) {
			intBoard[i] = EMPTY;
		}
		// listBox.clear();
		moveCount = 0;

		// if(intFirstMove == PLAYER1){
		// intMoveSymbol = PLAYER2;
		// } else {
		// intMoveSymbol = PLAYER1;
		// }
	}

	public int getNumberCross(int i, int max) {
		int number = 1;
		if (i < max) {
			number = i + 1;
		} else if (i >= max) {
			number = 2 * max - i - 1;
		}
		return number;
	}

	public int getStartIndexCross(int i, int max) {
		int startIndex = 0;
		if (i < max) {
			startIndex = 0;
		} else if (i >= max) {
			startIndex = i - max + 1;
		}
		return startIndex;
	}

	public int getEndIndexCross(int i, int max) {
		int end = 0;
		if (i < max) {
			end = i;
		} else if (i >= max) {
			end = max - 1;
		}
		return end;
	}

	public int getIndex1(int i, int j, int max1, int max) {
		int index = 0;
		if (i < max) {
			index = j + (max1 - j - 1) * max;
		} else {
			index = max * (i - j) + j;
		}
		return index;
	}

	public int getIndex2(int i, int j, int max1, int max) {
		int index = 0;
		if (i < max) {
			index = j + (max - max1 + j) * max;
		} else {
			index = j + (max - 1 - i + j) * max;
		}
		return index;
	}

	// public void Print() {
	// for (int x = 0; x < 3; x++) {
	// System.out.println();
	// for (int y = 0; y < 3; y++) {
	// System.out.print("|");
	// if (intBoard[y + (3 * x)] != 0)
	// if (intBoard[y + (3 * x)] == 1) {
	// System.out.print("[X]");
	// } else
	// System.out.print("[0]");
	// else
	// System.out.print(" " + (y + (3 * x)) + " ");
	// System.out.print("|");
	//
	// }
	// }
	// }
}
