package org.genshin.warehouse.products;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.genshin.spree.SpreeConnector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class Products {
	Context ctx;
	SpreeConnector spree;
	ArrayList<Product> list;
	public int count;
	
	public Products(Context ctx, SpreeConnector spree) {
		this.ctx = ctx;
		
		this.list = new ArrayList<Product>();
		
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
				JSONObject productJSON = products.getJSONObject(i).getJSONObject("product");
				
				//TODO put this in variant stuff
				String sku = "";
				try {
					sku = productJSON.getString("sku");
				} catch (JSONException e) {
					//No SKU
					sku = "";
				}
				
				Product product = new Product(productJSON);
				
				collection.add(product);
				
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
				
		list = collection;
		return collection;
	}

	public void clear() {
		this.list = new ArrayList<Product>();
		count = 0;
	}

	
	public ArrayList<Product> getNewestProducts(int limit) {
		ArrayList<Product> collection = new ArrayList<Product>();
		JSONObject productContainer = spree.connector.getJSONObject("api/products.json");
		collection = processProductContainer(productContainer);
		return collection;
	}
	
	public ArrayList<Product> findByBarcode(String code) {
		ArrayList<Product> collection = new ArrayList<Product>();
		JSONObject productContainer = spree.connector.getJSONObject("api/products/search.json?q[variants_including_master_visual_code_eq]=" + code);
		collection = processProductContainer(productContainer);
		return collection;
	}

	public ArrayList<Product> textSearch(String query) {
		ArrayList<Product> collection = new ArrayList<Product>();
		JSONObject productContainer = spree.connector.getJSONObject("api/products/search.json?q[name_cont]=" + query);
		collection = processProductContainer(productContainer);
		return collection;
	}
	
}
