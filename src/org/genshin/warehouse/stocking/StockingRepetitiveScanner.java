package org.genshin.warehouse.stocking;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.genshin.gsa.RepetitiveScanner;
import org.genshin.gsa.ScanSystem;
import org.genshin.gsa.RepetitiveScanner.RepetitiveScanCodes;
import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.R;
import org.genshin.warehouse.Warehouse;
import org.genshin.warehouse.Warehouse.ResultCodes;
import org.genshin.warehouse.products.Product;
import org.genshin.warehouse.products.Products;
import org.genshin.warehouse.products.ProductsMenuActivity;
import org.genshin.warehouse.racks.ContainerTaxon;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class StockingRepetitiveScanner extends RepetitiveScanner {
	@Override
	public void beforeScanning() {
		// TODO Auto-generated method stub
		
	}

	private void hookupInterface() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stocking_history);
        Warehouse.setContext(this);
        
        hookupInterface();
	}


	@Override
	public void onScanResult(Intent intent, String format, String contents) {
		if (ScanSystem.isQRCode(format)) {
			JSONObject qr;
			try {
				qr = new JSONObject(contents);
			} catch (JSONException e) {
				// Not a JSON QR Code
				return;
			}
			
			//it's a QR code, so see what type
			JSONObject containerJSON = null;
			try {
				containerJSON = qr.getJSONObject("container_taxon");
			} catch (JSONException e) {
				//not a container
				containerJSON = null;
			}
			
			if (containerJSON != null) {
				ContainerTaxon container = new ContainerTaxon(containerJSON);
				Warehouse.setContainer(container);
				
				Toast.makeText(this, "ContainerTaxon Set to:\n" + Warehouse.getContainer().name , Toast.LENGTH_LONG).show();
				
				status = RepetitiveScanCodes.FINISH.ordinal(); 
				return;
			} 
			
			
		} else if (ScanSystem.isProductCode(format)) {
			ArrayList<Product> found = Warehouse.Products().findByBarcode(contents);
			
			if (found.size() == 1) {
				Warehouse.Products().select(found.get(0));
				stockProductToContainer(Warehouse.Products().selected(), 1, Warehouse.getContainer());
			} else if (found.size() > 1) {
				// found multiple - select
				ProductsMenuActivity.selectProductActivity(this, format, contents);
			} else { 
				status = RepetitiveScanCodes.FINISH.ordinal();
				// not found, register new?
				Warehouse.Products().unregisteredBarcode(contents);
			}
			
			
		}
		
	}
	
	@Override
	public void onResultCode(int requestCode, int resultCode, Intent intent) {
		if (requestCode == ResultCodes.PRODUCT_SELECT.ordinal()) {
			stockProductToContainer(Warehouse.Products().selected(), 1, Warehouse.getContainer());
		}
	}
	
	public void stockProductToContainer(Product product, int quantity, ContainerTaxon container) {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		Toast.makeText(this, "Registering" , Toast.LENGTH_LONG).show();
		pairs.add(new BasicNameValuePair("stock_record[variant_id]", "" + product.id));
		pairs.add(new BasicNameValuePair("stock_record[quantity]", "" + quantity));
		if (container != null)
			pairs.add(new BasicNameValuePair("stock_record[container_taxon_id]", "" + container.id));
		pairs.add(new BasicNameValuePair("stock_record[direction]", "in"));
		  
		Warehouse.Spree().connector.postWithArgs("api/stock.json", pairs);
		
	/*	Warehouse.Products().find(product)*/product.refresh();
	}

	@Override
	protected void finishScanning() {
		// TODO Auto-generated method stub
		
	}
}
