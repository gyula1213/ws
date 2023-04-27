package example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ExampleStage extends Application {

  protected MenuBar menuBar;
  protected Menu mFile, mJavaFX, mExtra;
  protected MenuItem mExit, mButton, mCheck, mText, mGraphics, mImage, mKeyboard, mMouse, mAnimation;
  private VBox root;

  @Override
  public void start(Stage primaryStage) throws Exception {
    root = new VBox(10);
    primaryStage.setTitle("Example"); // Az ablak címe
    primaryStage.setScene(new Scene(root, 500,500)); // Az ablak mérete és
    // layout-hierarchia kezdete
    primaryStage.setResizable(true); // Az ablak ne legyen átméretezhető
    primaryStage.centerOnScreen(); // Az ablak a képernyő közepén legyen

    // A menüsor elkészítése
    menuBar = new MenuBar();
    mFile = new Menu("File");
    mJavaFX = new Menu("JavaFX");
    mExtra = new Menu("Extra");
    menuBar.getMenus().addAll(mFile, mJavaFX, mExtra);

    mExit = new MenuItem("Exit");
    mFile.getItems().addAll(mExit);

    mButton = new MenuItem("Button");
    mCheck = new MenuItem("CheckBox");
    mText = new MenuItem("TextField");
    mJavaFX.getItems().addAll(mButton, mCheck, mText);

    mGraphics = new MenuItem("Graphics");
    mImage = new MenuItem("Image");
    mKeyboard = new MenuItem("Keyboard");
    mMouse = new MenuItem("Mouse");
    mAnimation = new MenuItem("Animation");
    mExtra.getItems().addAll(mGraphics, mImage, mKeyboard, mMouse, mAnimation);
    root.getChildren().add(menuBar);
    root.getChildren().add(new Pane());  // Placeholder panel

    // Eseménykezelés beállítása
    primaryStage.setOnCloseRequest(event -> System.exit(0));
    mExit.setOnAction(event -> System.exit(0));
    mButton.setOnAction(event -> changePane(new ButtonPane()));
    mCheck.setOnAction(event -> changePane(new CheckBoxPane()));
    mText.setOnAction(event -> changePane(new TextFieldPane()));
    mGraphics.setOnAction(event -> changePane(new GraphicsPane()));
    mImage.setOnAction(event -> changePane(new ImagePane()));
    mKeyboard.setOnAction(event -> changePane(new KeyBoardPane()));
    mMouse.setOnAction(event -> changePane(new MousePane()));
    mAnimation.setOnAction(event -> changePane(new AnimationPane()));

    // Az ablak megjelenítése
    primaryStage.show();
  }

  private void changePane(Pane newPane) {
    root.getChildren().set(1, newPane);
    VBox.setVgrow(newPane, Priority.ALWAYS);
  }

  public static void main(String[] args) {
    launch(args);
  }
}
