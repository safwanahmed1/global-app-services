package uno.com;

public class Inteligence33 {

	public int nodeCount;
	public Node rootNode;
	public final int MAX_NODE = 10;

	public Inteligence33() {
	}

	public Inteligence33(Board b) {
		Board newB = new Board(b);
		rootNode = new Node(newB);

	}

	public int Move(int moveCount, Board board) {
		int result = 0;
		int u = (int) (Math.random() * 100) / 10;
//		System.out.println(" " + u);
		if (moveCount <= 2) {// thuc hien danh random
			// while (true) {
			// int posRandom=(int) (Math.random()*10);
			// if (posRandom==9) posRandom=8;
			// if (rootNode.board.setMove(posRandom)==true) {
			// System.out.println("Da thuc hien danh random vao vi tri"
			// +posRandom);
			// result=posRandom;
			// break;
			// }
			// }
			return randomMove(moveCount, board);

		} else {
			int max = -10;
			Node bestNode = new Node();
			for (int x = 0; x < rootNode.board.intBoard.length; x++) {
				Node n = new Node();
				n.copy(rootNode);
				if (n.board.setMove(x) == true) {
					rootNode.AddChildren(n);
					n.moveBox = x;
					int val = minimaxAB(n, true, 100, -10, 10);
					if (val >= max) {
						max = val;
						bestNode = n;

					}
				}
			}
			result = bestNode.moveBox;
		}
//		System.out.println("node Count " + nodeCount);
		return result;
	}

	private int minimaxAB(Node node, boolean min, int depth, int alpha, int beta) {
		if (boardPoint(node) != -2) { // || depth == 0) {
			// if (depth <= 0) {
			// System.out.print("Depth Reached");
			// }

			node.point = boardPoint(node);
			// node.Print();
			// System.out.println(nodeCount++ + " node.point: " + node.point +
			// " Alpha: " + alpha + " Beta: " + beta);

			return boardPoint(node);
		} else {
			if (min == true) {

				// if (nodeCount < MAX_NODE) {
				for (int x = 0; x < rootNode.board.intBoard.length; x++) {
					nodeCount++;
					Node n = new Node();
					n.copy(node);
					if (n.board.setMove(x) == true) {
						node.AddChildren(n);
						n.moveBox = x;
						// System.out.println("In min:"+ min);
						// if (nodeCount < MAX_NODE) {
						int val = minimaxAB(n, false, depth - 1, alpha, beta);

						if (val < beta) {
							beta = val;
							n.parent.point = val;
						}
						nodeCount++;
						// } else {
						// return beta;
						// }

					}
				}
				// } else {
				// return alpha;
				// }
				// System.out.println("Out min:"+ min);
				return beta;
			}

			if (min == false) {

				// if (nodeCount < MAX_NODE){
				for (int x = 0; x < rootNode.board.intBoard.length; x++) {
					nodeCount++;
					Node n = new Node();
					n.copy(node);
					if (n.board.setMove(x) == true) {
						node.AddChildren(n);
						n.moveBox = x;
						// System.out.println("In min:"+ min);
						// if (nodeCount < MAX_NODE) {
						int val = minimaxAB(n, true, depth - 1, alpha, beta);

						if (val > alpha) {
							alpha = val;
							n.parent.point = val;
						}
						nodeCount++;
						// } else {
						// return alpha;
						// }

					}

				}
				// } else {
				// return beta;
				// }
				// System.out.println("Out min:"+ min);
				return alpha;

			}

		}
		return -100;

	}

	private int boardPoint(Node n) {
		if (n.board.checkCondition() == 1) {
			return -1;
		}
		if (n.board.checkCondition() == 2) {
			return 1;
		}
		if (n.board.checkCondition() == -1) {
			return 0;
		}
		return -2;

	}

	private int randomMove(int moveCount, Board board) {
		int move = 0;
		if (moveCount == 0) {
			int x = Config.modeLevel / 2;
			int y = x;
			move = y * Config.modeLevel + x;
		} else if (moveCount == 1) {
			int index = -1;
			for (int i = 0; i < (Config.modeLevel * Config.modeLevel); i++) {
				if (board.intBoard[i] != 0) {
					index = i;
					break;
				}
			}
			if (Config.modeLevel == Mode.LEVEL33) {
				if (index != 4) {
					return 4;
				}
			}
			int r = (int) ((Math.random() * 100) % 4);
			if (r == 0) {
				move = getMoveRandom(index, 0);
				if (move < 0 || move >= Config.modeLevel * Config.modeLevel) {
					move = getMoveRandom(index, 3);
				}
			} else if (r == 1) {
				move = getMoveRandom(index, 1);
				if (move < 0 || move >= Config.modeLevel * Config.modeLevel) {
					move = getMoveRandom(index, 2);
				}
			} else if (r == 2) {
				move = getMoveRandom(index, 2);
				System.out.println("move " + move);
				if (move < 0 || move >= Config.modeLevel * Config.modeLevel) {
					move = getMoveRandom(index, 1);
				}
			} else if (r == 3) {
				move = getMoveRandom(index, 3);
				if (move < 0 || move >= Config.modeLevel * Config.modeLevel) {
					move = getMoveRandom(index, 0);
				}
			}
		} else if (moveCount == 2) {
			int index = -1;
			for (int i = 0; i < (Config.modeLevel * Config.modeLevel); i++) {
				if (board.intBoard[i] == 1) {
					index = i;
					break;
				}
			}
			int next = (int) ((Math.random() * 100) % 2);
			switch (index) {
			case 0:
				if(next == 0){
					return 1;
				} else {
					return 3;
				}
			case 1:
				if(next == 0){
					return 0;
				} else {
					return 3;
				}

			case 2:
				if(next == 0){
					return 1;
				} else {
					return 5;
				}
			case 3:
				if(next == 0){
					return 0;
				} else {
					return 6;
				}
			case 5:
				if(next == 0){
					return 2;
				} else {
					return 8;
				}
			case 6:
				if(next == 0){
					return 3;
				} else {
					return 7;
				}
			case 7:
				if(next == 0){
					return 6;
				} else {
					return 8;
				}
			case 8:
				if(next == 0){
					return 5;
				} else {
					return 7;
				}
			default:
				break;
			}
		}
		return move;
	}

	private int getMoveRandom(int index, int p) {
		int m = -1;
		switch (p) {
		case 0:
			m = index - Config.modeLevel - 1;
			break;
		case 1:
			m = index - Config.modeLevel + 1;
			break;
		case 2:
			m = index + Config.modeLevel - 1;
			break;
		case 3:
			m = index + Config.modeLevel + 1;
			break;
		default:
			break;
		}
		return m;
	}
}
