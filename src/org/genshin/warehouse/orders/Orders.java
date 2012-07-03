package org.genshin.warehouse.orders;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.genshin.gsa.Dialogs;
import org.genshin.spree.SpreeConnector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

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
	}
	
	private ArrayList<Order> processOrderContainer(JSONObject orderContainer) {
		ArrayList<Order> collection = new ArrayList<Order>();
		
		//Pick apart JSON object
		try {
			JSONArray orders = orderContainer.getJSONArray("orders");
			for (int i = 0; i < orders.length(); i++) {
				JSONObject orderJSON = orders.getJSONObject(i).getJSONObject("order");
				Order order = new Order(orderJSON);
				
				tmpList = new ArrayList();
				tmpList = putData(order.number);
				
				order.count = (Integer) tmpList.get(0);
				order.name = (String) tmpList.get(1);
				
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
		/*
		String escapedQuery = query;
		try {
			escapedQuery = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// WTF unsupported encoding? fine, just take it raw
			escapedQuery = query;
		}
		*/
		//JSONObject orderContainer = Warehouse.Spree().connector.getJSONObject("api/orders/search.json?q[name_cont]=" + escapedQuery);
		JSONObject orderContainer = spree.connector.getJSONObject("api/orders/search.json?q[name_cont]=" + query);
		collection = processOrderContainer(orderContainer);
		
		Dialogs.dismiss();
		
		return collection;

	}
	
	public ArrayList putData(String number) {
		tmpList = new ArrayList(DATACOUNT);
		String tmp = "";
		int num = 0;
		int cnt = 0;
		
		// orders.json にないデータを　orders/number.json から取り出す
		JSONObject container = spree.connector.getJSONObject("api/orders/" + number + ".json");

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
			StringBuilder sb = new StringBuilder(tmpStr.getString("firstname"));
			sb.append(" ");
			sb.append(tmpStr.getString("lastname"));	
			tmp = new String(sb);
			tmpList.add(tmp);
			cnt++;
	
			
			// 支払い方法は２つ以上になることもある？　その場合の処理は？
			/*
			items = orderStr.getJSONArray("payments");			
			tmpStr = items.getJSONObject(0).getJSONObject("payment");
			tmpStr = tmpStr.getJSONObject("payment_method");		
			sb = new StringBuilder(tmpStr.getString("name"));			
			tmp = new String(sb);
			tmpList.add(tmp);
			cnt++;
			*/
			
			// 配送方法は２つ以上になることもある？　その場合の処理は？
			/*
			items = orderStr.getJSONArray("shipments");			
			tmpStr = items.getJSONObject(0).getJSONObject("shipment");
			tmpStr = tmpStr.getJSONObject("shipping_method");		
			sb = new StringBuilder(tmpStr.getString("name"));
			tmp = new String(sb);	
			tmpList.add(tmp);
			cnt++;
			*/
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// jsonデータが空だった時にnullデータを挿入　NullPointerException対策
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
