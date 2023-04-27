package screen;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class SizePane extends VBox {

	protected Label label;
	protected TextField text;
	protected Text check;
	private int size = 4;
	private boolean isReady = false;

	public SizePane() {
		setSpacing(10);
		setPadding(new Insets(10, 10, 10, 10));

		label = new Label("Please set new size (2-20), and press ENTER ");
		label.setFont(Font.font("Courier NEW", FontWeight.BOLD, 16));

		text = new TextField("" + size);
		text.setOnAction(this::checkSize);
		text.setFont(Font.font("Courier NEW", FontWeight.BOLD, 20));

		check = new Text("Message: ");
		check.setFont(Font.font("Courier NEW", FontWeight.NORMAL, 16));

		getChildren().addAll(label, text, check);
	}

	public int getSize() {
		return size;
	}

	public boolean isReady() {
		return isReady;
	}

	private void checkSize(ActionEvent ev) {
		check.setText("");
		String s = text.getText();
		try {
			size = Integer.parseInt(s);
			check.setText("The new size is: " + size);
			check.setFill(Color.GREEN);
		} catch (NumberFormatException e) {
			check.setText("Invalid Number");
			check.setFill(Color.RED);
			return;
		}
		if (size < 2 || size > 20) {
			check.setText("Size must be between 2 and 20");
			check.setFill(Color.RED);
			return;
		}
	}
}
