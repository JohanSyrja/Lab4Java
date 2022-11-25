import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;


/**
 *
 * @author Joachim Parrow 2010 rev 2011, 2012, 2013, 2015, 2016
 *
 * Simulator for two balls
 */


public class Twoballs {
    final static int UPDATE_FREQUENCY = 100;    // Global constant: fps, ie times per second to simulate

    public static void main(String[] args) {
        JFrame frame = new JFrame("No collisions!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Table table = new Table();
        frame.add(table);
        frame.pack();
        frame.setVisible(true);
    }
}