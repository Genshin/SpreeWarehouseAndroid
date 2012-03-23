package org.genshin.warehouse;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;

public class ScanSystem extends Activity implements View.OnClickListener {
	public void onClick(View v) {
		Toast.makeText(v.getContext(), "scan", Toast.LENGTH_LONG).show();
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
    }
	
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
