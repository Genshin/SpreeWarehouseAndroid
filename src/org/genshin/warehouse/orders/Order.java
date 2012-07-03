package org.genshin.warehouse.orders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.Warehouse;
import org.genshin.warehouse.products.Variant;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateFormat;
import android.util.Log;

public class Order {
	public String number;
	public Date date;
	public String name;
	public int count;
	public double price;
	public String division;
	public String paymentState;
	public String pickingState;
	public String packingState;
	public String shipmentState;
	private int primaryVarientIndex;
	public ArrayList<Variant> variants;
	
	public Order() {
		this.number = "";
		this.date = new Date();
		this.name = "";
		this.count = 0;
		this.price = 0;
		this.division = "";
		this.paymentState = "";
		this.pickingState = "";
		this.packingState = "";
		this.shipmentState = "";
	}
	
	public Variant variant() {
		if (variants.size() == 0) // no variants
			return new Variant(); // return dummy
		
		return variants.get(primaryVarientIndex);
	}
	
	public Variant variant(int idx) {
		return variants.get(idx);
	}
	
	public Order(JSONObject orderJSON) {
		
		try {
			this.number = orderJSON.getString("number");
			
			String strDate = orderJSON.getString("created_at");		
			if (strDate != null && strDate != "null" && strDate != "") {
				Date date = null;

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");			
				try {
					date = format.parse(strDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}			
				this.date = date;
			}
			else
				this.date = null;
			
			this.price = orderJSON.getDouble("total");
			this.paymentState = orderJSON.getString("payment_state");
			this.division = "";

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		//obtainVariants(orderJSON);
	}
	
	/*
	public void addVariant(int id, String name, int countOnHand, // basics
			String visualCode, String sku, double price, // extended identifying information
			double weight, double height, double width, double depth, //physical specifications
			Boolean isMaster, double costPrice,	String permalink) { // extended data information
		variants.add(new Variant(id, name, countOnHand, visualCode, sku, price, weight, height, width, depth, isMaster, costPrice, permalink));

		if (isMaster) {
			this.primaryVarientIndex = variants.size() - 1; // set as last added variant
		}
	}
	*/
	
	/*
	private void processVariantJSON(JSONObject v) {
		//pre-build object
		boolean isMaster = false;
		try {
			isMaster = v.getBoolean("is_master");
		} catch (JSONException e) {
			isMaster = false;
		}
		
		Date date = this.date;
		try {
			date = v.getInt("id");
		} catch (JSONException e) {
			//no unique ID, so set to product ID
			date = this.date;
		}
		
		String number = this.number;
		try {
			number = v.getString("number");
		} catch (JSONException e) {
			number = this.number;
		}
		
		String name = this.name;
		try {
			name = v.getString("user_id");
		} catch (JSONException e) {
			name = this.name;
		}
		
		double count = this.count;
		try {
			count = v.getInt("total");
		} catch (JSONException e) {
			count = this.count;
		}
		
		double price = this.price;
		try {
			price = v.getDouble("item_total");
		} catch (JSONException e) {
			price = this.price;
		}
		
		String paymentMethod = this.paymentMethod;
		try {
			paymentMethod = v.getString("payment_state");
		} catch (JSONException e) {
			paymentMethod = this.paymentMethod;
		}
		
		String paymentState = this.paymentState;
		try {
			paymentState = v.getString("payment_state");
		} catch (JSONException e) {
			paymentState = this.paymentState;
		}
		
		String shippingMethod = this.shippingMethod;
		try {
			shippingMethod = v.getString("shipment_state");
		} catch (JSONException e) {
			shippingMethod = this.shippingMethod;
		}

		//addVariant(date, number, state,
		//			paymentState, shipmentState, mail, count, price);
		
	}
	/*
	
	/*
	private void obtainVariants(JSONObject orderJSON) {
		JSONArray variantArray = new JSONArray();
		
		try {
			variantArray = orderJSON.getJSONArray("variants");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// get master first
		for (int i = 0; i < variantArray.length(); i++) {
			JSONObject v = new JSONObject();
			try {
				v = variantArray.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
				// something broken? skip this one
				continue;
			}
			
			boolean isMaster = false;
			try {
				isMaster = v.getBoolean("is_master");
			} catch (JSONException e) {
				isMaster = false;
			}
			
			if (isMaster) {
				processVariantJSON(v);
				break;
			} 
		}		
	}
	*/
}
