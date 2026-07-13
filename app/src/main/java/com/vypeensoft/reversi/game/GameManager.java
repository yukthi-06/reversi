package com.vypeensoft.reversi.game;
import com.vypeensoft.reversi.ai.AIPlayer;
import com.vypeensoft.reversi.ai.MinimaxAI;
import com.vypeensoft.reversi.ai.RandomAI;
import com.vypeensoft.reversi.board.Board;
import com.vypeensoft.reversi.board.MoveValidator;
import com.vypeensoft.reversi.model.GameState;
import com.vypeensoft.reversi.model.Move;
import com.vypeensoft.reversi.storage.SettingsManager;
import java.util.List;
import android.os.Handler;
import android.os.Looper;

public class GameManager {
    private Board board;
    private GameState state;
    private GameListener listener;
    private AIPlayer ai;
    private Handler handler = new Handler(Looper.getMainLooper());
    public GameManager(GameListener listener, GameState initialState) {
        this.listener = listener;
        this.board = new Board();
        if(initialState != null) {
            this.state = initialState;
            for(int r=0; r<8; r++) for(int c=0; c<8; c++) board.set(r, c, state.board[r][c]);
        } else {
            this.state = new GameState();
            this.state.currentPlayer = SettingsManager.getInstance().firstPlayer.equals("BLACK") ? Board.BLACK : Board.WHITE;
            for(int r=0; r<8; r++) for(int c=0; c<8; c++) this.state.board[r][c] = board.get(r, c);
        }
        setupAI();
        updateScores();
    }
    private void setupAI() {
        if(SettingsManager.getInstance().aiEnabled) {
            if(SettingsManager.getInstance().difficulty.equals("HARD")) ai = new MinimaxAI();
            else ai = new RandomAI(); // Medium can be added later
        }
    }
    public Board getBoard() { return board; }
    public GameState getState() { return state; }
    
    public void playMove(int r, int c) {
        if(board.get(r, c) != Board.EMPTY) return;
        List<int[]> flipped = MoveValidator.getFlippedPieces(board, r, c, state.currentPlayer);
        if(flipped.isEmpty()) return;
        
        state.history.add(new Move(r, c, state.currentPlayer, flipped));
        board.set(r, c, state.currentPlayer);
        for(int[] f : flipped) board.set(f[0], f[1], state.currentPlayer);
        syncState();
        listener.onMovePlayed(r, c, flipped);
        
        int opp = state.currentPlayer == Board.BLACK ? Board.WHITE : Board.BLACK;
        if(MoveValidator.hasLegalMove(board, opp)) {
            state.currentPlayer = opp;
            updateScores();
            checkAITurn();
        } else if(!MoveValidator.hasLegalMove(board, state.currentPlayer)) {
            updateScores();
            listener.onGameOver();
        } else {
            // Pass turn
            updateScores();
            checkAITurn();
        }
    }
    
    private void checkAITurn() {
        if(ai != null && state.currentPlayer == Board.WHITE) {
            new Thread(() -> {
                try { Thread.sleep(800); } catch(Exception ignored) {}
                int[] m = ai.getMove(board, state.currentPlayer);
                handler.post(() -> {
                    if(m != null) playMove(m[0], m[1]);
                });
            }).start();
        }
    }
    
    public void undo() {
        if(state.history.isEmpty()) return;
        Move last = state.history.remove(state.history.size()-1);
        board.set(last.row, last.col, Board.EMPTY);
        int opp = last.player == Board.BLACK ? Board.WHITE : Board.BLACK;
        for(int[] f : last.flippedPieces) board.set(f[0], f[1], opp);
        state.currentPlayer = last.player;
        syncState();
        updateScores();
        listener.onBoardUpdated();
    }
    
    private void syncState() {
        for(int r=0; r<8; r++) for(int c=0; c<8; c++) state.board[r][c] = board.get(r, c);
    }
    
    private void updateScores() {
        state.scoreBlack = 0; state.scoreWhite = 0;
        for(int r=0; r<8; r++) {
            for(int c=0; c<8; c++) {
                if(board.get(r,c) == Board.BLACK) state.scoreBlack++;
                else if(board.get(r,c) == Board.WHITE) state.scoreWhite++;
            }
        }
        listener.onScoreUpdated(state.scoreBlack, state.scoreWhite, state.currentPlayer);
    }
    
    public interface GameListener {
        void onMovePlayed(int r, int c, List<int[]> flipped);
        void onScoreUpdated(int black, int white, int turn);
        void onGameOver();
        void onBoardUpdated();
    }
}
