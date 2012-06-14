package org.genshin.warehouse;

import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.racks.Container;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.format.DateFormat;
import java.util.Date;

public class Warehouse {
	//Result codes from other Activities
	public static enum ResultCodes { SCAN, SETTINGS };
	
	private static Context ctx;
	private static SpreeConnector spree;
	
	private static Container container;
	
	public Warehouse(Context homeContext) {
		Warehouse.ctx = homeContext;
		spree = new SpreeConnector(Warehouse.ctx);
	}
	
	public static SpreeConnector Spree() {
		return spree;
	}
	
	public static void ChangeActivityContext(Context newContext) {
		Warehouse.ctx = newContext;
		spree = new SpreeConnector(Warehouse.ctx);
	}
	
	public static String getLocalDateString(Date date) {
		return DateFormat.getDateFormat(ctx).format(date);
	}
	
	public static void setContainer(Container container) {
		Warehouse.container = container;
	}
	
	public static Container getContainer() {
		return Warehouse.container;
	}
}
