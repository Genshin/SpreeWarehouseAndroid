package org.genshin.warehouse.products;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.genshin.warehouse.Warehouse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;

public class Product {
	public int id;
	public String name;
	public String sku;
	public double price;
	public double costPrice;
	public String availableOn;
	public int countOnHand;
	public String description;
	public String metaDescription;
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
	
	public Product(JSONObject productJSON) {
		try {
			this.id = productJSON.getInt("id");
			this.name = productJSON.getString("name");
			this.sku = "";
			this.price = productJSON.getDouble("price");
			this.countOnHand = productJSON.getInt("count_on_hand");
			this.description = productJSON.getString("description");
			this.permalink = productJSON.getString("permalink");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		obtainImages(productJSON);
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
	
	private void obtainImages(JSONObject productJSON) {
		try {
			JSONArray imageInfoArray = productJSON.getJSONArray("images");
			for (int i = 0; i < imageInfoArray.length(); i++) {
				JSONObject imageInfo = imageInfoArray.getJSONObject(i).getJSONObject("image");
				this.imageNames.add(imageInfo.getString("attachment_file_name"));
				this.imageIDs.add(imageInfo.getInt("id"));
				
			}
			
			//Log.d("Products.obtainImages", "found " + imageInfoArray.length());
			for (int i = 0; i < this.imageNames.size(); i++) {
				URL url;
				try {
					url = new URL(Warehouse.Spree().connector.getBaseURL() + "/spree/products/" + this.imageIDs.get(i) + "/product/" + this.imageNames.get(i));
					try {
						InputStream is = (InputStream) url.getContent();
						Drawable imageData = Drawable.createFromStream(is, this.imageNames.get(i));
						this.images.add(imageData);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}			
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
