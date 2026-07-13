package com.vypeensoft.reversi.activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.vypeensoft.reversi.R;
import com.vypeensoft.reversi.board.Board;
import com.vypeensoft.reversi.game.GameManager;
import com.vypeensoft.reversi.storage.JsonStorage;
import com.vypeensoft.reversi.view.BoardView;
import java.util.List;
public class GameActivity extends AppCompatActivity implements GameManager.GameListener {
    private GameManager gameManager;
    private BoardView boardView;
    private TextView scoreText;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        boardView = findViewById(R.id.boardView);
        scoreText = findViewById(R.id.scoreText);
        Button btnUndo = findViewById(R.id.btnUndo);
        Button btnRestart = findViewById(R.id.btnRestart);
        
        boolean load = getIntent().getBooleanExtra("load", false);
        gameManager = new GameManager(this, load ? JsonStorage.loadGame() : null);
        boardView.setBoard(gameManager.getBoard());
        
        boardView.setOnCellClickListener((r, c) -> gameManager.playMove(r, c));
        btnUndo.setOnClickListener(v -> gameManager.undo());
        btnRestart.setOnClickListener(v -> {
            gameManager = new GameManager(this, null);
            boardView.setBoard(gameManager.getBoard());
            boardView.invalidate();
        });
    }
    @Override protected void onPause() {
        super.onPause();
        JsonStorage.saveGame(gameManager.getState());
    }
    @Override public void onMovePlayed(int r, int c, List<int[]> flipped) { boardView.invalidate(); }
    @Override public void onScoreUpdated(int black, int white, int turn) {
        scoreText.setText("Black: " + black + " | White: " + white + " (" + (turn == Board.BLACK ? "Black" : "White") + "'s turn)");
    }
    @Override public void onGameOver() {
        scoreText.setText("Game Over! " + (gameManager.getState().scoreBlack > gameManager.getState().scoreWhite ? "Black" : "White") + " wins!");
    }
    @Override public void onBoardUpdated() { boardView.invalidate(); }
}
