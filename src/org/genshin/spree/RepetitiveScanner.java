package org.genshin.spree;

import org.genshin.warehouse.Warehouse.ResultCodes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class RepetitiveScanner extends Activity {
	public static enum RepetitiveScanCodes {ERROR, STANDBY, START, CONTINUE, FINISH};
	private int status;
	private Intent intent;
	
	RepetitiveScanner() {
		intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("DEFAULT_BYTE_MODE_ENCODING", "UTF-8");
		status = RepetitiveScanCodes.STANDBY.ordinal();
	}
	
	public void beforeScanning() {
		status = RepetitiveScanCodes.CONTINUE.ordinal();
	}
	
	public void onScanResult(Intent intent, String format, String contents) {
		
	}
	
	private void finishScanning() {
		
	}
	
	public void start() {
		status = RepetitiveScanCodes.START.ordinal();
        beforeScanning();
				
	    
	}
	
	private void continueScanning() {
		startActivityForResult(intent, ResultCodes.SCAN.ordinal());
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
            	onScanResult(intent, intent.getStringExtra("SCAN_RESULT_FORMAT"), intent.getStringExtra("SCAN_RESULT"));
                if (status == RepetitiveScanCodes.CONTINUE.ordinal()) {
                	continueScanning();
                } else if (status == RepetitiveScanCodes.FINISH.ordinal()) {
                	finishScanning();
                }
            } else if (resultCode == RESULT_CANCELED) {
                finishScanning();
            }
        }
    }
}
