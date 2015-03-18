package dsmith86.github.io.csc424_hw6b;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private Game game;

    private enum GridSize {
        SMALL, MEDIUM, LARGE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GameSurfaceView gameSurfaceView = (GameSurfaceView)findViewById(R.id.gameSurfaceView);

        game = new Game(this, gameSurfaceView);
    }


    @Override
    protected void onPause() {
        super.onPause();
        game.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        game.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.choose_size) {
            final CharSequence[] gridSizes = {
                    getResources().getString(R.string.size_small),
                    getResources().getString(R.string.size_medium),
                    getResources().getString(R.string.size_large)};

            final AlertDialog.Builder sizeAlert = new AlertDialog.Builder(this);
            sizeAlert.setTitle(MainActivity.this.getResources().getString(R.string.settings_choose_size))
                    .setSingleChoiceItems(gridSizes, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GridSize gridSize = GridSize.values()[which];
                            switch (gridSize) {
                                case SMALL:
                                    game.loadGrid(R.raw.grid_small);
                                    break;
                                case MEDIUM:
                                    game.loadGrid(R.raw.grid_medium);
                                    break;
                                case LARGE:
                                    game.loadGrid(R.raw.grid_large);
                                    break;
                            }
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
