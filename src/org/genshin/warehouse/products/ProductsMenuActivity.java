package org.genshin.warehouse.products;

import java.util.List;

import org.genshin.warehouse.R;
import org.genshin.warehouse.ThumbListAdapter;
import org.genshin.warehouse.WarehouseActivity;
import org.genshin.warehouse.R.layout;
import org.genshin.warehouse.WarehouseActivity.resultCodes;
import org.genshin.warehouse.SpreeConnector;
import org.genshin.warehouse.ThumbListItem;
import org.genshin.warehouse.products.ProductDetailsActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductsMenuActivity extends Activity {
	private Products products;
	private SpreeConnector spree;
	private ProductListAdapter productsAdapter;
	private ListView productList;
	private TextView statusText;
	private ImageButton scanButton;
	public static enum resultCodes { scan };

	private void hookupInterface() {
		//Product List
        productList = (ListView) findViewById(R.id.product_menu_list);
        
        statusText = (TextView) findViewById(R.id.status_text);
        
		scanButton = (ImageButton) findViewById(R.id.products_menu_scan_button);
		scanButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Toast.makeText(v.getContext(), getString(R.string.scan), Toast.LENGTH_LONG).show();
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                //intent.putExtra("SCAN_MODE", "BARCODE_MODE");
                startActivityForResult(intent, resultCodes.scan.ordinal());
            }
		});
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);

        spree = new SpreeConnector(this);
        products = new Products(this, spree);
        
        hookupInterface();
        
        products.getNewestProducts(10);
        
        
        
        refreshProductMenu();
	}
	
	private void refreshProductMenu() {
		Log.d("PRODUCTLIST", "length " + products.list.size());
		
		ProductListItem[] productListItems = new ProductListItem[products.list.size()];
		
		for (int i = 0; i < products.list.size(); i++) {
			Product p = products.list.get(i);
			productListItems[i] = new ProductListItem(null, p.name, "SKU", p.countOnHand, p.permalink, p.id);
		}
		
		statusText.setText(products.count + "品");
		
		productsAdapter = new ProductListAdapter(this, productListItems);
		productList.setAdapter(productsAdapter);
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            	productListClickHandler(parent, view, position);
            }
        });
        
	}
	
	private void productListClickHandler(AdapterView<?> parent, View view, int position) {
		Intent productDetailsIntent = new Intent(this, ProductDetailsActivity.class);
		
		productDetailsIntent.putExtra("id", products.list.get(position).id);
		productDetailsIntent.putExtra("name", products.list.get(position).name);
		productDetailsIntent.putExtra("price", products.list.get(position).price);
		productDetailsIntent.putExtra("countOnHand", products.list.get(position).countOnHand);
		productDetailsIntent.putExtra("description", products.list.get(position).description);
		productDetailsIntent.putExtra("permalink", products.list.get(position).permalink);
		
    	startActivity(productDetailsIntent);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == resultCodes.scan.ordinal()) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                //TODO limit this to bar code types?
                if (format != "QR_CODE") {
                	//Assume barcode, and barcodes correlate to products
                	Toast.makeText(this, "[" + format + "]: " + contents + "\nSearching!", Toast.LENGTH_LONG).show();
                	products.scanSearch(contents);
                	refreshProductMenu();
                	Toast.makeText(this, "Results:" + products.count, Toast.LENGTH_LONG).show();
                }
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            	Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
