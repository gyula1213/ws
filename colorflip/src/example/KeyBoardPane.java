package example;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class KeyBoardPane extends Pane{
	protected boolean state[] = { false, false, false, false, false, false,
			false, false, false, false, false, false };
	protected final String str[] = { "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "C", "0", "E" };
	protected String code[] = { " ", " ", " ", " ", " ", " ", " ", " ", " " };
	protected int codepointer = 0;
	protected boolean ok = true;
	protected final KeyCode[] keys = { KeyCode.NUMPAD1, KeyCode.NUMPAD2,
			KeyCode.NUMPAD3, KeyCode.NUMPAD4, KeyCode.NUMPAD5,
			KeyCode.NUMPAD6, KeyCode.NUMPAD7, KeyCode.NUMPAD8,
			KeyCode.NUMPAD9 };
	protected final KeyCode[] keys2 = { KeyCode.DIGIT1, KeyCode.DIGIT2,
			KeyCode.DIGIT3, KeyCode.DIGIT4, KeyCode.DIGIT5, KeyCode.DIGIT6,
			KeyCode.DIGIT7, KeyCode.DIGIT8, KeyCode.DIGIT9 };
  
  protected Canvas canvas;

	public KeyBoardPane() {
		canvas = new Canvas(195,317);
    canvas.addEventFilter(MouseEvent.ANY,event -> canvas.requestFocus());
    addEventFilter(KeyEvent.KEY_PRESSED,this::keyPressed);
    addEventFilter(KeyEvent.KEY_RELEASED,this::keyReleased);
    repaint();
    getChildren().add(canvas);
	}

	protected void repaint() {
    GraphicsContext gc = canvas.getGraphicsContext2D();
    requestFocus();
		gc.setFill(Color.color(0, 0, 0));
		gc.fillRect(7, 7, 180, 50);
		gc.setStroke(Color.color(0.375, 0.375, 0.375));
		gc.strokeRect(7, 7, 180, 50);
		gc.strokeRect(1, 1, 193, 315);
		for (int i = 0; i < 12; ++i) {
			gc.setStroke(Color.color(0.375, 0.375, 0.375));
			gc.strokeRect(7 + 60 * (i % 3), 70 + 60 * (i / 3), 60, 60);
			if (state[i])
				gc.setFill(Color.color(1, 0, 0));
			else
				gc.setFill(Color.color(0.375, 0.375, 0.375));
			gc.setFont(Font.font("Courier NEW", FontWeight.BOLD, 50));
			gc.fillText(str[i], 23 + 60 * (i % 3), 120 + 60 * (i / 3));
		}
		for (int i = 0; i <= 7; ++i) {
			gc.setFont(Font.font("Courier New", FontWeight.BOLD, 30));
			gc.setFill(Color.color(0.781, 1, 0.117));
			gc.fillText(code[i], 13 + i * 20, 47);
		}
	}

	protected void keyPressed(KeyEvent ev) {
    for (int w = 0; w < 9; ++w)
			if (ev.getCode() == keys[w] || ev.getCode() == keys2[w])
				if (ok) {
					state[w] = true;
					code[codepointer] = Integer.toString(w + 1);
					codepointer++;
					ok = false;
				}
		if (ev.getCode() == KeyCode.ENTER) {
			state[11] = true;
			codepointer = 0;
			for (int i = 0; i < 9; ++i)
				code[i] = " ";
			ok = false;
		}
		if (ev.getCode() == KeyCode.NUMPAD0 && ok) {
			state[10] = true;
			code[codepointer] = "0";
			codepointer++;
			ok = false;
		}
		if (ev.getCode() == KeyCode.BACK_SPACE && codepointer > 0 && ok) {
			state[9] = true;
			codepointer--;
			code[codepointer] = " ";
			ok = false;
		}

		if (codepointer > 8)
			codepointer = 8;
		if (codepointer < 0)
			codepointer = 0;
    repaint();
	}

	protected void keyReleased(KeyEvent ev) {
		for (int w = 0; w < 9; ++w)
			if (ev.getCode() == keys[w] || ev.getCode() == keys2[w]) {
				state[w] = false;
				ok = true;
			}
		if (ev.getCode() == KeyCode.ENTER) {
			state[11] = false;
			ok = true;
		}
		if (ev.getCode() == KeyCode.NUMPAD0) {
			state[10] = false;
			ok = true;
		}
		if (ev.getCode() == KeyCode.BACK_SPACE) {
			state[9] = false;
			ok = true;
		}
    repaint();
	}

}
