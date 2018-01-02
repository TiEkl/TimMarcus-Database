package application;

import java.util.ArrayList;

public class staticData {
	private static ArrayList<Book> checkoutList; 
	
	
	public staticData() {
		checkoutList = new ArrayList<Book>();
	}
	
	public void addToCheckoutList(Book addition) {
		checkoutList.add(addition);
	}
	public ArrayList<Book> getCheckoutList() {
		return checkoutList;
	}
	public void clearCheckoutList() {
		checkoutList.clear();
	}
	public boolean checkoutContains(Book comparison) {
		return checkoutList.contains(comparison);
	}
	public int getCheckoutSize() {
		return checkoutList.size();
	}
	public boolean removeFromCheckout(Book removal) {
		return checkoutList.remove(removal);
	}
	public boolean removeFromCheckout(int book_id) {
		
		return checkoutList.removeIf(book -> book.getBook_ID() == book_id);
	}
}
