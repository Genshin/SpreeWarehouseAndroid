package profiles;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Profiles {
	private SQLiteDatabase db;
	private ProfileDBHelper helper;
	private String[] allColumns = {
		ProfileDBHelper.COLUMN_ID,
		ProfileDBHelper.COLUMN_SERVER,
		ProfileDBHelper.COLUMN_USER,
		ProfileDBHelper.COLUMN_PASSWORD,
		ProfileDBHelper.COLUMN_KEY };
	
	private Context ctx;
	
	//All profiles
	//全プロフィールのリスト
	public ArrayList<Profile> list;

	public Profiles(Context ctx) {
		this.ctx = ctx;
		helper = new ProfileDBHelper(ctx);
		
		getAllProfiles();
		
	}

	private void openDB() throws SQLException {
		db = helper.getWritableDatabase();
	}

	public void closeDB() {
		helper.close();
	}

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

	public void deleteProfile(Profile profile) {
		openDB();
		db.delete(ProfileDBHelper.TABLE_PROFILES, ProfileDBHelper.COLUMN_ID + " = " + profile.id, null);
		closeDB();
	}
	
	public void updateProfile(Profile profile) {
		openDB();
		ContentValues values = new ContentValues();
		values.put(ProfileDBHelper.COLUMN_SERVER, profile.server);
		values.put(ProfileDBHelper.COLUMN_USER, profile.user);
		values.put(ProfileDBHelper.COLUMN_PASSWORD, profile.password);
		
		db.update(ProfileDBHelper.TABLE_PROFILES, values, ProfileDBHelper.COLUMN_ID + " = " + profile.id, null);
		closeDB();
	}

	private Profile cursorToProfile(Cursor c) {
		Profile p = new Profile();
		p.set(c.getLong(0)/*id*/, c.getString(1)/*server*/, c.getString(2)/*user*/, c.getString(3)/*password*/);
		return p;
	}
	
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
}
