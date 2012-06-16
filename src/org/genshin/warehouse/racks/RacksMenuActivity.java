package org.genshin.warehouse.racks;

import java.util.ArrayList;

import org.genshin.spree.SpreeConnector;
import org.genshin.warehouse.R;
import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;

public class RacksMenuActivity extends Activity {
	SpreeConnector spree;
	ArrayList<ContainerTaxon> containerTaxons;
	
	private void loadContainers() {
		JSONArray taxonsJSON = spree.connector.getJSONArray("api/containers.json");
		
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.racks);
		
        spree = new SpreeConnector(this);
        
        loadContainers();
	}

}
