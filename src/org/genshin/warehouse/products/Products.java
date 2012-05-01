package org.genshin.warehouse.products;

import java.util.ArrayList;
import java.util.List;

import org.genshin.warehouse.SpreeConnector;
import org.genshin.warehouse.ThumbListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class Products {
	Context ctx;
	SpreeConnector spree;
	ArrayList<Product> list;
	ThumbListItem[] productsListItems;
	public int count;
	
	Products(Context ctx, SpreeConnector spree) {
		this.ctx = ctx;
		
		this.list = new ArrayList<Product>();
		productsListItems = new ThumbListItem[0];
		
		this.spree = spree;

		count = 0;
	}
	
	
	private ArrayList<Product> processProductContainer(JSONObject productContainer) {
		ArrayList<Product> collection = new ArrayList<Product>();
		
		//Pick apart JSON object
		try {
			this.count = productContainer.getInt("count");
			JSONArray products = productContainer.getJSONArray("products");
			for (int i = 0; i < products.length(); i++) {
				JSONObject product = products.getJSONObject(i).getJSONObject("product");
				collection.add(new Product(
						product.getInt("id"),
						product.getString("name"),
						0.0,
						product.getInt("count_on_hand"),
						product.getString("description"),
						product.getString("permalink")
					));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
				
		list = collection;
		return collection;
	}

	public ArrayList<Product> getNewestProducts(int limit) {
		ArrayList<Product> collection = new ArrayList<Product>();
		JSONObject productContainer = spree.connector.getJSONObject("api/products.json");
		collection = processProductContainer(productContainer);
		return collection;
	}
	
	public ArrayList<Product> scanSearch(String query) {
		ArrayList collection = new ArrayList<Product>();
		JSONObject productContainer = spree.connector.getJSONObject("api/products/search.json?q[visual_code_eq]=" + query);
		Log.d("Products.scanSearch", "Results: " + count);
		collection = processProductContainer(productContainer);
		return collection;
	}
	
}
