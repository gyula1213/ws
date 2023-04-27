package event;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import javax.swing.JFrame;

public class Center {

    void setCenter(JFrame frame) {
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        int x = (int) center.getX() - (frame.getWidth() / 2);
        int y = (int) center.getY() - (frame.getHeight() / 2);
        Point ablakCenter = new Point(x, y);
        frame.setLocation(ablakCenter);
    }
}
