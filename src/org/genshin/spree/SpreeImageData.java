package org.genshin.spree;

import android.graphics.drawable.Drawable;

public class SpreeImageData {
	public String name;
	public Integer id;
	public Drawable data;
	
	public SpreeImageData() {
		this.name = "";
		this.id = -1;
		this.data = null;
	}
	
	public SpreeImageData(String name, Integer id) {
		this.name = name;
		this.id = id;
		this.data = null;
	}
	
	public SpreeImageData(String name, Integer id, Drawable data) {
		this.name = name;
		this.id = id;
		this.data = data;
	}
}
