package dsmith86.github.io.csc424_hw6b;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Game implements GameSurfaceView.GameStateInterface {

    private static final int ROW_COUNT = 3;
    private static final int COL_COUNT = 3;

    private GridItem<Character>[][] grid;

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

        loadGrid(R.raw.grid_large);
    }

    public void loadGrid(int gridResource) {
        try {
            InputStream inputStream = context.getResources().openRawResource(gridResource);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            int n = Integer.parseInt(bufferedReader.readLine());

            grid = new GridItem[n][n];

            for (int i = 0; i < n; i++) {
                String line = bufferedReader.readLine();
                int columnCount = Math.min(line.length(), n);

                for (int j = 0; j < columnCount; j++) {
                    grid[i][j] = new GridItem<>(line.charAt(j));

                    if (i > 0) {
                        grid[i][j].setUp(grid[i - 1][j]);
                    }
                    if (j > 0) {
                        grid[i][j].setLeft(grid[i][j - 1]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GridItem<Character>[][] getGrid() {
        if (grid != null) {
            return grid;
        }
        return new GridItem[0][];
    }
}
