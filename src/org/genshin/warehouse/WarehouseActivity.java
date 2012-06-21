package org.genshin.warehouse;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.genshin.gsa.Dialogs;
import org.genshin.gsa.ScanSystem;
import org.genshin.gsa.ThumbListAdapter;
import org.genshin.gsa.ThumbListItem;
import org.genshin.warehouse.Warehouse.ResultCodes;
import org.genshin.warehouse.orders.OrdersMenuActivity;
import org.genshin.warehouse.packing.PackingMenuActivity;
import org.genshin.warehouse.products.Product;
import org.genshin.warehouse.products.ProductsMenuActivity;
import org.genshin.warehouse.profiles.Profiles;
import org.genshin.warehouse.racks.RacksMenuActivity;
import org.genshin.warehouse.settings.WarehouseSettingsActivity;
import org.genshin.warehouse.shipping.ShippingMenuActivity;
import org.genshin.warehouse.stocking.StockingMenuActivity;



public class WarehouseActivity extends Activity {
	Warehouse warehouse;
	
	//Interface objects
	private Button scanButton;
	private ListView menuList;
	private ImageView connectionStatusIcon;
	private Spinner profileSpinner; 
	private Profiles profiles;

	ThumbListItem[] menuListItems;
	
	private void createMainMenu() {
		//Create main menu list items
		menuListItems  = new ThumbListItem[] { 
				new ThumbListItem(R.drawable.products, getString(R.string.products), "", ProductsMenuActivity.class),
				new ThumbListItem(R.drawable.orders, getString(R.string.orders), "", OrdersMenuActivity.class),
				new ThumbListItem(R.drawable.stocking, getString(R.string.stocking), "", StockingMenuActivity.class),
				new ThumbListItem(R.drawable.racks, getString(R.string.racks), "", RacksMenuActivity.class),
				new ThumbListItem(R.drawable.picking, getString(R.string.picking), "", RacksMenuActivity.class),
				new ThumbListItem(R.drawable.packing, getString(R.string.packing), "", PackingMenuActivity.class),
				new ThumbListItem(R.drawable.shipping, getString(R.string.shipping), "", ShippingMenuActivity.class)
			};
	}
	
	private void loadProfiles() {
		//Load profiles from the local DB
		profiles = new Profiles(this);
		
		//set up spinner and select default
		profileSpinner = profiles.attachToSpinner(profileSpinner);
		profileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                profiles.selectProfile(position);
                checkConnection();
            }
	
			public void onNothingSelected(AdapterView<?> arg0) {	
                //checkConnection();
			}
		});
	}
	
	private void hookupInterface() {
		//Scan Button
		scanButton = (Button) findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		ScanSystem.initiateScan(v.getContext());
            }
		});
        
        //Menu List
        menuList = (ListView) findViewById(R.id.main_menu_actions_list);
        ThumbListAdapter menuAdapter = new ThumbListAdapter(this, menuListItems);
		menuList.setAdapter(menuAdapter);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	menuListClickHandler(parent, view, position);
            }
        });
        
      //Profile Spinner
      profileSpinner = (Spinner) findViewById(R.id.warehouse_profile_spinner);
      //Profile Spinner contents loaded and spinner refreshsed in loadProfiles
      loadProfiles();
      
      connectionStatusIcon = (ImageView) findViewById(R.id.connection_status_icon);
	}
	
	private void checkConnection() {
		Dialogs.showConnecting(this);
		String check = Warehouse.Spree().connector.test();
		Dialogs.dismiss();
		
		if (check == "OK") {
			connectionStatusIcon.setImageResource(android.R.drawable.presence_online);
		} else if (check == "ERROR"){
			connectionStatusIcon.setImageResource(android.R.drawable.presence_away);
		} else {
			connectionStatusIcon.setImageResource(android.R.drawable.presence_offline);
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        warehouse = new Warehouse(this); 
        
        createMainMenu();
        hookupInterface();
        
        checkConnection();
    }
    
    public void settingsClickHandler(View view)
	{
		Intent settingsIntent = new Intent(this, WarehouseSettingsActivity.class);
    	startActivityForResult(settingsIntent, ResultCodes.SETTINGS.ordinal());
	}
    
    private void menuListClickHandler(AdapterView<?> parent, View view,
            int position)
    {
        Intent menuItemIntent = new Intent(parent.getContext(), menuListItems[position].cls);
    	startActivity(menuItemIntent);
    }
    
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		Warehouse.ChangeActivityContext(this);
		
        if (requestCode == ResultCodes.SCAN.ordinal()) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                // Handle successful scan
				if (ScanSystem.isQRCode(format)) {
					//TODO if it's a QR code check if it's JSON

					//TODO if it's JSON parse it by the header

				} else if (ScanSystem.isProductCode(format)) {
					// if it's a Barcode it's a product
                	
                	ArrayList<Product> foundProducts = Warehouse.Products().findByBarcode(contents);
                	//one result means forward to that product
                	if (foundProducts.size() == 1) {
                		ProductsMenuActivity.showProductDetails(this, foundProducts.get(0));
                	} else if (foundProducts.size() == 0) {
                		//New product?
                		Warehouse.Products().unregisteredBarcode(contents);
                	} else if (foundProducts.size() > 1) {
						ProductsMenuActivity.selectProductActivity(this, format, contents);
                	}
                } else {
                	// not a hadled code type
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            	Toast.makeText(WarehouseActivity.this, getString(R.string.scan_cancelled), Toast.LENGTH_LONG).show();
            }
		} else if (resultCode == ResultCodes.PRODUCT_SELECT.ordinal()) {
			ProductsMenuActivity.showProductDetails(this, Warehouse.Products().selected());
        } else if (resultCode == ResultCodes.SETTINGS.ordinal()) {
        	loadProfiles();
        }
    }

}
