package dsmith86.github.io.csc424_hw6b;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Daniel on 3/17/2015.
 */
public class GameSurfaceView extends SurfaceView implements Runnable{
    private Thread thread;
    private SurfaceHolder surfaceHolder;
    private Boolean shouldDrawSurfaceView;
    private GameStateInterface gameStateInterface;
    private Tile[][] tiles;

    String first, last;

    public GameSurfaceView(Context context) {
        super(context);
        init();
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setGameStateInterface(GameStateInterface gameStateInterface) {
        this.gameStateInterface = gameStateInterface;
    }

    private void init() {
        shouldDrawSurfaceView = false;

        surfaceHolder = getHolder();

    }

    @Override
    public void run() {
        while (shouldDrawSurfaceView) {
            if (surfaceHolder.getSurface().isValid() && gameStateInterface != null) {
                Canvas canvas = surfaceHolder.lockCanvas();

                drawBackground(canvas);
                drawGrid(canvas);

                surfaceHolder.unlockCanvasAndPost(canvas);
                shouldDrawSurfaceView = false;
            }
        }
    }

    public void onPause() {
        shouldDrawSurfaceView = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

         thread = null;
    }

    public void onResume() {
        shouldDrawSurfaceView = true;

        thread = new Thread(this);
        thread.start();
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.argb(255, 80, 124, 0));
    }

    private void drawGrid(Canvas canvas) {
        Paint rectPaint = new Paint();
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setColor(Color.WHITE);

        Paint textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.BLACK);

        GridItem<Character>[][] grid = gameStateInterface.getGrid();
        tiles = new Tile[8][8];

        int rowCount = grid.length;
        int gridItemSize = (getWidth() - 100) / rowCount;
        textPaint.setTextSize(200/rowCount);
        int yStart = getHeight()/2 - (int)(rowCount/2.f * gridItemSize);

        for (int i = 0; i < rowCount; i++) {
            int colCount = grid[i].length;
            int xStart = getWidth()/2 - (int)(colCount/2.f * gridItemSize);

            for (int j = 0; j < colCount; j++) {
                Rect rect = new Rect(
                        j * gridItemSize + xStart + 2,
                        i * gridItemSize + yStart + 2,
                        j * gridItemSize + gridItemSize + xStart - 2,
                        i * gridItemSize + gridItemSize + yStart - 2);

                        tiles[i][j] = new Tile(Character.toString(grid[i][j].getData()), rect);


                canvas.drawRect(rect, rectPaint);

                canvas.drawText(Character.toString(grid[i][j].getData()),
                        (rect.left + rect.right)/2,
                        (rect.top + rect.bottom)/2 - (textPaint.descent() + textPaint.ascent()/2),
                        textPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){



        int x = (int) e.getX();
        int y = (int) e.getY();




        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            for (Tile[] top : tiles){
                for(Tile t : top) {
                    try {
                        if (t.containsTouch(x, y)) {
                            first = t.getLetter();

                            if (last == null)
                                last = first;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            for (Tile[] top : tiles){
                for(Tile t : top) {
                    try {
                        if (t.containsTouch(x, y)) {
                            last = t.getLetter();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        if (e.getAction() == MotionEvent.ACTION_UP) {
            for (Tile[] top : tiles){
                for(Tile t : top) {
                    try {
                        if (t.containsTouch(x, y)) {
                            last = t.getLetter();

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }



       // if(e.getAction() == MotionEvent.ACTION_UP){
      //      last = Character.toString(grid[x][y].getData());
      //  }

        Log.d("Things", first);
        Log.d("More Things", last);
        return true;


    }

    public interface GameStateInterface {
        GridItem<Character>[][] getGrid();
    }


}
