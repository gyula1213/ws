package example;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class MousePane extends BorderPane {
  private boolean close1 = false;
  private boolean close2 = false;
  private double direction1 = 0;
  private double direction2 = 0;
  private int r1 = 0;
  private int r2 = 0;

  private Canvas canvas;

  /**
   * Az objektumot felfűzzük a két egér figyelő láncára
   */
  public MousePane() {
    canvas = new Canvas(200,90);
    addEventFilter(MouseEvent.MOUSE_MOVED, this::mouseMoved);
    addEventFilter(MouseEvent.MOUSE_DRAGGED, this::mouseMoved);
    addEventFilter(MouseEvent.MOUSE_PRESSED, this::mousePressed);
    addEventFilter(MouseEvent.MOUSE_RELEASED, this::mouseReleased);
    repaint();
    setCenter(canvas);
  }

  /**
   * A kirajzoló függvény
   */
  private void repaint() {
    // Bal szem kirajzolása
    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.clearRect(0,0,200,90);
    if (!close1) {
      gc.setFill(Color.WHITE);
      gc.fillOval(0,0, 90, 90);
      gc.setFill(Color.BLACK);
      gc.fillOval(30 + (int) (r1 * Math.cos(direction1)),
                  30 + (int) (r1 * Math.sin(direction1)), 30, 30);
    } else {
      gc.setFill(Color.color(0.125,0.125,0.125));
      gc.fillOval(0, 0, 90, 90);
    }

    // Jobb szem kirajzolása
    if (!close2) {
      gc.setFill(Color.WHITE);
      gc.fillOval(110, 0, 90, 90);
      gc.setFill(Color.BLACK);
      gc.fillOval(140 + (int) (r2 * Math.cos(direction2)),
                  30 + (int) (r2 * Math.sin(direction2)), 30, 30);
    } else {
      gc.setFill(Color.color(0.125, 0.125, 0.125));
      gc.fillOval(110, 0, 90, 90);
    }
  }

  // Implementált egéresemény kezelő függvények
  private void mouseMoved(MouseEvent ev) {
    set_direction1(ev.getX(), ev.getY());
    set_direction2(ev.getX(), ev.getY());
    repaint();
  }

  private void mousePressed(MouseEvent ev) {
    if (ev.getButton() == MouseButton.PRIMARY)
      close1 = true;
    if (ev.getButton() == MouseButton.SECONDARY)
      close2 = true;
    repaint();
  }

  private void mouseReleased(MouseEvent ev) {
    if (ev.getButton() == MouseButton.PRIMARY)
      close1 = false;
    if (ev.getButton() == MouseButton.SECONDARY)
      close2 = false;
    repaint();
  }

  // segédfüggvények, amik beállítják a szemek irányát
  private void set_direction1(double dx, double dy) {
    double x0 = canvas.getLayoutX() + 45;
    double y0 = canvas.getLayoutY() + 45;
    direction1 = Math.PI / 2
        - Math.atan2(dx - x0, dy - y0);
    r1 = (int) Math.sqrt((dx - x0) * (dx - x0) + (dy - y0) * (dy - y0));
    if (r1 > 30)
      r1 = 30;
  }

  private void set_direction2(double dx, double dy) {
    double x0 = canvas.getLayoutX() + 155;
    double y0 = canvas.getLayoutY() + 45;
    direction2 = Math.PI / 2
        - Math.atan2(dx - x0, dy - y0);
    r2 = (int) Math.sqrt((dx - x0) * (dx - x0) + (dy - y0) * (dy - y0));
    if (r2 > 30)
      r2 = 30;
  }

}
