package org.genshin.warehouse;

import org.genshin.spree.SpreeConnector;
import org.genshin.spree.VisualCode;
import org.genshin.warehouse.products.Product;
import org.genshin.warehouse.racks.ContainerTaxon;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.format.DateFormat;
import java.util.Date;

public class Warehouse {
	//Result codes from other Activities
	public static enum ResultCodes { SCAN, SETTINGS };
	
	private static Context ctx;
	private static SpreeConnector spree;
	
	private static ContainerTaxon container;
	private static Product product;
	private static VisualCode code;
	
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
	
	public static void setContainer(ContainerTaxon container) {
		Warehouse.container = container;
	}
	
	public static ContainerTaxon getContainer() {
		return Warehouse.container;
	}
	
	public static void setVisualCode(VisualCode code) {
		Warehouse.code = code;
	}
	
	public static VisualCode getVisualCode() {
		return Warehouse.code;
	}
}
