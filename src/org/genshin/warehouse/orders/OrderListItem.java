package org.genshin.warehouse.orders;

import java.util.ArrayList;
import java.util.Date;

import org.genshin.warehouse.products.Variant;

import android.graphics.drawable.Drawable;

public class OrderListItem {
	public Date date;
	public String number;
	public String state;
	public String paymentState;
	public String shipmentState;
	public String email;
	public int count;
	public int price;
	
	public OrderListItem(Date date, String number, String state, String paymentState,
			String shipmentState, String email, int count, int price) {
		super();
		this.date = date;
		this.number = number;
		this.state = state;
		this.paymentState = paymentState;
		this.shipmentState = shipmentState;
		this.email = email;
		this.count = count;
		this.price = price;
	}

}
