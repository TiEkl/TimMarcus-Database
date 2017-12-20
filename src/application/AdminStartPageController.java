package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminStartPageController implements  Initializable {
	static Customer customer;


	@FXML
	private ToggleGroup removeBookCategory;

	@FXML RadioButton isbnSelected, titleSelected;


	@FXML
	private Tab manageCustomer, manageBooks, borrowedBooks, BorrowedBy, allBorrowedBooks, allDelayedBooks;

	@FXML
	private TextField addTitle, addStreet, addAuthor, costumerIdUpdate, addCustomerName, addCity,
	addPhoneNr, addISBN, addPublisher, addQuantity, addCardID, addGenre, addPages, addShelf, textSearchRemove, selectedBook,
	showCustomerName, showCustomerPhone, showCustomerCity, showCustomerStreet, showCustomerCardID, IDScan, borrowedName;


	@FXML
	private Button logOut, addCustomerButton, addBook, searchRemove, clearAddBookForm, selectCustomer,
	searchUpdateCustomer, confirmUpdateCustomer, searchBorrowedByButton, removeCustomer, getAllDelayedButton;

	@FXML
	private TableView<Customer> updateCustomerTable;
	@FXML private TableColumn<Customer, String> nameCustomer;
	@FXML private TableColumn<Customer, String> phoneCustomer;
	@FXML private TableColumn<Customer, String> cityCustomer;
	@FXML private TableColumn<Customer, String> streetCustomer;
	@FXML private TableColumn<Customer, String> cardIDCustomer;

	@FXML
	private TabPane adminManageTab, borrowedBooksTab;

	@FXML private TableView<BorrowedBook> borrowedByTable, allDelayedTable, allBorrowedTable;
	@FXML private TableView<Book> removeResult;
	@FXML private TableColumn<Book, String> titleCol;
	@FXML private TableColumn<Book, String> authorCol;
	@FXML private TableColumn<Book, String> genreCol;
	@FXML private TableColumn<Book, Long> isbnCol;
	@FXML private TableColumn<Book, Integer> quantityCol;
	@FXML private TableColumn<Book, Integer> bookIDCol;
	@FXML private TableColumn<BorrowedBook, String> delayedTitleCol, delayedReturnCol ,borrowedByTitleCol, borrowedByAuthorCol, borrowedByGenreCol;
	@FXML private TableColumn<BorrowedBook, String> allBorrowedTitleCol, allBorrowedBorrowedDateCol, allBorrowedReturnCol;
	@FXML private TableColumn<BorrowedBook, Long> borrowedByIsbnCol;
	@FXML private TableColumn<BorrowedBook, Integer> borrowedByQuantityCol, borrowedByBookIDCol, allBorrowedCardCol, allBorrowedDaysCol, allBorrowedbookIDCol, delayedCardCol, delayedDaysCol;

	Customer selectedCustomer;

	@FXML
	void searchRemoveBook(ActionEvent event) throws Exception {

		removeResult.setItems(getBook());
		
	}

	@FXML 
	void confirmUpdateCustomer(ActionEvent event) throws Exception{
		int card_id = Integer.valueOf(showCustomerCardID.getText());

		if(!selectedCustomer.getName().equals(showCustomerName.getText())) {
			try(Database db = new Database()) {
				db.updateCustomer("name", showCustomerName.getText(), card_id);
			}
		}
		if(!selectedCustomer.getPhoneNr().equals(showCustomerPhone.getText())) {
			try(Database db = new Database()) {
				db.updateCustomer("phone_nr", showCustomerPhone.getText(), card_id);
			}
		}
		if(!selectedCustomer.getCity().equals(showCustomerCity.getText())) {
			try(Database db = new Database()) {
				db.updateCustomer("city", showCustomerCity.getText(), card_id);
			}
		}
		if(!selectedCustomer.getStreet().equals(showCustomerStreet.getText())) {
			try(Database db = new Database()) {
				db.updateCustomer("street", showCustomerStreet.getText(), card_id);
			}
		}
		searchUpdateCustomer(event);
	}

	@FXML
	void searchUpdateCustomer(ActionEvent event) throws Exception {
		updateCustomerTable.setItems(getCustomer());

	}

	@FXML
	void selectCustomer(ActionEvent event) throws SQLException {

		selectedCustomer =	updateCustomerTable.getSelectionModel().getSelectedItem();
		showCustomerName.setText(selectedCustomer.getName());
		showCustomerPhone.setText(selectedCustomer.getPhoneNr());
		showCustomerCity.setText(selectedCustomer.getCity());
		showCustomerStreet.setText(selectedCustomer.getStreet());
		showCustomerCardID.setText(String.valueOf(selectedCustomer.getCard_id()));
	}

	public ObservableList<Customer> getCustomer() throws Exception {
		int card_id  = Integer.valueOf(costumerIdUpdate.getText());
		ObservableList<Customer> customerList = FXCollections.observableArrayList();
		try(Database db = new Database()) {
			Customer current = db.getCustomer(card_id);
			customerList.addAll(current);

		}
		return customerList;
	}

	@FXML
	void removeCustomer(ActionEvent event) throws Exception {
		try(Database db = new Database()) {
			int card_id = Integer.valueOf(showCustomerCardID.getText());
			String name = showCustomerName.getText();
			Alert remove = new Alert(AlertType.CONFIRMATION);
			remove.setTitle("You're about to delete a customer from the system");
			remove.setHeaderText("Are you sure you want to delete this customer?");
			remove.setContentText("Name: " + name + " with Card ID : " + card_id);
			Optional<ButtonType> result = remove.showAndWait();
			if (result.get() == ButtonType.OK){

				db.removeCustomer(card_id);
				showCustomerName.clear();
				showCustomerPhone.clear();
				showCustomerCity.clear();
				showCustomerStreet.clear();
				showCustomerCardID.clear();
				costumerIdUpdate.clear();

			} else if ((result.get() == ButtonType.CANCEL)) {

			}
		}
		
	}



	@FXML
	void getAllDelayedButton(ActionEvent event) throws SQLException, Exception {
		//try(Database db = new Database()) {
			allDelayedTable.setItems(getDelayedBook());	
		//}
	}
	@FXML
	void allDelayedBooks() throws Exception {
		//try(Database db = new Database()) {
			//db.getDelayedBooksList();
			allDelayedTable.setItems(getDelayedBook());

		//} 
	}



	int IDScanNumber;

	@FXML
	void searchBorrowedByButton (ActionEvent event) throws SQLException, Exception  {

		String IDScanString= IDScan.getText();
		IDScanNumber = Integer.valueOf(IDScanString);

		borrowedByTable.setItems(getBorrowedBook());
		try(Database db = new Database()) {
			Customer current = db.getCustomer(IDScanNumber);
			borrowedName.setText(current.getName());

		}
	}
	public ObservableList<BorrowedBook> getAllBorrowedBooks() throws Exception{
		ObservableList<BorrowedBook> book = FXCollections.observableArrayList();


		try(Database data = new Database()) {
			BorrowedBook [] searchArray = data.getBorrowedBooks();
			for(int i =0; i<searchArray.length; i++) {
				book.add(searchArray[i]);
			}
		}
		return book; 
	}
	public ObservableList<BorrowedBook> getBorrowedBook() throws Exception{
		ObservableList<BorrowedBook> book = FXCollections.observableArrayList();

		try(Database data = new Database()) {
			BorrowedBook [] searchArray = data.getBorrowedBooks(IDScanNumber);
			for(int i =0; i<searchArray.length; i++) {
				book.add(searchArray[i]);
			}
		}
		return book; 
	}

	public ObservableList<BorrowedBook> getDelayedBook() throws Exception{
		
		ObservableList<BorrowedBook> book = FXCollections.observableArrayList();
		try(Database data = new Database()) {
			BorrowedBook [] searchArray = data.getDelayedBooksList();
			for(int i = 0; i<searchArray.length; i++) {
				book.add(searchArray[i]);
			}
		}
		//Book [] searchArray = Main.library.getBorrowedBooks(IDScanNumber);
		return book; 
	}


	public ObservableList<Book> getBook() throws Exception {
		String searchField = textSearchRemove.getText();
		String category = "";
		ObservableList<Book> book = FXCollections.observableArrayList();

		if(isbnSelected.isSelected()) {
			category = "isbn";
		}

		else if(titleSelected.isSelected()) {
			category = "title";

		}
		try(Database db = new Database()) {

			Book [] searchArray = db.search(searchField, category);
			for(int i = 0; i < searchArray.length; i++) {
				book.add(searchArray[i]);
			}
		}
		return book;

	}


	@FXML
	void addBook(ActionEvent event) throws Exception {
		String isbn =addISBN.getText();
		String title = addTitle.getText();
		String author = addAuthor.getText();
		String genre = addGenre.getText();
		int shelf = Integer.valueOf(addShelf.getText());
		String publisher = addPublisher.getText();
		int quantity = Integer.valueOf(addQuantity.getText());
		int pages = Integer.valueOf(addPages.getText());

		try(Database db = new Database()) {
			db.addBook(isbn, title, author, genre, shelf, publisher, quantity, pages);
		}
		Alert addBook = new Alert(AlertType.INFORMATION);
		addBook.setTitle("New Book");
		addBook.setHeaderText("The book was successfully added to the library");
		addBook.setContentText("Title: " + title);
		addBook.showAndWait();
		clearAddBookForm(event);
	}
	@FXML
	void addCustomer(ActionEvent event) throws Exception {

		int card_id = Integer.valueOf(addCardID.getText());
		String name = addCustomerName.getText();
		String street = addStreet.getText();
		String city = addCity.getText();
		String phone_nr = addPhoneNr.getText();

		try(Database db = new Database()) {
			db.addCustomer(card_id, name, city, street, phone_nr );	
		}

		Alert addCustomer = new Alert(AlertType.INFORMATION);

		addCustomer.setTitle("New Customer");
		addCustomer.setHeaderText("Customer successfully added");
		addCustomer.setContentText( name + " was added as a customer, with Card ID: " + card_id);
		addCustomer.showAndWait();
		clearCustomerForm();

	}
	@FXML
	void searchRemoveButton(ActionEvent event) throws Exception {
		
		Book book = removeResult.getSelectionModel().getSelectedItem();
		String title = book.getTitle();
		int book_id =book.getBook_ID();
		Alert remove = new Alert(AlertType.CONFIRMATION);
		remove.setTitle("You're about to delete a book from the system");
		remove.setHeaderText("Are you sure you want to delete this title?");
		remove.setContentText("title: " + title);
		Optional<ButtonType> result = remove.showAndWait();
		if (result.get() == ButtonType.OK){

			try(Database db = new Database()) {
				db.removeBook(book_id);
				
			}
			searchRemoveBook(event);
		} else if ((result.get() == ButtonType.CANCEL)) {

		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Remove book-table Cell factories
		
		titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
		isbnCol.setCellValueFactory(new PropertyValueFactory<Book, Long>("isbn"));
		quantityCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("quantity"));
		bookIDCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("book_id"));
		//Customer Table Cell Factories
		nameCustomer.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
		phoneCustomer.setCellValueFactory(new PropertyValueFactory<Customer, String>("phoneNr"));
		cityCustomer.setCellValueFactory(new PropertyValueFactory<Customer, String>("city"));
		streetCustomer.setCellValueFactory(new PropertyValueFactory<Customer, String>("street"));
		cardIDCustomer.setCellValueFactory(new PropertyValueFactory<Customer, String>("card_id"));
		//BorrowedBy-table Cell Factories
		borrowedByTitleCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("title"));
		borrowedByAuthorCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("author"));
		borrowedByIsbnCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, Long>("isbn"));
		borrowedByQuantityCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, Integer>("quantity"));
		borrowedByBookIDCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, Integer>("book_id"));
		//All delayed-table Cell factories
		delayedCardCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, Integer>("card_id"));
		delayedTitleCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("title"));
		delayedReturnCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("returnDate"));
		delayedDaysCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, Integer>("days"));
		//All borrowed-table Cell factories
		allBorrowedCardCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, Integer>("card_id"));
		allBorrowedTitleCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("title"));
		allBorrowedBorrowedDateCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("borrowedDate"));
		allBorrowedReturnCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, String>("returnDate"));
		allBorrowedDaysCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, Integer>("days"));
		allBorrowedbookIDCol.setCellValueFactory(new PropertyValueFactory<BorrowedBook, Integer>("book_id"));

		/*try {
			allBorrowedTable.setItems(getAllBorrowedBooks());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public void removeBook(int book_id) throws Exception {
		try(Database db = new Database()) {
			db.removeBook(book_id);
		}
	}
	@FXML
	void logOut(ActionEvent event) throws IOException {
		Parent  My_View_parent = FXMLLoader.load(getClass().getResource("MyView.fxml"));
		Scene My_View_scene = new Scene(My_View_parent);
		Stage app_stage  = (Stage) ((Node) event.getSource()).getScene().getWindow();
		app_stage.setScene(My_View_scene);
		app_stage.show();
	}

	public void RadioButtons() {
		ToggleGroup toggleGroup = new ToggleGroup();

		isbnSelected.setToggleGroup(toggleGroup);
		titleSelected.setToggleGroup(toggleGroup);

	}

	@FXML
	void clearAddBookForm(ActionEvent event) {
		addTitle.clear();
		addAuthor.clear();
		addGenre.clear();
		addPublisher.clear();
		addPages.clear();
		addQuantity.clear();
		addISBN.clear();
		addShelf.clear();

	}

	public void clearCustomerForm() {
		addCustomerName.clear();
		addPhoneNr.clear();
		addCity.clear();
		addCardID.clear();
		addStreet.clear();
	}

}