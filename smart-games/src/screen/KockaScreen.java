package screen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

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
import kocka.KockaVezer;

public class KockaScreen extends Application {

	protected MenuBar menuBar;

	protected Menu mMain;	// Főmenü 
	protected MenuItem mExit, mSize, mNew, mOpen, mTeszt;

	protected Menu mKocka;	// Fejtés menűje
	protected MenuItem mRestart, mSave, mStop;
	protected MenuItem mNext, mPrev;

	protected MenuItem miOrig;	// Az adott step-et kiindulási állapotába hozza
	protected MenuItem miEnd;	// Az adott step-et végállapotába hozza
	protected Menu mForgatasok;	// Forgatási lehetőségek, az adott step-en

	private VBox root;

	private KockaVezer kocka;
	
	private Stage primaryStage;
	private SizePane sizePane;
	private KockaPane kockaPane;

	private boolean isPlaying = false;
	private int stepIndex = 0;		// A megoldás során hányadik lépésnél járunk (0) a kiinduló állapot
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		root = new VBox(10);
		primaryStage.setTitle("Kocka kirakó segítség");
		primaryStage.setScene(new Scene(root, 500, 500)); // meret
		primaryStage.setResizable(true); // Az ablak meretezheto
		primaryStage.centerOnScreen(); // Az ablak kozepen

		// A fomenu
		menuBar = new MenuBar();
		mMain = new Menu("Főmenü");
		menuBar.getMenus().addAll(mMain);

		// File menu
		mSize = new MenuItem("Méret beállítása");
		sizePane = new SizePane();
		mSize.setOnAction(event -> changePane(sizePane));
		
		mNew = new MenuItem("Új kocka");
		mNew.setOnAction(this::startKevertKocka);
		
		mTeszt = new MenuItem("Teszt kocka");
		mTeszt.setOnAction(this::startTesztKocka);
		
		mOpen = new MenuItem("Korábbi kocka betöltése");
		mOpen.setOnAction(this::continueOldKocka);
		
		mExit = new MenuItem("Exit");
		mMain.getItems().addAll(mSize, mNew, mTeszt, mOpen, mExit);

		// jatekmenu
		mKocka = new Menu("Kocka");

		mRestart = new Menu("Újra indítás");
		mRestart.setOnAction(this::restartKocka);
		
		mSave = new Menu("Save");
		mSave.setOnAction(this::saveFile);

		mStop = new Menu("Stop");
		mStop.setOnAction(this::stopKocka);

		mNext = new Menu("Következő állapot");
		mNext.setOnAction(this::nextStep);
		mPrev = new Menu("Előző állapot");
		mPrev.setOnAction(this::prevStep);

		mKocka = new Menu("Kocka");
		//mKocka.setOnAction(this::keveres);
		
		mForgatasok = new Menu("Forgatások");
		miOrig = new MenuItem("Set Orig");
		miOrig.setOnAction(this::setOrig);
		miEnd = new MenuItem("Set End");
		miEnd.setOnAction(this::setEnd);
		
		setStepsMenu();

		mKocka.getItems().addAll(mRestart, mStop, mSave, mNext, mPrev);
		
		root.getChildren().add(menuBar);
		root.getChildren().add(new Pane()); // Placeholder panel

		// exit
		primaryStage.setOnCloseRequest(event -> System.exit(0));
		mExit.setOnAction(event -> System.exit(0));

		primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
			System.out.println("Width: " + newVal);
			if ( kockaPane != null ) {
				kockaPane.setTableSize();
				kockaPane.repaint();
			}
		});

		primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
			System.out.println("Height: " + newVal);
			if ( kockaPane != null ) {
				kockaPane.setTableSize();
				kockaPane.repaint();
			}
		});

		// Az ablak megjelenitese
		primaryStage.show();
	}

	MenuItem [] miForgat = new MenuItem [0];
	/**
	 * Az aktuális step-nek megfelelő menü
	 */
	private void setStepsMenu() {
		if ( kocka == null )
			return;
		mKocka.getItems().remove(mPrev);
		if ( stepIndex != 0 )
			mKocka.getItems().add(mPrev);
		
		mKocka.getItems().remove(mNext);
		if ( stepIndex != kocka.getCntSteps() )
			mKocka.getItems().add(mNext);
		
		menuBar.getMenus().remove(mForgatasok);
		if ( stepIndex > 0 ) {
			mForgatasok.setText("Step("+stepIndex+") Target: " + kocka.getName(stepIndex));
			menuBar.getMenus().add(mForgatasok);
			mForgatasok.getItems().remove(miOrig);
			mForgatasok.getItems().remove(miEnd);
			for ( MenuItem mi : miForgat ) {
				mForgatasok.getItems().remove(mi);
			}
			mForgatasok.getItems().add(miOrig);
			mForgatasok.getItems().add(miEnd);
			String [] forgatasok = kocka.getCommands(stepIndex);
			miForgat = new MenuItem [forgatasok.length];
			int i=0;
			for ( String f : forgatasok ) {
				miForgat[i] = new MenuItem(f);
				miForgat[i].setOnAction(this::forgat);
				mForgatasok.getItems().add(miForgat[i]);
				i++;
			}
			kockaPane.setResult( kocka.getResult(stepIndex));
		}
	}
	/**
	 * Az adott step-en belül beállítja a kiindulási állapotot
	 * @param ev
	 */
	private void setOrig(ActionEvent ev) {
		kocka.setOrig(stepIndex);
		kockaPane.repaint();
	}
	/**
	 * Az adott step-en belül beállítja a kiindulási állapotot
	 * @param ev
	 */
	private void setEnd(ActionEvent ev) {
		kocka.setEnd(stepIndex);
		kockaPane.repaint();
	}
	/**
	 * Panelek közötti váltás
	 * -- Main, Kocka
	 * @param newPane
	 */
	private void changePane(Pane newPane) {
		root.getChildren().set(1, newPane);
		VBox.setVgrow(newPane, Priority.ALWAYS);
		menuBar.getMenus().removeAll(mMain, mKocka);
		if (isPlaying)
			kockaMenu();
		else
			mainMenu();
	}

	/**
	 * Főmenü felrakása
	 */
	private void mainMenu() {
		menuBar.getMenus().addAll(mMain);
	}

	/**
	 * Kocka menű felrakása
	 */
	private void kockaMenu() {
		menuBar.getMenus().addAll(mKocka);
	}

	/**
	 * Egy új, összekevert kocka indítása
	 * @param ev
	 */
	private void startKevertKocka(ActionEvent ev) {
		createKocka();
		kocka.setRandomTable();
		kocka.initGame();
		startKockaPane();
	}
	/**
	 * Egy új, teszt kocka indítása
	 * @param ev
	 */
	private void startTesztKocka(ActionEvent ev) {
		createKocka();
		kocka.setTesztTable();
		kocka.initGame();
		startKockaPane();
	}

	/**
	 * Kocka létrehozása (kapcsolatteremtés a logikával)
	 */
	private void createKocka() {
		kocka = new KockaVezer();	// Todo size alapján választjuk ki, de inkább a másik oldalon
		kocka.setType( "4x4x4" );			// Todo ez változó
		kocka.createKocka();
	}
	/**
	 * Kocka visszaállítása a kiinduló helyzetbe
	 * @param ev
	 */
	private void restartKocka(ActionEvent ev) {
		stepIndex = 0;
		setStepsMenu();
		kocka.restart();
		kockaPane.restartGame();
		kockaPane.repaint();
	}

	private void stopKocka(ActionEvent ev) {
		isPlaying = false;
		// kocka.stopGame();	TODO kell ez?
		kockaPane.stopGame();
		changePane(new Pane());
	}

	private void forgat(ActionEvent ev) {
		//kocka.forgat("jobb");
		MenuItem mi = (MenuItem)ev.getSource();
		String command = mi.getText();
		kocka.forgat(command);
		kockaPane.repaint();
	}
	private void nextStep(ActionEvent ev) {
		if ( stepIndex == kocka.getCntSteps() )
			return;
		stepIndex++;
		kocka.setActTable(stepIndex);	
		setStepsMenu();
		kockaPane.repaint();
	}
	private void prevStep(ActionEvent ev) {
		if ( stepIndex == 0 )
			return;
		stepIndex--;
		kocka.setActTable(stepIndex);	
		setStepsMenu();
		kockaPane.repaint();
	}

	private void startKockaPane() {
		isPlaying = true;
		stepIndex = 0;
		setStepsMenu();
		kockaPane = new KockaPane(primaryStage, kocka);
		kockaPane.setTableSize();
		kockaPane.startGame();
		changePane(kockaPane);
	}

	private void saveFile(ActionEvent ev) {
		try {
			FileWriter myWriter = new FileWriter("oldgame.txt");
			// kocka.saveFile(myWriter); TODO meg kell írni
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private void continueOldKocka(ActionEvent ev) {
		try {
			File myObj = new File("oldgame.txt");
			Scanner myReader = new Scanner(myObj);
			// kocka = new Kocka(myReader); TODO meg kell írni
			kocka.initGame();
			myReader.close();
			startKockaPane();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			System.out.println("Check the file: " + "oldgame.txt");
		}
	}

	public static void main(String[] args) {
		System.out.println("Program started");
		launch(args);
		System.out.println("End of program");
	}
}
