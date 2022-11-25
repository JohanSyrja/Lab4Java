import java.awt.*;
import java.util.ArrayList;

/**
 * ****************************************************************************************
 * Ball:
 * The ball has instance variables relating to its graphics and game state:
 * position, velocity, and the position from which a shot is aimed (if any).
 *
 */
class Ball {

    final double INIT_SICK_PROB = 0.7;
    final double GET_WELL_PROB = 0.005;
    final double DIE_PROB = 0.002;
    final double INFECT_PROB = 0.8;

    private final Color COLOR               = Color.white;
    private final Color SICK_COLOR          = Color.red;
    private final int    BORDER_THICKNESS    = 2;
    private static final double RADIUS              = 10;
    static final double DIAMETER            = 2 * RADIUS;
    private final double FRICTION            = 0;                          // its friction constant (normed for 100 updates/second)
    private final double FRICTION_PER_UPDATE =                                 // friction applied each simulation step
            1.0 - Math.pow(1.0 - FRICTION,                       // don't ask - I no longer remember how I got to this
                    100.0 / Twoballs.UPDATE_FREQUENCY);
    Coord position;
    private Coord velocity;

    private boolean northSide;
    private boolean southSide;

    public boolean isSick;
    boolean isDead;
    private final Coord INITIAL_VELOCITY = new Coord(Math.random()* 3, Math.random()* 3);


    Ball(Coord initialPosition) {
        isSick = Math.random() < INIT_SICK_PROB;
        isDead = false;
        position = initialPosition;
        northSide = false;
        southSide = false;
        velocity = INITIAL_VELOCITY;       // WARNING! Are initial velocities
    }                                // clones or aliases? Is this important?

    boolean isMovingToward(Ball collisionBall){
        return Coord.distance(Coord.add(position,velocity), Coord.add(collisionBall.position,collisionBall.velocity)) <
                Coord.distance(position,collisionBall.position);
    }

    boolean checkInfectPosition(Coord clickPosition){
        if (Coord.distance(position,clickPosition) <= RADIUS){
            return true;
        }
        return false;
    }
    boolean isColliding(Ball collisionBall){
        return Coord.distance(position,collisionBall.position) <= DIAMETER;
    }

    void ballCollision(Ball collisionBall){
        double x_diff = position.x - collisionBall.position.x;
        double y_diff = position.y - collisionBall.position.y;
        double hypo = Math.sqrt(Math.pow(x_diff,2) + Math.pow(y_diff,2));
        double dx = x_diff/hypo;
        double dy = y_diff/hypo;
        double impulse = ((collisionBall.velocity.x * dx) + (collisionBall.velocity.y * dy)) -
                ((velocity.x * dx)  + (velocity.y * dy));
        if (isColliding(collisionBall) && isMovingToward(collisionBall)){
            velocity.x = velocity.x + (impulse * dx);
            velocity.y = velocity.y + (impulse * dy);
            collisionBall.velocity.x  = collisionBall.velocity.x - (impulse * dx);
            collisionBall.velocity.y = collisionBall.velocity.y - (impulse * dy);
            if(isSick){
                collisionBall.isSick = Math.random() < INFECT_PROB;
            }
        }
    }
    boolean isMoving() {    // if moving too slow I am deemed to have stopped
        return velocity.magnitude() > FRICTION_PER_UPDATE;
    }

    void move() {
        if (isMoving()) {
            for (Ball ball : Table.balls) {
                for (Ball ball1 : Table.balls) {
                    ball.ballCollision(ball1);
                }
            }
            position.increase(velocity);
            velocity.decrease(Coord.mul(FRICTION_PER_UPDATE, velocity.norm()));
            if (ballCollisionWall()){

                if(southSide || northSide){
                    velocity.y *= -1;
                }
                else {
                    velocity.x *= -1;
                }
            }
            northSide = false;
            southSide = false;
            double temp = Math.random();
            if(isSick){
                isDead = temp < DIE_PROB;
                if (!isDead){
                    isSick = temp > GET_WELL_PROB;
                }
            }

        }
    }



    public ArrayList<Coord> getballHitbox(){
        ArrayList<Coord> hitbox = new ArrayList<Coord>(360);
        for (double i = 0; i < 360; i++){
            hitbox.add(new Coord(position.x - (RADIUS)*Math.sin((i)),
                    position.y + (RADIUS)*Math.cos((i))));
        }
        return hitbox;
    }


    boolean ballCollisionWall(){

        for (Coord ballCoord : getballHitbox()){
            if (ballCoord.y <= Table.WALL_THICKNESS){ //northWall
                northSide = true;
                return true;
            }
            if (ballCoord.y >= Table.TABLE_HEIGHT + Table.WALL_THICKNESS) { // southWall
                southSide = true;
                return true;
            }
            if (ballCoord.x <= Table.WALL_THICKNESS){ // westWall
                return true;
            }
            if  (ballCoord.x >= Table.TABLE_WIDTH/2 + Table.WALL_THICKNESS){ // eastWall
                return true;
            }
        }
        return false;
    }


    // paint: to draw the ball, first draw a black ball
    // and then a smaller ball of proper color inside
    // this gives a nice thick border
    void paint(Graphics2D g2D) {
        g2D.setColor(Color.black);
        g2D.fillOval(
                (int) (position.x - RADIUS + 0.5),
                (int) (position.y - RADIUS + 0.5),
                (int) DIAMETER,
                (int) DIAMETER);
        if (isSick){
            g2D.setColor(SICK_COLOR);
        } else g2D.setColor(COLOR);
        g2D.fillOval(
                (int) (position.x - RADIUS + 0.5 + BORDER_THICKNESS),
                (int) (position.y - RADIUS + 0.5 + BORDER_THICKNESS),
                (int) (DIAMETER - 2 * BORDER_THICKNESS),
                (int) (DIAMETER - 2 * BORDER_THICKNESS));
    }
} // end  class Ball