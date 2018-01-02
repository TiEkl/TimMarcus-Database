package application;

public class Book implements Comparable<Book> {
	final String EOL = System.lineSeparator();
	private String title, author, genre, publisher, coverURL;
	private int pages, book_id, quantity, shelf;
	public int getShelf() {
		return shelf;
	}
	public void setShelf(int shelf) {
		this.shelf = shelf;
	}
	private long isbn;
	private double rating;
	public void setRating(double rating) {
		this.rating = rating;
	}
	public Book(String title, String author, String genre, String publisher, int pages, long isbn, int book_id, int shelf) {
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.publisher = publisher;
		this.pages = pages;
		this.isbn = isbn;
		this.book_id = book_id;
		this.shelf = shelf;
	}
	public Book(String title, String author, String genre, String publisher, int pages, long isbn, int book_id, int quantity, double rating, int shelf, String coverURL) {
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.publisher = publisher;
		this.pages = pages;
		this.isbn = isbn;
		this.book_id = book_id;
		this.quantity = quantity;
		this.rating = rating;
		this.shelf = shelf;
		this.coverURL = coverURL;
	}
	public Book(Book another) {
		this.title = another.getTitle();
		this.author = another.getAuthor();
		this.genre = another.getGenre();
		this.publisher = another.getPublisher();
		this.pages = another.getPages();
		this.isbn = another.getIsbn();
		this.book_id = another.getBook_ID();
		this.shelf=another.getShelf();
	
	}
	public String getCoverURL() {
		return this.coverURL;
	}
	public String toString() {
		
		String result = "|Title: " + title + 
						" | Author: " + author +
						" | Genre: " + genre +
						" | Publisher: " + publisher  +
						" | Pages: " + pages +
						" | Quantity Available: " + quantity +
						" | Rating: " + rating +
						" | ISBN: " + isbn;
		return result;		

	}
	public String toString2() {
		String result = "| ID: " + book_id + " |Rating: " + rating;
		return result;
		
		
		
	}
	public int getBook_ID() {
		return this.book_id;
	}
	public String getTitle() {
		return this.title;
	}
	public String getAuthor() {
		return this.author;
	}
	public String getGenre() {
		return this.genre;
	}
	public String getPublisher() {
		return this.publisher;
	}
	public int getPages() {
		return this.pages;
	}
	public long getIsbn() {
		return this.isbn;
	}
	public void setRating(int count, int totalRating) {	
		this.rating = totalRating / count;
	}
	public double getRating() {
		return this.rating;
	}
	public int getQuantity() {
		return this.quantity;
	}
	@Override
	public int compareTo(Book compareBook) {
		
		if ( rating < compareBook.rating) {
			return 1;
		}
		else if ( rating > compareBook.rating ) {
			return -1;
		}
		return 0;
	}
	public String alreadyBorrowed() {
		return "You have already borrowed" + this.title + " , please remove it from the checkout.";
	}
	public String borrowedSuccess() {
		String result = title + " by " + author;
		return result;
	}
}
