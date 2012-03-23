package org.genshin.warehouse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import org.genshin.warehouse.*;
import org.genshin.warehouse.orders.OrdersMenuActivity;
import org.genshin.warehouse.packing.PackingMenuActivity;
import org.genshin.warehouse.picking.PickingMenuActivity;
import org.genshin.warehouse.products.ProductsMenuActivity;
import org.genshin.warehouse.settings.WarehouseSettingsActivity;
import org.genshin.warehouse.shipping.ShippingMenuActivity;
import org.genshin.warehouse.stocking.StockingMenuActivity;

public class WarehouseActivity extends Activity {
	
	enum resultCodes { scan };

	MListItem[] menuListItems;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        //設定ボタン	

		menuListItems  = new MListItem[] { 
				new MListItem(R.drawable.products, getString(R.string.products), "", ProductsMenuActivity.class),
				new MListItem(R.drawable.orders, getString(R.string.orders), "", OrdersMenuActivity.class),
				new MListItem(R.drawable.stocking, getString(R.string.stocking), "", StockingMenuActivity.class),
				new MListItem(R.drawable.picking, getString(R.string.picking), "", PickingMenuActivity.class),
				new MListItem(R.drawable.packing, getString(R.string.packing), "", PackingMenuActivity.class),
				new MListItem(R.drawable.shipping, getString(R.string.shipping), "", ShippingMenuActivity.class)
			};
        
        Button scanButton = (Button) findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Toast.makeText(v.getContext(), getString(R.string.scan), Toast.LENGTH_LONG).show();
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, resultCodes.scan.ordinal());
            }
		});

        ListView menuList = (ListView) findViewById(R.id.main_menu_actions_list);

        MListAdapter menuAdapter = new MListAdapter(this, menuListItems);
		menuList.setAdapter(menuAdapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	menuListClickHandler(parent, view, position);
            }
        });
        
        
    }
    
    public void settingsClickHandler(View view)
	{
		Intent settingsIntent = new Intent(this, WarehouseSettingsActivity.class);
    	startActivity(settingsIntent);
	}
    
    public void menuListClickHandler(AdapterView<?> parent, View view,
            int position)
    {
        //ListView listView = (ListView) parent;
        // クリックされたアイテムを取得します
        //String item = (listView.getItemAtPosition(position)).title;
        //Toast.makeText(WarehouseActivity.this, "pos: " + position, Toast.LENGTH_LONG).show();
        Intent menuItemIntent = new Intent(parent.getContext(), menuListItems[position].cls);
    	startActivity(menuItemIntent);
    }
    
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == resultCodes.scan.ordinal()) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast.makeText(WarehouseActivity.this, "[" + format + "]: " + contents, Toast.LENGTH_LONG).show();
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            	Toast.makeText(WarehouseActivity.this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

}
