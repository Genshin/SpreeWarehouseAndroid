package org.genshin.warehouse.products;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import spree.SpreeConnector;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class Products {
	Context ctx;
	SpreeConnector spree;
	ArrayList<Product> list;
	public int count;
	
	Products(Context ctx, SpreeConnector spree) {
		this.ctx = ctx;
		
		this.list = new ArrayList<Product>();
		
		this.spree = spree;

		count = 0;
	}
	
	
	private void obtainImages(JSONObject productJSON, Product product) {
		try {
			JSONArray imageInfoArray = productJSON.getJSONArray("images");
			for (int i = 0; i < imageInfoArray.length(); i++) {
				JSONObject imageInfo = imageInfoArray.getJSONObject(i).getJSONObject("image");
				product.imageNames.add(imageInfo.getString("attachment_file_name"));
				product.imageIDs.add(imageInfo.getInt("id"));
				
			}
			Log.d("Products.obtainImages", "found " + imageInfoArray.length());
			for (int i = 0; i < product.imageNames.size(); i++) {
				URL url;
				try {
					url = new URL(spree.connector.getBaseURL() + "/spree/products/" + product.imageIDs.get(i) + "/product/" + product.imageNames.get(i));
					try {
						InputStream is = (InputStream) url.getContent();
						Drawable imageData = Drawable.createFromStream(is, product.imageNames.get(i));
						product.images.add(imageData);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				/*try {
					sku = productJSON.getString("sku");
				} catch (JSONException e) {
					//No SKU
					sku = "";
				}*/
				
				Product product = new Product(
						productJSON.getInt("id"),
						productJSON.getString("name"),
						sku,
						productJSON.getDouble("price"),
						productJSON.getInt("count_on_hand"),
						productJSON.getString("description"),
						productJSON.getString("permalink"));
				
				obtainImages(productJSON, product);
				
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
	
	public ArrayList<Product> scanSearch(String query) {
		ArrayList<Product> collection = new ArrayList<Product>();
		JSONObject productContainer = spree.connector.getJSONObject("api/products/search.json?q[variants_including_master_visual_code_eq]=" + query);
		//Log.d("Products.scanSearch", "Results: " + count);
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
