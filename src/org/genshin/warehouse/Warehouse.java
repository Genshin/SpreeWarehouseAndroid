package org.genshin.warehouse;

import org.genshin.spree.SpreeConnector;

import android.content.Context;

public class Warehouse {
	//Result codes from other Activities
	public static enum ResultCodes { SCAN, SETTINGS };
	
	private static Context ctx;
	private static SpreeConnector spree;
	
	public Warehouse(Context homeContext) {
		Warehouse.ctx = homeContext;
		spree = new SpreeConnector(Warehouse.ctx);
	}
	
	public static SpreeConnector Spree() {
		return spree;
	}
}
