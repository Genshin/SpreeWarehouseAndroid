package org.genshin.warehouse.orders;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class OrderLineItem {
	public String name;
	public double price;
	public int quantity;
	public double total;
	
	public OrderLineItem() {
		this.name = "";
		this.price = 0;
		this.quantity = 0;
		this.total = 0;
	}
	
	public OrderLineItem(String name, double price, int quantity, double total) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.total = total;
	}
	
	public OrderLineItem(JSONObject itemJSON) {
		try {
			JSONObject str = itemJSON.getJSONObject("variant");
			this.name = str.getString("name");
			this.price = itemJSON.getDouble("price");
			this.quantity = itemJSON.getInt("quantity");
			this.total = price * quantity;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
