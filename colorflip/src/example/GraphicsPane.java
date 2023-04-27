package example;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GraphicsPane extends BorderPane {

  public GraphicsPane() {
    Canvas canvas = new Canvas(300,340);
    GraphicsContext gc = canvas.getGraphicsContext2D();

    // Rajzoljunk ide valami absztraktat
    gc.setFill(Color.GRAY);  // Az egész téglalap
    gc.fillRect(0,40,300,340);
    gc.setStroke(Color.BLACK);
    gc.strokeRect(0,40,300,340);
    gc.strokeLine(150,40,150,340);  // A felezővonal
    gc.setStroke(Color.color(0.25,0.25,0.25));  // A baloldali ovális
    gc.strokeOval(0,40,150,300);
    gc.setFill(Color.color(0.25,0.25,0.25));  // A jobboldali ovális
    gc.fillOval(150,40,150,300);
    gc.setFill(Color.color(0.5,0,0));  // A szöveg
    gc.setFont(Font.font("Arial", FontWeight.BOLD,20));
    gc.setTextAlign(TextAlignment.CENTER);
    gc.fillText("GRAPHICS", 150, 30);

    setCenter(canvas);
  }
}
