package com.vypeensoft.reversi.view;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.vypeensoft.reversi.board.Board;
import java.util.List;

public class BoardView extends View {
    private Board board;
    private Paint paintGrid, paintBlack, paintWhite, paintBg;
    private int cellSize;
    private OnCellClickListener listener;
    
    private List<int[]> flippingPieces;
    private float flipProgress = 0f;

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintGrid = new Paint(); paintGrid.setColor(Color.BLACK); paintGrid.setStrokeWidth(3);
        paintBlack = new Paint(); paintBlack.setColor(Color.BLACK); paintBlack.setAntiAlias(true);
        paintWhite = new Paint(); paintWhite.setColor(Color.WHITE); paintWhite.setAntiAlias(true);
        paintBg = new Paint(); paintBg.setColor(Color.parseColor("#1B5E20"));
    }
    
    public void setBoard(Board board) { this.board = board; invalidate(); }
    public void setOnCellClickListener(OnCellClickListener listener) { this.listener = listener; }
    
    public void animateFlips(List<int[]> flipped) {
        this.flippingPieces = flipped;
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(400);
        animator.addUpdateListener(anim -> {
            flipProgress = (float) anim.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

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
                    float radius = cellSize * 0.4f;
                    
                    boolean isFlipping = false;
                    if(flippingPieces != null) {
                        for(int[] f : flippingPieces) {
                            if(f[0] == r && f[1] == c) {
                                isFlipping = true; break;
                            }
                        }
                    }
                    
                    if(isFlipping) {
                        float scaleX = flipProgress < 0.5f ? (1f - (flipProgress * 2f)) : ((flipProgress - 0.5f) * 2f);
                        int drawColor = flipProgress < 0.5f ? (p == Board.BLACK ? Board.WHITE : Board.BLACK) : p;
                        
                        canvas.save();
                        canvas.scale(scaleX, 1f, cx, cy);
                        drawCoin(canvas, drawColor, cx, cy, radius);
                        canvas.restore();
                    } else {
                        drawCoin(canvas, p, cx, cy, radius);
                    }
                }
            }
        }
    }
    
    private void drawCoin(Canvas canvas, int p, float cx, float cy, float radius) {
        if(com.vypeensoft.reversi.storage.SettingsManager.getInstance().use3dCoins) {
            if(p == Board.BLACK) {
                paintBlack.setShader(new android.graphics.RadialGradient(cx - radius/3, cy - radius/3, radius, Color.parseColor("#666666"), Color.BLACK, android.graphics.Shader.TileMode.CLAMP));
                canvas.drawCircle(cx, cy, radius, paintBlack);
            } else {
                paintWhite.setShader(new android.graphics.RadialGradient(cx - radius/3, cy - radius/3, radius, Color.WHITE, Color.parseColor("#999999"), android.graphics.Shader.TileMode.CLAMP));
                canvas.drawCircle(cx, cy, radius, paintWhite);
            }
        } else {
            paintBlack.setShader(null);
            paintWhite.setShader(null);
            
            Paint paintShadow = new Paint();
            paintShadow.setColor(Color.parseColor("#44000000"));
            paintShadow.setAntiAlias(true);
            canvas.drawCircle(cx + 3, cy + 4, radius, paintShadow);
            
            if(p == Board.BLACK) {
                paintBlack.setColor(Color.parseColor("#333333"));
                canvas.drawCircle(cx, cy, radius, paintBlack);
                paintBlack.setColor(Color.BLACK);
                canvas.drawCircle(cx, cy, radius * 0.7f, paintBlack);
            } else {
                paintWhite.setColor(Color.parseColor("#DDDDDD"));
                canvas.drawCircle(cx, cy, radius, paintWhite);
                paintWhite.setColor(Color.WHITE);
                canvas.drawCircle(cx, cy, radius * 0.7f, paintWhite);
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
