package hu.bme.mit.brszta.ui;

import javax.swing.*;
import java.awt.*;

public class MyCanvas extends JPanel {

    private int state = -1;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        switch (state) {
            case 0: // draw circle
                g.clearRect(0, 0, 300, 300);
                g.setColor(Color.RED);
                g.drawOval(10, 10, 100, 100);
                break;

            case 1: // draw rectangle
                g.clearRect(0, 0, 300, 300);
                g.setColor(Color.BLACK);
                g.drawRect(10, 10, 100, 100);
                break;

            case 2: // draw text
                g.clearRect(0, 0, 300, 300);
                g.setColor(Color.DARK_GRAY);
                g.drawString("Hello there! General Kenobi!", 10, 10);
                break;
        }

    }

    /**
     * A setState metódus csak az állapotát állítja be a canvasnek, majd jelzi a rendszer felé, hogy érdemes lenne
     * újrarajzolni azt.
     * @param state
     */
    public void setState(int state) {
        this.state = state;
        invalidate();
        repaint();
    }
}
