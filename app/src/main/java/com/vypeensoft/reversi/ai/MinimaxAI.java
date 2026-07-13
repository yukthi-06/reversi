package com.vypeensoft.reversi.ai;
import com.vypeensoft.reversi.board.Board;
import com.vypeensoft.reversi.board.MoveValidator;
import java.util.ArrayList;
import java.util.List;
public class MinimaxAI implements AIPlayer {
    private int maxDepth = 5;
    public void setDepth(int depth) { this.maxDepth = depth; }
    @Override public int[] getMove(Board board, int player) {
        int[] bestMove = null; int bestVal = Integer.MIN_VALUE;
        for(int r=0; r<8; r++) {
            for(int c=0; c<8; c++) {
                List<int[]> flipped = MoveValidator.getFlippedPieces(board, r, c, player);
                if(flipped.size() > 0) {
                    Board clone = new Board();
                    for(int i=0; i<8; i++) for(int j=0; j<8; j++) clone.set(i, j, board.get(i, j));
                    clone.set(r, c, player);
                    for(int[] f : flipped) clone.set(f[0], f[1], player);
                    int opp = player == Board.BLACK ? Board.WHITE : Board.BLACK;
                    int val = minimax(clone, maxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, player, opp);
                    if(val > bestVal || bestMove == null) {
                        bestVal = val; bestMove = new int[]{r, c};
                    }
                }
            }
        }
        return bestMove;
    }
    private int minimax(Board board, int depth, int alpha, int beta, boolean isMax, int p, int opp) {
        if(depth == 0) return evaluate(board, p, opp);
        int currentPlayer = isMax ? p : opp;
        boolean hasMove = MoveValidator.hasLegalMove(board, currentPlayer);
        if(!hasMove) {
            boolean oppHasMove = MoveValidator.hasLegalMove(board, isMax ? opp : p);
            if(!oppHasMove) return evaluate(board, p, opp) * 1000;
            return minimax(board, depth - 1, alpha, beta, !isMax, p, opp);
        }
        int bestVal = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for(int r=0; r<8; r++) {
            for(int c=0; c<8; c++) {
                List<int[]> flipped = MoveValidator.getFlippedPieces(board, r, c, currentPlayer);
                if(flipped.size() > 0) {
                    Board clone = new Board();
                    for(int i=0; i<8; i++) for(int j=0; j<8; j++) clone.set(i, j, board.get(i, j));
                    clone.set(r, c, currentPlayer);
                    for(int[] f : flipped) clone.set(f[0], f[1], currentPlayer);
                    int val = minimax(clone, depth - 1, alpha, beta, !isMax, p, opp);
                    if(isMax) {
                        bestVal = Math.max(bestVal, val);
                        alpha = Math.max(alpha, bestVal);
                    } else {
                        bestVal = Math.min(bestVal, val);
                        beta = Math.min(beta, bestVal);
                    }
                    if(beta <= alpha) break;
                }
            }
        }
        return bestVal;
    }
    private int evaluate(Board b, int p, int opp) {
        int score = 0;
        for(int r=0; r<8; r++) {
            for(int c=0; c<8; c++) {
                if(b.get(r,c) == p) score += getWeight(r, c);
                else if(b.get(r,c) == opp) score -= getWeight(r, c);
            }
        }
        return score;
    }
    private int getWeight(int r, int c) {
        if((r==0||r==7)&&(c==0||c==7)) return 100;
        if((r==0||r==7)&&(c==1||c==6)) return -20;
        if((r==1||r==6)&&(c==0||c==7)) return -20;
        if((r==1||r==6)&&(c==1||c==6)) return -50;
        if(r==0||r==7||c==0||c==7) return 10;
        return 1;
    }
}
