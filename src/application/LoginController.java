package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	Database library;

	@FXML
	private MenuItem exit, about;
	@FXML
	private Button GoBack;
	@FXML
	private Button AdminLoginButton;
	@FXML
	private TextField adminUserName;
	@FXML
	private PasswordField adminPassword;


	
	@FXML
	void AdminLoginButton(ActionEvent event) throws Exception {
		try(Database db = new Database()) {
				if(db.verifyLogin(adminUserName.getText(), adminPassword.getText())) {
				Parent Admin_parent = FXMLLoader.load(getClass().getResource("AdminStartPage.fxml"));
				Scene Admin_scene = new Scene(Admin_parent);
				Stage app_stage  = (Stage) ((Node) event.getSource()).getScene().getWindow();
				app_stage.setScene(Admin_scene);
				app_stage.show();
			}
			else  {
				Alert incorrect = new Alert(AlertType.INFORMATION);
				incorrect.setTitle("Failed login");
				incorrect.setHeaderText(null);
				incorrect.setContentText("Incorrect login-credentials!");
				incorrect.showAndWait();
			}
		}
	}

	@FXML
	void GoBack(ActionEvent event) throws IOException {
		Parent  My_View_parent = FXMLLoader.load(getClass().getResource("MyView.fxml"));
		Scene My_View_scene = new Scene(My_View_parent);
		Stage app_stage  = (Stage) ((Node) event.getSource()).getScene().getWindow();
		app_stage.setScene(My_View_scene);
		app_stage.show();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	@FXML
	void exitProgram(ActionEvent event) {
		Platform.exit();

	}
	
	@FXML
	void aboutMenuButton(ActionEvent event) {
		Alert addBook = new Alert(AlertType.INFORMATION);
		addBook.setTitle("Library System");
		addBook.setHeaderText("Made by :Tim Eklund & Marcus Danielsson");
		addBook.setContentText("SQLite | Java | JavaFX | jBCrypt");
		addBook.showAndWait();
		
	}
}
