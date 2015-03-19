package dsmith86.github.io.csc424_hw6b;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Daniel on 3/17/2015.
 */
public class GameSurfaceView extends SurfaceView implements Runnable{

    private final int GRID_PADDING = 2;

    private Thread thread;
    private SurfaceHolder surfaceHolder;
    private Boolean shouldDrawSurfaceView;
    private GameStateInterface gameStateInterface;
    private Handler deleteHandler;
    private boolean shouldDeleteLine = false;

    private Point firstTouch = null;
    private Point mostRecentTouch = null;
    private int gridItemSize;
    private int xStart;
    private int yStart;
    private boolean selectionIsValid = false;

    public GameSurfaceView(Context context) {
        super(context);
        init();
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);

        deleteHandler = new Handler();

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
                drawSelection(canvas);

                surfaceHolder.unlockCanvasAndPost(canvas);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();


        if (gameStateInterface.getGridBounds().contains(x, y)) {
            shouldDeleteLine = false;

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                firstTouch = new Point(x, y);
                mostRecentTouch = firstTouch;

                firstTouch = snapToGrid(firstTouch);
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mostRecentTouch = new Point(x, y);
                mostRecentTouch = snapToGrid(mostRecentTouch);
            }

            selectionIsValid = gridSelectionIsValid(firstTouch, mostRecentTouch);

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (selectionIsValid) {

                } else {
                    shouldDeleteLine = true;
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (shouldDeleteLine) {
                                final Lock mutex = new ReentrantLock(true);
                                mutex.lock();

                                firstTouch = null;
                                mostRecentTouch = null;

                                mutex.unlock();
                            }
                        }
                    };

                    deleteHandler.postDelayed(runnable, 300);

                }
            }
            return true;
        }

        return false;
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.argb(255, 44, 62, 80));
    }

    private void drawGrid(Canvas canvas) {
        Paint rectPaint = new Paint();
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setColor(Color.WHITE);

        Paint textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.BLACK);

        GridItem<Character>[][] grid = gameStateInterface.getGrid();

        Rect gridBounds = new Rect();

        int rowCount = grid.length;
        gridItemSize = (getWidth() - 100) / rowCount;
        textPaint.setTextSize(300/rowCount);
        yStart = getHeight()/2 - (int)(rowCount/2.f * gridItemSize);

        gridBounds.top = yStart + GRID_PADDING;
        gridBounds.bottom = yStart + rowCount * gridItemSize - GRID_PADDING;

        int colCount = grid[0].length;
        xStart = getWidth()/2 - (int)(colCount/2.f * gridItemSize);

        gridBounds.left = xStart + GRID_PADDING;
        gridBounds.right = xStart + colCount * gridItemSize - GRID_PADDING;

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                Rect rect = new Rect(
                        j * gridItemSize + xStart + GRID_PADDING,
                        i * gridItemSize + yStart + GRID_PADDING,
                        j * gridItemSize + gridItemSize + xStart - GRID_PADDING,
                        i * gridItemSize + gridItemSize + yStart - GRID_PADDING);

                canvas.drawRect(rect, rectPaint);

                canvas.drawText(Character.toString(grid[i][j].getData()),
                        (rect.left + rect.right)/2,
                        (rect.top + rect.bottom)/2 - (textPaint.descent() + textPaint.ascent()/2),
                        textPaint);
            }
        }

        gameStateInterface.setGridBounds(gridBounds);
    }

    private void drawSelection(Canvas canvas) {
        if (firstTouch != null && mostRecentTouch != null) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);

            if (selectionIsValid) {
                paint.setColor(Color.argb(255, 44, 62, 80));
            } else {
                paint.setColor(Color.RED);
            }

            paint.setStrokeCap(Paint.Cap.ROUND);

            canvas.drawLine(firstTouch.x, firstTouch.y, mostRecentTouch.x, mostRecentTouch.y, paint);
        }
    }

    Point snapToGrid(Point point) {
        point.x = (int)Math.floor((point.x - xStart) / gridItemSize)
                * gridItemSize + xStart + gridItemSize/2;
        point.y = (int)Math.floor((point.y - yStart) / gridItemSize)
                * gridItemSize + yStart + gridItemSize/2;

        return point;
    }

    boolean gridSelectionIsValid(Point first, Point second) {
        if (first == null || second == null) {
            return false;
        }

        int deltaX = second.x - first.x;
        int deltaY = second.y - first.y;

        long angle = Math.round((float) Math.toDegrees(Math.atan2(-deltaX, deltaY)));

        return angle % 45 == 0;
    }

    public interface GameStateInterface {
        GridItem<Character>[][] getGrid();
        void setGridBounds(Rect gridBounds);
        Rect getGridBounds();
    }
}
