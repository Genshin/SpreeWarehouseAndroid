package org.genshin.warehouse.products;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class Products {
	Context ctx;
	
	Products(Context ctx) {
		this.ctx = ctx;
	}
	

	public List<Product> getNewestProducts(int limit) {
		List<Product> collection = new ArrayList<Product>();
		
		return collection;
	}
	
}
