package org.genshin.warehouse.products;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;

public class Product {
	public int id;
	public String name;
	public String sku;
	public double price;
	public int countOnHand;
	public String description;
	public String permalink;
	public ArrayList<String> imageNames;
	public ArrayList<Integer> imageIDs;
	public ArrayList<Drawable> images;
	public ArrayList<Variant> variants;


	public Product() {
		this.id = -1;
		this.name = "";
		this.sku = "";
		this.price = 0.0;
		this.countOnHand = 0;
		this.description = "";
		this.permalink = "";
		this.imageNames = new ArrayList<String>();
		this.imageIDs = new ArrayList<Integer>();
		this.images = new ArrayList<Drawable>();
	}
	
	public Product(int id, String name, String sku, double price, int countOnHand, String description, String permalink) {
		this.id = id;
		this.name = name;
		this.sku = sku;
		this.price = price;
		this.countOnHand = countOnHand;
		this.description = description;
		this.permalink = permalink;
		this.imageNames = new ArrayList<String>();
		this.imageIDs = new ArrayList<Integer>();
		this.images = new ArrayList<Drawable>();
	}
	
	public void addVariant(int id, String name, int countOnHand, String visualCode, String sku, double price, double weight, double height, double width, double depth, Boolean isMaster, double costPrice, String permalink) {
		variants.add(new Variant(id, name, countOnHand, visualCode, sku, price, weight, height, width, depth, isMaster, costPrice, permalink));
	}
}
