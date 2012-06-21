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
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.order_list_item, parent, false);
		
	    TextView number = (TextView) rowView.findViewById(R.id.orders_list_item_number);
		number.setText(data[position].number);
		TextView date = (TextView) rowView.findViewById(R.id.orders_list_item_date);
		if (data[position].date != null)
			date.setText(Warehouse.getLocalDateString(data[position].date));
		else 
			date.setText("");
		TextView name = (TextView) rowView.findViewById(R.id.orders_list_item_name);
		name.setText(data[position].name);
		
		TextView count = (TextView) rowView.findViewById(R.id.orders_list_item_count);
		count.setText("" + data[position].count);
		TextView price = (TextView) rowView.findViewById(R.id.orders_list_item_price);
		price.setText("" + data[position].price);
		TextView division = (TextView) rowView.findViewById(R.id.orders_list_item_division);
		division.setText(data[position].division);
		
		TextView paymentMethod = 
				(TextView) rowView.findViewById(R.id.orders_list_item_payment_method);
		paymentMethod.setText(data[position].paymentMethod);
		TextView paymentState = 
				(TextView) rowView.findViewById(R.id.orders_list_item_payment_state);
		paymentState.setText(data[position].paymentState);
		TextView shippingMethod = 
				(TextView) rowView.findViewById(R.id.orders_list_item_shipping_method);
		shippingMethod.setText(data[position].shippingMethod);

		return rowView;
	}

}
