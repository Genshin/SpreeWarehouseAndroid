package org.genshin.warehouse.orders;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailsPayment {

	public Date date;
	public double amount;
	public String paymentMethod;
	public String paymentState;
	public String action;
	
	public OrderDetailsPayment() {
		this.date = new Date();
		this.amount = 0;
		this.paymentMethod = "";
		this.paymentState = "";
		this.action = "";
	}
	
	public OrderDetailsPayment
		(Date date, double amount, String method, String state, String action) {
		this.date = new Date();
		this.amount = amount;
		this.paymentMethod = method;
		this.paymentState = state;
		this.action = "";
	}
	
	public OrderDetailsPayment(JSONObject jsonObject) {	

		this.date = null;
		this.action = null;
		
		try {
			this.amount = jsonObject.getDouble("amount");
			this.paymentState = jsonObject.getString("state");
			JSONObject str = jsonObject.getJSONObject("payment_method");
			this.paymentMethod = str.getString("name");		
		} catch (JSONException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
	};
}
