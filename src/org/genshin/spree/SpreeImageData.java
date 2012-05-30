package org.genshin.spree;

import android.graphics.drawable.Drawable;

public class SpreeImageData {
	public String name;
	public Integer id;
	public Drawable image;
	
	public SpreeImageData() {
		this.name = "";
		this.id = -1;
		this.image = null;
	}
	
	public SpreeImageData(String name, Integer id) {
		this.name = name;
		this.id = id;
		this.image = null;
	}
	
	public SpreeImageData(String name, Integer id, Drawable image) {
		this.name = name;
		this.id = id;
		this.image = image;
	}
}
