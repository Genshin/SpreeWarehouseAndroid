package org.genshin.warehouse.racks;

import java.util.ArrayList;

import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.R;
import org.genshin.warehouse.Warehouse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;

public class RacksMenuActivity extends Activity {
	SpreeConnector spree;
	ContainerTaxonomies containerTaxonomies;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.racks);
		
        Warehouse.setContext(this);
        
        containerTaxonomies = new ContainerTaxonomies();
	}

}
