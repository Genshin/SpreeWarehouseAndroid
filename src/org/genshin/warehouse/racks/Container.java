package org.genshin.warehouse.racks;

import org.json.JSONException;
import org.json.JSONObject;

public class Container {
	public String name;
	public String permalink;
	public String updatedAt; //TODO make this a date
	public String warehouse;
	public Container parent; //TODO this isn't a good idea. Also fuck Java for not having a good pointer/reference concept to fit this.
	
	public Container(JSONObject json) {
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
	
	}
}
