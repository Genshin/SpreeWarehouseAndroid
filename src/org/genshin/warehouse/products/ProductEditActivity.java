package org.genshin.warehouse.products;

import android.app.Activity;
import android.os.Bundle;

import org.genshin.warehouse.R;
import org.genshin.warehouse.products.Product;

public class ProductEditActivity extends Activity {
	private Product product;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_edit);
	}


}
