package example;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class EventFilterDemo extends Application {
  private Scene scene;
  private BorderPane rootPane;
  private Button button;
  private MyEventHandler eventHandler = new MyEventHandler("handling");
  private MyEventHandler eventFilter = new MyEventHandler("filtering");


  @Override
  public void start(Stage primaryStage) throws Exception {
    button = new Button("Button");
    rootPane = new BorderPane(button);
    scene = new Scene(rootPane, 200, 200);

    // Ezt a Stage nem kapja meg
    scene.addEventFilter(MouseEvent.MOUSE_ENTERED, eventFilter);
    primaryStage.addEventFilter(MouseEvent.MOUSE_ENTERED, eventFilter);
    // Ez esetben viszont már a Stage-től indul a lánc
    //primaryStage.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET,eventFilter);
    // A láncon végig fog menni a kattintás esemény
    // Próbáljuk ki, hogy valahol elfogyasztjuk az eseményt!
    scene.addEventFilter(MouseEvent.MOUSE_CLICKED, eventFilter);
    rootPane.addEventFilter(MouseEvent.MOUSE_CLICKED, eventFilter);
    rootPane.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    scene.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    // Ezzel minden egéreseményt megkap (az EventType hierarchiában a MouseEvent.ANY leszármazottait)
    button.addEventFilter(MouseEvent.ANY, eventFilter);
    // Egy szemantikus esemény: nem csak egéreseményre kapja meg
    button.addEventHandler(ActionEvent.ACTION, eventHandler);


    primaryStage.setTitle("Event Filter Demo");
    primaryStage.setResizable(false);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private static class MyEventHandler implements EventHandler<Event> {
    private final boolean consume;
    private final String type;

    private MyEventHandler(boolean consume, String type) {
      this.consume = consume;
      this.type = type;
    }

    public MyEventHandler(String type) {
      this.type = type;
      this.consume = false;
    }

    @Override
    public void handle(Event event) {
      System.out.println(
          event.getSource() + " is " + type + " an event of type "
              + event.getEventType() + ": "
              + event);
      if (consume) {
        event.consume();
      }
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
