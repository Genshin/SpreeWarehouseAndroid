package org.genshin.spree;

import org.genshin.warehouse.Warehouse.ResultCodes;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;

public class ScanSystem extends Activity {
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }
}
