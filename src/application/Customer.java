package application;

public class Customer {
	
	private String city, street, phoneNr, name;
	private int card_id, debt;
	
	public Customer(String name,String city, String street, String phoneNr, int card_id ) {
		
		this.name = name;
		this.city = city;
		this.street = street;
		this.phoneNr = phoneNr;
		this.card_id = card_id;
	}
	public Customer(String name,String city, String street, String phoneNr, int card_id, int debt ) {
		this.name = name;
		this.city = city;
		this.street = street;
		this.phoneNr = phoneNr;
		this.card_id = card_id;
		this.debt = debt;
		
	}
	
	public String getCity() {
		return city;
	}

	public String getStreet() {
		return street;
	}

	public String getPhoneNr() {
		return phoneNr;
	}

	public String getName() {
		return name;
	}
	public int getDebt() {
		return debt;
	}
	public int getCard_id() {
		return card_id;
	}
	public String tooltipToString() {
		return this.name + ". " + this.street + ", " + this.city +"."; 
	}
}
