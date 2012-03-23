package org.genshin.warehouse;

import android.app.Activity;

public class MListItem {
	public int icon;
	public String title;
	public String subText;
	public Class<?> cls;
	
	public MListItem(int icon, String title, String subText, Class<?> cls) {
		super();
		this.icon = icon;
		this.title = title;
		this.subText = subText;
		this.cls = cls;
	}
}
