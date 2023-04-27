package example;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class CheckBoxPane extends FlowPane {

  protected Label label = new Label("< >< >< >");
  protected CheckBox check1, check2, check3;

  public CheckBoxPane() {
    setHgap(10);
    setAlignment(Pos.BASELINE_CENTER);

    check1 = new CheckBox("A");
    check1.setOnAction(event -> evaluate());
    check2 = new CheckBox("B");
    check2.setOnAction(event -> evaluate());
    check3 = new CheckBox("C");
    check3.setOnAction(event -> evaluate());

    getChildren().addAll(check1, check2, check3, label);
  }

  private void evaluate() {
    String s = "   <";
    if (check1.isSelected())
      s += " A ";
    else
      s += " ";
    s += "> <";
    if (check2.isSelected())
      s += " B ";
    else
      s += " ";
    s += "> <";
    if (check3.isSelected())
      s += " C ";
    else
      s += " ";
    s += ">";
    label.setText(s);
  }

}
