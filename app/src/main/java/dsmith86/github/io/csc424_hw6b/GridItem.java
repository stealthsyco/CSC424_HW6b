package dsmith86.github.io.csc424_hw6b;

/**
 * Created by Daniel on 3/18/2015.
 */
public class GridItem<T> {
    private GridItem left, right, up, down, upLeft, upRight, downLeft, downRight;
    private T data;

    public GridItem(T data) {
        left = right = up = down = null;
        upLeft = upRight = downLeft = downRight = null;
        this.data = data;
    }

    public void setLeft(GridItem left) {
        left.right = this;
        this.left = left;
        this.upLeft = left.up;

        if (left.up != null) {
            left.up.downRight = this;
        }
    }

    public void setUp(GridItem up) {
        up.down = this;
        this.up = up;
        this.upRight = up.right;

        if (up.right != null) {
            up.right.downLeft = this;
        }
    }


}
