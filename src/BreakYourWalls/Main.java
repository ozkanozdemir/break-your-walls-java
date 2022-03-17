package BreakYourWalls;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static final int DIM_WIDTH = 700;
    private static final int DIM_HEIGHT = 600;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                Gameplay gamePlay = new Gameplay();
                frame.setBounds(10, 10, DIM_WIDTH, DIM_HEIGHT);
                frame.setTitle("Break Your Walls");
                frame.setResizable(false);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(gamePlay);
            }
        });
    }

}
