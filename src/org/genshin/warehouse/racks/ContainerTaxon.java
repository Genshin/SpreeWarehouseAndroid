package org.genshin.warehouse.racks;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ContainerTaxon {
	public int id;
	public String name;
	public String permalink;
	public String updatedAt; //TODO make this a date
	public String warehouse;
	public ContainerTaxon root;
	
	public ContainerTaxon(JSONObject json) {
		try {
			id = json.getInt("id"); //TODO this will fail from QR code scans.
		} catch (JSONException e) {
			// TODO QR Code scans will NOT have IDs. 
			// Cross reference/search with permalink and if that fails trace the tree.
			
		}
		
		try {
			name = json.getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			name = json.getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			permalink = json.getString("permalink");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			updatedAt = json.getString("updated_at");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Log.d("container", "container added!");
	}
}
