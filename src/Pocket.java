import java.awt.*;

class Pocket extends Ball{
    private Coord position;
    static int POCKET_RADIUS = 20;
    private final int POCKET_DIAMETER = 40;

    Pocket(Coord initialPosition) {
        super(initialPosition);
        position = new Coord(initialPosition.x,initialPosition.y);
    }

    Coord getPosition(){
        return position;
    }

    void paint(Graphics2D g2D) {                           //Ritar upp de svarta hålen som cirklar
        g2D.setColor(Color.black);
        g2D.fillOval(
                (int) (position.x - POCKET_RADIUS + 0.5),
                (int) (position.y - POCKET_RADIUS + 0.5),
                POCKET_DIAMETER,
                POCKET_DIAMETER);
    }
}
