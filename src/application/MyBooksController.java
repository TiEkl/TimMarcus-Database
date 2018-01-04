package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.awt.List;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MyBooksController implements Initializable {
	@FXML
	private MenuItem exit, about;

	// Event Listener on MenuItem[#exit].onAction
	
	@FXML
	private Button AdvSearch, Toplist, CheckOut, GoBack, MyBooks, enterCardIDButton, returnBookButton;

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
	void returnBookButton(ActionEvent event) throws SQLException, Exception {
		Book aBook = result.getSelectionModel().getSelectedItem();

		int cardID =Integer.valueOf(IDScan.getText());
		double returnRating;
		String title = aBook.getTitle();
		try(Database db = new Database()) {
			Alert remove = new Alert(AlertType.CONFIRMATION);
			remove.setTitle("Return book");
			remove.setHeaderText("Would you want to rate this title?");
			remove.setContentText("title: " + title);
			Optional<ButtonType> result = remove.showAndWait();
			if (result.get() == ButtonType.OK){

				ArrayList<Integer> choices = new ArrayList<Integer>();
				choices.add(1);
				choices.add(2);
				choices.add(3);
				choices.add(4);
				choices.add(5);

				ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, choices);
				dialog.setTitle("Rate");
				dialog.setHeaderText("Set your rating");
				dialog.setContentText("Rate between 1-5:");

				Optional<Integer> optionResult = dialog.showAndWait();
				if (optionResult.isPresent()){
					int test = (optionResult.get()); 
					returnRating = ((double) test);

					db.returnBook(cardID, aBook.getBook_ID(), returnRating);
					enterCardIDButton(event);
				}
				else {
					db.returnBook(cardID, aBook.getBook_ID(), 0);
					
				}

			} else if ((result.get() == ButtonType.CANCEL)) {
				db.returnBook(cardID, aBook.getBook_ID(), 0);
				enterCardIDButton(event);
			}
		}
	}

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
		
		//Make textfield IDScan integer only
		IDScan.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		            IDScan.setText(newValue.replaceAll("[^\\d]", ""));
		        }
	            if (IDScan.getText().length() > 4) {
	                String s = IDScan.getText().substring(0, 4);
	                IDScan.setText(s);
	            }
		    }
		});
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
	@FXML
	void exitProgram(ActionEvent event) {
		Platform.exit();

	}
	
	@FXML
	void aboutMenuButton(ActionEvent event) {
		Alert addBook = new Alert(AlertType.INFORMATION);
		addBook.setTitle("Library System");
		addBook.setHeaderText("Made by :Tim Eklund & Marcus Danielsson");
		addBook.setContentText("SQLite | Java | JavaFX");
		addBook.showAndWait();
		
	}

}