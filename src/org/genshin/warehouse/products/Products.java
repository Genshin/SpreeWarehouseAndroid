package org.genshin.warehouse.products;

import java.util.ArrayList;
import java.util.List;

import org.genshin.warehouse.SpreeConnector;
import org.json.JSONObject;

import android.content.Context;

public class Products {
	Context ctx;
	SpreeConnector spree;
	
	Products(Context ctx, SpreeConnector spree) {
		this.ctx = ctx;

		this.spree = spree;
	}
	
	


	public List<Product> getNewestProducts(int limit) {
		List<Product> collection = new ArrayList<Product>();
		
		JSONObject ob = spree.getJSON("api/products.json");

		return collection;
	}
	
}
