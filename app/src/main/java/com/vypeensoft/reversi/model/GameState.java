package com.vypeensoft.reversi.model;
import java.util.ArrayList;
import java.util.List;
public class GameState {
    public int[][] board;
    public int currentPlayer; // 1 for Black, 2 for White
    public int scoreBlack;
    public int scoreWhite;
    public List<Move> history = new ArrayList<>();
    public String difficulty = "MEDIUM";
    public String gameMode = "HVSAI";
    public long elapsedTime = 0;
    public GameState() { board = new int[8][8]; }
}
