package org.genshin.warehouse.picking;

import org.genshin.warehouse.R;
import org.genshin.warehouse.picking.PickingListItem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PickingListAdapter extends ArrayAdapter<PickingListItem> {
	Context context;
	PickingListItem[] data;

	public PickingListAdapter(Context context, PickingListItem[] data) {
		super(context, R.layout.picking_list_item, data);
		this.context = context;
        this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.picking_list_item, parent, false);
		ImageView thumb = (ImageView)rowView.findViewById(R.id.picking_list_item_image);
		
		if (data[position].thumb != null)
			thumb.setImageDrawable(data[position].thumb);
		else
			thumb.setImageResource(R.drawable.spree);
		
		TextView name = (TextView) rowView.findViewById(R.id.picking_list_item_name);
		name.setText(data[position].name);
		TextView sku = (TextView) rowView.findViewById(R.id.picking_list_item_sku);
		sku.setText(data[position].sku);
		Button button = (Button) rowView.findViewById(R.id.picking_list_item_button);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.v("test", "Button Push!");
			}
		});

		return rowView;
	}
}
