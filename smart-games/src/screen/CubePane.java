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

public class CubePane extends BorderPane {
	private Stage screen;

	private Text resultText = new Text();
	private Text timeText = new Text();
	
	private HBox centerBox = new HBox();
	private Canvas canvas;

	private Cubeable cube;
	private int size;
	private double width;
	private double height;
	private double widthField;
	private boolean isSolverMode = false;

	private Timer timer;
	private int sec = 0;
	private String clock;
	private boolean isReady;

	private int tableIndex = 0;		// A megoldás során hányadik lépésnél járunk (0) a kiinduló állapot
	/**
	 * A jatek tablaja
	 */
	public CubePane(Stage screen, Cubeable cube) {
		this.screen = screen;
		this.cube = cube;

		// Canvas in Center
		canvas = new Canvas();
		canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, this::mousePressed);
		centerBox.setPadding(new Insets(10, 0, 10, 50));
		centerBox.getChildren().addAll(canvas);
		setCenter(centerBox);
		
		// Result on top
		if (cube.getCnt() == 0) {
			resultText.setFill(Color.BLUE);
			resultText.setText("Good luck!");
		}
		else {
			resultText.setFill(Color.BLACK);
			resultText.setText("Number of steps: " + cube.getCnt());
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
		cube.restart();
		startGame();
	}

	public void startGame() {
		size = cube.getSize();
		sec = cube.getSec();
		setSec(sec);
		timer = new Timer(this, sec);
		timer.start();
		isReady = false;
		repaint();
	}

	private double posX[] = new double[6];
	private double posY[] = new double[6];
	/**
	 * 
	 */
	private void paintTable(GraphicsContext gc, int[] tomb) {
		for (int k = 0; k < 6; k++) {	// Lapok
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					setColor( gc, tomb[k*size*size+4*j+i] );
					gc.fillRect(posX[k] + i * widthField, posY[k] + j * widthField, widthField, widthField);
					gc.setStroke(Color.WHITE);
					gc.strokeRect(posX[k] + i * widthField, posY[k] + j * widthField, widthField, widthField);
				}
			}
		}
	}
	private void setColor(GraphicsContext gc, int col) {
		String [] cols = cube.getColors();
	    //private static String [] colors = {"x", "F", "P", "K", "N", "Z", "S"};
		switch( cols[col] )
		{
		case "x": 
			gc.setFill(Color.GRAY);
			break;
		case "F": 
			gc.setFill(Color.BLACK);
			break;
		case "P": 
			gc.setFill(Color.RED);
			break;
		case "K": 
			gc.setFill(Color.BLUE);
			break;
		case "N": 
			gc.setFill(Color.ORANGE);
			break;
		case "Z": 
			gc.setFill(Color.GREEN);
			break;
		case "S": 
			gc.setFill(Color.YELLOW);
			break;
		}
	}

	public void repaint() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		canvas.setCursor(Cursor.CROSSHAIR);
		canvas.setWidth(4 * size * this.widthField);
		canvas.setHeight(3 * size * this.widthField);
		posX[0] = size * this.widthField;
		posY[0] = 0;
		posX[1] = size * this.widthField;
		posY[1] = size * this.widthField;
		posX[2] = 2 * size * this.widthField;
		posY[2] = size * this.widthField;
		posX[3] = 3 * size * this.widthField;
		posY[3] = size * this.widthField;
		posX[4] = 0;
		posY[4] = size * this.widthField;
		posX[5] = size * this.widthField;
		posY[5] = 2 * size * this.widthField;
		gc.clearRect(0, 0, width, height);
		cube.setActTable(tableIndex);
		paintTable(gc, cube.getActTable());
	}
	
	public void forgat( String name) {
		cube.forgat(name);
	}

	public void nextStage() {
		tableIndex++;
	}
	public void prevStage() {
		tableIndex--;
	}

	private void mousePressed(MouseEvent ev) {
		if (isReady) {
			resultText.setFill(Color.BROWN);
			resultText.setText("You are ready in " + cube.getCommands(tableIndex) + " steps. Time: " + clock);
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
		//cube.step(col, row);
		if (cube.checkActTable()) {
			isReady = true;
			resultText.setFill(Color.GREEN);
			resultText.setText("You are ready in " + cube.getCnt() + " steps. Time: " + clock);
			stopGame();
		} else {
			resultText.setFill(Color.BLACK);
			resultText.setText("Number of steps: " + cube.getCnt());
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
		cube.setSec(sec);
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
		private CubePane pane;

		public Timer(CubePane pane, int sec) {
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
