package org.genshin.warehouse.stocking;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.genshin.spree.RepetitiveScanner;
import org.genshin.spree.ScanSystem;
import org.genshin.spree.RepetitiveScanner.RepetitiveScanCodes;
import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.R;
import org.genshin.warehouse.Warehouse;
import org.genshin.warehouse.products.Product;
import org.genshin.warehouse.products.Products;
import org.genshin.warehouse.racks.ContainerTaxon;
import org.json.JSONException;
import org.json.JSONObject;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class StockingRepetitiveScanner extends RepetitiveScanner {
	SpreeConnector spree;
	Products products;


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
        Warehouse.ChangeActivityContext(this);
        
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
			ArrayList<Product> found = products.findByBarcode(contents);
			
			if (found.size() == 1) {
				stockProductToContainer(found.get(0), 1, Warehouse.getContainer());
			} else if (found.size() > 1) {
				// found multiple - select
			} else { 
				status = RepetitiveScanCodes.FINISH.ordinal();
				// not found, register new?
				products.unregisteredBarcode(contents);
			}
			
			
		}
		
	}
	
	public void stockProductToContainer(Product product, int quantity, ContainerTaxon container) {
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		Toast.makeText(this, "Registering" , Toast.LENGTH_LONG).show();
		pairs.add(new BasicNameValuePair("stock_record[variant_id]", "" + product.id));
		pairs.add(new BasicNameValuePair("stock_record[quantity]", "1"));
		if (container != null)
			pairs.add(new BasicNameValuePair("stock_record[container_taxon_id]", "" + container.id));
		pairs.add(new BasicNameValuePair("stock_record[direction]", "in"));
		
		spree.connector.postWithArgs("api/stock.json", pairs);
		status = RepetitiveScanCodes.FINISH.ordinal();
	}

	@Override
	protected void finishScanning() {
		// TODO Auto-generated method stub
		
	}

}
