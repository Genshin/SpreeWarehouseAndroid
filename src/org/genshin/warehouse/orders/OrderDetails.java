package org.genshin.warehouse.orders;

import java.util.ArrayList;

import org.genshin.gsa.Dialogs;
import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.products.Product;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class OrderDetails {
	
	public String number;
	public String statement;
	public Double mainTotal;
	//public String shipmentState;
	//public String paymentState;

	public double itemTotal;
	public double cost;
	public double lastTotal;
	
	public String paymentAddress;
	public String shipmentAddress;
	public String email;
	
	Context ctx;
	SpreeConnector spree;
	public int count;
	ArrayList<OrderLineItem> list;
	
	public OrderDetails(Context ctx, SpreeConnector spree) {
		this.ctx = ctx;
		this.list = new ArrayList<OrderLineItem>();
		this.spree = spree;

		count = 0;
	}
	
	public void putOrderDetails(String number) {
		JSONObject container = spree.connector.getJSONObject("api/orders/" + number + ".json");
		this.number = number;

		try {
			JSONObject str = container.getJSONObject("order");

			this.statement = str.getString("state");
			this.mainTotal = str.getDouble("total");
			this.email = str.getString("email");

			str = str.getJSONObject("bill_address");
			StringBuilder sb = new StringBuilder();
			
			sb.append(str.getString("firstname"));
			sb.append(" ");
			sb.append(str.getString("lastname"));
			sb.append(" (");
			sb.append(str.getString("phone"));
			sb.append(")\n");
			sb.append(str.getString("address1"));
			sb.append(", ");
			sb.append(str.getString("address2"));
			sb.append(", ");
			sb.append(str.getString("city"));
			sb.append(", ");
			sb.append(str.getString("state_name"));
			sb.append(", ");
			sb.append(str.getString("zipcode"));
			sb.append(", ");
			
			str = str.getJSONObject("country");
			sb.append(str.getString("iso_name"));			

			this.paymentAddress = new String(sb);
			
			str = container.getJSONObject("order");
			str = str.getJSONObject("ship_address");
			sb = new StringBuilder();
			
			sb.append(str.getString("firstname"));
			sb.append(" ");
			sb.append(str.getString("lastname"));
			sb.append(" (");
			sb.append(str.getString("phone"));
			sb.append(")\n");
			sb.append(str.getString("address1"));
			sb.append(", ");
			sb.append(str.getString("address2"));
			sb.append(", ");
			sb.append(str.getString("city"));
			sb.append(", ");
			sb.append(str.getString("state_name"));
			sb.append(", ");
			sb.append(str.getString("zipcode"));
			sb.append(", ");
			
			str = str.getJSONObject("country");
			sb.append(str.getString("iso_name"));
			
			this.shipmentAddress = new String(sb);
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}
	
	public ArrayList<OrderLineItem> processOLIContainer(String number) {
		ArrayList<OrderLineItem> collection = new ArrayList<OrderLineItem>();
		JSONObject container = spree.connector.getJSONObject("api/orders/" + number + ".json");

		try {
			JSONObject str = container.getJSONObject("order");
			JSONArray items = str.getJSONArray("line_items");
			
			for (int i = 0; i < items.length(); i++) {
				JSONObject itemJSON = items.getJSONObject(i).getJSONObject("line_item");
				OrderLineItem item = new OrderLineItem(itemJSON);	
				collection.add(item);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
				
		list = collection;
		return collection;
	}
	
	public void totalCalc (int num, OrderDetails order) {

		for (int i = 0; i < num; i++) {
			itemTotal += order.list.get(i).total;
			cost = 0;
		}
		lastTotal = itemTotal + cost;
	}
}
