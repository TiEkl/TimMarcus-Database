package application;




import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.sun.glass.ui.Accessible.EventHandler;
import com.sun.glass.ui.MenuBar;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.application.*;

public class MyViewController implements Initializable {

	Book book;
	AdvancedSearchController advsearch;
	String searchResultText;	
	private static String textSearch;
	private static String searchCategory = "title";

	public String getTextSearch() {
		return textSearch;
	}

	public void setTextSearch(String textSearch) {
		MyViewController.textSearch = textSearch;
	}

	public  String getSearchCategory() {
		return searchCategory;
	}

	public  void setSearchCategory(String searchCategory) {
		MyViewController.searchCategory = searchCategory;
	}

	@FXML
	private ToggleGroup RadioOption;
	@FXML
	private RadioButton RadioAuthor, RadioTitle;
	@FXML
	private Button Toplist, AdvSearch, CheckOut, SearchButton, MyBooks, GoToAdminLogin;
	@FXML
	private AnchorPane rootView;
	@FXML
	private MenuItem exit, EnterAdminLogin, about;
	@FXML TextField Search;

	@FXML
	void Search(ActionEvent event) {
		advsearch.setTextSearch(Search.getText()) ;
	}

	@FXML
	private ToggleGroup radioOption;
	@FXML
	void GoToAdminLogin(ActionEvent event) throws IOException, SQLException {
		//library.closeConn();
		Parent Admin_Login_parent = FXMLLoader.load(getClass().getResource("Login.fxml"));
		Scene Admin_Login_scene = new Scene(Admin_Login_parent);

		Stage app_stage  = (Stage) ((Node) event.getSource()).getScene().getWindow();

		app_stage.setScene(Admin_Login_scene);
		app_stage.show();
	}


	@FXML 
	public void onEnter(ActionEvent ae) throws IOException, SQLException {
		SearchButton(ae);
	}



	@FXML
	void SearchButton(ActionEvent event) throws IOException, SQLException{
		//library.closeConn();
		setTextSearch(Search.getText());

		if(RadioTitle.isSelected()) {
			setSearchCategory("title");
		}

		else if(RadioAuthor.isSelected()) {

			setSearchCategory("author");

		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdvancedSearch.fxml"));

		Parent root = (Parent) loader.load();

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		stage.setScene(new Scene(root));
		stage.show();
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




	@FXML
	void AdvSearch(ActionEvent event) throws IOException, SQLException {

		if(RadioTitle.isSelected()) {
			setSearchCategory("title");
		}
		else if(RadioAuthor.isSelected()) {
			setSearchCategory("author");
		}
		FXMLLoader loader = new FXMLLoader(getClass().getResource("AdvancedSearch.fxml"));
		Parent root = (Parent) loader.load();

		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		stage.setScene(new Scene(root));
		stage.show();
	}




	public void RadioButtons() {
		ToggleGroup toggleGroup = new ToggleGroup();

		RadioAuthor.setToggleGroup(toggleGroup);
		RadioTitle.setToggleGroup(toggleGroup);

	}



	@FXML
	void EnterMyBorrowedBooks(ActionEvent event) throws IOException {
		Parent My_Books_parent = FXMLLoader.load(getClass().getResource("MyBooks.fxml"));
		Scene My_Books_scene = new Scene(My_Books_parent);
		Stage app_stage  = (Stage) ((Node) event.getSource()).getScene().getWindow();
		app_stage.setScene(My_Books_scene);
		app_stage.show();

	}

	@FXML
	void GoToToplist(ActionEvent event) throws IOException {
		Parent Toplist_parent = FXMLLoader.load(getClass().getResource("Toplist.fxml"));
		Scene Toplist_scene = new Scene(Toplist_parent);
		Stage app_stage  = (Stage) ((Node) event.getSource()).getScene().getWindow();
		app_stage.setScene(Toplist_scene);
		app_stage.show();

	}

	@FXML
	void GoToCheckOut(ActionEvent event)  throws IOException {
		Parent CheckOut_parent = FXMLLoader.load(getClass().getResource("CheckOut.fxml"));
		Scene CheckOut_scene = new Scene(CheckOut_parent);
		Stage app_stage  = (Stage) ((Node) event.getSource()).getScene().getWindow();
		app_stage.setScene(CheckOut_scene);
		app_stage.show();

	}



	@FXML
	void EnterAdminLogin(ActionEvent event) throws IOException, SQLException {
		GoToAdminLogin(event);
	}

	public void initialize(URL location, ResourceBundle resources) {

	}
	

}
