package uno.com;

public class Inteligence66 {
	int[][] a = new int[6][6];
	public Inteligence66(Board board){
		int b[] = board.intBoard;
		for(int i = 0;i<36;i++){
			a[i%6][i/6] = b[i];
		}
	}
	public int move(){
		int move = 0;
		int hang = 0,cot = 0;
		int hGood = 0;
        int cGood = 0;
        int rGood = 0;
//		if (a[hang][cot] == 0) {
//            a[hang][cot] = 1;
            int h=0,c=0;
            for (h = 0; h < 6; h++) {
                for (c = 0; c < 6; c++) {
                    if (a[h][c] == 0) {
                        if (rGood <= new Cell(h, c).rate(a, 2)) {
                            rGood = new Cell(h, c).rate(a, 2);
                            hGood = h;
                            cGood = c;
                        }
                    }
                }
            }
            move = hGood + cGood*6;
//        }
		return move;
	}
	
}
