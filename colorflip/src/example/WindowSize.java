package example;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author KENSOFT
 */
public class WindowSize extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Label lbl_size = new Label();
        
        StackPane root = new StackPane();
        root.getChildren().add(lbl_size);
        
        Scene scene = new Scene(root, 582, 448);
        
        primaryStage.setTitle("Window Size");
        primaryStage.setScene(scene);
        primaryStage.show();
        // The line of code below is here is to get the window size
//        lbl_size.textProperty().bind(Bindings.format("Window size: %1$.0fx%2$.0f", primaryStage.widthProperty(), primaryStage.heightProperty()));
//        lbl_size.textProperty().bind(Bindings.format("Window size: %1$.0fx%2$.0f", scene.widthProperty(), scene.heightProperty()));
        lbl_size.textProperty().bind(Bindings.format("Window size: %1$.0fx%2$.0f", root.widthProperty(), root.heightProperty()));
        System.out.println(Bindings.format("Window size: %1$.0fx%2$.0f", root.widthProperty(), root.heightProperty()));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}