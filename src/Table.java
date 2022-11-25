import javax.swing.*;
import javax.swing.border.Border;
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
    public static final  int   TABLE_WIDTH    = 600;
    public static final  int   TABLE_HEIGHT   = 500;
    public static  final int   WALL_THICKNESS = 20;
    private final Color COLOR          = Color.green;
    private final Color WALL_COLOR     = Color.black;
    int nbrBalls;
    public static ArrayList<Ball> balls;
    private final Timer simulationTimer;



    Table() {
        nbrBalls = 10;
        JButton b1 = new JButton("Start");
        JTextField t1 = new JTextField("");
        setPreferredSize(new Dimension(TABLE_WIDTH + 2 * WALL_THICKNESS,
                TABLE_HEIGHT + 2 * WALL_THICKNESS));
        createInitialBalls();
        setLayout(new BorderLayout());
        add(b1);
        add(t1);
        addMouseListener(this);

        simulationTimer = new Timer((int) (1000.0 / Twoballs.UPDATE_FREQUENCY), this);
    }

    private void createInitialBalls(){
        balls = new ArrayList<>(nbrBalls);
        for (int i = 0; i < nbrBalls; i++){
            balls.add(new Ball(new Coord(WALL_THICKNESS *  2 + (Math.random()* TABLE_WIDTH/4),WALL_THICKNESS + Math.random() * (TABLE_HEIGHT - WALL_THICKNESS))));
        }
        repaint();
    }

    public void actionPerformed(ActionEvent e) {          // Timer event
        for (Ball ball : balls) {
          ball.move();
          repaint();
        }

    }


    public void mousePressed(MouseEvent event) {
        repaint();                                          //  To show aiming line
    }

    public void mouseReleased(MouseEvent e) {
        if (!simulationTimer.isRunning()) {
            simulationTimer.start();
        }
    }


    // Obligatory empty listener methods
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

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
