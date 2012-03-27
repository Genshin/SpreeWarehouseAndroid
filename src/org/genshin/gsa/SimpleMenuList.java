package org.genshin.gsa;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;

public class SimpleMenuList {
	private Context ctx;
	
	final class _item {
		public String title;
		public Class<?> cls;

		public _item(String title, Class<?> cls) {
			this.title = title;
			this.cls = cls;
		}	
	}
	
	public SimpleMenuList(Context ctx) {
		this.ctx = ctx;
	}
	
	public ArrayList<_item> items;
	
	// add
	// Adds a list item
	// リストにアイテムを追加
	public int add(String title, Class<?> cls) {
		_item newItem = new _item(title, cls);
		items.add(newItem);
		return items.size() - 1;
	}
	
	public int add(int titleRstring, Class<?> cls) {
		_item newItem = new _item(ctx.getString(titleRstring), cls);
		items.add(newItem);
		return items.size() - 1;
	}
}
