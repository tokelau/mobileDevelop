
public class Circle {
    int x, y;
    public float getXPos(float xPos) {
        if (xPos < 40) {
            return 1050;
        } else if (xPos > 1050) {
            return 40;
        }
        return xPos;
    }
    public float getYPos(float yPos) {
        if (yPos < 40) {
            return 1830;
        } else if (yPos > 1830) {
            return 1830;
        }
        return yPos;
    }
}
