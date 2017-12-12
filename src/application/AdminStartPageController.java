package application;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminStartPageController {

	 @FXML
	    private Button GoBack;
	
	 

	    @FXML
	  
	    	 void GoBack(ActionEvent event) throws IOException {
	 	    	Parent  My_View_parent = FXMLLoader.load(getClass().getResource("MyView.fxml"));
	 	    	Scene My_View_scene = new Scene(My_View_parent);
	 	    	Stage app_stage  = (Stage) ((Node) event.getSource()).getScene().getWindow();
	 	    	app_stage.setScene(My_View_scene);
	 	    	app_stage.show();
	    }
	    }
	    
