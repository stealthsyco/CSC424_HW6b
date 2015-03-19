package dsmith86.github.io.csc424_hw6b;

import android.graphics.Rect;

/**
 * Created by stealthsyco on 3/18/15.
 */
public class Tile {

    private String letter;
    private Rect boundingRect;
    private int x, y;
    private boolean isTouched;


    public Tile(String inLetter, Rect theBoundingRect, int xLoc, int yLoc){

        letter = inLetter;
        boundingRect = theBoundingRect;
        x = xLoc;
        y = yLoc;
        isTouched = false;


    }

    public boolean containsTouch(int x, int y){
        return boundingRect.contains(x, y);
    }

    public String getLetter(){
        return letter;
    }

    public int getX(){ return x; }
    public int getY(){ return y; }
    public boolean getTouched() { return isTouched; }

    public boolean setTouched(boolean bool){
       isTouched = bool;
       return isTouched;
    }


}
