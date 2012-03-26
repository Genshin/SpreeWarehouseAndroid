package org.genshin.warehouse;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataStoreHelper extends SQLiteOpenHelper {

	public static final String TABLE_WAREHOUSE_SETTINGS = "warehousesettings";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_SERVER = "server";
	//public static final String COLUMN_ 
	
	public DataStoreHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
