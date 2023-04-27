package screen;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TablePane extends BorderPane {
	private Stage screen;

	private Text resultText = new Text();
	private Text timeText = new Text();
	
	private HBox centerBox = new HBox();
	private Canvas canvas;

	private Colorflip colorflip;
	private int size;
	private double width;
	private double height;
	private double widthField;
	private boolean isSolverMode = false;

	private Timer timer;
	private int sec = 0;
	private String clock;
	private boolean isReady;
	/**
	 * A jatek tablaja
	 */
	public TablePane(Stage screen, Colorflip colorflip) {
		this.screen = screen;
		this.colorflip = colorflip;

		// Canvas in Center
		canvas = new Canvas();
		canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, this::mousePressed);
		centerBox.setPadding(new Insets(10, 0, 10, 50));
		centerBox.getChildren().addAll(canvas);
		setCenter(centerBox);
		
		// Result on top
		if (colorflip.getCnt() == 0) {
			resultText.setFill(Color.BLUE);
			resultText.setText("Good luck!");
		}
		else {
			resultText.setFill(Color.BLACK);
			resultText.setText("Number of steps: " + colorflip.getCnt());
		}
		resultText.setFont(Font.font("Courier NEW", FontWeight.BOLD, 16));
//		resultText.setAlignment(Pos.BASELINE_CENTER);
		
//		resultText.setStyle("-fx-font-size: 1.5em;");
//		resultText.setX(120);
		
		//resultBox.setPadding(new Insets(10, 0, 10, 0));
		//resultBox.getChildren().addAll(resultText);
		setTop(resultText);

		// Timer on right
		timeText.setFill(Color.RED);
		timeText.setFont(Font.font("Courier NEW", FontWeight.BOLD, 32));
		setRight(timeText);

		startGame();
	}

	public void setSolverMode(boolean flag) {
		isSolverMode = flag;
	}

	public void stopGame() {
		timer.stop();
	}

	public void restartGame() {
		resultText.setFill(Color.BROWN);
		resultText.setText("Game restarted");
		startGame();
	}

	public void startGame() {
		size = colorflip.getSize();
		sec = colorflip.getSec();
		setSec(sec);
		timer = new Timer(this, sec);
		timer.start();
		isReady = false;
		repaint();
	}

	/**
	 * 
	 */
	private void paintTable(GraphicsContext gc, int[][] tomb) {
		for (int i = 0; i < tomb.length; i++) {
			for (int j = 0; j < tomb[i].length; j++) {
				if (tomb[i][j] == 1)
					gc.setFill(Color.RED);
				else
					gc.setFill(Color.GREEN);
				gc.fillRect(0 + i * widthField, 0 + j * widthField, widthField, widthField);
				gc.setStroke(Color.BLACK);
				gc.strokeRect(0 + i * widthField, 0 + j * widthField, widthField, widthField);
				if (isSolverMode && colorflip.isInSolve(i, j)) {
					gc.strokeOval(0 + i * widthField, 0 + j * widthField, widthField, widthField);
				}
			}
		}
	}

	public void repaint() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		canvas.setCursor(Cursor.CROSSHAIR);
		canvas.setWidth(size * this.widthField);
		canvas.setHeight(size * this.widthField);
		gc.clearRect(0, 0, width, height);
		paintTable(gc, colorflip.getActTable());
	}

	private void mousePressed(MouseEvent ev) {
		if (isReady) {
			resultText.setFill(Color.BROWN);
			resultText.setText("You are ready in " + colorflip.getCnt() + " steps. Time: " + clock);
			return;
		}
		System.out.println("widthField: (" + widthField + ")");
		System.out.println("Pressed: (" + ev.getX() + "," + ev.getY() + ")");
		int row = (int) ((ev.getY()) / widthField) + 1;
		int col = (int) ((ev.getX()) / widthField) + 1;
		System.out.println("Pos: (" + row + "," + col + ")");
		if (row < 1 || row > size || col < 1 || col > size) {
			System.out.println("Invalid Pos");
			return;
		}
		colorflip.step(col, row);
		if (colorflip.checkActTable()) {
			isReady = true;
			resultText.setFill(Color.GREEN);
			resultText.setText("You are ready in " + colorflip.getCnt() + " steps. Time: " + clock);
			stopGame();
		} else {
			resultText.setFill(Color.BLACK);
			resultText.setText("Number of steps: " + colorflip.getCnt());
		}
		repaint();
	}

	public void setTableSize(double w, double h) {
		width = screen.getWidth() - 160;
		height = screen.getHeight();
		widthField = Math.min(height / size, width / size) * 0.9;
		repaint();
	}

	private void setSec(int sec) {
		this.sec = sec;
		colorflip.setSec(sec);
		int m = sec / 60;
		int s = sec % 60;
		if (s < 10)
			clock = m + ":0" + s;
		else
			clock = m + ":" + s;
		timeText.setText(clock);
	}

	public class Timer extends Thread {

		private int sec;
		private TablePane pane;

		public Timer(TablePane pane, int sec) {
			this.pane = pane;
			this.sec = sec;
		}

		@Override
		public void run() {
			System.out.println("Timer started");
			while (true) {
				try {
					Thread.sleep(1000);
					sec++;
					pane.setSec(sec);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
