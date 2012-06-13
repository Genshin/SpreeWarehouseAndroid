package org.genshin.warehouse.orders;

import java.util.ArrayList;

import org.genshin.gsa.Dialogs;
import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

public class Orders {
	
	Context ctx;
	SpreeConnector spree;
	ArrayList<Order> list;
	public int count;
	
	public Orders(Context ctx, SpreeConnector spree) {
		
		this.ctx = ctx;		
		this.list = new ArrayList<Order>();	
		this.spree = spree;
		count = 0;
	}
	
	private ArrayList<Order> processOrderContainer(JSONObject orderContainer) {
		ArrayList<Order> collection = new ArrayList<Order>();
		
		//Pick apart JSON object
		try {
			this.count = orderContainer.getInt("count");
			JSONArray orders = orderContainer.getJSONArray("orders");
			for (int i = 0; i < orders.length(); i++) {
				JSONObject orderJSON = orders.getJSONObject(i).getJSONObject("order");
				Order order = new Order(orderJSON);
				
				collection.add(order);			
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
				
		list = collection;
		return collection;
	}

	public void clear() {
		this.list = new ArrayList<Order>();
		count = 0;
	}

	public ArrayList<Order> getNewestOrders(int limit) {
		Dialogs.showLoading(ctx);
		
		ArrayList<Order> collection = new ArrayList<Order>();
		JSONObject orderContainer = spree.connector.getJSONObject("api/orders.json");
		collection = processOrderContainer(orderContainer);
		
		Dialogs.dismiss();
		
		return collection;
	}

	public ArrayList<Order> textSearch(String query) {
		Dialogs.showSearching(ctx);

		ArrayList<Order> collection = new ArrayList<Order>();
		JSONObject orderContainer = spree.connector.getJSONObject("api/orders/search.json?q[name_cont]=" + query);
		collection = processOrderContainer(orderContainer);
		
		Dialogs.dismiss();
		
		return collection;
	}

}
