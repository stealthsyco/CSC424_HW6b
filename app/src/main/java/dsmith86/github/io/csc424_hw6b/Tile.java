package dsmith86.github.io.csc424_hw6b;

import android.graphics.Rect;

/**
 * Created by stealthsyco on 3/18/15.
 */
public class Tile {

    String letter;

    private Rect boundingRect;

    public Tile(String inLetter, Rect theBoundingRect){

        letter = inLetter;
        boundingRect = theBoundingRect;


    }

    public boolean containsTouch(int x, int y){
        return boundingRect.contains(x, y);
    }

    public String getLetter(){
        return letter;
    }


}
