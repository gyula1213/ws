package example;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ButtonPane extends FlowPane {

  protected Label label;
  protected Button button1, button2, button3;

  public ButtonPane() {
    setHgap(10);
    setAlignment(Pos.BASELINE_CENTER);

    label = new Label("This is a text.");
    label.setFont(Font.font("Arial", FontWeight.BOLD,30));

    button1 = new Button("Red");
    button1.setOnAction(event -> colorLabel(Color.RED));
    button2 = new Button("Blue");
    button2.setOnAction(event -> colorLabel(Color.BLUE));
    button3 = new Button("Green");
    button3.setOnAction(event -> colorLabel(Color.GREEN));

    getChildren().addAll(label,button1,button2,button3);
  }

  private void colorLabel(Color color) {
    label.setTextFill(color);
  }
}