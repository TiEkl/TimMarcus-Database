package application;
import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.swing.JOptionPane;

import org.mindrot.jbcrypt.BCrypt;

public class Database  {
	static ArrayList<Book> checkoutList = new ArrayList<Book>();
	private static Book[] top10Array = new Book[10];
	static final String dbUrl = "jdbc:sqlite:./sqlite/db/library.db";
	final String EOL = System.lineSeparator();
	static Connection conn;
	Statement stmt;
	final long UNIXMONTH = 2592000;
	final long UNIXWEEK = 604800;
	public Database() throws SQLException { 
		createLibraryDatabase();
		stmt = conn.createStatement();
		createBooksTable();
		createBorrowedBooksTable();
		createCustomerTable();
		createHistoryTable();
		createAdminTable();
		createDebtTable();
		top10Array = createTop10();
		/*createAdmin("tiEkl", "hejhej123");
		createAdmin("maDan", "password");
		createAdmin("saBol", "1ab2c3");
		System.out.println("Admins created");*/
	}
	public void createAdmin(String username, String password) throws SQLException {
		String sql = "INSERT INTO admin (username, password) VALUES(?,?)";
		String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		PreparedUpdate(sql, username, encryptedPassword);
	}
	public void createLibraryDatabase() throws SQLException {
		File dir = new File("./sqlite/db");
		// attempt to create the directory here
		boolean successful = dir.mkdirs();
		if (successful)
		{
			// creating the directory succeeded
			System.out.println("directory was created successfully");
		}
		else
		{
			// creating the directory failed
			System.out.println("failed trying to create the directory");
		}
			conn = DriverManager.getConnection(dbUrl);
			if (conn != null) {
				System.out.println("A new database has been created.");
			}
	}
	public void createBooksTable() throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS books (" +
				"book_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+
				"title TEXT NOT NULL,  " +
				"author TEXT NOT NULL,  " +
				"genre TEXT NOT NULL, " +
				"shelf INTEGER NOT NULL, "+
				"publisher TEXT NOT NULL, " +
				"quantity INTEGER NOT NULL, " +
				"pages INTEGER NOT NULL, " +
				"isbn TEXT NOT NULL" +
				");";
		PreparedExecute(sql);
	}
	public void createBorrowedBooksTable()throws SQLException  {
		String sql = "CREATE TABLE IF NOT EXISTS borrowed_books (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
				"card_id INTEGER NOT NULL, "+
				"book_id INTEGER NOT NULL, " +
				"borrowed_epoch INTEGER(8) NOT NULL, " +
				"return_epoch INTEGER(8) NOT NULL " +
				");";
		PreparedExecute(sql);
	}
	public void createCustomerTable() throws SQLException  {
		String sql = "CREATE TABLE IF NOT EXISTS customer (" +
				"card_id INTEGER PRIMARY KEY NOT NULL,"+
				"name TEXT NOT NULL,  " +
				"city TEXT NOT NULL, " +
				"street TEXT NOT NULL," +
				"phone_nr TEXT NOT NULL" +
				");";
		PreparedExecute(sql);
	}
	public void createHistoryTable() throws SQLException  {
		String sql = "CREATE TABLE IF NOT EXISTS history (" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
				"card_id INTEGER NOT NULL, " +
				"book_id INTEGER NOT NULL,  " +
				"returned_on_time TEXT NOT NULL,  " +
				"rating REAL, " +
				" UNIQUE (card_id, book_id) ON CONFLICT REPLACE" +
				");";
		PreparedExecute(sql);
	}
	public void createDebtTable() throws SQLException  {
		String sql = "CREATE TABLE IF NOT EXISTS customer_debt (" +
				"card_id INTEGER PRIMARY KEY NOT NULL, "+
				"accumulated_fees INTEGER DEFAULT 0,  " +
				"paid INTEGER DEFAULT 0" +
				");";
		PreparedExecute(sql);
	}
	public void createAdminTable() throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS admin (" +
					"id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"username TEXT UNIQUE NOT NULL," +
					"password TEXT NOT NULL);";
		PreparedExecute(sql);
	}
	public void addBook(String isbn, String title, String author, String genre, int shelf, String publisher, int quantity, int pages) throws SQLException  {
		String sql = "INSERT INTO books " +
				"(title, author, genre, shelf, publisher, quantity, pages, isbn) " + EOL +
				"VALUES " +
				"(?,?,?,?,?,?,?,?);";
		PreparedUpdate(sql, title, author, genre, shelf, publisher, quantity, pages, isbn);

	}
	public void addCustomer(int card_id, String name, String city, String street, String phone_nr) throws SQLException {
		
		String sqlCustomer = "INSERT INTO customer " +
				"(card_id, name, city, street, phone_nr) " +
				"VALUES " +
				"(?,?,?,?,?)";
		PreparedUpdate(sqlCustomer, card_id, name, city, street, phone_nr);
		String sqlDebt ="INSERT INTO customer_debt" +
				"(card_id) " +
				"VALUES " +
				"(?);";
		PreparedUpdate(sqlDebt, card_id);
	}
	
	public void removeCustomer(int card_id) throws SQLException {
		String sql = "DELETE FROM customer WHERE card_id = ?";
		PreparedUpdate(sql, card_id);
	}
	public Customer[] getCustomerList() throws SQLException {
		
		String sql = "SELECT * FROM customer";
		ResultSet customerSet = PreparedQuery(sql);
		Customer[] customerArray = rsToCustomerArray(customerSet);
		return customerArray;
	}
	public Customer[] rsToCustomerArray(ResultSet customerSet) throws SQLException {
		ArrayList<Customer> customerList= new ArrayList<Customer>();
		String name, city, street, phoneNr;
		int card_id;
		while(customerSet.next()) {
			name = customerSet.getString("name");
			city = customerSet.getString("city");
			phoneNr = customerSet.getString("phone_nr");
			street = customerSet.getString("street");
			card_id = customerSet.getInt("card_id");
			Customer temp = new Customer(name, city, street, phoneNr, card_id);
			customerList.add(temp);	
		}
		Customer[] customerArray = customerList.toArray(new Customer[customerList.size()]);
		return customerArray;
	}
	public Customer rsToCustomer(ResultSet customerSet) throws SQLException {
		String name, city, street, phoneNr;
		int card_id;
		name = customerSet.getString("name");
		city = customerSet.getString("city");
		phoneNr = customerSet.getString("phone_nr");
		street = customerSet.getString("street");
		card_id = customerSet.getInt("card_id");
		Customer result = new Customer(name, city, street, phoneNr, card_id);
		return result;
	}

	public void updateCustomer(String category, String update, int card_id) throws SQLException {
		String sql = "UPDATE  customer SET "+category+" = ? WHERE card_id = ?";
		PreparedUpdate(sql, update, card_id);
	}
	public void addBorrowed(int book_id, int card_id, int nrWeeks) throws SQLException {
			long unixBorrowed = System.currentTimeMillis() / 1000L;
			long unixReturn = unixBorrowed + (UNIXWEEK * nrWeeks);
			String sql = "INSERT INTO borrowed_books" +
						"(book_id, card_id, borrowed_epoch, return_epoch)"  +
						"VALUES " +
						"(?,?,?,?);";
			PreparedUpdate(sql, book_id, card_id, unixBorrowed, unixReturn);
		}
	public String addBorrowedList(ArrayList<Book> checkOut,int card_id, int nrWeeks) throws SQLException {
		
		long unixBorrowed = System.currentTimeMillis() / 1000L;
		long unixReturn = unixBorrowed + (UNIXWEEK * nrWeeks);
		int length = checkOut.size();
		int book_id;
		String result;
		String sql = "INSERT INTO borrowed_books" +
				"(book_id, card_id, borrowed_epoch, return_epoch)"  +
				"VALUES ";
		for(int i = 0; i < length; i++) {
			book_id = checkOut.get(i).getBook_ID();
			if(checkIfAlreadyBorrowed(book_id, card_id)) {
				result = searchOneBook(book_id).alreadyBorrowed();
				return result;
			}
			PreparedUpdate(sql, book_id, card_id, unixBorrowed, unixReturn);
		}
		result = "You have successfully borrowed the following book(s): " + EOL;
		for(Book borrowed : checkOut) {
			result+= borrowed.successBorrow() + EOL;
		}
		checkoutList.clear();
		return result;
	}
	public  ArrayList<Book> getCheckoutList() {
		return checkoutList;
	}
	public boolean checkIfAlreadyBorrowed(int book_id, int card_id) throws SQLException {
		
		boolean result = false;
		BorrowedBook[] borrowedList = getBorrowedBooks(card_id);
		for(int i = 0; i < borrowedList.length; i++) {
			
			if(book_id == borrowedList[i].getBook_ID()) {
				result = true;
			}
		}
		return result;
	}
	public BorrowedBook[] getBorrowedBooks(int card_id) throws SQLException {
		String sql = "SELECT * FROM books INNER JOIN borrowed_books USING(book_id)"
					+ " WHERE card_id = ?";
		ResultSet borrowedSet = PreparedQuery(sql, card_id);		
		BorrowedBook[] borrowedArray = getBorrowedArray(borrowedSet, card_id);
		return borrowedArray;
	}
	public BorrowedBook getOneBorrowedBook(int card_id, int book_id) throws SQLException {
		
		String title, author, genre, publisher;
		long isbn, borrowed_epoch, return_epoch;
		int pages;
		String sql = "SELECT * FROM books INNER JOIN borrowed_books USING(book_id)"
					+ " WHERE card_id = ? AND book_id = ?";
		ResultSet rs2 = PreparedQuery(sql, card_id, book_id);		
		borrowed_epoch = rs2.getLong("borrowed_epoch");
		return_epoch = rs2.getLong("return_epoch");
		title = rs2.getString("title");
		author = rs2.getString("author");
		genre = rs2.getString("genre");
		publisher = rs2.getString("publisher");
		isbn = rs2.getLong("isbn");
		pages = rs2.getInt("pages");
		BorrowedBook result = new BorrowedBook(book_id,title, author, genre, publisher, pages, isbn, borrowed_epoch, return_epoch, card_id);	
		
		return result;
	}
	public Book searchOneBook(int book_id) throws SQLException {
		
		String title, author, genre, publisher;
		long isbn;
		int pages, quantity, shelf;
		double rating;
		String sql = "SELECT * FROM books"
					+ " WHERE book_id = ?";
		
		ResultSet rs = PreparedQuery(sql, book_id);		
		title = rs.getString("title");
		author = rs.getString("author");
		genre = rs.getString("genre");
		publisher = rs.getString("publisher");
		isbn = rs.getLong("isbn");
		pages = rs.getInt("pages");
		quantity = getNumberAvailable(book_id);
		shelf = rs.getInt("shelf");
		rating = getRating(book_id);
		Book result = new Book(title, author, genre, publisher, pages, isbn, book_id, quantity, rating, shelf);			
		rs.close();
		stmt.close();
		return result;
	}
	public String getDelayedBooksList() throws SQLException {
		
		long todayEpoch = System.currentTimeMillis() / 1000L;
		String result= "";
		String title, author, genre, publisher;
		long isbn, borrowed_epoch, return_epoch;
		int pages, book_id, card_id;
		ArrayList<BorrowedBook> delayedList = new ArrayList<BorrowedBook>();
		String sql = "SELECT * FROM books INNER JOIN borrowed_books USING(book_id)  WHERE return_epoch < ? ORDER BY card_id asc";
		
		ResultSet books = PreparedQuery(sql, todayEpoch);
		
		while(books.next()) {
			book_id = books.getInt("book_id");
			borrowed_epoch = books.getLong("borrowed_epoch");
			return_epoch = books.getLong("return_epoch");
			title = books.getString("title");
			author = books.getString("author");
			genre = books.getString("genre");
			publisher = books.getString("publisher");
			isbn = books.getLong("isbn");
			pages = books.getInt("pages");
			card_id = books.getInt("card_id");
			BorrowedBook temp = new BorrowedBook(book_id,title, author, genre, publisher, pages, isbn, borrowed_epoch, return_epoch, card_id);
			delayedList.add(temp);
		}
		
		for(BorrowedBook delayedBook : delayedList) {
			result+= delayedBook.delayedString() + EOL;
		}
		
		return result;
	}
	public boolean verifyLogin(String username, String password) throws SQLException {
		
		boolean result = false;
		String sql = "SELECT * FROM admin WHERE username = ?";
		ResultSet rs = PreparedQuery(sql, username);
		String hashed = rs.getString("password");
		if (BCrypt.checkpw(password, hashed)) {
			result = true;
		}		
		return result;
	}
	public Book[] search(String search, String category) throws SQLException  {
		
		ArrayList<Book> searchedBooks = new ArrayList<Book>();
		
		String sql = "SELECT * FROM books " +
				"WHERE " + category + " LIKE ?";
			ResultSet rs = PreparedQuery(sql ,"%"+ search+"%");
			
			/*while (rs.next()) {
				String title = rs.getString("title");
				String author = rs.getString("author");
				String genre = rs.getString("genre");
				String publisher = rs.getString("publisher");
				int pages = rs.getInt("pages");
				int shelf = rs.getInt("shelf");	  
				int book_id = rs.getInt("book_id");
				long isbn = rs.getLong("isbn");
				int quantity = getNumberAvailable(book_id);
				double rating = getRating(book_id);
				Book temp = new Book(title, author, genre, publisher, pages, isbn, book_id, quantity, rating, shelf);	
				searchedBooks.add(temp);
			}*/
			//stmt2.close();
			Book[] searchedArray = getBookArray(rs);
			//Book[] searchedArray = searchedBooks.toArray(new Book[searchedBooks.size()]);
			return searchedArray;
	}
	public Book[] searchAuthorTitle(String title, String author) throws SQLException {
				
		String sql = "SELECT * FROM books " +
				"WHERE title LIKE ? AND author LIKE ?" ;
			ResultSet rs = PreparedQuery(sql ,"%"+ title +"%", "%" + author + "%");
			
			/*while (rs.next()) {
				String title = rs.getString("title");
				String author = rs.getString("author");
				String genre = rs.getString("genre");
				String publisher = rs.getString("publisher");
				int pages = rs.getInt("pages");
				int shelf = rs.getInt("shelf");	  
				int book_id = rs.getInt("book_id");
				long isbn = rs.getLong("isbn");
				int quantity = getNumberAvailable(book_id);
				double rating = getRating(book_id);
				Book temp = new Book(title, author, genre, publisher, pages, isbn, book_id, quantity, rating, shelf);	
				searchedBooks.add(temp);
			}*/
			//stmt2.close();
			Book[] searchedArray = getBookArray(rs);
			//Book[] searchedArray = searchedBooks.toArray(new Book[searchedBooks.size()]);
			return searchedArray;
		
	}
	public Book[] createTop10() throws SQLException {
		ArrayList<Book> searchedBooks = new ArrayList<Book>();
		String sql = "SELECT * FROM books";
		//try {
		//Statement stmt2 = conn.createStatement();
		ResultSet rs = PreparedQuery(sql);
		while (rs.next()) {
			String title = rs.getString("title");
			String author = rs.getString("author");
			String genre = rs.getString("genre");
			String publisher = rs.getString("publisher");
			int pages = rs.getInt("pages");
			int shelf = rs.getInt("shelf");	  
			int book_id = rs.getInt("book_id");
			long isbn = rs.getLong("isbn");
			int quantity = getNumberAvailable(book_id);
			double rating = getRating(book_id);
			Book temp = new Book(title, author, genre, publisher, pages, isbn, book_id, quantity, rating, shelf);	
			searchedBooks.add(temp);
			}
			Collections.sort(searchedBooks);
			//stmt2.close();
			Book[] returnArray = new Book[10];
			for(int i = 0; i < 10; i++) {
				returnArray[i] = searchedBooks.get(i);
			}
			return returnArray;
		//}
	}
	public double[] getRating(Book[] searchedArray) throws SQLException {
		
		double[] ratingArray = new double[searchedArray.length];
		
		for(int i = 0; i < searchedArray.length; i++) {
			/*double count = 0, sum = 0;
			String sqlCount = "SELECT count(*) FROM history WHERE book_id =? AND rating > 0";
			String sqlSum = "SELECT sum(rating) FROM history WHERE book_id =?";*/ 
			
			ratingArray[i] = getRating(searchedArray[i].getBook_ID());
			
			/*ResultSet rsCount = PreparedQuery(sqlCount, searchedArray[i].getBook_ID());
			count = rsCount.getDouble(1);
			ResultSet rsSum = PreparedQuery(sqlSum, searchedArray[i].getBook_ID());
			sum = rsSum.getDouble(1);
			rsCount.close();
			rsSum.close();
			double rating = sum / count; 
			ratingArray[i] = rating;*/
		}
		
		return ratingArray;
	}
	public double getRating(int book_id) throws SQLException {
		
		double rating,count, sum;
		String sqlCount = "SELECT count(*) FROM history WHERE book_id =? AND rating > 0";
		String sqlSum = "SELECT sum(rating) FROM history WHERE book_id =?";
		ResultSet rsCount = PreparedQuery(sqlCount, book_id);
		count = rsCount.getDouble(1);
		ResultSet rsSum = PreparedQuery(sqlSum, book_id);
		sum =rsSum.getDouble(1);
		rsCount.close();
		rsSum.close();
		if (count == 0) {
			return 0;
		}
		rating =sum / count; 
		return rating;
	}	
	public void addDebt(int card_id, int debt) throws SQLException {
		String sql = "UPDATE customer_debt " +
					 " SET accumulated_fees = accumulated_fees + ? " +
					 " WHERE card_id = ? ";
		PreparedUpdate(sql, debt, card_id);
	}
	public void payDebt(int card_id, int payment) throws SQLException {
		String sql = "UPDATE customer_debt " +
				 " SET paid = paid + ? " +
				 " WHERE card_id = ? ";
	PreparedUpdate(sql, payment, card_id);
	}
	public void returnBook(int card_id, int book_id, double rating) throws SQLException {
		BorrowedBook book;
		book = getOneBorrowedBook(card_id, book_id);
		if (!book.onTime()) {
			int debt = 0;
			debt = book.getDaysOver() * 2;
			addDebt(card_id, debt);
		}
		String onTime =book.returnOnTime();
		String insertHistory = "INSERT INTO history " +
				 "(card_id, book_id, returned_on_time, rating)" +
				 "VALUES " +
				 "( ?, ?, ?, ?);";
		PreparedUpdate(insertHistory, card_id, book_id, onTime, rating);

		String deleteBorrowed =  "DELETE FROM borrowed_books WHERE card_id = ? AND book_id = ?";		
		PreparedUpdate(deleteBorrowed, card_id, book_id);
		createTop10();
	}
	public boolean checkIfAvailable(int book_id) throws SQLException {
		boolean result = false;
		int quantity, borrowed;
		String countBooks = "SELECT count(*) FROM borrowed_books WHERE book_id =?";
		ResultSet nrOfBorrowed = PreparedQuery(countBooks, book_id);
		borrowed = nrOfBorrowed.getInt(1);
		String sqlQuantity = "SELECT quantity FROM books WHERE book_id =?";
		ResultSet bookQuantity = PreparedQuery(sqlQuantity, book_id);
		quantity = bookQuantity.getInt("quantity");
		if(borrowed >= quantity) {
			result = false;
		}
		else if (borrowed < quantity) {
			result = true;
		}
		return result;
	}
	public void removeBook(int book_id) throws SQLException {
		String delete = "DELETE FROM books WHERE book_id = ?";
		PreparedUpdate(delete, book_id);
	}
	public void changeQuantityBook(int book_id, int change) throws SQLException {
		String getQuantity = "SELECT quantity FROM books WHERE book_id = ?";
		ResultSet tableQuantity = PreparedQuery(getQuantity, book_id);
		int quantity = tableQuantity.getInt(1);
	
		if(quantity - change <= 0) {
			removeBook(book_id);
		}
		else {
			int newQuantity = quantity + change;
			String setNewQuantity = "UPDATE books SET quantity = ? WHERE book_id = ?";
			PreparedUpdate(setNewQuantity, newQuantity, book_id);
		}
		
		
	}
	public BorrowedBook[] getBorrowedArray(ResultSet borrowedSet, int card_id) throws SQLException {
		
		ArrayList<BorrowedBook> borrowed_list = new ArrayList<BorrowedBook>();
		String title, author, genre, publisher;
		long isbn, borrowed_epoch, return_epoch;
		int pages, book_id;
	
		while (borrowedSet.next()) {
			book_id = borrowedSet.getInt("book_id");
			borrowed_epoch = borrowedSet.getLong("borrowed_epoch");
			return_epoch = borrowedSet.getLong("return_epoch");
			title = borrowedSet.getString("title");
			author = borrowedSet.getString("author");
			genre = borrowedSet.getString("genre");
			publisher = borrowedSet.getString("publisher");
			isbn = borrowedSet.getLong("isbn");
			pages = borrowedSet.getInt("pages");
			BorrowedBook temp = new BorrowedBook(book_id,title, author, genre, publisher, pages, isbn, borrowed_epoch, return_epoch, card_id);
			borrowed_list.add(temp);	
		}
		BorrowedBook[] borrowedArray = borrowed_list.toArray(new BorrowedBook[borrowed_list.size()]);
		return borrowedArray;
	}
	public Book[] getBookArray(ResultSet bookSet) throws SQLException {
		
		ArrayList<Book> bookList = new ArrayList<Book>();
		String title, author, genre, publisher;
		long isbn;
		int pages, book_id, quantity, shelf;
		double rating;
		while (bookSet.next()) {
			book_id = bookSet.getInt("book_id");
			title = bookSet.getString("title");
			author = bookSet.getString("author");
			genre = bookSet.getString("genre");
			publisher = bookSet.getString("publisher");
			isbn = bookSet.getLong("isbn");
			pages = bookSet.getInt("pages");
			shelf = bookSet.getInt("shelf");
			quantity = getNumberAvailable(book_id);
			rating = getRating(book_id);
			Book temp = new Book(title, author, genre, publisher, pages, isbn, book_id, quantity, rating, shelf);
			bookList.add(temp);	
		}
		Book[] bookArray = bookList.toArray(new Book[bookList.size()]);
		return bookArray;
	}
	public Book[] getGenreBooks(String genre) throws SQLException {
		
		String sql = "SELECT * FROM books WHERE genre = ?";
		ResultSet genreBooks = PreparedQuery(sql, genre);
		Book[] result = getBookArray(genreBooks);
		return result;
	}
	public int getNumberAvailable(int book_id) throws SQLException {
		
		int quantity, borrowed, result;
		String countBooks = "SELECT count(*) FROM borrowed_books WHERE book_id = ?";
		ResultSet nrOfBorrowed = PreparedQuery(countBooks, book_id);
		borrowed = nrOfBorrowed.getInt(1);
		String sqlQuantity = "SELECT quantity FROM books WHERE book_id =?";
		ResultSet bookQuantity = PreparedQuery(sqlQuantity, book_id);
		quantity = bookQuantity.getInt("quantity");
		result = quantity - borrowed;
		return result;		
	}
	public Book[] getTop10() {
		return top10Array;
	}
	public void top10Test (int book_id) throws SQLException {
		Book candidate = searchOneBook(book_id);
		double candidateRating = candidate.getRating();
		double topRating = top10Array[0].getRating();
		double minRating = top10Array[9].getRating();
		
		if(candidateRating < minRating) {
			return;
		}
		else if(candidateRating > topRating) {
			top10Array[0] = candidate;
		}
		else if(candidateRating > minRating && candidateRating < topRating) {
			for(int i = 9; i >= 1; i--) {
				double top10i = top10Array[i].getRating();
				double top10minus = top10Array[i-1].getRating();
				
				if(candidateRating > top10i && candidateRating < top10minus){
					//insertIntoTop10(i, candidate);
					top10Array[i] = candidate;
				}
				/*else if(candidateRating < top10i && candidateRating > top10minus) {
					top10Array[i-1] = candidate;
				}*/
			}
		}
	}
	/*public Book[] insertIntoTop10(int candidateIndex, Book candidate) {
		
		
		
		
	}*/
	public void addToCheckout(Book addition) {
		checkoutList.add(addition);
	}
	public void addToCheckout(int book_id) throws SQLException {
		checkoutList.add(searchOneBook(book_id));
	}
	public void setCheckout(ArrayList<Book> list) {
		checkoutList = list;
	}
	public void removeFromCheckout(Book remove) {
		checkoutList.remove(remove);
	}
	public ResultSet PreparedQuery(String query, Object...objects) throws SQLException {
			PreparedStatement pstmt = conn.prepareStatement(query);
				for(int i = 0; i < objects.length; i++) {
					Object obj = objects[i];
					String check = obj.getClass().getSimpleName();
					switch(check) {
					case "Integer":
						pstmt.setInt(i+1, (int)obj);
						break;
					case "Double":
						pstmt.setDouble(i+1, (double)obj);
						break;
					case "String":
						pstmt.setString(i+1, (String)obj);
						break;
					case "Long":
						pstmt.setLong(i+1, (Long)obj);
						break;
					default:
						System.out.println("Magic type, no idea!");
						break;
					}
				}
			ResultSet result = pstmt.executeQuery();
			return result;	
	}
	public void PreparedUpdate(String update, Object...objects) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(update);
		for(int i = 0; i < objects.length; i++) {
			Object obj = objects[i];
			String check = obj.getClass().getSimpleName();
			switch(check) {
			case "Integer":
				pstmt.setInt(i+1, (int)obj);
				break;
			case "Double":
				pstmt.setDouble(i+1, (double)obj);
				break;
			case "String":
				pstmt.setString(i+1, (String)obj);
				break;
			case "Long":
				pstmt.setLong(i+1, (Long)obj);
				break;
			default:
				System.out.println("Magic type, no idea!");
				break;
			}
		}
	pstmt.executeUpdate();
	pstmt.close();	
}
	public void closeConn() throws SQLException {
		conn.close();
	}		
	
	public void PreparedExecute(String sql) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.execute();
		pstmt.close();
	}

	public Customer getCustomer(int card_id) throws SQLException {
		String sql = "SELECT * FROM customer " +
				"WHERE card_id = ?";

		ResultSet rs = PreparedQuery(sql, card_id);  
		Customer result = rsToCustomer(rs);
		return result;	
	}

}
