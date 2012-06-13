package org.genshin.warehouse.picking;

import android.graphics.drawable.Drawable;

public class PickingListItem {
	public Drawable thumb;
	public String name;
	public String sku;
	public String link;
	public int id;
	
	public PickingListItem(Drawable thumb, String name, String sku, String link, int id) {
		super();
		this.id = id;
		this.thumb = thumb;
		this.name = name;
		this.sku = sku;
		this.link = link;
	}

}
