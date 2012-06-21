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
	
	ArrayList tmpList;
	
	public final static int DATACOUNT = 4;
	
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
				
				/*
				order.count = putCount(order.number);
				order.paymentMethod = putPaymentMethod(order.number);
				order.shippingMethod = putShippingMethod(order.number);
				*/
				
				tmpList = new ArrayList();
				tmpList = putData(order.number);
				
				order.count = (Integer) tmpList.get(0);
				order.name = (String) tmpList.get(1);
				order.paymentMethod = (String) tmpList.get(2);
				order.shippingMethod = (String) tmpList.get(3);
				
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
	
	public ArrayList putData(String number) {
		tmpList = new ArrayList(DATACOUNT);
		String tmp = "";
		int cnt = 0;
		
		JSONObject container = spree.connector.getJSONObject("api/orders/" + number + ".json");
		int num = 0;

		try {
			JSONObject orderStr = container.getJSONObject("order");

			JSONArray items = orderStr.getJSONArray("line_items");
			for (int i = 0; i < items.length(); i++) {
				JSONObject itemJSON = items.getJSONObject(i).getJSONObject("line_item");
				tmp = itemJSON.getString("quantity");
				num += Integer.parseInt(tmp);
			}
			tmpList.add(num);
			cnt++;
			
			JSONObject tmpStr = orderStr.getJSONObject("bill_address");
			StringBuilder sb = new StringBuilder();
			
			sb.append(tmpStr.getString("firstname"));
			sb.append(" ");
			sb.append(tmpStr.getString("lastname"));	
			tmp = new String(sb);
			tmpList.add(tmp);
			cnt++;
			
			items = orderStr.getJSONArray("payments");			
			tmpStr = items.getJSONObject(0).getJSONObject("payment");
			tmpStr = tmpStr.getJSONObject("payment_method");		
			
			sb = new StringBuilder();			
			sb.append(tmpStr.getString("name"));
			tmp = new String(sb);
			tmpList.add(tmp);
			cnt++;
			
			items = orderStr.getJSONArray("shipments");			
			tmpStr = items.getJSONObject(0).getJSONObject("shipment");
			tmpStr = tmpStr.getJSONObject("shipping_method");			
			
			sb = new StringBuilder();			
			sb.append(tmpStr.getString("name"));
			tmp = new String(sb);
			
			tmpList.add(tmp);
			cnt++;
			
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		if (cnt == DATACOUNT)
			return tmpList;
		else {
			for (int i = cnt; i < DATACOUNT; i++) {
				tmpList.add(null);
			}
			return tmpList;
		}
	}
}
