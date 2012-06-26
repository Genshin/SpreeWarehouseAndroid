package org.genshin.warehouse.orders;

import org.genshin.warehouse.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class OrderDetailsPaymentAdapter extends ArrayAdapter<OrderDetailsPayment>{
	Context context;
	OrderDetailsPayment[] data;

	public OrderDetailsPaymentAdapter(Context context, OrderDetailsPayment[] data) {
		super(context, R.layout.order_details_payment_list, data);
		this.context = context;
        this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater;
		if (convertView == null) {
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.order_details_payment_list, parent, false);
		}

		TextView date = (TextView) convertView.findViewById(R.id.order_details_payment_date);
		date.setText("yyyy/MM/dd");
		TextView amount = (TextView) convertView.findViewById(R.id.order_details_payment_amount);
		amount.setText("" + data[position].amount + " 円");
		TextView method = (TextView) convertView.findViewById(R.id.order_details_payment_method);
		method.setText("" + data[position].paymentMethod);
		TextView state = (TextView) convertView.findViewById(R.id.order_details_payment_state);
		state.setText("" + data[position].paymentState);
		TextView action = (TextView) convertView.findViewById(R.id.order_details_payment_action);
		action.setText("");
		
		return convertView;
	}
}
