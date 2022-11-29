import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
/**
 * ****************************************************************************************
 * Table
 * The table has some constants and instance variables relating to the graphics and
 * the balls. When simulating the balls it starts a timer
 * which fires UPDATE_FREQUENCY times per second. Each time the timer is
 * activated one step of the simulation is performed. The table reacts to
 * events to accomplish repaints and to stop or start the timer.
 *
 */
class Table extends JPanel implements MouseListener, MouseMotionListener, ActionListener {
    public static final  double   TABLE_WIDTH      = 800;
    public static final  double   TABLE_HEIGHT     = 400;
    public static  final int   WALL_THICKNESS   = 20;
    private final Color COLOR                   = new Color(0, 204, 0);
    private final Color WALL_COLOR              = new Color(80, 40, 0);
    static Ball cueBall;
    public static ArrayList<Ball> balls;
    public static ArrayList<Pocket> pockets;
    public static Timer simulationTimer;
    private final double   POCKET_RADIUS           = 25;


    static boolean turn = true;
    static int PLAYER_ONE_SCORE;
    static int PLAYER_TWO_SCORE = 0;
    private static final JLabel myLabel = new JLabel();
    private static final JLabel myLabel2 = new JLabel();
    private final JLabel myLabel3 = new JLabel();
    private final JButton newGameButton = new JButton("New Game");
    private final JButton quitButton = new JButton("Quit");



    Table() {

        setPreferredSize(new Dimension((int) (TABLE_WIDTH + 2 * WALL_THICKNESS),
                (int) (TABLE_HEIGHT + 3 * WALL_THICKNESS)));
        createInitialBalls();
        createPocket();

        addMouseListener(this);
        addMouseMotionListener(this);
        myLabel.setForeground(Color.WHITE);
        myLabel2.setForeground(Color.WHITE);
        myLabel3.setForeground(Color.WHITE);

        add(myLabel);
        add(myLabel2);
        add(myLabel3);
        add(newGameButton);
        add(quitButton);

        myLabel.setText("Player 1: " + "" + PLAYER_ONE_SCORE + "");
        myLabel2.setText("Player 2: " + "" + PLAYER_TWO_SCORE + "");
        myLabel3.setText("PLAYER 1, YOU'RE UP!");

        newGameButton.addActionListener(this);
        quitButton.addActionListener(this);

        newGameButton.setVisible(true);
        quitButton.setVisible(true);


        simulationTimer = new Timer((int) (1000.0 / Twoballs.UPDATE_FREQUENCY), this);
    }

    private void createInitialBalls(){
        balls = new ArrayList<>();
        final Coord cueBallInitialPosition = new Coord(TABLE_HEIGHT/2, TABLE_HEIGHT/2);
        cueBall = createCueBall(cueBallInitialPosition);
        balls.add(cueBall);
        balls.add(new Ball(new Coord(TABLE_HEIGHT+TABLE_HEIGHT/2 + 4, TABLE_HEIGHT/2)));
        balls.add(new Ball(new Coord(TABLE_HEIGHT+TABLE_HEIGHT/2+2*Ball.RADIUS + 2, TABLE_HEIGHT/2-Ball.RADIUS - 4)));
        balls.add(new Ball(new Coord(TABLE_HEIGHT+TABLE_HEIGHT/2+2*Ball.RADIUS + 2, TABLE_HEIGHT/2+Ball.RADIUS + 4)));
        balls.add(new Ball(new Coord(TABLE_HEIGHT+TABLE_HEIGHT/2+4*Ball.RADIUS + 2, TABLE_HEIGHT/2-2*Ball.RADIUS - 4)));
        balls.add(new Ball(new Coord(TABLE_HEIGHT+TABLE_HEIGHT/2+4*Ball.RADIUS + 2, TABLE_HEIGHT/2+2*Ball.RADIUS + 4)));
        balls.add(new Ball(new Coord(TABLE_HEIGHT+TABLE_HEIGHT/2+4*Ball.RADIUS + 2, TABLE_HEIGHT/2)));
        balls.add(new Ball(new Coord(TABLE_HEIGHT+TABLE_HEIGHT/2+6*Ball.RADIUS+2, TABLE_HEIGHT/2-3*Ball.RADIUS-8)));
        balls.add(new Ball(new Coord(TABLE_HEIGHT+TABLE_HEIGHT/2+6*Ball.RADIUS+2, TABLE_HEIGHT/2+3*Ball.RADIUS+8)));
        balls.add(new Ball(new Coord(TABLE_HEIGHT+TABLE_HEIGHT/2+6*Ball.RADIUS+2, TABLE_HEIGHT/2-2*Ball.RADIUS+12)));
        balls.add(new Ball(new Coord(TABLE_HEIGHT+TABLE_HEIGHT/2+6*Ball.RADIUS+2, TABLE_HEIGHT/2+2*Ball.RADIUS-12)));

    }

    private Ball createCueBall(Coord cueBallPosition){
        cueBall = new Ball(cueBallPosition);
        return cueBall;
    }
    private void createPocket(){
        final Coord pocket1Pos = new Coord(2*WALL_THICKNESS - (POCKET_RADIUS/2)+3, TABLE_HEIGHT + 3*WALL_THICKNESS - (4*POCKET_RADIUS/2));
        final Coord pocket2Pos = new Coord(2*WALL_THICKNESS - (POCKET_RADIUS/2)+3, 3*WALL_THICKNESS - POCKET_RADIUS - 4);

        final Coord pocket3Pos = new Coord(2*WALL_THICKNESS + TABLE_WIDTH - (POCKET_RADIUS)-5, TABLE_HEIGHT + 3*WALL_THICKNESS - (4*POCKET_RADIUS/2));
        final Coord pocket4Pos = new Coord(2*WALL_THICKNESS + TABLE_WIDTH - (POCKET_RADIUS)-5, 3*WALL_THICKNESS - POCKET_RADIUS - 4);

        final Coord pocket5Pos = new Coord(TABLE_WIDTH/2, TABLE_HEIGHT + 3*WALL_THICKNESS - (4*POCKET_RADIUS/2));
        final Coord pocket6Pos = new Coord(TABLE_WIDTH/2, 3*WALL_THICKNESS - POCKET_RADIUS - 4);

        pockets = new ArrayList<>();
        pockets.add(new Pocket(pocket1Pos));
        pockets.add(new Pocket(pocket2Pos));
        pockets.add(new Pocket(pocket3Pos));
        pockets.add(new Pocket(pocket4Pos));
        pockets.add(new Pocket(pocket5Pos));
        pockets.add(new Pocket(pocket6Pos));
    }
    static void scoreAddition() {
        if (turn) {
            PLAYER_ONE_SCORE++;
            myLabel.setText("PLAYER 1: " + PLAYER_ONE_SCORE);

        } else {
            PLAYER_TWO_SCORE ++;
            myLabel2.setText("PLAYER 2: " + PLAYER_TWO_SCORE);

        }
    }

    public void actionPerformed(ActionEvent e) {          // Timer event

        balls.removeIf(b -> b.ballNotOnTable && b != cueBall);
        if (cueBall.ballNotOnTable){
            cueBall.velocity = new Coord(0,0);
        }
        for (Ball ball : balls) {
            ball.move();
            repaint();
        }

        if(balls.stream().allMatch(b -> !b.isMoving() && !b.ballNotOnTable)){
            simulationTimer.stop();
            if (turn) {
                myLabel3.setText("PLAYER 1, YOU'RE UP");

            } else {
                myLabel3.setText("PLAYER 2, YOU'RE UP");
            }

        }
        if (e.getSource() == quitButton) {
            System.exit(0);
        }
        if (e.getSource() == newGameButton) {
            createInitialBalls();
            PLAYER_ONE_SCORE = 0;
            PLAYER_TWO_SCORE= 0;
            myLabel.setText("PLAYER 1: " + "" + PLAYER_ONE_SCORE + "");
            myLabel2.setText("PLAYER 2: " + "" + PLAYER_TWO_SCORE + "");
            myLabel3.setText("PLAYER 1, YOU'RE UP!");
            turn = true;
        }

    }

    public void mousePressed(MouseEvent event) {
        Coord mousePosition = new Coord(event);             // HÄR BESTÄMS VILKA BOLLAR MAN KAN SKJUTA IVÄG
        if (!cueBall.ballNotOnTable) {
            if (balls.stream().noneMatch(Ball::isMoving)) {
                cueBall.setAimPosition(mousePosition);
                repaint();
            }//  To show aiming line
        }
    }

    public void mouseReleased(MouseEvent e) {               // HÄR BESTÄMS VAD SOM SKA SKE NÄR MAN SLÄPPER MUSKNAPPEN
        cueBall.shoot();
        if (!simulationTimer.isRunning()) {
            simulationTimer.start();
        }
    }

    public void mouseDragged(MouseEvent event) {
        Coord mousePosition = new Coord(event);
        cueBall.updateAimPosition(mousePosition);             // RITAR UPP STRECKET NÄR MUSEN DRAS
        repaint();
    }
    public void mouseClicked(MouseEvent e) {
        Coord mouseClicked = new Coord(e);
        ArrayList<Ball> tempList = (ArrayList<Ball>)balls.clone();
        tempList.remove(0);
        if(cueBall.ballNotOnTable){
            cueBall.position = mouseClicked;
            if (!cueBall.pocketCollision() && tempList.stream().noneMatch(cueBall::isColliding) && !cueBall.ballCollisionWall()){
                cueBall.velocity = new Coord(0,0);
                cueBall.ballNotOnTable = false;
                repaint();
            }

        }
    }

    // Obligatory empty listener methods
    // ÖVRIGA OBLIGATORISKA METODER FÖR MOUSELISTENER
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

    public static void playerTurn() {
        turn = !turn;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2D = (Graphics2D) graphics;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // This makes the graphics smoother
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2D.setColor(COLOR);
        g2D.fillRect(WALL_THICKNESS, WALL_THICKNESS, (int) TABLE_WIDTH, (int) TABLE_HEIGHT);

        for (Pocket pocket : pockets) {
            pocket.paint(g2D);
        }

        g2D.setPaint(WALL_COLOR);
        g2D.setStroke(new BasicStroke(WALL_THICKNESS));
        g2D.draw(new Rectangle2D.Double(WALL_THICKNESS, WALL_THICKNESS, TABLE_WIDTH, TABLE_HEIGHT));

        if (!cueBall.ballNotOnTable) {
            Ball.COLOR = new Color(255, 255, 255);
            g2D.setStroke(new BasicStroke(1));
            cueBall.paint(g2D);
        }
        else {
            myLabel3.setText("PLACE CUEBALL");
        }



        for (Ball ball : balls) {
            if(ball != cueBall){
                Ball.COLOR = Color.RED;
                ball.paint(g2D);
            }

        }
    }
}  // end class Table
