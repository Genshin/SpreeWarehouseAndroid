package org.genshin.warehouse.products;

import org.genshin.warehouse.R;
import org.genshin.warehouse.R.layout;

import android.app.Activity;
import android.os.Bundle;

public class ProductsMenuActivity extends Activity {
	Products products;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products);
        
        products = new Products(this);
        
        products.getNewestProducts(10);
	}
}
