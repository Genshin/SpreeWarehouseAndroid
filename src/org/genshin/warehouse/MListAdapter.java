package org.genshin.warehouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import org.genshin.warehouse.MListItem;



public class MListAdapter extends ArrayAdapter<MListItem> {
	Context context;
    MListItem data[] = null;
    
	public MListAdapter(Context context, MListItem[] data) {
		super(context, R.layout.main_menu_item, data);
		this.context = context;
        this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.main_menu_item, parent, false);
		TextView title = (TextView) rowView.findViewById(R.id.menu_item_title);
		title.setText(data[position].title);
		TextView subText = (TextView) rowView.findViewById(R.id.menu_item_subtext);
		subText.setText(data[position].subText);
		ImageView icon = (ImageView)rowView.findViewById(R.id.menu_item_icon);
		icon.setImageResource(data[position].icon);
		
		return rowView;
	}
}
