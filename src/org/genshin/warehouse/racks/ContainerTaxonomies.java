package org.genshin.warehouse.racks;

import java.util.ArrayList;

import org.genshin.warehouse.Warehouse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ContainerTaxonomies {

	public ArrayList<ContainerTaxonomy> taxonomies;
	
	public ContainerTaxonomy get(int index) {
		return taxonomies.get(index);
	}
	
	public int size() {
		return taxonomies.size();
	}
	
	public ContainerTaxonomies() {
		if (taxonomies == null) {
			taxonomies = new ArrayList<ContainerTaxonomy>();
		}
	}
	
	public ContainerTaxonomies(JSONArray taxonomiesJSON) {
		if (taxonomies == null) {
			taxonomies = new ArrayList<ContainerTaxonomy>();
		}
		
		getTaxonomies(taxonomiesJSON);
	}
	
	public void getTaxonomies(JSONArray taxonomiesJSON) {		
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
				
				Log.d("ContainerTaxonomies", "Added container taxonomy " + taxonomies.get(taxonomies.size() - 1).name);
			}
		}
	}
}
