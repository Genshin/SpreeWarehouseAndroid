package org.genshin.warehouse.orders;

import org.genshin.warehouse.R;
import org.genshin.warehouse.products.ProductListItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderListAdapter extends ArrayAdapter<OrderListItem>{
	
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
		
		TextView date = (TextView) rowView.findViewById(R.id.orders_list_item_date);
		date.setText("" + data[position].date);
		TextView number = (TextView) rowView.findViewById(R.id.orders_list_item_number);
		number.setText(data[position].number);
		
		TextView state = (TextView) rowView.findViewById(R.id.orders_list_item_state);
		state.setText(data[position].state);
		TextView payment = (TextView) rowView.findViewById(R.id.orders_list_item_payment);
		payment.setText("" + data[position].paymentState);
		TextView shipment = (TextView) rowView.findViewById(R.id.orders_list_item_shipment);
		shipment.setText("" + data[position].shipmentState);
		
		TextView email = (TextView) rowView.findViewById(R.id.orders_list_item_email);
		email.setText("" + data[position].email);
		TextView total = (TextView) rowView.findViewById(R.id.orders_list_item_total);
		total.setText(data[position].count + " / " + data[position].price);
		
		
		return rowView;
	}

}
