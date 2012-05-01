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
	
	Products(Context ctx, SpreeConnector spree) {
		this.ctx = ctx;
		
		this.list = new ArrayList<Product>();
		productsListItems = new ThumbListItem[0];
		
		this.spree = spree;
	}
	
	
	

	public ArrayList<Product> getNewestProducts(int limit) {
		ArrayList<Product> collection = new ArrayList<Product>();
		
		JSONObject products = spree.connector.getJSONObject("api/products.json");
		
		/*for (int i = 0; i < products.length(); i++) {
			try {
				JSONObject product = products.getJSONObject(i).getJSONObject("product");
				collection.add(new Product(
						product.getInt("id"),
						product.getString("name"),
						0.0,
						product.getInt("count_on_hand"),
						product.getString("description"),
						product.getString("permalink")
					));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return collection;
			}			
		}*/

		list = collection;
		return collection;
	}
	
}
