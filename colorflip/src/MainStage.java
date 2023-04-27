import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainStage extends Application {

	protected MenuBar menuBar;
	protected Menu mMain, mGame;
	protected MenuItem mExit, mSize, mNew, mOpen;
	protected MenuItem mRestart, mSolver, mSave, mStop;
	private VBox root;
	private Colorflip colorflip;
	private Stage primaryStage;
	private SizePane sizePane;
	private TablePane tablePane;
	private double width, height;

	private boolean isPlaying = false;
	private boolean isSolverMode = false;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		root = new VBox(10);
		primaryStage.setTitle("Color Flip");
		primaryStage.setScene(new Scene(root, 500, 500)); // meret
		primaryStage.setResizable(true); // Az ablak meretezheto
		primaryStage.centerOnScreen(); // Az ablak kozepen

		// A fomenu
		menuBar = new MenuBar();
		mMain = new Menu("File");
		menuBar.getMenus().addAll(mMain);

		// File menu
		mSize = new MenuItem("Change Size");
		sizePane = new SizePane();
		mSize.setOnAction(event -> changePane(sizePane));
		mNew = new MenuItem("New Game");
		mNew.setOnAction(this::startNewGame);
		mOpen = new MenuItem("Continue old game");
		mOpen.setOnAction(this::continueOldGame);
		mExit = new MenuItem("Exit");
		mMain.getItems().addAll(mSize, mNew, mOpen, mExit);

		// jatekmenu
		mGame = new Menu("Game");

		mRestart = new Menu("Restart");
		mRestart.setOnAction(this::restartGame);
		mSolver = new Menu("Set solver on");
		mSolver.setOnAction(this::changeSolverMode);
		mSave = new Menu("Save");
		mSave.setOnAction(this::saveFile);
		mStop = new Menu("Stop");
		mStop.setOnAction(this::stopGame);
		mGame.getItems().addAll(mRestart, mSolver, mSave, mStop);

		root.getChildren().add(menuBar);
		root.getChildren().add(new Pane()); // Placeholder panel

		// exit
		primaryStage.setOnCloseRequest(event -> System.exit(0));
		mExit.setOnAction(event -> System.exit(0));

		primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
			System.out.println("Width: " + newVal);
			width = (double) newVal;
			if (tablePane != null)
				tablePane.setTableSize(width, height);
		});

		primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
			System.out.println("Height: " + newVal);
			height = (double) newVal;
			if (tablePane != null)
				tablePane.setTableSize(width, height);
		});

		// Az ablak megjelenitese
		primaryStage.show();
	}

	private void changePane(Pane newPane) {
		root.getChildren().set(1, newPane);
		VBox.setVgrow(newPane, Priority.ALWAYS);
		menuBar.getMenus().removeAll(mMain, mGame);
		if (isPlaying)
			gameMenu();
		else
			mainMenu();
		if (isSolverMode)
			mSolver.setText("Set solver off");
		else
			mSolver.setText("Set solver on");
	}

	private void mainMenu() {
		menuBar.getMenus().addAll(mMain);
	}

	private void gameMenu() {
		menuBar.getMenus().addAll(mGame);
	}

	private void startNewGame(ActionEvent ev) {
		colorflip = new Colorflip();
		colorflip.newGame(sizePane.getSize());
		startGame();
	}

	private void restartGame(ActionEvent ev) {
		colorflip.startGame();
		tablePane.restartGame();
		tablePane.repaint();
	}

	private void stopGame(ActionEvent ev) {
		isPlaying = false;
		colorflip.stopGame();
		tablePane.stopGame();
		changePane(new Pane());
	}

	private void changeSolverMode(ActionEvent ev) {
		isSolverMode = !isSolverMode;
		tablePane.setSolverMode(isSolverMode);
		if (isSolverMode)
			mSolver.setText("Set solver off");
		else
			mSolver.setText("Set solver on");
		tablePane.repaint();
	}

	private void startGame() {
		isPlaying = true;
		isSolverMode = false;
		tablePane = new TablePane(primaryStage, colorflip);
		tablePane.setTableSize(width, height);
		tablePane.setSolverMode(isSolverMode);
		changePane(tablePane);
	}

	private void saveFile(ActionEvent ev) {
		try {
			FileWriter myWriter = new FileWriter("oldgame.txt");
			colorflip.saveFile(myWriter);
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private void continueOldGame(ActionEvent ev) {
		try {
			File myObj = new File("oldgame.txt");
			Scanner myReader = new Scanner(myObj);
			colorflip = new Colorflip(myReader);
			myReader.close();
			startGame();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			System.out.println("Check the file: " + "oldgame.txt");
		}
	}

	public static void main(String[] args) {
		System.out.println("Program started");
		if (args.length > 0 && args[0].equals("-nogui")) {
			Colorflip colorflip = new Colorflip();
			ColorflipConsole cc = new ColorflipConsole(colorflip);
			System.exit(1);
		} else
			launch(args);
		System.out.println("End of program");
	}
}
