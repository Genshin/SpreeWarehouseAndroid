package org.genshin.spree;

import org.genshin.warehouse.Warehouse.ResultCodes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public abstract class RepetitiveScanner extends Activity {
	public static enum RepetitiveScanCodes {ERROR, STANDBY, START, CONTINUE, FINISH};
	protected int status;
	private Intent intent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("DEFAULT_BYTE_MODE_ENCODING", "UTF-8");
		status = RepetitiveScanCodes.STANDBY.ordinal();
        
        start();
	}
	
	public abstract void beforeScanning();
	
	public abstract void onScanResult(Intent intent, String format, String contents);
	
	protected abstract void finishScanning();
	
	public void start() {
		status = RepetitiveScanCodes.START.ordinal();
        beforeScanning();
        status = RepetitiveScanCodes.CONTINUE.ordinal();
        continueScanning();
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
