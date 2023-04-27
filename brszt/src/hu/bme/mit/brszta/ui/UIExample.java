package hu.bme.mit.brszta.ui;

import javax.swing.*;
import java.awt.*;

public class UIExample {
    private JButton pushMeButton;
    private JLabel label;
    private JPanel contentPane;
    private JButton paintButton;
    private JPanel canvas;

    private static class MyPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawString("This is my custom Panel!", 10, 30);
            g.setColor(Color.RED);
            g.fillRect(40, 35, 40, 40);
            g.setColor(Color.BLACK);
            g.drawRect(45, 40, 40, 40);
        }
    }

    public UIExample() {
        pushMeButton.addActionListener(e -> {
            label.setText("You have just pushed me");
        });

        paintButton.addActionListener(e ->  {
            Graphics graphics = canvas.getGraphics();
            new MyPanel().paintComponent(graphics);
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("UI Example");
        frame.setContentPane(new UIExample().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
