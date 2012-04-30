package org.genshin.warehouse.products;

public class Product {
	public String name;
	public double price;
	public int countOnHand;
	public String description;
	public String permalink;
	public int id;
	
	public Product(int id, String name, double price, int countOnHand, String description, String permalink) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.countOnHand = countOnHand;
		this.description = description;
		this.permalink = permalink;
	}
}
