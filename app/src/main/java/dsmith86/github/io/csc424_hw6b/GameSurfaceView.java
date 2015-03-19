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
    private String word;
    private int counter, correctWords;
    private ArrayList<String> correct;
    private StringBuilder sb;

    private ArrayList<String> words;
    private String first, last;
    private Tile firstTile, lastTile;


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
        word = "NULL";

        correct = new ArrayList<String>();
        words = new ArrayList<String>();
        words.add("STINGRAY");
        words.add("STING");
        words.add("SQUIRREL");
        words.add("STAR");
        words.add("SNAKE");
        words.add("RIGOR");
        words.add("SULK");
        words.add("RAP");
        words.add("NEED");
        words.add("CURL");

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

        Paint counterPaint = new Paint();
        counterPaint.setTextSize(50);
        counterPaint.setColor(Color.BLACK);

        GridItem<Character>[][] grid = gameStateInterface.getGrid();
        tiles = new Tile[8][8];

        canvas.drawText("Number of Correct Words: " + correctWords, 100, 50, counterPaint);

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

                        tiles[i][j] = new Tile(Character.toString(grid[i][j].getData()), rect, j, i);


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
        sb = new StringBuilder();

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            for (Tile[] top : tiles){
                for(Tile t : top) {
                    try {
                        if (t.containsTouch(x, y)) {
                            firstTile = t;
                            first = t.getLetter();
                            if (t.getTouched() == false) {
                                t.setTouched(true);
                                counter++;
                            }
                            if (last == null)
                                lastTile = t;
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
                            if (t.getTouched() == false) {
                                t.setTouched(true);
                                counter++;
                            }
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
                            lastTile = t;
                            last = t.getLetter();
                            if (t.getTouched() == false) {
                                t.setTouched(true);
                                counter++;
                            }
                            Log.d("FT-X", Integer.toString(firstTile.getX()));
                            Log.d("FT-Y", Integer.toString(firstTile.getY()));
                            Log.d("LT-X", Integer.toString(lastTile.getX()));
                            Log.d("LT-Y", Integer.toString(lastTile.getY()));
                            Log.d("Counter", Integer.toString(counter));




                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            if (lastTile.getY() == firstTile.getY()){

                for (int i = firstTile.getX(); i < counter + firstTile.getX(); i ++) {
                    Log.d("Letters Collected", tiles[firstTile.getY()][i].getLetter());
                    sb.append(tiles[firstTile.getY()][i].getLetter());
                    }

                for (Tile[] top : tiles){
                    for (Tile t : top){
                        t.setTouched(false);
                    }
                }
                counter = 0;
                word = sb.toString();
                Log.d("The Word Selected", word);
                compareWords(word);
                }

            if (lastTile.getX() == firstTile.getX()){
                for (int i = firstTile.getY(); i < counter + firstTile.getY(); i++) {
                    Log.d("Letters Collected", tiles[i][firstTile.getX()].getLetter());
                    sb.append(tiles[i][firstTile.getX()].getLetter());
                }

                for (Tile[] top : tiles){
                    for (Tile t : top){
                        t.setTouched(false);
                    }
                }
                counter = 0;
                word = sb.toString();
                Log.d("The Word Selected", word);
                compareWords(word);
            }

            else if(firstTile.getX() + counter > lastTile.getX()){
                Log.d("Haha", "We got here");

                for (int i = firstTile.getX(); i <= lastTile.getX(); i++) {
                    Log.d("Letters Collected", tiles[i][i].getLetter());
                    sb.append(tiles[i][i].getLetter());

                }

                for (Tile[] top : tiles){
                    for (Tile t : top){
                        t.setTouched(false);
                    }
                }
                counter = 0;
                word = sb.toString();
                Log.d("The Word Selected", word);
                compareWords(word);

            }
        }



        Log.d("Things", Integer.toString(correctWords));
        for (String w : correct){
            Log.d("Words", w);
        }
        return true;


    }

    public interface GameStateInterface {
        GridItem<Character>[][] getGrid();
    }

    public void compareWords(String inWord){
        for (String w : words){
            if(w.equals(inWord) && !correct.contains(inWord)){
                correctWords +=1;
                correct.add(inWord);
                //words.remove(inWord);
                if (surfaceHolder.getSurface().isValid() && gameStateInterface != null) {
                    Canvas canvas = surfaceHolder.lockCanvas();

                    drawBackground(canvas);
                    drawGrid(canvas);

                    surfaceHolder.unlockCanvasAndPost(canvas);
                    shouldDrawSurfaceView = false;
                }
            }
        }
    }


}
