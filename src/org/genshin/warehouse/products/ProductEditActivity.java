package org.genshin.warehouse.products;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ToggleButton;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.R;
import org.genshin.warehouse.products.Product;

public class ProductEditActivity extends Activity {
	private Boolean isNew;
	private Product product;

	private EditText nameEdit;
	private EditText skuEdit;
	private EditText priceEdit;
	private EditText permalinkEdit;
	private EditText descriptionEdit;

	private ImageSwitcher imageSwitcher;
	private ProductImageViewer imageViewer;

	private Button saveButton;
	private Button deleteButton;
	private ToggleButton listedToggle;

	private void initViewElements() {
		nameEdit = (EditText) findViewById(R.id.product_name_edit);
		skuEdit = (EditText) findViewById(R.id.product_sku_edit);
		priceEdit = (EditText) findViewById(R.id.product_price_edit);
		permalinkEdit = (EditText) findViewById(R.id.product_permalink_edit);
	    descriptionEdit = (EditText) findViewById(R.id.product_description_edit);

		imageSwitcher = (ImageSwitcher) findViewById(R.id.product_image_switcher);

		deleteButton = (Button) findViewById(R.id.delete_button);
		saveButton = (Button) findViewById(R.id.save_button);


	}

	private void hookupInterface() {
		nameEdit.setText(product.name);
		skuEdit.setText(product.sku);
		priceEdit.setText("" + product.price);
		permalinkEdit.setText(product.permalink);
		descriptionEdit.setText(product.description);

		imageViewer = new ProductImageViewer(this);
		imageSwitcher.setFactory(imageViewer);
		imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_out));
        if (product.images.size() == 0)
        	imageSwitcher.setImageResource(R.drawable.spree);
        else
        	imageSwitcher.setImageDrawable(product.images.get(0));

		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				saveProduct();
			}
		});

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_edit);

        product = ProductsMenuActivity.getSelectedProduct();
        
		initViewElements();
        hookupInterface();
	}

	private void saveProduct() {
		SpreeConnector spree = new SpreeConnector(this);
		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("product[name]", nameEdit.getText().toString()));
		pairs.add(new BasicNameValuePair("product[price]", priceEdit.getText().toString()));
		pairs.add(new BasicNameValuePair("product[permalink]", permalinkEdit.getText().toString()));
		pairs.add(new BasicNameValuePair("product[description]", descriptionEdit.getText().toString()));
		//TODO add SKU etc.
		
		spree.connector.putWithArgs("api/products/" + product.id + ".json", pairs);

		
	}
}
