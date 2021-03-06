package org.genshin.warehouse.orders;

import java.util.ArrayList;
import java.util.Date;

import org.genshin.warehouse.products.Variant;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class OrderListItem {
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

	
	public OrderListItem(String number, Date date, String name, int count, double price,
			String division, String paymentState, String pickingState, 
			String packingState, String shipmentState) {
		super();
		this.number = number;
		this.date = date;
		this.name = name;
		this.count = count;
		this.price = price;
		this.division = division;
		this.paymentState = paymentState;
		this.pickingState = pickingState;
		this.packingState = packingState;
		this.shipmentState = shipmentState;
	}

}
