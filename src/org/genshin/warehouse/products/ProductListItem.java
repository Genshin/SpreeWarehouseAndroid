package org.genshin.warehouse.products;

import android.widget.ImageView;

public class ProductListItem {
	public ImageView thumb;
	public String name;
	public String sku;
	public String link;
	public int id;
	public int count;
	
	public ProductListItem(ImageView thumb, String name, String sku, int count, String link, int id) {
		super();
		this.thumb = thumb;
		this.name = name;
		this.sku = sku;
		this.count = count;
		this.link = link;
	}
}
