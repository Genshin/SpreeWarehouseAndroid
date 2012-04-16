/******************************************************************************
 * Warehouse Profiles manager
 *
 * Holds a list of all registered profiles, and manages the database 
 * interactions to obtain and manage that list.
 *
 * プロフィルのリストを管理し、DBとのやりとりを代理に行う。
 ******************************************************************************/

package org.genshin.warehouse.profiles;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

public class Profiles {
	private SQLiteDatabase db;
	private ProfileDBHelper helper;
	private String[] allColumns = {
		ProfileDBHelper.COLUMN_ID,
		ProfileDBHelper.COLUMN_SERVER,
		ProfileDBHelper.COLUMN_USER,
		ProfileDBHelper.COLUMN_PASSWORD,
		ProfileDBHelper.COLUMN_KEY };
	
	//All profiles
	//全プロフィールのリスト
	public int selectedProfilePosition;
	public ArrayList<Profile> list;
	//Selected profile
	//選択されているプロフィール
	public Profile selected;

	private Context ctx;
	public Profiles(Context ctx) {
		this.ctx = ctx;
		helper = new ProfileDBHelper(ctx);
		
		getAllProfiles();
		
	}

	//Open DB. Be sure to open the DB before an interaciton.
	private void openDB() throws SQLException {
		db = helper.getWritableDatabase();
	}

	//Close the DB. Be sure to close after an interaction (since Java doesn't have a destructor methodology and GC timing is unpredictable...).
	public void closeDB() {
		helper.close();
	}

	//Create a new profile
	public Profile createProfile(String server, String user, String password) {
		openDB();
			ContentValues values = new ContentValues();
			values.put(ProfileDBHelper.COLUMN_SERVER, server);
			values.put(ProfileDBHelper.COLUMN_USER, user);
			values.put(ProfileDBHelper.COLUMN_PASSWORD, password);
			long insertID = db.insert(ProfileDBHelper.TABLE_PROFILES, null, values);
			Cursor cursor = db.query(ProfileDBHelper.TABLE_PROFILES, allColumns, ProfileDBHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
			cursor.moveToFirst();
			Profile newProfile = cursorToProfile(cursor); 
			cursor.close();
		closeDB();
		
		return newProfile;
	}

	//Delete the specified profile
	public void deleteProfile(Profile profile) {
		openDB();
			db.delete(ProfileDBHelper.TABLE_PROFILES, ProfileDBHelper.COLUMN_ID + " = " + profile.id, null);
		closeDB();
	}
	
	//Update a profile
	//The profile ID is held within the profile object
	public void updateProfile(Profile profile) {
		openDB();
			ContentValues values = new ContentValues();
			values.put(ProfileDBHelper.COLUMN_SERVER, profile.server);
			values.put(ProfileDBHelper.COLUMN_USER, profile.user);
			values.put(ProfileDBHelper.COLUMN_PASSWORD, profile.password);
			
			db.update(ProfileDBHelper.TABLE_PROFILES, values, ProfileDBHelper.COLUMN_ID + " = " + profile.id, null);
		closeDB();
	}

	//Converts a cursor record to a Profile object
	private Profile cursorToProfile(Cursor c) {
		Profile p = new Profile();
		p.set(c.getLong(0)/*id*/, c.getString(1)/*server*/, c.getString(2)/*user*/, c.getString(3)/*password*/);
		return p;
	}
	
	//Gets a list of all available profiles and updates the local list
	public List<Profile> getAllProfiles() {
		openDB();
			this.list = new ArrayList<Profile>();
			
			Cursor cur = db.query(ProfileDBHelper.TABLE_PROFILES, allColumns, null, null, null, null, null);
			
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

	private void selectProfile(int position) {
		if (position < 0 || position >= list.size())
			return; //OOB

        selectedProfilePosition = position;
		selected = list.get(position);
    }

	public void deleteSelectedProfile() {
        		//TODO profiles.deleteProfile(profiles.list.get(selectedProfile));
	}

	public void attachToSpinner(Spinner spinner) {
    	spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                //Spinner spinner = (Spinner)parent;
                //int item = spinner.getSelectedItemPosition();
                selectProfile(position);
            }

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
		});
	}

	public ArrayAdapter<String> getArrayAdapter() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (int i = 0; i < list.size(); i++) {
			adapter.add(list.get(i).user + ":" + list.get(i).server);
		}

		return adapter;
	}
}
