package com.vypeensoft.reversi.model;
import java.util.List;
public class Move {
    public final int row;
    public final int col;
    public final List<int[]> flippedPieces;
    public final int player;
    public Move(int row, int col, int player, List<int[]> flippedPieces) {
        this.row = row; this.col = col; this.player = player; this.flippedPieces = flippedPieces;
    }
}
