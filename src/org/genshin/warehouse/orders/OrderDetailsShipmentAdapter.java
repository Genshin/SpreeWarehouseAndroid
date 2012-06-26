package org.genshin.warehouse.orders;

import org.genshin.warehouse.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrderDetailsShipmentAdapter extends ArrayAdapter<OrderDetailsShipment>{
	Context context;
	OrderDetailsShipment[] data;

	public OrderDetailsShipmentAdapter(Context context, OrderDetailsShipment[] data) {
		super(context, R.layout.order_details_shipment_list, data);
		this.context = context;
        this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater;
		if (convertView == null) {
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.order_details_shipment_list, parent, false);
		}
		
		TextView number = (TextView) convertView.findViewById(R.id.order_details_shipment_number);
		number.setText(data[position].number);
		TextView method = (TextView) convertView.findViewById(R.id.order_details_shipment_method);
		method.setText(data[position].shippingMethod);
		TextView cost = (TextView) convertView.findViewById(R.id.order_details_shipment_cost);
		cost.setText(data[position].cost + " 円");
		TextView tracking = 
				(TextView) convertView.findViewById(R.id.order_details_shipment_tracking);
		tracking.setText(data[position].tracking);
		TextView state = (TextView) convertView.findViewById(R.id.order_details_shipment_state);
		state.setText("" + data[position].state);
		
		TextView date = (TextView) convertView.findViewById(R.id.order_details_shipment_date);
		date.setText("yyyy/MM/dd");
		TextView action = (TextView) convertView.findViewById(R.id.order_details_shipment_action);
		action.setText("");
		
		return convertView;
	}
}
