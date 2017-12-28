package application;
	
import java.awt.Button;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	//static Database library;
	static staticData checkoutData;
	public void start(Stage primaryStage) {
		try {
			try ( Database db = new Database()) {
				db.createHistoryTable();
			}
			checkoutData = new staticData();
			Parent root = FXMLLoader.load(getClass().getResource("/application/MyView.fxml"));
			Scene scene = new Scene(root,1080,750);

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Library System");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		launch(args);
	}
}
