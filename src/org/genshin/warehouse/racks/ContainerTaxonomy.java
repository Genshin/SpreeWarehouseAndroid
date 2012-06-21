/////////////////////////////////////////////////////////////
// This is for a SINGLE Container Taxonomy

package org.genshin.warehouse.racks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ContainerTaxonomy {
	public int id;
	public String name;
	public ContainerTaxon root;
	
	public ContainerTaxonomy(JSONObject taxonomyJSON) {
		getTaxonomyInfo(taxonomyJSON);
		getRoot(taxonomyJSON);
	}
	
	private void getTaxonomyInfo(JSONObject taxonomyJSON) {
		try {
			this.id = taxonomyJSON.getInt("id");
		} catch (JSONException e) {
			// No ID, but it could contain permalink etc. 
			Log.d("ContainerTaxonomy.getTaxonomyInfo", "Taxonomy did not have a proper ID, setting to -1");
			this.id = -1;
		}
	}
	
	private void getRoot(JSONObject taxonomyJSON) {
		JSONObject rootJSON = null;
		try {
			rootJSON = taxonomyJSON.getJSONObject("root");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			rootJSON = null;
		}
		
		if (rootJSON != null)
			this.root = new ContainerTaxon(rootJSON);
	}
	
	//TODO will probably just be a tree - prune this?
	/*private void getTaxonTree(JSONObject taxonomyJSON) {
		JSONObject innerTaxonomyJSON = null;
		try {
			innerTaxonomyJSON = taxonomyJSON.getJSONObject("container_taxonomy");
		} catch (JSONException e) {
			//no inner taxonomies
			innerTaxonomyJSON = null;
		}
		
		if (innerTaxonomyJSON != null) {
			//process inner taxonomy
			//taxonomies.add(new ContainerTaxonomy(innerTaxonomyJSON));
		}
	}*/
}
