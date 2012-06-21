package org.genshin.gsa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import org.genshin.warehouse.R;
import org.genshin.warehouse.R.id;
import org.genshin.warehouse.R.layout;



public class ThumbListAdapter extends ArrayAdapter<ThumbListItem> {
	Context context;
    ThumbListItem data[] = null;
    
	public ThumbListAdapter(Context context, ThumbListItem[] data) {
		super(context, R.layout.thumb_menu_item, data);
		this.context = context;
        this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.thumb_menu_item, parent, false);
		TextView title = (TextView) rowView.findViewById(R.id.menu_item_title);
		title.setText(data[position].title);
		TextView subText = (TextView) rowView.findViewById(R.id.menu_item_subtext);
		subText.setText(data[position].subText);
		ImageView icon = (ImageView)rowView.findViewById(R.id.menu_item_icon);
		icon.setImageResource(data[position].icon);
		
		return rowView;
	}
}
