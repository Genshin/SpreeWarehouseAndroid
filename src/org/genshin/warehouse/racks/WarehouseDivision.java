package org.genshin.warehouse.racks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class WarehouseDivision {
	public int id;
	public String name;
	public String code;
	public String location;
	public String details;
	ContainerTaxonomies containers;
	
	WarehouseDivision(JSONObject warehouseJSON) {
		try {
			id = warehouseJSON.getInt("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			name = warehouseJSON.getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			code = warehouseJSON.getString("code");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			location = warehouseJSON.getString("location");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			details = warehouseJSON.getString("details");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONArray taxonomiesJSON = null;
		try {
			 taxonomiesJSON = warehouseJSON.getJSONArray("container_taxonomies");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			taxonomiesJSON = null;
		}
		
		if (taxonomiesJSON != null) {
			Log.d("WarehouseDivision", "Registering taxonomies to " + name);
			containers = new ContainerTaxonomies(taxonomiesJSON);
		} else {
			containers = new ContainerTaxonomies();
		}
	}
}
