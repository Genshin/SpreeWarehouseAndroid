package org.genshin.warehouse;

import java.util.ArrayList;

import org.genshin.gsa.RESTConnector;
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
		connector.setup(profile.server, profile.port, profile.user, profile.password);
	}

	public Object getObject(String path, Class<?> containerClass) {
		return connector.getObject(path, containerClass);
	}

	public ArrayList<?> getList(String path, Class<?> containerClass) {
		ArrayList<String> collection = new ArrayList<String>();
		
		return collection;
	}

}
