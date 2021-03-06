package org.genshin.warehouse.stocking;

import java.util.ArrayList;

import org.genshin.gsa.RepetitiveScanner;
import org.genshin.gsa.ScanSystem;
import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.R;
import org.genshin.warehouse.Warehouse;
import org.genshin.warehouse.Warehouse.ResultCodes;
import org.genshin.warehouse.products.Product;
import org.genshin.warehouse.products.Products;
import org.genshin.warehouse.racks.ContainerTaxonomies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class StockingMenuActivity extends Activity {
	private EditText supplierText;
	private EditText orderNumberText;
	private ImageButton stockingScanButton;
	private ContainerTaxonomies containerTaxonomies;
	private Spinner targetContainerSpinner;

	private void hookupInterface() {
		supplierText = (EditText) findViewById(R.id.supplier_text);
		orderNumberText = (EditText) findViewById(R.id.order_number_text);
		
		stockingScanButton = (ImageButton) findViewById(R.id.scan_stocking_button);
		stockingScanButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StockingRepetitiveScanner.class);
                //intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, ResultCodes.SCAN.ordinal());
            }
		});
		
		targetContainerSpinner = (Spinner) findViewById(R.id.target_container_spinner);
		//TODO fill

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stocking);

        Warehouse.setContext(this);
        containerTaxonomies = new ContainerTaxonomies();
        
        hookupInterface();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        /*if (requestCode == ResultCodes.SCAN.ordinal()) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                // Handle successful scan
                //if it's a Barcode it's a product
                if (ScanSystem.isProductCode(format)) {
                	ArrayList<Product> foundProducts = Warehouse.Products().findByBarcode(contents);
                	//one result means forward to that product
                	if (foundProducts.size() == 1) {
						//found exact product, stock it
						Toast.makeText(this, "found product", Toast.LENGTH_LONG).show();
                	}
                	
                } else {
					//QR code
					Toast.makeText(this, "processing QR", Toast.LENGTH_LONG).show();

				}
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            	Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            }
        }*/
	}
	
}
