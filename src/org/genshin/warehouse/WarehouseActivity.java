package org.genshin.warehouse;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.genshin.gsa.RESTConnector;
import org.genshin.warehouse.*;
import org.genshin.warehouse.orders.OrdersMenuActivity;
import org.genshin.warehouse.packing.PackingMenuActivity;
import org.genshin.warehouse.picking.PickingMenuActivity;
import org.genshin.warehouse.products.ProductsMenuActivity;
import org.genshin.warehouse.profiles.Profile;
import org.genshin.warehouse.profiles.Profiles;
import org.genshin.warehouse.settings.WarehouseSettingsActivity;
import org.genshin.warehouse.shipping.ShippingMenuActivity;
import org.genshin.warehouse.stocking.StockingMenuActivity;


public class WarehouseActivity extends Activity {
	//Interface objects
	private Button scanButton;
	private ListView menuList;
	private Spinner profileSpinner;
	private ToggleButton loginToggleButton; 
	private Profiles profiles;
	//Result codes from other Activities
	public static enum resultCodes { scan };

	ThumbListItem[] menuListItems;
	
	private void createMainMenu() {
		//Create main menu list items
		menuListItems  = new ThumbListItem[] { 
				new ThumbListItem(R.drawable.products, getString(R.string.products), "", ProductsMenuActivity.class),
				new ThumbListItem(R.drawable.orders, getString(R.string.orders), "", OrdersMenuActivity.class),
				new ThumbListItem(R.drawable.stocking, getString(R.string.stocking), "", StockingMenuActivity.class),
				new ThumbListItem(R.drawable.picking, getString(R.string.picking), "", PickingMenuActivity.class),
				new ThumbListItem(R.drawable.packing, getString(R.string.packing), "", PackingMenuActivity.class),
				new ThumbListItem(R.drawable.shipping, getString(R.string.shipping), "", ShippingMenuActivity.class)
			};
	}
	
	private void loadProfiles() {
		//get spinner
		profileSpinner = (Spinner) findViewById(R.id.warehouse_profile_spinner);
		
		//Load profiles from the local DB
		profiles = new Profiles(this);
		
		//set up spinner and select default
		profileSpinner = profiles.attachToSpinner(profileSpinner);
	}
	
	private void hookupInterface() {
		//Scan Button
		scanButton = (Button) findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		Toast.makeText(v.getContext(), getString(R.string.scan), Toast.LENGTH_LONG).show();
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                //intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, resultCodes.scan.ordinal());
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
        
        //Profile Spinner hooked up and loaded in loadProfiles
        loadProfiles();
        
        

        //Login Toggle Button
        loginToggleButton = (ToggleButton) findViewById(R.id.login_toggleButton);
        loginToggleButton.setTextOff("Connect");
        loginToggleButton.setTextOn("Disconnect");
        loginToggleButton.setChecked(false);
        
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
        
        createMainMenu();
        hookupInterface();
    }
    
    public void settingsClickHandler(View view)
	{
		Intent settingsIntent = new Intent(this, WarehouseSettingsActivity.class);
    	startActivity(settingsIntent);
	}
    
    private void menuListClickHandler(AdapterView<?> parent, View view,
            int position)
    {
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
                //TODO if it's a barcode it's a product
                //TODO if it's a QR code check if it's JSON
                //TODO if it's JSON parse it by the header
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            	Toast.makeText(WarehouseActivity.this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }

}
