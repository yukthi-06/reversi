package com.vypeensoft.reversi.board;
public class Board {
    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    private int[][] grid = new int[8][8];
    public Board() { reset(); }
    public void reset() {
        for(int r=0; r<8; r++) for(int c=0; c<8; c++) grid[r][c] = EMPTY;
        grid[3][3] = WHITE; grid[3][4] = BLACK;
        grid[4][3] = BLACK; grid[4][4] = WHITE;
    }
    public int get(int r, int c) { return grid[r][c]; }
    public void set(int r, int c, int val) { grid[r][c] = val; }
    public int[][] getGrid() { return grid; }
}
