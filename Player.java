import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    private int x, y;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
