package org.genshin.warehouse.products;

import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.R;
import org.genshin.warehouse.products.ProductsMenuActivity.resultCodes;
import org.genshin.warehouse.profiles.Profile;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetailsActivity extends Activity {
	SpreeConnector spree;
	
	Bundle extras;
	//private EditText id;
	private TextView name;
	private TextView sku;
	private TextView skuTitle;
	private TextView price;
	private TextView countOnHand;
	private TextView description;
	private TextView permalink;
	private ImageSwitcher imageSwitcher;
	private ProductImageViewer imageViewer;
	//private Image image;

	Product product;

	
	private void initViewElements() {
		//id = (TextView) findViewById(R.id.product_id);
		name = (TextView) findViewById(R.id.product_name);
		sku = (TextView) findViewById(R.id.product_sku);
		skuTitle = (TextView) findViewById(R.id.product_sku_title);
		price = (TextView) findViewById(R.id.product_price);
		countOnHand = (TextView) findViewById(R.id.product_count_on_hand);
		description = (TextView) findViewById(R.id.product_description);
		permalink = (TextView) findViewById(R.id.product_permalink);
		imageSwitcher = (ImageSwitcher) findViewById(R.id.product_image_switcher);
	}
	
	private void hookupInterface() {
		imageViewer = new ProductImageViewer(this);
		imageSwitcher.setFactory(imageViewer);
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_out));
        if (product.images.size() == 0)
        	imageSwitcher.setImageResource(R.drawable.spree);
        else
        	imageSwitcher.setImageDrawable(product.images.get(0));
	}

	private void getProductInfo() {
		product = ProductsMenuActivity.getSelectedProduct();
	}

	private void setViewFields() {
		name.setText(product.name);
		
		if (product.sku == "") {
			sku.setVisibility(View.GONE);
			skuTitle.setVisibility(View.GONE);
		} else
			sku.setText(product.sku);
		
		price.setText("" + product.price);
		countOnHand.setText("" + product.countOnHand);
		description.setText(product.description);
		permalink.setText(product.permalink);

	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        
        spree = new SpreeConnector(this);

		getProductInfo();
		initViewElements();
		setViewFields();
		hookupInterface();
	}
	
	public static enum menuCodes { stock, destock, registerVisualCode, addProductImage, editProductDetails };
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		Resources res = getResources();
        // メニューアイテムを追加します
        menu.add(Menu.NONE, menuCodes.stock.ordinal(), Menu.NONE, res.getString(R.string.stock_in));
        menu.add(Menu.NONE, menuCodes.destock.ordinal(), Menu.NONE, res.getString(R.string.destock));
        menu.add(Menu.NONE, menuCodes.registerVisualCode.ordinal(), Menu.NONE, res.getString(R.string.register_barcode));
        menu.add(Menu.NONE, menuCodes.addProductImage.ordinal(), Menu.NONE, res.getString(R.string.add_product_image));
        menu.add(Menu.NONE, menuCodes.editProductDetails.ordinal(), Menu.NONE, res.getString(R.string.edit_product_details));
        return super.onCreateOptionsMenu(menu);
    }
	
	public static enum resultCodes { scan };
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		  
		//Java can't do this!? WTF!
        /*switch (item.getItemId()) {
        	default:
        		return super.onOptionsItemSelected(item);
        	case registerVisualCode:
            
        		return true;
        }*/
		if (item.getItemId() == menuCodes.registerVisualCode.ordinal()) {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            //intent.putExtra("SCAN_MODE", "BARCODE_MODE");
            startActivityForResult(intent, resultCodes.scan.ordinal());
        	
			return true;
		}
        
        return false;
    }
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == resultCodes.scan.ordinal()) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                //TODO limit this to bar code types?
                if (format != "QR_CODE") {
                	//Assume barcode, and barcodes correlate to products
                	//Toast.makeText(this, "[" + format + "]: " + contents + "\nSearching!", Toast.LENGTH_LONG).show();
                	spree.connector.genericPut("api/products/" + product.permalink + "?product[visual_code]=" + contents);
                	//Toast.makeText(this, "Results:" + products.count, Toast.LENGTH_LONG).show();
                }
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            	Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

}
