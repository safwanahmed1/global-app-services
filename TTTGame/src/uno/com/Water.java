package uno.com;

import java.util.ArrayList;

public class Water {

    Cell cell1;
    Cell cell2;
    Cell cell3;
    Cell cell4;

    public Water(Cell cell1, Cell cell2, Cell cell3, Cell cell4) {
        this.cell1 = cell1;
        this.cell2 = cell2;
        this.cell3 = cell3;
        this.cell4 = cell4;
    }

    boolean isWater() {
        if (cell1.isCell() && cell2.isCell() && cell3.isCell() && cell4.isCell()) {
            return true;
        } else {
            return false;
        }
    }

    boolean equal(Water otherWater) {
        boolean result = true;
        if (cell1.hang == otherWater.cell1.hang && cell1.cot == otherWater.cell1.cot) {
            result &= true;
        } else {
            result &= false;
        }
        if (cell2.hang == otherWater.cell2.hang && cell2.cot == otherWater.cell2.cot) {
            result &= true;
        } else {
            result &= false;
        }
        if (cell3.hang == otherWater.cell3.hang && cell3.cot == otherWater.cell3.cot) {
            result &= true;
        } else {
            result &= false;
        }
        if (cell4.hang == otherWater.cell4.hang && cell4.cot == otherWater.cell4.cot) {
            result &= true;
        } else {
            result &= false;
        }
        return result;
    }

    public boolean isIn(ArrayList<Water> al) {
        boolean result = false;
        int i = 0;
        if (al.isEmpty()) {
            result = false;
        } else {
            while (!result && i < al.size()) {
                if (equal(al.get(i))) {
                    result = true;
                }
                i++;
            }
        }
        return result;
    }

    public int rateNormalDefend(int[][] a, int x) {
        int r = 10;
        if (cell1.isCell()) {
            if (a[cell1.hang][cell1.cot] == x) {
                if (cell2.isCell()) {
                    if (a[cell2.hang][cell2.cot] == 0) {
                        r = 25;
                    } else if (a[cell2.hang][cell2.cot] == x) {
                        r = 200;
                        if (cell3.isCell()) {
                            if (a[cell3.hang][cell3.cot] == 0) {
                                r += 800;
                            } else if (a[cell3.hang][cell3.cot] == x) {
                                r = 1500;
                                if (!cell4.isCell()) {
                                    r += 500;
                                }
                            }
                        } else {
                            r += 80;
                        }
                    }
                } else {
                    r += 40;
                }
            }
        }
        return r;
    }

    public int rateNormalAttack(int[][] a, int x) {
        int r = 10;
        if (cell1.isCell()) {
            if (a[cell1.hang][cell1.cot] == x) {
                if (cell2.isCell()) {
                    if (a[cell2.hang][cell2.cot] == 0) {
                        r = 25;
                    } else if (a[cell2.hang][cell2.cot] == x) {
                        r = 30;
                        if (cell3.isCell()) {
                            if (a[cell3.hang][cell3.cot] == 0) {
                                r = 80;
                            } else if (a[cell3.hang][cell3.cot] == x) {
                                r = 2000;
                                if (!cell4.isCell()) {
                                    r += 100;
                                }
                            }
                        }
                    }
                }
            }
        }
        return r + 10;
    }

    public int rateSpecialDefend(int[][] a, int x) {
        int r = 10;
        if (cell1.isCell()) {
            if (a[cell1.hang][cell1.cot] == x) {
                if (cell3.isCell()) {
                    if (a[cell3.hang][cell3.cot] == 0) {
                        r = 25;
                    } else if (a[cell3.hang][cell3.cot] == x) {
                        r = 30;
                        if (cell4.isCell()) {
                            if (a[cell4.hang][cell4.cot] == 0) {
                                r = 80;
                            } else if (a[cell3.hang][cell3.cot] == x) {
                                r = 500;
                            }
                        } else {
                            r += 80;
                        }
                    }
                } else {
                    r += 40;
                }
            }
        }
        return r;
    }

    public int rateSpecialAttack(int[][] a, int x) {
        int r = 10;
        if (cell1.isCell()) {
            if (a[cell1.hang][cell1.cot] == x) {
                if (cell3.isCell()) {
                    if (a[cell3.hang][cell3.cot] == 0) {
                        r = 25;
                    } else if (a[cell3.hang][cell3.cot] == x) {
                        r = 30;
                        if (cell4.isCell()) {
                            if (a[cell4.hang][cell4.cot] == 0) {
                                r = 80;
                            } else if (a[cell4.hang][cell4.cot] == x) {
                                r = 500;
                            }
                        }
                    }
                }
            } else if (a[cell1.hang][cell1.cot] == 0) {
                if (cell3.isCell()) {
                    if (a[cell3.hang][cell3.cot] == 0) {
                        r += 10;
                    } else if (a[cell3.hang][cell3.cot] == x) {
                        r += 15;
                        if (cell4.isCell()) {
                            if (a[cell4.hang][cell4.cot] == 0) {
                                r += 30;
                            } else if (a[cell4.hang][cell4.cot] == x) {
                                r += 300;
                            }
                        }
                    }
                }
            }
        }
        return r;
    }
}
