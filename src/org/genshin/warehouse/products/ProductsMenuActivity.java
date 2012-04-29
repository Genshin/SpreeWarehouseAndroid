package org.genshin.warehouse.products;

import org.genshin.warehouse.R;
import org.genshin.warehouse.R.layout;
import org.genshin.warehouse.SpreeConnector;

import android.app.Activity;
import android.os.Bundle;

public class ProductsMenuActivity extends Activity {
	private Products products;
	private SpreeConnector spree;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);

        spree = new SpreeConnector(this);
        products = new Products(this, spree);
        
        products.getNewestProducts(10);

	}
}
