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
	
	public ArrayList<_item> items;
	
	public SimpleMenuList(Context ctx) {
		this.ctx = ctx;
		items = new ArrayList<_item>();
	}
	
	// add
	// Adds a list item
	// Returns: new item index
	// リストにアイテムを追加
	// 戻り値: 追加されたアイテムの要素数
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
	
	public String[] getTitles() {
		String[] titles = new String[items.size()];
		 for (int i = 0; i < items.size(); i++)
		 {
			 titles[i] = items.get(i).title;
		 }
		 
		 return titles;
	}
	
	public Class<?> getIntentContext(int num) {
		return items.get(num).cls;
	}
}