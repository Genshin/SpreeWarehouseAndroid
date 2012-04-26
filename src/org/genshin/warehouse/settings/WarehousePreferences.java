package org.genshin.warehouse.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class WarehousePreferences {
	private static final String WAREHOUSE_PREFERENCES = "WarehousePreferences";

	private static final String DEFAULT_PROFILE_ID = "DefaultProfileID";
	private int defaultProfileID;

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;

	public WarehousePreferences(Context ctx) {
		preferences = ctx.getSharedPreferences(WAREHOUSE_PREFERENCES, 0);
		editor = preferences.edit();
	}

	public long getDefaultProfileID() {
		return preferences.getLong(DEFAULT_PROFILE_ID, 0);
	}

	public void setDefaultProfileID(long id) {
		editor.putLong(DEFAULT_PROFILE_ID, id);
		editor.commit();
	}
}
