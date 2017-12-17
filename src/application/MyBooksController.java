package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MyBooksController implements Initializable {
	@FXML
	private MenuItem exit;

	// Event Listener on MenuItem[#exit].onAction
	@FXML
	public void exitProgram(ActionEvent event) {
		// TODO Autogenerated
	}
	@FXML
	private Button AdvSearch, Toplist, CheckOut, GoBack, MyBooks, enterCardIDButton;

	@FXML
	private TextField nameInfo, IDScan;


	@FXML private TableView<BorrowedBook> result;
	@FXML private TableColumn<BorrowedBook, String> borrowedTitleCol;
	@FXML private TableColumn<BorrowedBook, String> borrowedDateCol;
	@FXML private TableColumn<BorrowedBook, String> borrowedReturnCol;
	@FXML private TableColumn<BorrowedBook, Integer> borrowedDaysCol;
	@FXML private TableColumn<BorrowedBook, Integer> borrowedBookIDCol;


	int IDScanNumber;


	@FXML
	void enterCardIDButton(ActionEvent event) throws Exception {
		String IDScanString= IDScan.getText();
		IDScanNumber = Integer.valueOf(IDScanString);

		result.setItems(getBorrowedBook());
		try(Database db = new Database()) {
			Customer current = db.getCustomer(IDScanNumber);
			nameInfo.setText(current.getName());
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

	@FXML
	void AdvSearch(ActionEvent event) throws IOException {
		Parent Advanced_Search_parent = FXMLLoader.load(getClass().getResource("AdvancedSearch.fxml"));
		Scene Advanced_Search_scene = new Scene(Advanced_Search_parent);
		Stage app_stage  = (Stage) ((Node) event.getSource()).getScene().getWindow();
		app_stage.setScene(Advanced_Search_scene);
		app_stage.show();

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

	public void initialize(URL location, ResourceBundle resources) {

		//set up the columns in the table
		borrowedTitleCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("title"));
		borrowedDateCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("borrowedDate"));
		borrowedReturnCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("returnDate"));
		borrowedDaysCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, Integer>("days"));
		borrowedBookIDCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, Integer>("book_id"));

	}

	public ObservableList<BorrowedBook> getBorrowedBook() throws Exception{
		ObservableList<BorrowedBook> book = FXCollections.observableArrayList();

		try(Database data = new Database()) {
			BorrowedBook [] searchArray=data.getBorrowedBooks(IDScanNumber);
			for(int i =0; i<searchArray.length; i++) {
				book.add(searchArray[i]);
			}
		}
		return book; 
	}

}