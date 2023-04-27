package example;

//import all the relevant classes
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
//main class
public class TimerProgramSample extends Application
{
	//set the delay as 0
	int del = 0;
	public void start(Stage st) {
		UIinitialisation(st);
	}
	private void UIinitialisation(Stage st) {
		//create object for horizantal box
		HBox hb = new HBox(12);
		//set the padding
		hb.setPadding(new Insets(12));
		//create object for timer class
		Timer tm = new java.util.Timer();
		//create object for spinner class
		Spinner sp = new Spinner(1, 62, 5);
		//set the prefernce width
		sp.setPrefWidth(85);
		//create button
		Button b = new Button("Yayyy. . . Timer works. . .");
		//set the action event on clicking the button
		b.setOnAction(event -> {
			del = (int) sp.getValue();
			//schedule the timer
			tm.schedule(new subtimer(), del*1000);
		});
		//get the children of horizontal box
		hb.getChildren().addAll(b, sp);
		//on close event
		st.setOnCloseRequest(event -> {
			tm.cancel();
		});
		//create a scene
		Scene sc = new Scene(hb);
		//set the title
		st.setTitle("Timer Working");
		//set the scene
		st.setScene(sc);
		//display the result
		st.show();
	}
	//subclass that extends the TimerTask
	private class subtimer extends TimerTask {
		//run method
		@Override
		public void run() {
			//method
			Platform.runLater(() -> {
				//create object for Alert class
				Alert al = new Alert(Alert.AlertType.INFORMATION);
				//set the title
				al.setTitle("Dialog box");
				//set the header text
				al.setHeaderText("Oh oh.. Time elapsed");
				//create a string
				String c;
				//check the condition of delay
				if (del == 1) {
					//display one second is elapsed
					c = "1 sec elapsed";
				}
				else
				{
					c = String.format("%d sec elapsed",	del);
				}
				al.setContentText(c);
				al.showAndWait();
			});
		}
	}
	//main method
	public static void main(String[] args) {
		//launch the app
		launch(args);
	}
}