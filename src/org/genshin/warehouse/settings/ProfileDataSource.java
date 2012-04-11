package org.genshin.warehouse.settings;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ProfileDataSource {
	private SQLiteDatabase db;
	private ProfileDBHelper helper;
	private String[] allColumns = {
		ProfileDBHelper.COLUMN_ID,
		ProfileDBHelper.COLUMN_SERVER,
		ProfileDBHelper.COLUMN_USER,
		ProfileDBHelper.COLUMN_PASSWORD,
		ProfileDBHelper.COLUMN_KEY };
	
	private Context ctx;

	public ProfileDataSource(Context ctx) {
		this.ctx = ctx;
		helper = new ProfileDBHelper(ctx);
	}

	public void open() throws SQLException {
		db = helper.getWritableDatabase();
	}

	public void close() {
		helper.close();
	}

	public Profile createProfile(String server, String user, String password) {
		ContentValues values = new ContentValues();
		values.put(ProfileDBHelper.COLUMN_SERVER, server);
		values.put(ProfileDBHelper.COLUMN_USER, user);
		values.put(ProfileDBHelper.COLUMN_PASSWORD, password);
		long insertID = db.insert(ProfileDBHelper.TABLE_PROFILES, null, values);
		Cursor cursor = db.query(ProfileDBHelper.TABLE_PROFILES, allColumns, ProfileDBHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
		cursor.moveToFirst();
		Profile newProfile = cursorToProfile(cursor); 
		cursor.close();
		
		return newProfile;
	}

	public void deleteProfile(Profile profile) {
		db.delete(ProfileDBHelper.TABLE_PROFILES, ProfileDBHelper.COLUMN_ID + " = " + profile.id, null);
	}
	
	public void updateProfile(Profile profile) {
		ContentValues values = new ContentValues();
		values.put(ProfileDBHelper.COLUMN_SERVER, profile.server);
		values.put(ProfileDBHelper.COLUMN_USER, profile.user);
		values.put(ProfileDBHelper.COLUMN_PASSWORD, profile.password);
		
		db.update(ProfileDBHelper.TABLE_PROFILES, values, ProfileDBHelper.COLUMN_ID + " = " + profile.id, null);
	}

	private Profile cursorToProfile(Cursor c) {
		Profile p = new Profile();
		p.set(c.getLong(0)/*id*/, c.getString(1)/*server*/, c.getString(2)/*user*/, c.getString(3)/*password*/);
		return p;
	}
	
	public List<Profile> getAllProfiles() {
		List<Profile> profiles = new ArrayList<Profile>();
		
		Cursor cur = db.query(ProfileDBHelper.TABLE_PROFILES, allColumns, null, null, null, null, null);
		
		cur.moveToFirst();
		
		while (!cur.isAfterLast()) {
			Profile profile = cursorToProfile(cur);
			profiles.add(profile);
			cur.moveToNext();
		}
		cur.close();
		
		return profiles;
		
	}

}
