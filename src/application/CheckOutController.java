package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;

public class CheckOutController implements Initializable {
	@FXML
	private MenuItem exit, about ;
	final String EOL = System.lineSeparator();
	private static Database library;
	List<Integer> choiceWeeks = new ArrayList<>();
	private final ObservableList<Book> checkoutData = FXCollections.observableArrayList();
	// Event Listener on MenuItem[#exit].onAction
    private Button AdvSearch, Toplist, CheckOut, GoBack, MyBooks, removeSelected, borrowBooks, IDScan;
    private int cardID;
 	@FXML TextField IDScanText, showNameField;

	@FXML private TableView<Book> checkoutTable;
	@FXML private TableColumn<Book, String> TitleCol;
	@FXML private TableColumn<Book, String> AuthorCol;
	@FXML private TableColumn<Book, String> GenreCol;
	@FXML private TableColumn<Book, Integer> PagesCol;
	@FXML private TableColumn<Book, String> PublisherCol;
	@FXML private TableColumn<Book, Long> ISBNCol;
	@FXML private TableColumn<Book, Integer> QuantityCol;
	@FXML private TableColumn<Book, Integer> Book_idCol;
	
	
	    
	@FXML
	void borrowBooksButton(ActionEvent event) throws Exception {
		int amountWeeks;
		ChoiceDialog<Integer> dialog = new ChoiceDialog<>(1, choiceWeeks);
		dialog.setTitle("Choose the number of weeks");
		dialog.setHeaderText(null);
		dialog.setContentText("Choose the number of weeks to borrow the books: ");
		
		Optional<Integer> result = dialog.showAndWait();
		
		if(result.isPresent()) {
			amountWeeks = result.get();
		}
		else {
			return;
		}
		try(Database db = new Database()) {
			
			String borrowSuccess = db.addBorrowedList(Integer.valueOf(IDScanText.getText()), amountWeeks);
			Alert addBook = new Alert(AlertType.INFORMATION);
			addBook.setTitle("Borrow Books");
			addBook.setHeaderText(null);
			addBook.setContentText(borrowSuccess);
			addBook.showAndWait();

			checkoutTable.getItems().clear();
			IDScanText.clear();


		}
		checkoutTable.getItems().clear();
		IDScanText.clear();
	}
	    
	@FXML 
	void  IDScanButton(ActionEvent event) throws Exception{
		cardID =  Integer.valueOf(IDScanText.getText());
		try (Database db = new Database()) {
			Customer current = db.getCustomer(cardID);
			showNameField.setText(current.getName());
		}
	}
	    
	@FXML
	void removeSelectedBook(ActionEvent event) throws SQLException, Exception {
		Book removal = checkoutTable.getSelectionModel().getSelectedItem();
		checkoutTable.getItems().remove(removal);
		Main.checkoutData.removeFromCheckout(removal);

		try (Database db = new Database()) {
			Main.checkoutData.removeFromCheckout(removal);
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
	
	/*static void setList(ArrayList<Book> list) {
		checkoutList = list;
	}
	
	public ObservableList<Book> getCheckout() throws SQLException{
		
		ObservableList<Book> book = FXCollections.observableArrayList();
		for(int i = 0; i < checkoutList.size(); i++) {
			book.add(checkoutList.get(i));
		}
		//book.addAll(checkoutList);
		return book;
	}*/
	public ObservableList<Book> getCheckoutData() {
		return checkoutData;
	}
	public void setCheckoutData() {
		
		ArrayList<Book> checkoutList = Main.checkoutData.getCheckoutList();
		int size = checkoutList.size();
		for(int i = 0; i < checkoutList.size(); i++) {
			checkoutData.add(checkoutList.get(i));
		}
	}
	public void initialize(URL location, ResourceBundle resources) {
		//set up the columns in the table
		TitleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		AuthorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
		GenreCol.setCellValueFactory(new PropertyValueFactory<Book, String>("genre"));
		PublisherCol.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
		PagesCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("pages"));
		ISBNCol.setCellValueFactory(new PropertyValueFactory<Book, Long>("isbn"));
		QuantityCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("quantity"));
		
		setCheckoutData();
		checkoutTable.setItems(getCheckoutData());
		
		IDScanText.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		            IDScanText.setText(newValue.replaceAll("[^\\d]", ""));
		        }
	            if (IDScanText.getText().length() > 4) {
	                String s = IDScanText.getText().substring(0, 4);
	                IDScanText.setText(s);
	            }
		    }
		});
		
		choiceWeeks.add(1);
		choiceWeeks.add(2);
		choiceWeeks.add(4);
		choiceWeeks.add(8);
		
	}
	void initData(ArrayList<Book> list) throws SQLException {
		
		for(int i = 0; i < list.size(); i++) {
			checkoutData.add(list.get(i));
		}
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
