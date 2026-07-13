package com.vypeensoft.reversi.ai;
import com.vypeensoft.reversi.board.Board;
import com.vypeensoft.reversi.board.MoveValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class RandomAI implements AIPlayer {
    @Override public int[] getMove(Board board, int player) {
        List<int[]> valid = new ArrayList<>();
        for(int r=0; r<8; r++) {
            for(int c=0; c<8; c++) {
                if(MoveValidator.getFlippedPieces(board, r, c, player).size() > 0) valid.add(new int[]{r, c});
            }
        }
        if(valid.isEmpty()) return null;
        return valid.get(new Random().nextInt(valid.size()));
    }
}
