package org.genshin.warehouse.orders;

import org.genshin.warehouse.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrderDetailsAdapter extends ArrayAdapter<OrderLineItem>{
	Context context;
	OrderLineItem[] data;

	public OrderDetailsAdapter(Context context, OrderLineItem[] data) {
		super(context, R.layout.order_details_list_item, data);
		this.context = context;
        this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater;
		if (convertView == null) {
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.order_details_list_item, parent, false);
		}

		TextView name = (TextView) convertView.findViewById(R.id.order_details_name);
		name.setText(data[position].name);
		TextView price = (TextView) convertView.findViewById(R.id.order_details_price);
		price.setText("" + data[position].price);
		TextView quantity = (TextView) convertView.findViewById(R.id.order_details_quantity);
		quantity.setText("" + data[position].quantity);
		TextView total = (TextView) convertView.findViewById(R.id.order_details_total);
		total.setText("" + data[position].total );
	
		return convertView;
	}

}
