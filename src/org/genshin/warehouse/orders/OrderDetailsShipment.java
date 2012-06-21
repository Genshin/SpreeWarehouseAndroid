package org.genshin.warehouse.orders;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailsShipment {
	
	public String number;
	public String shippingMethod;
	public String tracking;
	public double cost;
	public String state;
	public Date date;
	public String action;
	
	public OrderDetailsShipment() {
		this.number = "";
		this.shippingMethod = "";
		this.tracking = "";
		this.cost = 0;
		this.state = "";
		this.date = new Date();
		this.action = "";
	}
	
	public OrderDetailsShipment(String number, String method, 
			String tracking, double cost, String state, Date date, String action) {
		this.number = number;
		this.shippingMethod = method;
		this.tracking = tracking;
		this.cost = cost;
		this.state = state;
		this.date = new Date();
		this.action = "";
	}
	
	public OrderDetailsShipment(JSONObject jsonObject) {	

		this.date = null;
		this.action = null;
		
		try {
			this.number = jsonObject.getString("number");
			this.tracking = jsonObject.getString("tracking");
			this.cost = jsonObject.getDouble("cost");
			this.state = jsonObject.getString("state");
			JSONObject str = jsonObject.getJSONObject("shipping_method");
			this.shippingMethod = str.getString("name");		
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
	};

}
