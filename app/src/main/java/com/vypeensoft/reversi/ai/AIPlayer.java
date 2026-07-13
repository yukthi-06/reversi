package com.vypeensoft.reversi.ai;
import com.vypeensoft.reversi.board.Board;
public interface AIPlayer {
    int[] getMove(Board board, int player);
}
