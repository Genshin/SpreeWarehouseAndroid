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
	ArrayList<ContainerTaxonomy> taxonomies;
	
	public static ArrayList<ContainerTaxonomy> getContainers() {
		JSONArray taxonomiesJSON = Warehouse.Spree().connector.getJSONArray("/api/container_taxonomies.json");
		
		ArrayList<ContainerTaxonomy> taxonomies = new ArrayList<ContainerTaxonomy>();
		
		for (int i = 0; i < taxonomiesJSON.length(); i++) {
			JSONObject taxonomyJSON = null;
			try {
				taxonomyJSON = taxonomiesJSON.getJSONObject(i);
			} catch (JSONException e) {
				taxonomyJSON = null;
				e.printStackTrace();
			}
			if (taxonomyJSON != null)
				taxonomies.add(new ContainerTaxonomy(taxonomyJSON));
			
		}
		
		return taxonomies;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.racks);
		
        Warehouse.ChangeActivityContext(this);
        
        taxonomies = RacksMenuActivity.getContainers();
	}

}
