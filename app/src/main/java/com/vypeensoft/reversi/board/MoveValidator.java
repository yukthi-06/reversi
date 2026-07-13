package com.vypeensoft.reversi.board;
import java.util.ArrayList;
import java.util.List;
public class MoveValidator {
    private static final int[][] DIRS = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
    public static List<int[]> getFlippedPieces(Board board, int r, int c, int player) {
        List<int[]> flipped = new ArrayList<>();
        if(board.get(r, c) != Board.EMPTY) return flipped;
        int opp = player == Board.BLACK ? Board.WHITE : Board.BLACK;
        for(int[] d : DIRS) {
            int nr = r + d[0], nc = c + d[1];
            List<int[]> temp = new ArrayList<>();
            while(nr >= 0 && nr < 8 && nc >= 0 && nc < 8 && board.get(nr, nc) == opp) {
                temp.add(new int[]{nr, nc});
                nr += d[0]; nc += d[1];
            }
            if(nr >= 0 && nr < 8 && nc >= 0 && nc < 8 && board.get(nr, nc) == player) {
                flipped.addAll(temp);
            }
        }
        return flipped;
    }
    public static boolean hasLegalMove(Board board, int player) {
        for(int r=0; r<8; r++) {
            for(int c=0; c<8; c++) {
                if(getFlippedPieces(board, r, c, player).size() > 0) return true;
            }
        }
        return false;
    }
}
