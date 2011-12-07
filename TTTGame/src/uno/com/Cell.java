package uno.com;

import java.util.ArrayList;

public class Cell {

    int hang;
    int cot;
    int max;
    public Cell(int hang, int cot) {
        this.hang = hang;
        this.cot = cot;
        if(Config.modeLevel == Mode.LEVEL33){
        	max = 3;
        } else {
        	max = 6;
        }
    }

    public boolean isCell() {
        if (hang >= 0 && hang < max && cot >= 0 && cot < max) {
            return true;
        } else {
            return false;
        }
    }

    public void show() {
        System.out.println("hang: " + hang + " , cot: " + cot);
    }

    public ArrayList<Water> genarateNormalWater() {
        ArrayList<Water> r = new ArrayList<Water>();
        r.add(new Water(new Cell(hang - 1, cot), new Cell(hang - 2, cot), new Cell(hang - 3, cot), new Cell(hang - 4, cot)));
        r.add(new Water(new Cell(hang + 1, cot), new Cell(hang + 2, cot), new Cell(hang + 3, cot), new Cell(hang + 4, cot)));

        r.add(new Water(new Cell(hang, cot - 1), new Cell(hang, cot - 2), new Cell(hang, cot - 3), new Cell(hang, cot - 4)));
        r.add(new Water(new Cell(hang, cot + 1), new Cell(hang, cot + 2), new Cell(hang, cot + 3), new Cell(hang, cot + 4)));

        r.add(new Water(new Cell(hang - 1, cot - 1), new Cell(hang - 2, cot - 2), new Cell(hang - 3, cot - 3), new Cell(hang - 4, cot - 4)));
        r.add(new Water(new Cell(hang + 1, cot + 1), new Cell(hang + 2, cot + 2), new Cell(hang + 3, cot + 3), new Cell(hang + 4, cot + 4)));

        r.add(new Water(new Cell(hang - 1, cot + 1), new Cell(hang - 2, cot + 2), new Cell(hang - 3, cot + 3), new Cell(hang - 4, cot + 4)));
        r.add(new Water(new Cell(hang + 1, cot - 1), new Cell(hang + 2, cot - 2), new Cell(hang + 3, cot - 3), new Cell(hang + 4, cot - 4)));
        return r;
    }

    public ArrayList<Water> genarateSpecialWater() {
        ArrayList<Water> r = new ArrayList<Water>();
        r.add(new Water(new Cell(hang - 1, cot - 1), new Cell(hang, cot), new Cell(hang + 1, cot + 1), new Cell(hang + 2, cot + 2)));
        r.add(new Water(new Cell(hang + 1, cot - 1), new Cell(hang, cot), new Cell(hang - 1, cot + 1), new Cell(hang - 2, cot + 2)));
        r.add(new Water(new Cell(hang - 1, cot), new Cell(hang, cot), new Cell(hang + 1, cot), new Cell(hang + 2, cot)));
        r.add(new Water(new Cell(hang + 1, cot), new Cell(hang, cot), new Cell(hang - 1, cot), new Cell(hang - 2, cot)));
        r.add(new Water(new Cell(hang - 1, cot + 1), new Cell(hang, cot), new Cell(hang + 1, cot - 1), new Cell(hang + 2, cot - 2)));
        r.add(new Water(new Cell(hang + 1, cot + 1), new Cell(hang, cot), new Cell(hang - 1, cot - 1), new Cell(hang - 2, cot - 2)));
        r.add(new Water(new Cell(hang, cot - 1), new Cell(hang, cot), new Cell(hang, cot + 1), new Cell(hang, cot + 2)));
        r.add(new Water(new Cell(hang, cot + 1), new Cell(hang, cot), new Cell(hang, cot - 1), new Cell(hang, cot - 2)));
        return r;
    }

    public int rateNormal(int[][] a, int x) {
        int r = 0;
        ArrayList<Water> list = genarateNormalWater();
        for (int i = 0; i < list.size(); i++) {
            r += list.get(i).rateNormalDefend(a, 3 - x) + list.get(i).rateNormalAttack(a, x);
        }
        return r;
    }

    public int rateSpecial(int[][] a, int x) {
        int r = 0;
        ArrayList<Water> list = genarateSpecialWater();
        for (int i = 0; i < list.size(); i++) {
            r += list.get(i).rateSpecialDefend(a, 3 - x) + list.get(i).rateSpecialAttack(a, x);
        }
        return r;
    }

    public int rate(int[][] a, int x) {
        return rateNormal(a, x) + rateSpecial(a, x);
    }

    public boolean checkWin(int[][] a, int x) {
        boolean result = false;
        ArrayList<Water> r = new ArrayList<Water>();

        r.add(new Water(new Cell(hang - 3, cot), new Cell(hang - 2, cot), new Cell(hang - 1, cot), new Cell(hang, cot)));
        r.add(new Water(new Cell(hang - 2, cot), new Cell(hang - 1, cot), new Cell(hang, cot), new Cell(hang + 1, cot)));
        r.add(new Water(new Cell(hang - 1, cot), new Cell(hang, cot), new Cell(hang + 1, cot), new Cell(hang + 2, cot)));
        r.add(new Water(new Cell(hang, cot), new Cell(hang + 1, cot), new Cell(hang + 2, cot), new Cell(hang + 3, cot)));

        r.add(new Water(new Cell(hang, cot - 3), new Cell(hang, cot - 2), new Cell(hang, cot - 1), new Cell(hang, cot)));
        r.add(new Water(new Cell(hang, cot - 2), new Cell(hang, cot - 1), new Cell(hang, cot), new Cell(hang, cot + 1)));
        r.add(new Water(new Cell(hang, cot - 1), new Cell(hang, cot), new Cell(hang, cot + 1), new Cell(hang, cot + 2)));
        r.add(new Water(new Cell(hang, cot), new Cell(hang, cot + 1), new Cell(hang, cot + 2), new Cell(hang, cot + 3)));

        r.add(new Water(new Cell(hang - 3, cot - 3), new Cell(hang - 2, cot - 2), new Cell(hang - 1, cot - 1), new Cell(hang, cot)));
        r.add(new Water(new Cell(hang - 2, cot - 2), new Cell(hang - 1, cot - 1), new Cell(hang, cot), new Cell(hang + 1, cot + 1)));
        r.add(new Water(new Cell(hang - 1, cot - 1), new Cell(hang, cot), new Cell(hang + 1, cot + 1), new Cell(hang + 2, cot + 2)));
        r.add(new Water(new Cell(hang, cot), new Cell(hang + 1, cot + 1), new Cell(hang + 2, cot + 2), new Cell(hang + 3, cot + 3)));

        r.add(new Water(new Cell(hang - 3, cot + 3), new Cell(hang - 2, cot + 2), new Cell(hang - 1, cot + 1), new Cell(hang, cot)));
        r.add(new Water(new Cell(hang - 2, cot + 2), new Cell(hang - 1, cot + 1), new Cell(hang, cot), new Cell(hang + 1, cot - 1)));
        r.add(new Water(new Cell(hang - 1, cot + 1), new Cell(hang - 0, cot), new Cell(hang + 1, cot - 1), new Cell(hang + 2, cot - 2)));
        r.add(new Water(new Cell(hang, cot), new Cell(hang + 1, cot - 1), new Cell(hang + 2, cot - 2), new Cell(hang + 3, cot - 3)));

        for (int i = r.size() - 1; i >= 0; i--) {
            if (!r.get(i).isWater()) {
                r.remove(i);
            }
        }
        int i = 0;
        while (!result && i < r.size()) {
            if (a[r.get(i).cell1.hang][r.get(i).cell1.cot] == x
                    && a[r.get(i).cell2.hang][r.get(i).cell2.cot] == x
                    && a[r.get(i).cell3.hang][r.get(i).cell3.cot] == x
                    && a[r.get(i).cell4.hang][r.get(i).cell4.cot] == x) {
                result = true;
            }
            i++;
        }
        return result;
    }
}