package example;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class  ImagePane extends BorderPane {

  public ImagePane() {
    Image image = new Image("file:kep.jpg", true); // Töltse be a háttérben
    // jar file-ba való exportálás esetén:
    // image = new Image(getClass().getResource("/kep.jpg").toExternalForm(),
    // true);
    Label caption = new Label("Heroes of Might and Magic V");
    caption.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    VBox vbox = new VBox(10, new ImageView(image), caption);
    vbox.setAlignment(Pos.BASELINE_CENTER);
    setCenter(vbox);
  }

}
