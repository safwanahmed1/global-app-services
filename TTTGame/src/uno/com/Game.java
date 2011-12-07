package uno.com;

public class Game {
	public Board gBoard = new Board(Board.intFirstMove);
	public Player player1;
	public Player player2;
	public int Player1Score;
	public int Player2Score;
	public boolean twoPlayer;
	public int gameCount;
	public int outOff;

	public Game(Player player1, Player player2, Board board) {
		this.gBoard = board;
		this.player1 = player1;
		this.player2 = player2;
		this.twoPlayer = false;
	}
	//Anh hieu
//abgsajdhsjdjklsjdl
//	hoangnt
//	kjklsdjklsjflksdjflksdjlfkjsdlkfjsdlkjflskdjfsdlkjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj
//	lại thay đổi nữa
	// public Game(String player1Name,String player2Name){
	// gBoard =new Board();
	// this.player1= new Player(player1Name);
	// this.player2= new Player(player2Name);
	// this.twoPlayer=true;
	// }
	public void NewBoard() {
		gBoard = new Board(Board.intFirstMove);
		gameCount++;
	}

	/*
	 * false tra ve 3 neu true thi tra ve condition
	 */
	public int move(int box) {
		if (gBoard.setMove(box) == false)
			return 3;
		else {
			int r = gBoard.checkCondition();
			System.out.println("condition "+r);
			return r;
		}
	}

	// public String PlayerName(int playerNo){
	// if (playerNo==1)
	// return player1.playerName;
	// return player2.playerName;
	// }
	public void Win(int playerNo) {
		if (playerNo == 1)
			this.Player1Score++;
		else
			this.Player2Score++;

	}

	public int getScore(int playerNumber) {
		if (playerNumber == 1)
			return this.Player1Score;
		else
			return this.Player2Score;

	}

	public void Reset() {
		this.Player1Score = 0;
		this.Player2Score = 0;
		this.gameCount = 0;

	}

	public boolean endGame() {
		if (outOff == gameCount) {
			return false;
		}
		return true;

	}

}
