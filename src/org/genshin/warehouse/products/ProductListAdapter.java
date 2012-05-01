package org.genshin.warehouse.products;

import org.genshin.warehouse.R;
import org.genshin.warehouse.products.ProductListItem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductListAdapter extends ArrayAdapter<ProductListItem> {
	Context context;
	ProductListItem[] data;

	public ProductListAdapter(Context context, ProductListItem[] data) {
		super(context, R.layout.product_list_item, data);
		this.context = context;
        this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.product_list_item, parent, false);
		ImageView thumb = (ImageView)rowView.findViewById(R.id.products_list_item_image);
		thumb.setImageResource(R.drawable.spree);//data[position].thumb);
		TextView name = (TextView) rowView.findViewById(R.id.products_list_item_name);
		name.setText(data[position].name);
		/*TextView sku = (TextView) rowView.findViewById(R.id.products_list_item_sku);
		sku.setText(data[position].sku);*/
		TextView count = (TextView) rowView.findViewById(R.id.products_list_item_count);
		count.setText("" + data[position].count);
		
		
		return rowView;
	}
}
