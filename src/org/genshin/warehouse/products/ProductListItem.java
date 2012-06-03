package org.genshin.warehouse.products;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ProductListItem {
	public Drawable thumb;
	public String name;
	public String sku;
	public String link;
	public int id;
	public int count;
	public double price;
	
	public ProductListItem(Drawable thumb, String name, String sku, int count, String link, double price, int id) {
		super();
		this.id = id;
		this.thumb = thumb;
		this.name = name;
		this.sku = sku;
		this.count = count;
		this.link = link;
		this.price = price;
	}
}
