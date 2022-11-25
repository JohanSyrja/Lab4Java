import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
class Table extends JPanel implements MouseListener, ActionListener {

    private static final int BUTTON_SIZE_X = 140;
    private static final int BUTTON_SIZE_Y = 70;
    public static final  int   TABLE_WIDTH    = 600;
    public static final  int   TABLE_HEIGHT   = 500;
    public static  final int   WALL_THICKNESS = 20;
    private final Color COLOR          = Color.green;
    private final Color WALL_COLOR     = Color.black;
    private static final int NBR_BALLS = 15;
    public static ArrayList<Ball> balls;
    private final Timer simulationTimer;


    Table() {
        int button_pos_x = TABLE_WIDTH - TABLE_WIDTH / 4;
        int button_pos_y = TABLE_HEIGHT / 3;
        int text_pos_x = TABLE_WIDTH - TABLE_WIDTH / 4;
        int text_pos_y = TABLE_HEIGHT / 2;

        JButton b1 = new JButton("Start/Stop");
        JLabel t1 = new JLabel("");
        setPreferredSize(new Dimension(TABLE_WIDTH + 2 * WALL_THICKNESS,
                TABLE_HEIGHT + 2 * WALL_THICKNESS));
        createInitialBalls();
        b1.setLayout(null);
        b1.setBounds(button_pos_x, button_pos_y,BUTTON_SIZE_X,BUTTON_SIZE_Y);
        t1.setLayout(null);
        t1.setBounds(text_pos_x, text_pos_y,BUTTON_SIZE_X,BUTTON_SIZE_Y);
        add(b1);
        add(t1);
        simulationTimer = new Timer((int) (1000.0 / Twoballs.UPDATE_FREQUENCY), this);
        addMouseListener(this);
        b1.addActionListener(e -> {
            if (simulationTimer.isRunning()) {
                simulationTimer.stop();
                t1.setText("Nbr of dead: " + (NBR_BALLS - balls.size()));
            }
            else simulationTimer.start();
        });
    }

    private void createInitialBalls(){
        balls = new ArrayList<>(NBR_BALLS);
        for (int i = 0; i < NBR_BALLS; i++){
            balls.add(new Ball(new Coord(WALL_THICKNESS *  2 + (Math.random()* TABLE_WIDTH/4),WALL_THICKNESS + Math.random() * (TABLE_HEIGHT - WALL_THICKNESS))));
            for (Ball ball : balls) {
                if (!ball.equals(balls.get(i)) && balls.get(i).isColliding(ball)){
                    balls.get(i).position = new Coord(WALL_THICKNESS *  2 + (Math.random()* TABLE_WIDTH/4),WALL_THICKNESS * 2 + Math.random() * (TABLE_HEIGHT - WALL_THICKNESS * 2));
                }
            }
        }
        repaint();
    }

    public void actionPerformed(ActionEvent e) {          // Timer event
        balls.removeIf(n -> n.isDead);
        for (Ball ball : balls) {
            ball.move();
            repaint();
        }
    }

    public void mouseClicked(MouseEvent e) {
        Coord mousePosition = new Coord(e.getX(),e.getY());
        System.out.println(mousePosition.x + mousePosition.y);
        for (Ball ball : balls) {
            if(ball.checkInfectPosition(mousePosition)){
                ball.isSick = true;
                repaint();
            }
        }
    }



    // Obligatory empty listener methods

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}


    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2D = (Graphics2D) graphics;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // This makes the graphics smoother
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2D.setColor(WALL_COLOR);
        g2D.fillRect(0, 0, TABLE_WIDTH/2 + 2 * WALL_THICKNESS, TABLE_HEIGHT + 2 * WALL_THICKNESS);

        g2D.setColor(COLOR);
        g2D.fillRect(WALL_THICKNESS, WALL_THICKNESS, TABLE_WIDTH/2, TABLE_HEIGHT);

        for (Ball ball : balls) {
            ball.paint(g2D);
        }
    }
}  // end class Table