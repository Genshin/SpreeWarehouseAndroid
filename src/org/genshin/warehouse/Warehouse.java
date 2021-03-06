package org.genshin.warehouse;

import org.genshin.gsa.VisualCode;
import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.products.Product;
import org.genshin.warehouse.products.Products;
import org.genshin.warehouse.racks.ContainerTaxon;
import org.genshin.warehouse.racks.WarehouseDivision;
import org.genshin.warehouse.racks.WarehouseDivisions;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.format.DateFormat;

import java.util.ArrayList;
import java.util.Date;

public class Warehouse {
	//Result codes from other Activities
	public static enum ResultCodes { SCAN, SETTINGS, PRODUCT_SELECT };
	
	private static Context ctx;
	private static SpreeConnector spree;
	
	private static ContainerTaxon container;
	private static VisualCode code;

	private static Products products;
	
	private static WarehouseDivisions warehouses;
	
	public Warehouse(Context homeContext) {
		Warehouse.ctx = homeContext;
		Warehouse.container = null;
		spree = new SpreeConnector(Warehouse.ctx);
		products = new Products(homeContext);
	}
	
	public static SpreeConnector Spree() {
		return spree;
	}
	
	public static Products Products() {
		return Warehouse.products;
	}
	
	public static WarehouseDivisions Warehouses() {
		if (Warehouse.warehouses == null)
			Warehouse.warehouses = new WarehouseDivisions();
		
		return Warehouse.warehouses;
	}
	
	public static void setContext(Context newContext) {
		Warehouse.ctx = newContext;
		spree = new SpreeConnector(Warehouse.ctx);
	}
	
	public static Context getContext() {
		return Warehouse.ctx;
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
