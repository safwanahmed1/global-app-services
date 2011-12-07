package uno.com;

import java.util.ArrayList;

import android.util.Log;

public class Box {
	public static final byte LEFT = 0;
	public static final byte RIGHT = 1;
	public static final byte UP = 2;
	public static final byte DOWN = 3;
	public static final byte CROSS_UP_LEFT = 4;
	public static final byte CROSS_UP_RIGHT = 5;
	public static final byte CROSS_DOWN_RIGHT = 6;
	public static final byte CROSS_DOWN_LEFT = 7;

	public static final byte BORDER_LEFT = 0;
	public static final byte BORDER_RIGHT = 1;
	public static final byte BORDER_UP = 2;
	public static final byte BORDER_DOWN = 3;
	public static final byte BORDER_LEFT_UP_CORNER = 4;
	public static final byte BORDER_LEFT_DOWN_CORNER = 5;
	public static final byte BORDER_RIGHT_UP_CORNER = 6;
	public static final byte BORDER_RIGHT_DOWN_CORNER = 7;

	public static final byte LIKE = 1;
	public static final byte UNLIKE = 2;
	public static final byte UNCHECK = 0;

	public byte checkedLeft, checkedRight, checkedUp, checkedDown;
	public byte checkedLeftCrossUp, checkedRightCrossUp;
	public byte checkedLeftCrossDown, checkedRightCrossDown;
	public byte index;
	public byte symbol;
	public byte sign;
	public Board board;
	public byte way;
	public boolean isInBorder;
	public byte border = -1;
	public byte size;
	public byte winCondition;
	public ArrayList<Box> listBox;
	public ArrayList<Box> listLikeBox;

	public Box(byte index, byte symbol,byte sign, Board board) {
		this.index = index;
		this.symbol = symbol;
		this.sign = sign;
		this.board = board;
		this.listBox = board.listBox;
		this.listLikeBox = new ArrayList<Box>();
		this.winCondition = board.winCondition;
		size = Config.modeLevel;
	}

	public void setIndex(byte index) {
		this.index = index;
	}

	public void setSymbol(byte symbol) {
		this.symbol = symbol;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public byte getSymbolRightIndex() {
		byte symbol = 0;
		return symbol;
	}

	public void setChecked(Box box, byte way, byte check) {
		switch (way) {
		case UP:
			box.checkedUp = check;
			break;
		case DOWN:
			box.checkedDown = check;
			break;
		case LEFT:
			box.checkedLeft = check;
			break;
		case RIGHT:
			box.checkedRight = check;
			break;
		case CROSS_DOWN_LEFT:
			box.checkedLeftCrossDown = check;
			break;
		case CROSS_DOWN_RIGHT:
			box.checkedRightCrossDown = check;
			break;
		case CROSS_UP_LEFT:
			box.checkedLeftCrossUp = check;
			break;
		case CROSS_UP_RIGHT:
			box.checkedRightCrossUp = check;
			break;
		default:
			break;
		}
	}

	public byte checkIsBorder() {
		// byte border
		if (index == 0) {
			isInBorder = true;
			border = BORDER_LEFT_UP_CORNER;
			
		}
		if (index == size - 1) {
			isInBorder = true;
			border = BORDER_RIGHT_UP_CORNER;
		}
		if (index == (size * size) - 1) {
			isInBorder = true;
			border = BORDER_RIGHT_DOWN_CORNER;
		}
		if (index == (size - 1) * size) {
			isInBorder = true;
			border = BORDER_LEFT_DOWN_CORNER;
		}
		if (index > 0 && index < size - 1) {
			isInBorder = true;
			border = BORDER_UP;
		}
		if (index % size == 0) {
			if (index != 0 && index != (size - 1) * size) {
				isInBorder = true;
				border = BORDER_LEFT;
			}
		}
		if (index % size == size - 1) {
			if (index != size - 1 && index != size * size - 1) {
				isInBorder = true;
				border = BORDER_RIGHT;
			}
		}
		if (index / size == size - 1) {
			if (index != (size - 1) * size && index != size * size - 1) {
				isInBorder = true;
				border = BORDER_DOWN;
			}
		}
		return border;
	}

	public boolean checkLike(Box n) {
		if (this.symbol == n.symbol) {
			return true;
		} else {
			return false;

		}
	}

	public byte checkCross1() {
		byte result = 0;
		byte end = 1;
		byte distance = 1;
		if (checkedLeftCrossUp == UNCHECK) {
			while (end < winCondition) {
				Box boxUpLeft = getBox(CROSS_UP_LEFT, distance);
				if (boxUpLeft != null) {
					if (checkLike(boxUpLeft)) {
						setChecked(boxUpLeft, CROSS_DOWN_RIGHT, LIKE);
						end++;
						distance++;
						listLikeBox.add(boxUpLeft);
					} else {
						setChecked(boxUpLeft, CROSS_DOWN_RIGHT, UNLIKE);
						distance = 1;
						break;
					}
				} else {
					distance = 1;
					break;
				}
			}
		}
		if (end < winCondition) {
			if (checkedRightCrossDown == UNCHECK)
				while (end < winCondition) {
					Box boxDownRight = getBox(CROSS_DOWN_RIGHT, distance);
					if (boxDownRight != null) {
						if (checkLike(boxDownRight)) {
							setChecked(boxDownRight, CROSS_UP_LEFT, LIKE);
							end++;
							distance++;
							listLikeBox.add(boxDownRight);
						} else {
							setChecked(boxDownRight, CROSS_UP_LEFT, UNLIKE);
							distance = 1;
							break;
						}
					} else {
						distance = 1;
						break;
					}
				}
		}
//		Log.d("cross1", ""+end);
		if(end == winCondition){
			result = symbol;
			board.listScoreBox = listLikeBox;
		} else {
			resetLikeBox();
			result = Board.NO_CONDITION;
		}
		return result;
	}
	public byte checkCross2(){
		byte result = 0;
		byte end = 1;
		byte distance = 1;
		if (checkedLeftCrossDown == UNCHECK) {
			while (end < winCondition) {
				Box boxDownLeft = getBox(CROSS_DOWN_LEFT, distance);
				if (boxDownLeft != null) {
					if (checkLike(boxDownLeft)) {
						setChecked(boxDownLeft, CROSS_UP_RIGHT, LIKE);
						end++;
						distance++;
						listLikeBox.add(boxDownLeft);
					} else {
						setChecked(boxDownLeft, CROSS_UP_RIGHT, UNLIKE);
						distance = 1;
						break;
					}
				} else {
					distance = 1;
					break;
				}
			}
		}
		if (end < winCondition) {
			if (checkedRightCrossUp == UNCHECK)
				while (end < winCondition) {
					Box boxUpRight = getBox(CROSS_UP_RIGHT, distance);
					if (boxUpRight != null) {
						if (checkLike(boxUpRight)) {
							setChecked(boxUpRight, CROSS_DOWN_LEFT, LIKE);
							end++;
							distance++;
							listLikeBox.add(boxUpRight);
						} else {
							setChecked(boxUpRight, CROSS_DOWN_LEFT, UNLIKE);
							distance = 1;
							break;
						}
					} else {
						distance = 1;
						break;
					}
				}
		}
//		Log.d("cross2", ""+end);
		if(end == winCondition){
			result = symbol;
			board.listScoreBox = listLikeBox;
		} else {
			result = Board.NO_CONDITION;
			resetLikeBox();
		}
		return result;
	}
	public byte checkRow(){
		byte result = 0;
		byte end = 1;
		byte distance = 1;
		if (checkedLeft == UNCHECK) {
			while (end < winCondition) {
//				Log.d("check Left", "sdf");
				Box boxLeft = getBox(LEFT, distance);
				if (boxLeft != null) {
					if (checkLike(boxLeft)) {
						setChecked(boxLeft, RIGHT, LIKE);
						end++;
						distance++;
						listLikeBox.add(boxLeft);
					} else {
						setChecked(boxLeft, RIGHT, UNLIKE);
						distance = 1;
						break;
					}
				} else {
					distance = 1;
					break;
				}
			}
		}
		if (end < winCondition) {
			if (checkedRight == UNCHECK)
				while (end < winCondition) {
//					Log.d("check Right", "sdfsdf");
					Box boxRight = getBox(RIGHT, distance);
					if (boxRight != null) {
						if (checkLike(boxRight)) {
							setChecked(boxRight, LEFT, LIKE);
							end++;
							distance++;
							listLikeBox.add(boxRight);
						} else {
							setChecked(boxRight, LEFT, UNLIKE);
							distance = 1;
							break;
						}
					} else {
						distance = 1;
						break;
					}
				}
		}
//		Log.d("row", ""+end);
		if(end == winCondition){
			result = symbol;
			board.listScoreBox = listLikeBox;
		} else {
			result = Board.NO_CONDITION;
			resetLikeBox();
		}
		return result;
	}
	public byte checkColumn(){
		byte result = 0;
		byte end = 1;
		byte distance = 1;
		if (checkedUp == UNCHECK) {
			while (end < winCondition) {
				Box boxUp = getBox(UP, distance);
				if (boxUp != null) {
					if (checkLike(boxUp)) {
						setChecked(boxUp, DOWN, LIKE);
						end++;
						distance++;
						listLikeBox.add(boxUp);
					} else {
						setChecked(boxUp, DOWN, UNLIKE);
						distance = 1;
						break;
					}
				} else {
					distance = 1;
					break;
				}
			}
		}
		if (end < winCondition) {
			if (checkedDown == UNCHECK)
				while (end < winCondition) {
					Box boxDown = getBox(DOWN, distance);
					if (boxDown != null) {
						if (checkLike(boxDown)) {
							setChecked(boxDown, UP, LIKE);
							end++;
							distance++;
							listLikeBox.add(boxDown);
						} else {
							setChecked(boxDown, UP, UNLIKE);
							distance = 1;
							break;
						}
					} else {
						distance = 1;
						break;
					}
				}
		}
//		Log.d("column ", ""+end);
		if(end == winCondition){
			result = symbol;
			board.listScoreBox = listLikeBox;
		} else {
			result = Board.NO_CONDITION;
			resetLikeBox();
		}
		return result;
	}

	public byte checkWay(byte way) {
		byte result = 0;
		switch (way) {
		case UP:

			break;
		case DOWN:
			break;
		case LEFT:
			break;
		case RIGHT:
			break;
		case CROSS_DOWN_LEFT:
			break;
		case CROSS_DOWN_RIGHT:
			break;
		case CROSS_UP_LEFT:
			break;
		case CROSS_UP_RIGHT:
			break;
		default:
			break;
		}
		return result;
	}

	public Box getBox(byte way, byte distance) {
		switch (way) {
		case UP:
			if (checkIsBorder() == BORDER_UP) {
				return null;
			} else {
				for (int i = 0; i < listBox.size(); i++) {
					Box boxUp = listBox.get(i);
					if (boxUp.index != index) {
						if (boxUp.index == index - (size * distance)
								&& boxUp.checkIsBox()) {
							return boxUp;
						}
					}
				}
			}
			break;
		case DOWN:
			if (checkIsBorder() == BORDER_DOWN) {
				return null;
			} else {
				for (int i = 0; i < listBox.size(); i++) {
					Box boxDown = listBox.get(i);
					if (boxDown.index != index) {
						if (boxDown.index == index + size * distance
								&& boxDown.checkIsBox()) {
							return boxDown;
						}
					}
				}
			}
			break;
		case LEFT:
			if (checkIsBorder() == BORDER_LEFT) {
//				Log.d("Leftnull", "null");
				return null;
			} else {
//				Log.d("check left", "sdf");
				if(index % Config.modeLevel >= distance)
				for (int i = 0; i < listBox.size(); i++) {
					Box boxLeft = listBox.get(i);
					if (boxLeft.index != index) {
						if (boxLeft.index == index - distance  
								&& boxLeft.checkIsBox()) {
							return boxLeft;
						}
					}
				}
			}
			break;
		case RIGHT:
			if (checkIsBorder() == BORDER_RIGHT) {
				return null;
			} else {
//				Log.d("check Right", "sdfsdf");
				if(Config.modeLevel - 1 -index % Config.modeLevel >= distance)
				for (int i = 0; i < listBox.size(); i++) {
					Box boxRight = listBox.get(i);
					if (boxRight.index != index) {
						if (boxRight.index == index + distance
								&& boxRight.checkIsBox()) {
							return boxRight;
						}
					}
				}
			}
			break;
		case CROSS_DOWN_LEFT:
			byte check = checkIsBorder();
			if (check == BORDER_DOWN || check == BORDER_LEFT
					|| check == BORDER_LEFT_DOWN_CORNER) {
				return null;
			} else {
				if(index % Config.modeLevel >= distance)
				for (int i = 0; i < listBox.size(); i++) {
					Box boxDownLeft = listBox.get(i);
					if (boxDownLeft.index != index) {
						if (boxDownLeft.index == index - distance + size * distance
								&& boxDownLeft.checkIsBox()) {
//							Log.d("distance", ""+distance);
							return boxDownLeft;
						}
					}
				}
			}
			break;
		case CROSS_DOWN_RIGHT:
			byte check1 = checkIsBorder();
			if (check1 == BORDER_DOWN || check1 == BORDER_RIGHT
					|| check1 == BORDER_RIGHT_DOWN_CORNER) {
				return null;
			} else {
				if(Config.modeLevel - 1 -index % Config.modeLevel >= distance)
				for (int i = 0; i < listBox.size(); i++) {
					Box boxDownRight = listBox.get(i);
					if (boxDownRight.index != index) {
						if (boxDownRight.index == index + distance + size * distance
								&& boxDownRight.checkIsBox()) {
//							Log.d("distance", ""+distance);
							return boxDownRight;
						}
					}
				}
			}
			break;
		case CROSS_UP_LEFT:
			byte check2 = checkIsBorder();
			if (check2 == BORDER_UP || check2 == BORDER_LEFT
					|| check2 == BORDER_LEFT_UP_CORNER) {
				return null;
			} else {
				if(index % Config.modeLevel >= distance)
				for (int i = 0; i < listBox.size(); i++) {
					Box boxUpLeft = listBox.get(i);
					if (boxUpLeft.index != index) {
						if (boxUpLeft.index == index - distance - size * distance
								&& boxUpLeft.checkIsBox()) {
//							Log.d("distance", ""+distance);
							return boxUpLeft;
						}
					}
				}
			}
			break;
		case CROSS_UP_RIGHT:
			byte check3 = checkIsBorder();
			if (check3 == BORDER_UP || check3 == BORDER_RIGHT
					|| check3 == BORDER_RIGHT_UP_CORNER) {
				return null;
			} else {
				if(Config.modeLevel - 1 -index % Config.modeLevel >= distance)
				for (int i = 0; i < listBox.size(); i++) {
					Box boxUpRight = listBox.get(i);
					if (boxUpRight.index != index) {
						if (boxUpRight.index == index + distance - size * distance
								&& boxUpRight.checkIsBox()) {
							return boxUpRight;
						}
					}
				}
			}
			break;
		default:
			break;
		}
		return null;
	}

	public boolean checkIsBox() {
		if (index >= 0 && index < size * size) {
			return true;
		} else {
			return false;
		}
	}

	public byte checkResult() {
		byte result = 0;
		if(checkedLeft == UNCHECK || checkedRight == UNCHECK){
			result = checkRow();
		}
		if(result == 0){
			if(checkedUp == UNCHECK || checkedDown == UNCHECK){
				result = checkColumn();
			}
		}
		if(result == 0){
			if(checkedLeftCrossUp == UNCHECK || checkedRightCrossDown == UNCHECK){
				result = checkCross1();
			}
		}
		if(result == 0){
			if(checkedLeftCrossDown == UNCHECK || checkedRightCrossUp == UNCHECK){
				result = checkCross2();
			}
		}
		return result;
	}
	public void clearCheck(){
		checkedLeft = UNCHECK;
		checkedRight = UNCHECK;
		checkedDown = UNCHECK;
		checkedUp = UNCHECK;
		checkedLeftCrossDown = UNCHECK;
		checkedLeftCrossUp = UNCHECK;
		checkedRightCrossDown = UNCHECK;
		checkedRightCrossUp = UNCHECK;
	}
	public void resetLikeBox(){
		this.listLikeBox.clear();
		this.listLikeBox.add(this);
	}
}
