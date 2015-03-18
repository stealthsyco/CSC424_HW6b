package dsmith86.github.io.csc424_hw6b;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Daniel on 3/17/2015.
 */
public class Game implements GameSurfaceView.GameStateInterface {

    private static final int ROW_COUNT = 30;
    private static final int COL_COUNT = 20;

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
    }
}
