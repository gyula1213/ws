package example;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class TextFieldPane extends FlowPane {

  protected TextField text;
  protected Label label1, label2;

  public TextFieldPane() {
    setHgap(10);
    setAlignment(Pos.BASELINE_CENTER);

    text = new TextField("Write here!");
    text.setPrefColumnCount(30);
    text.setOnAction(this::rememberText);

    label1 = new Label("Text :  ");
    label2 = new Label("Memory :   ");

    getChildren().addAll(text,label1,label2);
  }

  private void rememberText (ActionEvent ev) {
    String s = label1.getText();
    label2.setText("Memory: " + s.split(":")[1]);
    label1.setText("Text: " + text.getText());
  }
}
