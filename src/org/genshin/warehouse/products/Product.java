package org.genshin.warehouse.products;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.genshin.spree.SpreeImageData;
import org.genshin.warehouse.Warehouse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;
import android.util.Log;

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
	public ArrayList<SpreeImageData> images;
	public ArrayList<Variant> variants;


	private void init() {
		this.images = new ArrayList<SpreeImageData>();
	}
	
	public Product() {
		init();
		
		this.id = -1;
		this.name = "";
		this.sku = "";
		this.price = 0.0;
		this.countOnHand = 0;
		this.description = "";
		this.permalink = "";
	}
	
	public Product(JSONObject productJSON) {
		init();
		
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
		init();
		
		this.id = id;
		this.name = name;
		this.sku = sku;
		this.price = price;
		this.countOnHand = countOnHand;
		this.description = description;
		this.permalink = permalink;
	}
	
	public void addVariant(int id, String name, int countOnHand, String visualCode, String sku, double price, double weight, double height, double width, double depth, Boolean isMaster, double costPrice, String permalink) {
		variants.add(new Variant(id, name, countOnHand, visualCode, sku, price, weight, height, width, depth, isMaster, costPrice, permalink));
	}
	
	private void obtainImages(JSONObject productJSON) {
		try {
			JSONArray imageInfoArray = productJSON.getJSONArray("images");
			for (int i = 0; i < imageInfoArray.length(); i++) {
				JSONObject imageInfo = imageInfoArray.getJSONObject(i).getJSONObject("image");
				//Log.d("FOUNDIMAGE", imageInfo.toString());
				images.add(new SpreeImageData(imageInfo.getString("attachment_file_name"), imageInfo.getInt("id")));
			}
			
			for (int i = 0; i < this.images.size(); i++) {
				URL url;
				try {
					url = new URL(Warehouse.Spree().connector.getBaseURL() + "/spree/products/" + images.get(i).id + "/product/" + images.get(i).name);
					try {
						InputStream is = (InputStream) url.getContent();
						Drawable imageData = Drawable.createFromStream(is, this.images.get(i).name);
						this.images.get(i).data = imageData;
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
