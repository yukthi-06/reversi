package com.vypeensoft.reversi.view;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.vypeensoft.reversi.board.Board;
public class BoardView extends View {
    private Board board;
    private Paint paintGrid, paintBlack, paintWhite, paintBg;
    private int cellSize;
    private OnCellClickListener listener;
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintGrid = new Paint(); paintGrid.setColor(Color.BLACK); paintGrid.setStrokeWidth(3);
        paintBlack = new Paint(); paintBlack.setColor(Color.BLACK); paintBlack.setAntiAlias(true);
        paintWhite = new Paint(); paintWhite.setColor(Color.WHITE); paintWhite.setAntiAlias(true);
        paintBg = new Paint(); paintBg.setColor(Color.parseColor("#1B5E20")); // Green
    }
    public void setBoard(Board board) { this.board = board; invalidate(); }
    public void setOnCellClickListener(OnCellClickListener listener) { this.listener = listener; }
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth(), h = getHeight();
        int size = Math.min(w, h);
        cellSize = size / 8;
        canvas.drawRect(0, 0, size, size, paintBg);
        for(int i=0; i<=8; i++) {
            canvas.drawLine(0, i*cellSize, size, i*cellSize, paintGrid);
            canvas.drawLine(i*cellSize, 0, i*cellSize, size, paintGrid);
        }
        if(board == null) return;
        for(int r=0; r<8; r++) {
            for(int c=0; c<8; c++) {
                int p = board.get(r, c);
                if(p != Board.EMPTY) {
                    float cx = c * cellSize + cellSize / 2f;
                    float cy = r * cellSize + cellSize / 2f;
                    canvas.drawCircle(cx, cy, cellSize * 0.4f, p == Board.BLACK ? paintBlack : paintWhite);
                }
            }
        }
    }
    @Override public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            int c = (int)(event.getX() / cellSize);
            int r = (int)(event.getY() / cellSize);
            if(r >= 0 && r < 8 && c >= 0 && c < 8 && listener != null) {
                listener.onCellClick(r, c);
            }
            return true;
        }
        return super.onTouchEvent(event);
    }
    public interface OnCellClickListener { void onCellClick(int r, int c); }
}
