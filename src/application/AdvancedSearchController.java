package application;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AdvancedSearchController implements Initializable   {
	static MyViewController myview ;
	Book bookClass;
	static Database library ;
	@FXML
	private AnchorPane rootPane;
	@FXML
	private MenuItem exit, about;
	

	@FXML
	private TextField Search, SearchAuthor;
	
	private static String searchCategory;
	
	public String textSearch;

	@FXML private TableView<Book> result;
	@FXML private TableColumn<Book, String> TitleCol;
	@FXML private TableColumn<Book, String> AuthorCol;
	@FXML private TableColumn<Book, String> GenreCol;
	@FXML private TableColumn<Book, Integer> PagesCol;
	@FXML private TableColumn<Book, String> PublisherCol;
	@FXML private TableColumn<Book, Long> ISBNCol;
	@FXML private TableColumn<Book, Integer> QuantityCol;
	@FXML private TableColumn<Book, Integer> Book_idCol;
	@FXML private TableColumn<Book, Double> RatingCol;

    @FXML
    private Button AdvSearch, Toplist, CheckOut, SearchButton, GoBack, MyBooks, AddSelectedBook, AddCheckOut, generateToplist;

	public String getTextSearch() {
		return textSearch;
	}

	public void setTextSearch(String textSearch) {
		this.textSearch = textSearch;
	}
 
   
    
    @FXML
    void SearchButton(ActionEvent event) throws Exception {
    		
    	MyViewController MyViewCo = new MyViewController();
    
	    	if(Search.getText().trim().isEmpty()){
	    		MyViewCo.setSearchCategory("author");
	    		MyViewCo.setTextSearch(SearchAuthor.getText());
	    	}
	    	else if(SearchAuthor.getText().trim().isEmpty()) {
	    		MyViewCo.setSearchCategory("title");
	    		MyViewCo.setTextSearch(Search.getText());
	    	}
	    	else if(!SearchAuthor.getText().trim().isEmpty() && !Search.getText().trim().isEmpty()) {		
	    		
	    		String titleText = Search.getText();
	    		String authorText = SearchAuthor.getText();
	    		
	    		result.setItems(getBook(true, titleText, authorText));
	    		return;
	    	}
	    	result.setItems(getBook(true));
    }

	@FXML
	void SearchAuthor(ActionEvent event) throws IOException {
		setTextSearch(Search.getText()); 
	}

	@FXML 
	public void onEnter(ActionEvent ae) throws Exception {
		SearchButton(ae);
	}

	@FXML
	void AddToCheckOut(ActionEvent event) throws SQLException {
		Book aBook = result.getSelectionModel().getSelectedItem();
		int book_id = aBook.getBook_ID();
		System.out.println(book_id);
		Main.checkoutData.addToCheckoutList(aBook);
	}
	@FXML
	void generateToplist(ActionEvent event) throws Exception {
		result.setItems(getBook(false));
	}
	@FXML
	void Search(ActionEvent event) {

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
	void GoToCheckOut(ActionEvent event)  throws IOException, SQLException {
		Parent CheckOut_parent = FXMLLoader.load(getClass().getResource("CheckOut.fxml"));
		Scene CheckOut_scene = new Scene(CheckOut_parent);
		Stage app_stage  = (Stage) ((Node) event.getSource()).getScene().getWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CheckOut.fxml"));
		app_stage.setScene(CheckOut_scene);
		loader.load();

		app_stage.show();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		myview = new MyViewController();
		//set up the columns in the table
		TitleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		AuthorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
		GenreCol.setCellValueFactory(new PropertyValueFactory<Book, String>("genre"));
		PublisherCol.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
		PagesCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("pages"));
		ISBNCol.setCellValueFactory(new PropertyValueFactory<Book, Long>("isbn"));
		QuantityCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("quantity"));
		Book_idCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("book_id"));
		RatingCol.setCellValueFactory(new PropertyValueFactory<Book, Double>("rating"));
		
		try {
			result.setItems(getBook(true));
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		result.setRowFactory((tableView) -> {
		      return new TooltipTableRow<Book>((Book book) -> {
		        return book.getCoverURL();
		      });
		});
		
       /* result.setRowFactory(tv -> new TableRow<Book>() {
            private Tooltip hoverImage = new Tooltip();
            @Override
            public void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                if (book == null) {
                    setTooltip(null);
                } else {
                    hoverImage.setText(book.getTitle()+", written by:  "+book.getAuthor());
                    setTooltip(hoverImage);
                }
            }
        });*/
	}

	public ObservableList<Book> getBook(boolean searchMethod, String...strings) throws Exception{

		Book [] searchArray;
		ObservableList<Book> book = FXCollections.observableArrayList();
		if (searchMethod) {
			if (strings.length >= 2) {
				try(Database db = new Database()) {
					searchArray = db.searchAuthorTitle(strings[0], strings[1]);
					for(int i = 0; i < searchArray.length; i++) {
						book.add(searchArray[i]);
					}
				}
			}
			else if (strings.length <= 1) {
				try(Database db = new Database()) {
					searchArray = db.search(myview.getTextSearch(), myview.getSearchCategory());
					for(int i =0; i<searchArray.length; i++) {
						book.add(searchArray[i]);
					} 
				}
			}	
		}
		else if (!searchMethod) {
			try (Database db = new Database()) {
				searchArray = db.getTop10();
				for(int i =0; i<searchArray.length; i++) {
					book.add(searchArray[i]);
				}
			}
		}
		return book;
	}
	

	public void setStringResultAuthor(String text) {
		SearchAuthor.setText(text);	

	}

	public void setStringResultTitle(String text) {
		Search.setText(text);

	}

	public  String getSearchCategory() {
		return searchCategory;
	}

	public void setSearchCategory(String searchCategory) {
		AdvancedSearchController.searchCategory = searchCategory;
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