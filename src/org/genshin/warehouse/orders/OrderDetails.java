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
	ArrayList<OrderDetailsPayment> paymentList;
	ArrayList<OrderDetailsShipment> shipmentList;
	
	public OrderDetails(Context ctx, SpreeConnector spree) {
		this.ctx = ctx;
		this.list = new ArrayList<OrderLineItem>();
		this.spree = spree;
	}
	
	public void putOrderDetails(JSONObject container) {
		
		JSONObject str = container;

		try {
			this.statement = str.getString("state");
			this.mainTotal = str.getDouble("total");
			this.email = str.getString("email");
			
			this.itemTotal = str.getDouble("item_total");
			this.cost = str.getDouble("adjustment_total");
			this.lastTotal = str.getDouble("total");

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
			
			str = container;
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
	
	public void processOLIContainer(JSONObject container) {
		ArrayList<OrderLineItem> collection = new ArrayList<OrderLineItem>();

		try {
			JSONArray items = container.getJSONArray("line_items");
			
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i).getJSONObject("line_item");
				OrderLineItem lineItem = new OrderLineItem(item);	
				collection.add(lineItem);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
				
		list = collection;
	}
	
	public void getPayment(JSONObject container) {
		ArrayList<OrderDetailsPayment> collection = new ArrayList<OrderDetailsPayment>();

		try {
			JSONArray items = container.getJSONArray("payments");
			
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i).getJSONObject("payment");
				OrderDetailsPayment lineItem = new OrderDetailsPayment(item);	
				collection.add(lineItem);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
				
		paymentList = collection;
	}
	
	public void getShipment(JSONObject container) {
		ArrayList<OrderDetailsShipment> collection = new ArrayList<OrderDetailsShipment>();

		try {
			JSONArray items = container.getJSONArray("shipments");
			
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i).getJSONObject("shipment");
				OrderDetailsShipment lineItem = new OrderDetailsShipment(item);	
				collection.add(lineItem);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
				
		shipmentList = collection;
	}
}
