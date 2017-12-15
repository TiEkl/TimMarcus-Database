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
	 static Database library;
	 static Customer customer;

	 @FXML
	 private ToggleGroup removeBookCategory;
	 
	 @FXML RadioButton isbnSelected, titleSelected;
	 
	
    @FXML
    private Tab manageCustomer, manageBooks;

    @FXML
    private TextField addTitle, addStreet, addAuthor, costumerIdUpdate, addCustomerName, addCity,
    addPhoneNr, addISBN, addPublisher, addQuantity, addCardID, addGenre, addPages, addShelf, textSearchRemove, selectedBook,
    showCustomerName, showCustomerPhone, showCustomerCity, showCustomerStreet, showCustomerCardID;


    @FXML
    private Button logOut, addCustomerButton, addBook, searchRemove, clearAddBookForm, selectCustomer, searchUpdateCustomer;

    @FXML
    private TableView<Customer> updateCustomerTable;
    @FXML private TableColumn<Customer, String> nameCustomer;
    @FXML private TableColumn<Customer, String> phoneCustomer;
    @FXML private TableColumn<Customer, String> cityCustomer;
    @FXML private TableColumn<Customer, String> streetCustomer;
    @FXML private TableColumn<Customer, String> cardIDCustomer;

    @FXML
    private TabPane adminManageTab;

	@FXML private TableView<Book> removeResult;
	
    @FXML private TableColumn<Book, String> titleCol;
    @FXML private TableColumn<Book, String> authorCol;
    @FXML private TableColumn<Book, String> genreCol;
    @FXML private TableColumn<Book, Long> isbnCol;
    @FXML private TableColumn<Book, Integer> quantityCol;
    @FXML private TableColumn<Book, Integer> bookIDCol;
  
@FXML
void searchRemoveBook(ActionEvent event) throws SQLException {
	titleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
	authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
	isbnCol.setCellValueFactory(new PropertyValueFactory<Book, Long>("isbn"));
	quantityCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("quantity"));
	bookIDCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("book_id"));

	removeResult.setItems(getBook());
}
@FXML
void searchUpdateCustomer(ActionEvent event) throws SQLException {
	nameCustomer.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
	phoneCustomer.setCellValueFactory(new PropertyValueFactory<Customer, String>("phone_nr"));
	cityCustomer.setCellValueFactory(new PropertyValueFactory<Customer, String>("city"));
	streetCustomer.setCellValueFactory(new PropertyValueFactory<Customer, String>("street"));
	cardIDCustomer.setCellValueFactory(new PropertyValueFactory<Customer, String>("card_id"));

	updateCustomerTable.setItems(getCustomer());
	
}



@FXML
void selectCustomer(ActionEvent event) throws SQLException {

Customer customer =	updateCustomerTable.getSelectionModel().getSelectedItem();

showCustomerName.setText(customer.getName());
showCustomerPhone.setText(customer.getPhoneNr());
showCustomerCity.setText(customer.getCity());
showCustomerStreet.setText(customer.getStreet());
showCustomerCardID.setText(String.valueOf(customer.getCard_id()));
}




public ObservableList<Customer> getCustomer() throws SQLException {
	
	
	int card_id  = Integer.valueOf(costumerIdUpdate.getText());
	
	
	Customer current = library.getCustomer(card_id);
	ObservableList<Customer> customer = FXCollections.observableArrayList();
	
		customer.addAll(current);
	
	return customer;
}


public ObservableList<Book> getBook() throws SQLException {
	String searchField = textSearchRemove.getText();
	String category="";
	
	
	if(isbnSelected.isSelected()) {
		category = "isbn";
	}
	
	else if(titleSelected.isSelected()) {
		category = "title";
		
	}
	
	Book [] searchArray = library.search(searchField, category);
	ObservableList<Book> book = FXCollections.observableArrayList();
	for(int i = 0; i < searchArray.length; i++) {
		book.add(searchArray[i]);
	}
	return book;
	
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

    
    @FXML
    void addBook(ActionEvent event) throws SQLException {
    	String isbn =addISBN.getText();
    	String title = addTitle.getText();
    	String author = addAuthor.getText();
    	String genre = addGenre.getText();
    	int shelf = Integer.valueOf(addShelf.getText());
    	String publisher = addPublisher.getText();
    	int quantity = Integer.valueOf(addQuantity.getText());
    	int pages = Integer.valueOf(addPages.getText());
    	
     	Alert addBook = new Alert(AlertType.INFORMATION);
    	addBook.setTitle("New Book");
    	addBook.setHeaderText("The book was successfully added to the Library");
    	addBook.setContentText("Title: " + title);
    	addBook.showAndWait();
    	
    	library.addBook(isbn, title, author, genre, shelf, publisher, quantity, pages);
    	clearAddBookForm(event);
    }
    @FXML
    void addCustomer(ActionEvent event) throws SQLException {
    
    	int card_id = Integer.valueOf(addCardID.getText());
    String name = addCustomerName.getText();
    String street = addStreet.getText();
    String city = addCity.getText();
    String phone_nr = addPhoneNr.getText();
    

    	library.addCustomer(card_id, name, city, street, phone_nr );
    	Alert addCustomer = new Alert(AlertType.INFORMATION);
   
    	addCustomer.setTitle("New Customer");
    	addCustomer.setHeaderText("Customer successfully added");
    	addCustomer.setContentText( name + " was added as a customer, with Card ID: " + card_id);
    	addCustomer.showAndWait();
    	clearCustomerForm();
    	
    }
    @FXML
    void searchRemoveButton(ActionEvent event) throws SQLException {
    	Book book = removeResult.getSelectionModel().getSelectedItem();
    	String title = book.getTitle();
    	int book_id =book.getBook_ID();
    	Alert remove = new Alert(AlertType.CONFIRMATION);
    	remove.setTitle("You're about to delete a book from the system");
    	remove.setHeaderText("Are you sure you want to delete this title?");
    	remove.setContentText("title: " + title);
    	

    	
    	Optional<ButtonType> result = remove.showAndWait();
    	if (result.get() == ButtonType.OK){
    		library.removeBook(book_id);
    		searchRemoveBook(event);
    	} else if ((result.get() == ButtonType.CANCEL)) {
    		
    	}
    	
      }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			library = new Database();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void removeBook(int book_id) throws SQLException {
		
	 	
	 	library.removeBook(book_id);
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
}
