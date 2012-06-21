package org.genshin.gsa;

import android.app.Activity;

public class ThumbListItem {
	public int icon;
	public String title;
	public String subText;
	public Class<?> cls;
	
	public ThumbListItem(int icon, String title, String subText, Class<?> cls) {
		super();
		this.icon = icon;
		this.title = title;
		this.subText = subText;
		this.cls = cls;
	}
}
