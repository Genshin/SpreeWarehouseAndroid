package org.genshin.spree;

import java.util.ArrayList;

import org.genshin.warehouse.profiles.Profile;
import org.genshin.warehouse.profiles.Profiles;
import org.json.JSONException;
import org.json.JSONObject;



import android.content.Context;
import android.util.Log;

public class SpreeConnector {
	Context ctx;
	public RESTConnector connector;
	private Profile profile;

	public SpreeConnector(Context ctx) {
		this.ctx = ctx;

		//set profile
		Profiles profiles = new Profiles(ctx);
		this.profile = profiles.selected;
		
		connector = new RESTConnector();
		connector.setup(profile.server, profile.port, profile.apiKey);
	}

	public ArrayList<?> getList(String path, Class<?> containerClass) {
		ArrayList<String> collection = new ArrayList<String>();
		
		return collection;
	}

}
