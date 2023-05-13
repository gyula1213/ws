package screen;

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
import kocka.kocka4.Kocka4x4x4;

public class MainStage extends Application {

	protected MenuBar menuBar;
	protected Menu mMain, mGame;
	protected Menu mForgat;
	protected MenuItem mExit, mSize, mNew, mOpen, mCube;
	protected MenuItem mRestart, mSolver, mSave, mStop;
	protected MenuItem mKever;
	protected MenuItem mRestartCube;
	protected MenuItem mJobb, mJobb2, mFelul, mFelul2, mElol, mElol2, mBal, mAlul, mHatul, mJobbReteg, mBalReteg;
	protected MenuItem mNext, mPrev;
	private VBox root;
	private Colorflip colorflip;
	private Cubeable cube;
	private Stage primaryStage;
	private SizePane sizePane;
	private TablePane tablePane;
	private CubePane cubePane;
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
		mCube = new MenuItem("Kocka");
		mCube.setOnAction(this::startCube);
		mOpen = new MenuItem("Continue old game");
		mOpen.setOnAction(this::continueOldGame);
		mExit = new MenuItem("Exit");
		mMain.getItems().addAll(mSize, mNew, mCube, mOpen, mExit);

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

		mRestartCube = new Menu("Újra");
		mRestartCube.setOnAction(this::restartCube);
		mJobb = new MenuItem("Jobb szélsőt forgat");
		mJobb.setOnAction(this::jobb);
		mJobb2 = new MenuItem("Jobb szélső kettőt forgat");
		mJobb2.setOnAction(this::jobb2);
		mFelul = new MenuItem("Felsőt forgat");
		mFelul.setOnAction(this::felul);
		mFelul2 = new MenuItem("Felső kettőt forgat");
		mFelul2.setOnAction(this::felul2);
		mElol = new MenuItem("Elöl forgat");
		mElol.setOnAction(this::elol);
		mElol2 = new MenuItem("Első kettőt forgat");
		mElol2.setOnAction(this::elol2);
		mBal = new MenuItem("bal oldalon forgat");
		mBal.setOnAction(this::bal);
		mAlul = new MenuItem("Alul forgat");
		mAlul.setOnAction(this::alul);
		mHatul = new MenuItem("Hátul forgat");
		mHatul.setOnAction(this::hatul);
		mJobbReteg = new MenuItem("Jobb réteget forgat");
		mJobbReteg.setOnAction(this::jobbreteg);
		mBalReteg = new MenuItem("Bal réteget forgat");
		mBalReteg.setOnAction(this::balreteg);
		mNext = new Menu("Következő állapot");
		mNext.setOnAction(this::nextStage);
		mPrev = new Menu("Előző állapot");
		mPrev.setOnAction(this::prevStage);
		mKever = new Menu("Véletlenszerű összekeverés");
		mKever.setOnAction(this::keveres);
		mForgat = new Menu("Forgatások");

		mGame.getItems().addAll(mRestartCube, mSolver, mSave, mStop, mNext, mPrev, mKever);
		mForgat.getItems().addAll(mJobb, mJobb2, mFelul, mFelul2, mElol, mElol2, mBal, mAlul, mHatul, mJobbReteg, mBalReteg);

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
		menuBar.getMenus().addAll(mGame, mForgat);
	}

	private void startNewGame(ActionEvent ev) {
		colorflip = new Colorflip();
		colorflip.newGame(sizePane.getSize());
		startGame();
	}

	private void startCube(ActionEvent ev) {
		cube = new Kocka4x4x4(2);
		cube.initGame();
		startCube();
	}

	private void restartCube(ActionEvent ev) {
		//colorflip.startGame();
		cubePane.restartGame();
		cubePane.repaint();
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

	private void jobb(ActionEvent ev) {
		cubePane.forgat("jobb");
		cubePane.repaint();
	}
	private void jobb2(ActionEvent ev) {
		cubePane.forgat("jobb2");
		cubePane.repaint();
	}
	private void jobbreteg(ActionEvent ev) {
		cubePane.forgat("jobb_reteg");
		cubePane.repaint();
	}
	private void felul(ActionEvent ev) {
		cubePane.forgat("felul");
		cubePane.repaint();
	}
	private void felul2(ActionEvent ev) {
		cubePane.forgat("felul2");
		cubePane.repaint();
	}
	private void elol(ActionEvent ev) {
		cubePane.forgat("elol");
		cubePane.repaint();
	}
	private void elol2(ActionEvent ev) {
		cubePane.forgat("elol2");
		cubePane.repaint();
	}
	private void bal(ActionEvent ev) {
		cubePane.forgat("bal");
		cubePane.repaint();
	}
	private void balreteg(ActionEvent ev) {
		cubePane.forgat("bal_reteg");
		cubePane.repaint();
	}
	private void alul(ActionEvent ev) {
		cubePane.forgat("alul");
		cubePane.repaint();
	}
	private void hatul(ActionEvent ev) {
		cubePane.forgat("hatul");
		cubePane.repaint();
	}
	private void nextStage(ActionEvent ev) {
		cubePane.nextStage();
		cubePane.repaint();
	}
	private void prevStage(ActionEvent ev) {
		cubePane.prevStage();
		cubePane.repaint();
	}
	private void keveres(ActionEvent ev) {
		cubePane.keveres();
		cubePane.repaint();
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

	private void startCube() {
		isPlaying = true;
		isSolverMode = false;
		cubePane = new CubePane(primaryStage, cube);
		cubePane.setTableSize(width, height);
		changePane(cubePane);
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
			//ColorflipConsole cc = new ColorflipConsole(colorflip);
			System.exit(1);
		} else
			launch(args);
		System.out.println("End of program");
	}
}
