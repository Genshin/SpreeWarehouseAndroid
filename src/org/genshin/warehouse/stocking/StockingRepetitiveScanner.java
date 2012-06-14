package org.genshin.warehouse.stocking;

import org.genshin.spree.RepetitiveScanner;
import org.genshin.spree.RepetitiveScanner.RepetitiveScanCodes;
import org.genshin.warehouse.Warehouse;
import org.genshin.warehouse.racks.Container;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.widget.Toast;

public class StockingRepetitiveScanner extends RepetitiveScanner {

	@Override
	public void beforeScanning() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScanResult(Intent intent, String format, String contents) {
		if (format.equals("QR_CODE")) {
			Toast.makeText(this, "Format is: [" + format + "]" , Toast.LENGTH_LONG).show();
			
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
				Container container = new Container(containerJSON);
				Warehouse.setContainer(container);
				
				Toast.makeText(this, "Container Set to:\n" + Warehouse.getContainer().name , Toast.LENGTH_LONG).show();
				
				status = RepetitiveScanCodes.FINISH.ordinal(); 
				return;
			} 
			
			
		} else if (format == "BARCODE") {
			
		}
		
	}

	@Override
	protected void finishScanning() {
		// TODO Auto-generated method stub
		
	}

}
