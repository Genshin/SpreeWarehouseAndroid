package org.genshin.warehouse.racks;

import java.util.ArrayList;

import org.genshin.warehouse.Warehouse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContainerTaxonomies {

	public static ArrayList<ContainerTaxonomy> taxonomies;
	
	public ContainerTaxonomies() {
		if (taxonomies == null) {
			taxonomies = new ArrayList<ContainerTaxonomy>();
			updateTaxonomies();
		}
	}
	
	public void updateTaxonomies() {
		JSONArray taxonomiesJSON = Warehouse.Spree().connector.getJSONArray("/api/container_taxonomies.json");
		
		for (int i = 0; i < taxonomiesJSON.length(); i++) {
			JSONObject taxonomyJSON = null;
			try {
				taxonomyJSON = taxonomiesJSON.getJSONObject(i);
			} catch (JSONException e) {
				taxonomyJSON = null;
				e.printStackTrace();
			}
				
			if (taxonomyJSON != null) {
				// take off the wrapper
				JSONObject taxonomyJSONData = null;
				try {
					taxonomyJSONData = taxonomyJSON.getJSONObject("container_taxonomy");
				} catch (JSONException e) {
					taxonomyJSONData = null;
				}
				
				if (taxonomyJSONData != null)
					taxonomies.add(new ContainerTaxonomy(taxonomyJSONData));
			}
		}
	}
}
