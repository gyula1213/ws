package example
;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class EventHandlingDemo extends Application {
  @Override
  public void start(Stage primaryStage) throws Exception {
    FlowPane root = new FlowPane();
    Button innerClass = new Button("Inner class");
    Button anonymousClass = new Button("Anonymous class");
    Button lambda = new Button("Lambda");
    Button methodReference = new Button("Method reference");
    root.getChildren().addAll(innerClass,anonymousClass,lambda,methodReference);

    // Eseménykezelők regisztrálása
    innerClass.setOnAction(textWriter);
    anonymousClass.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        System.out.println(event);
      }
    });
    lambda.setOnAction(event -> System.out.println(event));
    methodReference.setOnAction(System.out::println);

    // Ablak megnyitása
    primaryStage.setScene(new Scene(root));
    primaryStage.setTitle("Event Handling Demo");
    primaryStage.show();
  }

  private class TextWriter implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
      System.out.println(event);
    }
  }

  private TextWriter textWriter = new TextWriter();

  public static void main(String[] args) {
    launch(args);
  }
}
