package org.genshin.warehouse.settings;

import org.genshin.gsa.SimpleMenuList;
import org.genshin.warehouse.R;
import org.genshin.warehouse.R.id;
import org.genshin.warehouse.R.layout;
import org.genshin.warehouse.profiles.ProfileSettingsActivity;
import org.genshin.warehouse.WarehouseActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class WarehouseSettingsActivity extends Activity {
	SimpleMenuList menu;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warehouse_settings);

              
		ListView menuList = (ListView) findViewById(R.id.settings_category_list);
        
        menu = new SimpleMenuList(this);
        	menu.add(R.string.profiles, ProfileSettingsActivity.class);
        	menu.add(R.string.defaults, WarehouseActivity.class);
        	menu.add(R.string.camera, ProfileSettingsActivity.class);
        
        

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, menu.getTitles());
		menuList.setAdapter(adapter);

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	menuListClickHandler(parent, view, position);
            }
        });

	}
	
	private void menuListClickHandler(AdapterView<?> parent, View view,
            int position)
    {
    	Intent toIntent = new Intent(parent.getContext(), menu.getIntentContext(position));
    	startActivity(toIntent);
    }

}
