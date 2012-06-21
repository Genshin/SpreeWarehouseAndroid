package org.genshin.warehouse.products;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.genshin.gsa.Dialogs;
import org.genshin.warehouse.R;
import org.genshin.warehouse.Warehouse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class Products {
	private Context ctx;
	private Product selected;
	ArrayList<Product> list;
	public int count;
	
	public Products(Context ctx) {
		this.ctx = ctx;
		this.selected = null;
		this.list = new ArrayList<Product>();
		count = 0;
	}
	
	public void select(Product product) {
		selected = product;
	}
	
	public Product selected() {
		return selected;
	}
	
	private ArrayList<Product> processProductContainer(JSONObject productContainer) {
		ArrayList<Product> collection = new ArrayList<Product>();
		
		if (productContainer == null)
			return null;
		
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
		Dialogs.showLoading(ctx);
		
		ArrayList<Product> collection = new ArrayList<Product>();
		JSONObject productContainer = Warehouse.Spree().connector.getJSONObject("api/products.json?page=1");
		collection = processProductContainer(productContainer);
		
		Dialogs.dismiss();
		
		return collection;
	}
	
	public ArrayList<Product> findByBarcode(String code) {
		//Dialogs.showSearching(ctx);
		
		ArrayList<Product> collection = new ArrayList<Product>();
		JSONObject productContainer = Warehouse.Spree().connector.getJSONObject("api/products/search.json?q[variants_including_master_visual_code_eq]=" + code);
		collection = processProductContainer(productContainer);
		
		//Dialogs.dismiss();
		
		return collection;
	}

	public ArrayList<Product> textSearch(String query) {
		Dialogs.showSearching(ctx);

		ArrayList<Product> collection = new ArrayList<Product>();
		String escapedQuery = query;
		try {
			escapedQuery = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// WTF unsupported encoding? fine, just take it raw
			escapedQuery = query;
		}
		JSONObject productContainer = Warehouse.Spree().connector.getJSONObject("api/products/search.json?q[name_cont]=" + escapedQuery);
		collection = processProductContainer(productContainer);
		
		Dialogs.dismiss();
		
		return collection;
	}
	
	 public void unregisteredBarcode(final String code) {
		AlertDialog.Builder question = new AlertDialog.Builder(ctx);

		question.setMessage(ctx.getString(R.string.unregistered_barcode_new_product));
		question.setTitle(ctx.getString(R.string.unregistered_barcode_title));
		question.setIcon(R.drawable.newproduct);
		question.setPositiveButton(ctx.getString(R.string.register_to_new_product), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
				Intent intent = new Intent(ctx, ProductEditActivity.class);
				intent.putExtra("IS_NEW", true);
				intent.putExtra("BARCODE", code);
	            ctx.startActivity(intent);
			}
		});
		
		question.setNeutralButton(ctx.getString(R.string.register_to_existing_product), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});

		question.show();
	}
	
}
