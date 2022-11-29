import javax.swing.*;

/**
 *
 * @author Ludvig Wengström, Johan Syrjä, Sebastian .T Mendez 2022
 *
 * Simulator for two balls
 */


public class Twoballs {
    final static int UPDATE_FREQUENCY = 100;    // Global constant: fps, ie times per second to simulate

    public static void main(String[] args) {
        JFrame frame = new JFrame("8 Ball Pool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Table table = new Table();
        frame.add(table);
        frame.pack();
        frame.setVisible(true);
    }
}
