package org.genshin.warehouse.products;

import java.io.InputStream;
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
	public String availableOn;
	public int countOnHand;
	public String description;
	public String metaDescription;
	public String permalink;
	public String visualCode;
	public SpreeImageData thumbnail;
	public ArrayList<SpreeImageData> images;
	private int primaryVarientIndex;
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
		this.visualCode = "";
	}
	
	public Variant variant() {
		if (variants.size() == 0) // no variants
			return new Variant(); // return dummy
		
		return variants.get(primaryVarientIndex);
	}
	
	public Variant variant(int idx) {
		return variants.get(idx);
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
			if (this.description == null)
				this.description = "";
			this.permalink = productJSON.getString("permalink");
			this.visualCode = productJSON.getString("visual_code");
			if (this.visualCode == null)
				this.visualCode = ""; // This should be added by master variant
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		obtainImagesInfo(productJSON);
		obtainVariants(productJSON);
		obtainThumbnail();
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
	
	public void addVariant(int id, String name, int countOnHand, // basics
			String visualCode, String sku, double price, // extended identifying information
			double weight, double height, double width, double depth, //physical specifications
			Boolean isMaster, double costPrice,	String permalink) { // extended data information
		variants.add(new Variant(id, name, countOnHand, visualCode, sku, price, weight, height, width, depth, isMaster, costPrice, permalink));

		if (isMaster) {
			this.visualCode = visualCode;
			this.sku = sku;
			this.primaryVarientIndex = variants.size() - 1; // set as last added variant
		}
	}
	
	private void obtainThumbnail() {
		if (images.size() > 0) {
			this.thumbnail = images.get(0);
			this.thumbnail.name = this.thumbnail.name;
			this.thumbnail = getThumbnailData(this.thumbnail);
		} else
			this.thumbnail = null;
	}
	
	private void obtainImagesInfo(JSONObject productJSON) {
		try {
			JSONArray imageInfoArray = productJSON.getJSONArray("images");
			for (int i = 0; i < imageInfoArray.length(); i++) {
				JSONObject imageInfo = imageInfoArray.getJSONObject(i).getJSONObject("image");
				images.add(new SpreeImageData(imageInfo.getString("attachment_file_name"), imageInfo.getInt("id")));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private SpreeImageData getThumbnailData(SpreeImageData image) {
		String path = "spree/products/" + image.id + "/small/" + image.name;

		InputStream is = Warehouse.Spree().connector.getStream(path);
		if (is != null) {
			Drawable imageData = Drawable.createFromStream(is, image.name);
			image.data = imageData;
		}

		return image;
	}

	private SpreeImageData getImageData(SpreeImageData image) {
		String path = "spree/products/" + image.id + "/product/" + image.name;

		InputStream is = Warehouse.Spree().connector.getStream(path);
		if (is != null) {
			Drawable imageData = Drawable.createFromStream(is, image.name);
			image.data = imageData;
		}

		return image;
	}
	
	private void obtainImages(JSONObject productJSON) {
		
		if (images.size() <= 0) {
			obtainImagesInfo(productJSON);
		}
		
		for (int i = 0; i < this.images.size(); i++) {
					getImageData(this.images.get(i));			
		}
	}
	
	private void processVariantJSON(JSONObject v) {
		//pre-build object
		boolean isMaster = false;
		try {
			isMaster = v.getBoolean("is_master");
		} catch (JSONException e) {
			isMaster = false;
		}
		
		int id = this.id;
		try {
			id = v.getInt("id");
		} catch (JSONException e) {
			//no unique ID, so set to product ID
			id = this.id;
		}
		
		String name = this.name;
		try {
			name = v.getString("name");
		} catch (JSONException e) {
			name = this.name;
		}
		
		int countOnHand = this.countOnHand;
		try {
			countOnHand = v.getInt("count_on_hand");
		} catch (JSONException e) {
			countOnHand = this.countOnHand;
		}
		
		String visualCode = this.visualCode;
		try {
			visualCode = v.getString("visual_code");
		} catch (JSONException e) {
			visualCode = this.visualCode;
		}
		
		String sku = this.sku;
		try {
			sku = v.getString("sku");
		} catch (JSONException e) {
			sku = this.sku;
		}
		
		double price = this.price;
		try {
			price = v.getDouble("price");
		} catch (JSONException e) {
			price = this.price;
		}
		
		double weight = 0.0;
		try {
			weight = v.getDouble("weight");
		} catch (JSONException e) {
			weight = this.variant().weight;
		}
		
		double height = 0.0;
		try {
			height = v.getDouble("height");
		} catch (JSONException e) {
			height = this.variant().height;
		}
		
		double width = 0.0;
		try {
			width = v.getDouble("width");
		} catch (JSONException e) {
			width = this.variant().width;
		}
		
		double depth = 0.0;
		try {
			depth = v.getDouble("depth");
		} catch (JSONException e) {
			depth = this.variant().depth;
		}
		
		double costPrice = 0.0;
		try {
			costPrice = v.getDouble("cost_price");
		} catch (JSONException e) {
			
		}
		
		String permalink = this.permalink;
		try {
			permalink = v.getString("permalink");
		} catch (JSONException e) {
			
		}
		addVariant(id, name, countOnHand,
			visualCode,	sku, price,
			weight, height, width, depth,
			isMaster, costPrice, permalink);
		
	}
	
	private void obtainVariants(JSONObject productJSON) {
		JSONArray variantArray = new JSONArray();
		
		
		try {
			variantArray = productJSON.getJSONArray("variants");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		//Log.d("VARIANTS", "Length: " + variantArray.length());
		
		// get master first
		for (int i = 0; i < variantArray.length(); i++) {
			JSONObject v = new JSONObject();
			try {
				v = variantArray.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
				// something broken? skip this one
				continue;
			}
			
			boolean isMaster = false;
			try {
				isMaster = v.getBoolean("is_master");
			} catch (JSONException e) {
				isMaster = false;
			}
			
			if (isMaster) {
				processVariantJSON(v);
				break;
			} 
		}
		
		/*for (int i = 0; i < variantArray.length(); i++) {
			JSONObject v = new JSONObject();
			try {
				v = variantArray.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
				// something broken? skip this one
				continue;
			}
			
			boolean isMaster = false;
			try {
				isMaster = v.getBoolean("is_master");
			} catch (JSONException e) {
				isMaster = false;
			}
			
			if (!isMaster) {
				processVariantJSON(v);
			}
		}*/
		
	}
}
