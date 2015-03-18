package dsmith86.github.io.csc424_hw6b;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Daniel on 3/17/2015.
 */
public class Game implements GameSurfaceView.GameStateInterface {

    private static final int ROW_COUNT = 3;
    private static final int COL_COUNT = 3;

    private GridItem<Pair<Integer, Integer>>[][] grid;

    private GameSurfaceView gameSurfaceView;
    private Context context;

    public Game(Context context, GameSurfaceView gameSurfaceView) {
        this.gameSurfaceView = gameSurfaceView;
        this.context = context;
    }

    public void onPause() {
        gameSurfaceView.onPause();
        gameSurfaceView.setGameStateInterface(null);
    }

    public void onResume() {
        gameSurfaceView.onResume();
        gameSurfaceView.setGameStateInterface(this);

        init();
    }

    private void init() {
        grid = new GridItem[ROW_COUNT][COL_COUNT];

        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COL_COUNT; j++) {
                grid[i][j] = new GridItem<>(new Pair<>(i, j));

                if (i > 0) {
                    grid[i][j].setUp(grid[i - 1][j]);
                }
                if (j > 0) {
                    grid[i][j].setLeft(grid[i][j - 1]);
                }
            }
        }
    }
}
