package org.genshin.warehouse.orders;

import org.genshin.warehouse.R;
import org.genshin.warehouse.Warehouse;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderListAdapter extends ArrayAdapter<OrderListItem> {
	
	Context context;
	OrderListItem[] data;

	public OrderListAdapter(Context context, OrderListItem[] data) {
		super(context, R.layout.order_list_item, data);
		this.context = context;
        this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater;
		if (convertView == null) {
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.order_list_item, parent, false);
		}
		
	    TextView number = (TextView) convertView.findViewById(R.id.orders_list_item_number);
		number.setText(data[position].number);
		TextView date = (TextView) convertView.findViewById(R.id.orders_list_item_date);
		if (data[position].date != null)
			date.setText(Warehouse.getLocalDateString(data[position].date));
		else 
			date.setText("");
		TextView name = (TextView) convertView.findViewById(R.id.orders_list_item_name);
		name.setText(data[position].name);
		
		TextView count = (TextView) convertView.findViewById(R.id.orders_list_item_count);
		count.setText(data[position].count + " 個");
		TextView price = (TextView) convertView.findViewById(R.id.orders_list_item_price);
		price.setText(data[position].price + " 円");
		TextView division = (TextView) convertView.findViewById(R.id.orders_list_item_division);
		division.setText(data[position].division);
		
		/*
		TextView paymentMethod = 
				(TextView) convertView.findViewById(R.id.orders_list_item_payment_method);
		paymentMethod.setText(data[position].paymentMethod);
		*/
		
		ImageView paymentState = 
					(ImageView) convertView.findViewById(R.id.payment_status_icon);
		if (data[position].paymentState == "paid")
			paymentState.setImageResource(android.R.drawable.presence_online);
		else if (data[position].paymentState == "balance_due")
			paymentState.setImageResource(android.R.drawable.presence_busy);
		else
			paymentState.setImageResource(android.R.drawable.presence_away);
		Log.v("test", "" + data[position].paymentState);
			
		ImageView pickingState = 
					(ImageView) convertView.findViewById(R.id.picking_status_icon);
		//if (data[position].pickingState == "completed")
		//	pickingSState.setImageResource(android.R.drawable.presence_online);
		//else
			pickingState.setImageResource(android.R.drawable.presence_invisible);
				
		ImageView packingState = 
					(ImageView) convertView.findViewById(R.id.packing_status_icon);
		//if (data[position].packingState == "completed")
		//	packingState.setImageResource(android.R.drawable.presence_online);
		//else
			packingState.setImageResource(android.R.drawable.presence_invisible);

		ImageView shipmentState = 
					(ImageView) convertView.findViewById(R.id.shipment_status_icon);
		if (data[position].shipmentState == "completed")
			shipmentState.setImageResource(android.R.drawable.presence_online);
		else
			shipmentState.setImageResource(android.R.drawable.presence_busy);

		return convertView;
	}

}
