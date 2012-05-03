package org.genshin.warehouse.products;

import org.genshin.warehouse.R;
import org.genshin.warehouse.profiles.Profile;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

public class ProductDetailsActivity extends Activity {
	Bundle extras;
	//private EditText id;
	private TextView name;
	private TextView price;
	private TextView countOnHand;
	private TextView description;
	private TextView permalink;
	//private Image image;

	Product product;

	
	private void initViewElements() {
		//id = (TextView) findViewById(R.id.product_id);
		name = (TextView) findViewById(R.id.product_name);
		price = (TextView) findViewById(R.id.product_price);
		countOnHand = (TextView) findViewById(R.id.product_count_on_hand);
		description = (TextView) findViewById(R.id.product_description);
		permalink = (TextView) findViewById(R.id.product_permalink);
	}

	private void getProductInfo() {
		extras = getIntent().getExtras();
		Bundle productBundle = extras.getBundle("productBundle");
		product = productBundle.getParcelable("product");/*new Product(
				extras.getInt("id"),
				extras.getString("name"),
				extras.getDouble("price"),
				extras.getInt("countOnHand"),
				extras.getString("description"),
				extras.getString("permalink")
			);*/
	}

	private void setViewFields() {
		name.setText(product.name);
		price.setText("" + product.price);
		countOnHand.setText("" + product.countOnHand);
		description.setText(product.description);
		permalink.setText(product.permalink);

	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);

		getProductInfo();
		initViewElements();
		setViewFields();
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
}
