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

import org.genshin.warehouse.settings.WarehousePreferences;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Profiles {
	private SQLiteDatabase db;
	private ProfileDBHelper helper;
	private String[] allColumns = {
		ProfileDBHelper.COLUMN_ID,
		ProfileDBHelper.COLUMN_SERVER,
		ProfileDBHelper.COLUMN_PORT,
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
	
	private WarehousePreferences preferences;

	private Context ctx;
	public Profiles(Context ctx) {
		this.ctx = ctx;
		helper = new ProfileDBHelper(ctx);
		preferences = new WarehousePreferences(ctx);
		
		getAllProfiles();
		getDefaultProfile();
		
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
	public Profile createProfile(String server, long port, String user, String password) {
		openDB();
			ContentValues values = new ContentValues();
			values.put(ProfileDBHelper.COLUMN_SERVER, server);
			values.put(ProfileDBHelper.COLUMN_PORT, port);
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

	//Update a profile
	//The profile ID is held within the profile object
	public void updateProfile(Profile profile) {
		openDB();
			ContentValues values = new ContentValues();
			values.put(ProfileDBHelper.COLUMN_SERVER, profile.server);
			values.put(ProfileDBHelper.COLUMN_PORT, profile.port);
			values.put(ProfileDBHelper.COLUMN_USER, profile.user);
			values.put(ProfileDBHelper.COLUMN_PASSWORD, profile.password);
			
			db.update(ProfileDBHelper.TABLE_PROFILES, values, ProfileDBHelper.COLUMN_ID + " = " + profile.id, null);
		closeDB();
	}

	//Converts a cursor record to a Profile object
	private Profile cursorToProfile(Cursor c) {
		Profile p = new Profile();
		p.set(c.getLong(0)/*id*/, c.getString(1)/*server*/, c.getLong(2) /*port*/, c.getString(3)/*user*/, c.getString(4)/*password*/);
		return p;
	}
	
	private int getProfileListPosition(Profile profile) {
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).id == profile.id)
				return i;

		return 0;
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

		selected = getDefaultProfile();
		selectedProfilePosition = getProfileListPosition(selected);
		
		return list;
	}

	public void selectProfile(int position) {
		if (position < 0 || position >= list.size())
			return; //OOB
		
		Profile sel = list.get(position);
		
		preferences.setDefaultProfileID(sel.id);
		
        selectedProfilePosition = position;
		selected = list.get(position);
    }
	
	//Delete the specified profile
	public void deleteProfile(Profile profile) {
		if (profile == null || profile.id == -1)
			return;

		openDB();
			db.delete(ProfileDBHelper.TABLE_PROFILES, ProfileDBHelper.COLUMN_ID + " = " + profile.id, null);
		closeDB();
	}

	//Deletes profile at the specified position in "list"
	public void deleteProfile(int position) {
		deleteProfile(list.get(position));
	}

	//Deletes the currently selected profile
	public void deleteSelectedProfile() {
        deleteProfile(selected);
	}

	public Spinner attachToSpinner(Spinner spinner) {
		AdapterView.OnItemSelectedListener selected = new AdapterView.OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view,
                    int position, long id) {
                selectProfile(position);
            }

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub	
			}
		};
		
		spinner.setAdapter(getArrayAdapter());
    	spinner.setOnItemSelectedListener(selected);
    	spinner.setSelection(selectedProfilePosition);
  
    	return spinner;
	}

	public ArrayAdapter<String> getArrayAdapter() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (int i = 0; i < list.size(); i++) {
			adapter.add(list.get(i).user + ":" + list.get(i).server);
		}

		return adapter;
	}
	
	private Profile createDummyProfile() {
		Profile dummy = new Profile();
		dummy.id = -1;
		return dummy;
	}
	
	public Profile getDefaultProfile() {
		if (list.size() > 0) {
			//Get default ID from preferences
			long defID = preferences.getDefaultProfileID();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).id == defID) {
					selectProfile(i);
					return selected;
				}
			}
			
			selectProfile(0);
			return selected;
		}
		
		return createDummyProfile();
	}
}
