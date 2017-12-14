package application;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
	private MenuItem exit;


    @FXML
    private Button AdvSearch, Toplist, CheckOut, SearchButton, GoBack, MyBooks, AddSelectedBook, AddCheckOut, generateToplist;
   
    
    @FXML
    private TextField Search, SearchAuthor;
	
	public String textSearch;
	
	
	
	   
	
	public String getTextSearch() {
		return textSearch;
	}



	public void setTextSearch(String textSearch) {
		this.textSearch = textSearch;
	}
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

    //final ObservableList<Book> data = FXCollections.observableArrayList(book1, book2);
 
    @FXML
    void SearchAuthor(ActionEvent event) throws IOException {
    	 setTextSearch(Search.getText());
    	 
    }
   
    @FXML
    void exitProgram(ActionEvent event) {
    }
    
    @FXML 
    public void onEnter(ActionEvent ae) throws IOException, SQLException {
  	  SearchButton(ae);
    }

    @FXML
    void SearchButton(ActionEvent event) throws SQLException {
    		
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
	    	//initialize(null, null);

    }

    @FXML
    void AddToCheckOut(ActionEvent event) {

    }
    
 

    @FXML
    void generateToplist(ActionEvent event) throws SQLException {
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
    void GoToCheckOut(ActionEvent event)  throws IOException {
    	Parent CheckOut_parent = FXMLLoader.load(getClass().getResource("CheckOut.fxml"));
    	Scene CheckOut_scene = new Scene(CheckOut_parent);
    	Stage app_stage  = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	app_stage.setScene(CheckOut_scene);
    	app_stage.show();

    }

  

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			library = new Database ();
			 myview = new MyViewController();
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	
	}
	public ObservableList<Book> getBook(boolean searchMethod, String...strings) throws SQLException{
		
		Book [] searchArray;
		ObservableList<Book> book = FXCollections.observableArrayList();
		if (searchMethod) {
			if (strings.length >= 2) {
				searchArray = library.searchAuthorTitle(strings[0], strings[1]);
				for(int i = 0; i < searchArray.length; i++) {
					book.add(searchArray[i]);
				}
			}
			else if (strings.length <= 1) {
				searchArray=library.search(myview.getTextSearch(), myview.getSearchCategory());
				for(int i =0; i<searchArray.length; i++) {
					book.add(searchArray[i]);
				} 
			}	
		}
		else if (!searchMethod) {
			searchArray=library.getTop10();
			for(int i =0; i<searchArray.length; i++) {
				book.add(searchArray[i]);
			}
		}
		
		return book;
	}
	/* public ObservableList<Book> getToplist() throws SQLException{
		
		ObservableList<Book> book = FXCollections.observableArrayList();
		
		
	
		} 
	
		
		return book;
	}
	*/
	
	public void setStringResultAuthor(String text) {
		
	SearchAuthor.setText(text);	
	//setTextSearch(text) ;
	//setSearchCategory("author");
	
	}
	public void setStringResultTitle(String text) {
		
		Search.setText(text);
		//setTextSearch(text) ;
		//setSearchCategory("title");
		}
	
	private static String searchCategory;

	public  String getSearchCategory() {
		return searchCategory;
	}


	public void setSearchCategory(String searchCategory) {
		AdvancedSearchController.searchCategory = searchCategory;
	}
	
	/*
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Title.setCellValueFactory(new PropertyValueFactory<Database, String>("id"));
        Author.setCellValueFactory(new PropertyValueFactory<Database, String>("name"));
        Genre.setCellValueFactory(new PropertyValueFactory<Database, String>("active"));
		Pages.setCellValueFactory(new PropertyValueFactory<Database, String>("active"));
		Publisher.setCellValueFactory(new PropertyValueFactory<Database, String>("active"));
		Quantity.setCellValueFactory(new PropertyValueFactory<Database, String>("active"));
		ISBN.setCellValueFactory(new PropertyValueFactory<Database, String>("active"));
        
	}



	private Database[] parseUserList() {
		String result = "";
		ResultSet rs;
		Statement stmt;
		try {
			
			Connection conn = DriverManager.getConnection(Database.dbUrl);

		      
		     stmt = conn.createStatement();
		       rs = stmt.executeQuery("SELECT title, author, genre, pages, shelf  FROM books WHERE title LIKE '%two%'");
		      
		      while (rs.next()) {
		    	  String title = rs.getString("title");
		    	  String author = rs.getString("author");
		    	  String genre = rs.getString("genre");
		    	  String pages = rs.getString("pages");
		    	  String publisher = rs.getString("Publisher");
		    	  String Quantity = rs.getString("Quantity");
		    	  String ISBN = rs.getString("ISBN");
		    	  
		    	  
		return null;
	}
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, e);
		}
			return Database  ;
		}	*/
}