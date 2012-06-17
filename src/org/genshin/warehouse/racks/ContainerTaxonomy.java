package org.genshin.warehouse.racks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ContainerTaxonomy {
	public int id;
	public String name;
	public ContainerTaxonomy root;
	public ArrayList<ContainerTaxon> containerTaxons;
	//TODO variants
	
	public ContainerTaxonomy(JSONObject taxonomyJSON) {
		/*for (int i = 0; i < taxonomyJSONArray.length(); i++) {
			JSONObject taxonJSON = null;
			try {
				taxonJSON = taxonomyJSONArray.getJSONObject(i);
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			
			taxonJSON
		}*/
		
		//TODO read root
		getContainerTaxons(taxonomyJSON);

		
	}
	
	private void getContainerTaxons(JSONObject taxonomyJSON) {
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
	}
}
