package org.genshin.spree.profiles;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ProfileDBHelper extends SQLiteOpenHelper {
	private SQLiteDatabase db;
	
	private static final String TABLE_PROFILES = "profiles";
	private static final String COLUMN_ID = "id";
	private static final String COLUMN_SERVER = "server";
	private static final String COLUMN_PORT = "port";
	private static final String COLUMN_PROFILENAME = "profileName";
	private static final String COLUMN_APIKEY = "apiKey";
	private static final String DATABASE_NAME = "warehouse.db";
	private static final int DATABASE_VERSION = 3;
	
	private String[] allColumns = {
			ProfileDBHelper.COLUMN_ID,
			ProfileDBHelper.COLUMN_SERVER,
			ProfileDBHelper.COLUMN_PORT,
			ProfileDBHelper.COLUMN_PROFILENAME,
			ProfileDBHelper.COLUMN_APIKEY };

	public static final String DATABASE_CREATE =
			//Profiles
			"create table "
			+ TABLE_PROFILES + "(" 
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_SERVER + " string not null, "
			+ COLUMN_PORT + " integer not null, "
			+ COLUMN_PROFILENAME + " string not null, "
			+ COLUMN_APIKEY + " string not null);";
	
	public ProfileDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	//Open DB. Be sure to open the DB before an interaciton.
	private void openDB() throws SQLException {
		db = this.getWritableDatabase();
	}


	//Close the DB. Be sure to close after an interaction (since Java doesn't have a destructor methodology and GC timing is unpredictable...).
	public void closeDB() {
		this.close();
	}

	//Create a new profile
	public Profile createProfile(String server, long port, String profileName, String apiKey) {
		openDB();
			ContentValues values = new ContentValues();
			values.put(ProfileDBHelper.COLUMN_SERVER, server);
			values.put(ProfileDBHelper.COLUMN_PORT, port);
			values.put(ProfileDBHelper.COLUMN_PROFILENAME, profileName);
			values.put(ProfileDBHelper.COLUMN_APIKEY, apiKey);
			long insertID = db.insert(ProfileDBHelper.TABLE_PROFILES, null, values);
			Cursor cursor = db.query(ProfileDBHelper.TABLE_PROFILES, allColumns, ProfileDBHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
			cursor.moveToFirst();
			Profile newProfile = cursorToProfile(cursor); 
			cursor.close();
		closeDB();
		
		return newProfile;
	}

	//Update a profile
	//The profile ID is held within the profile object
	public void updateProfile(Profile profile) {
		openDB();
			ContentValues values = new ContentValues();
			values.put(ProfileDBHelper.COLUMN_SERVER, profile.server);
			values.put(ProfileDBHelper.COLUMN_PORT, profile.port);
			values.put(ProfileDBHelper.COLUMN_PROFILENAME, profile.profileName);
			values.put(ProfileDBHelper.COLUMN_APIKEY, profile.apiKey);
			
			db.update(ProfileDBHelper.TABLE_PROFILES, values, ProfileDBHelper.COLUMN_ID + " = " + profile.id, null);
		closeDB();
	}

	//Converts a cursor record to a Profile object
	private Profile cursorToProfile(Cursor c) {
		Profile p = new Profile();
		p.set(c.getLong(0)/*id*/, c.getString(1)/*server*/, c.getInt(2) /*port*/, c.getString(3)/*profileName*/, c.getString(4)/*apiKey*/);
		return p;
	}
	
	//Gets a list of all available profiles and updates the local list
	public ArrayList<Profile> getAllProfiles() {
		ArrayList<Profile> list;
		
		openDB();
			list = new ArrayList<Profile>();
			
			Cursor cur = db.query(TABLE_PROFILES, allColumns, null, null, null, null, null);
			
			cur.moveToFirst();
			
			while (!cur.isAfterLast()) {
				Profile profile = cursorToProfile(cur);
				list.add(profile);
				cur.moveToNext();
			}
			cur.close();
		closeDB();
		
		return list;
	}

	
	public void deleteProfile(Profile profile) {
		openDB();
			db.delete(ProfileDBHelper.TABLE_PROFILES, ProfileDBHelper.COLUMN_ID + " = " + profile.id, null);
		closeDB();
	}
}
