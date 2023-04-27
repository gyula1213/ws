package example;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AnimationPane extends StackPane {

  public AnimationPane() {
    Rectangle background = new Rectangle(406, 432);
    background.setFill(Color.color(0.375, 0.375, 0.375));
    getChildren().add(background);
    setAlignment(background, Pos.CENTER);
    Pane circles = new Pane();
    circles.getChildren().addAll(
        new RotatingCircle(120, Color.RED, 4),
        new RotatingCircle(60, Color.YELLOW, 3),
        new RotatingCircle(190, Color.BLUE, 2),
        new RotatingCircle(180, Color.GREEN, 1)
                                );
    getChildren().add(circles);
  }

  private class RotatingCircle extends Pane {
    private final Arc arc;
    private final Circle circle;

    private RotatingCircle(double radius, Color color, int speed) {
      if (!(radius <= 190 && radius >= 0)) {
        radius = 0;
      }
      arc = new Arc();
      arc.setRadiusX(radius);
      arc.setRadiusY(radius);
      arc.setStroke(color);
      arc.setStrokeWidth(10);
      speed = Math.min(5, Math.max(0, speed));
      arc.setStartAngle(0);
      arc.setLength(30);
      new ArcRotation(this, speed).play();

      circle = new Circle(radius);
      circle.setFill(null);
      circle.setStrokeWidth(10);
      circle.setStroke(Color.color(0.75,0.75,0.75));

      getChildren().addAll(circle,arc);
    }

    public void moveTo(double x, double y) {
      circle.setCenterX(x);
      circle.setCenterY(y);
      arc.setCenterX(x);
      arc.setCenterY(y);
    }

    public void rotate(double direction) {
      arc.setStartAngle(direction);
    }
  }

  private class ArcRotation extends Transition {

    private final RotatingCircle rotatingCircle;

    public ArcRotation(RotatingCircle rotatingCircle, double speed) {
      this.rotatingCircle = rotatingCircle;
      // Kb. 15 ms-enként mozduljon el speed fokot
      setCycleDuration(Duration.millis(360 * 15 / speed));
      setCycleCount(Animation.INDEFINITE);  // Ne álljon meg
      setInterpolator(Interpolator.LINEAR); // Egyenletesen mozogjon
    }

    @Override
    protected void interpolate(double frac) {
      rotatingCircle.rotate(frac * 360);  // Egyenletesen megy körbe
      // Hagyományos módszerrel középre igazítva kiszámolná az alakzat
      // "közepét", ami kb. a súlypontja, mi viszont az eredeti kör közepét
      // szeretnénk középre helyezni.
      rotatingCircle.moveTo(AnimationPane.this.getWidth() / 2,
                            AnimationPane.this.getHeight() / 2);
    }
  }

}
