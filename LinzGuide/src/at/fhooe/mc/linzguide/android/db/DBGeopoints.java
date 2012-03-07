package at.fhooe.mc.linzguide.android.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import at.fhooe.mc.linzguide.android.util.DataGeopoint;
import at.fhooe.mc.linzguide.android.util.RawDataProvider;

public class DBGeopoints {

	// Database fields
	public static final String GP_KEY_ROWID = "_id";
	public static final String GP_KEY_NAME = "name";
	public static final String GP_KEY_ADDRESS = "address";
	public static final String GP_KEY_WEBSITE = "website";
	public static final String GP_KEY_CATEGROY = "category";
	public static final String GP_KEY_UNIQUE_ADDRESS = "unique_address";
	public static final String GP_KEY_X = "x";
	public static final String GP_KEY_Y = "y";
	public static final String GP_KEY_LAT = "lat";
	public static final String GP_KEY_LON = "lon";

	public static final String EA_DATABASE_TABLE = "entered_alc";
	public static final String EA_KEY_ROWID = "_id";
	public static final String EA_KEY_NAME = "name";
	public static final String EA_KEY_PERCENT = "percent";
	public static final String EA_KEY_MIXING = "mixing";

	private static final String AA_DATABASE_TABLE = "available_alc";
	public static final String AA_KEY_ROWID = "_id";
	public static final String AA_KEY_NAME = "name";
	public static final String AA_KEY_PERCENT = "percent";
	public static final String AA_KEY_MIXING = "mixing";

	private static final String PG_DATABASE_TABLE = "prev_games";
	public static final String PG_KEY_ROWID = "_id";
	public static final String PG_KEY_NAME = "name";
	public static final String PG_KEY_START_TIME = "start_time";
	public static final String PG_KEY_END_TIME = "end_time";
	public static final String PG_KEY_PERSON_COUNT = "person_count";
	public static final String PG_KEY_SAVED = "saved";
	public static final String PG_KEY_AV_GRAM = "av_gram";

	private Context context;
	private SQLiteDatabase db;
	private DBGeopointsHelper dbGeopointsHelper;

	ContentValues werte;

	public DBGeopoints(Context context) {
		this.context = context;
	}

	private DBGeopoints open() throws SQLException {
		if(!isOpen()){
			dbGeopointsHelper = new DBGeopointsHelper(context);
			db = dbGeopointsHelper.getWritableDatabase();
		}
		return this;
	}

	private boolean isOpen() {
		if(db != null){
			return db.isOpen();
		}else{
			return false;
		}
	}

	
	public void closeOnPause() {
		if(isOpen()){
			dbGeopointsHelper.close();
		}
	}

	public void saveGeoPoint(int type, String name, String address, String website, String category, String uniqueAddress, int x, int y, int lat, int lon) {
		werte = new ContentValues();

		werte.put(GP_KEY_NAME, name);
		werte.put(GP_KEY_ADDRESS, address);
		werte.put(GP_KEY_WEBSITE, website);
		werte.put(GP_KEY_CATEGROY, category);
		werte.put(GP_KEY_UNIQUE_ADDRESS, uniqueAddress);
		werte.put(GP_KEY_X, x);
		werte.put(GP_KEY_Y, y);
		werte.put(GP_KEY_LAT, lat);
		werte.put(GP_KEY_LON, lon);
		
		open();
		db.insert(RawDataProvider.prefNames[type], null, werte);
	}
	
	public void saveGeoPoint(int type, DataGeopoint gp) {
		saveGeoPoint(type, gp.name, gp.address, gp.website, gp.category, gp.uniqueAddress, gp.x, gp.y, gp.lat, gp.lon);
	}
	
	public void deleteAllGeopointsOfType(int type) {
		open();
		db.delete(RawDataProvider.prefNames[type], null, null);
	}
	
	public ArrayList<DataGeopoint> getAllGeopointsOfType(int type) {
		open();
		Cursor c = db.query(RawDataProvider.prefNames[type], new String[] {
				GP_KEY_ADDRESS, GP_KEY_CATEGROY, GP_KEY_LAT, GP_KEY_LON, GP_KEY_NAME, GP_KEY_ROWID, GP_KEY_UNIQUE_ADDRESS, GP_KEY_WEBSITE, GP_KEY_X, GP_KEY_Y },
				null, null, null, null, null);
		
		if (c != null) {
			c.moveToFirst();
			
			ArrayList<DataGeopoint> items = new ArrayList<DataGeopoint>();
			DataGeopoint item;
			
			while(!c.isAfterLast()){
				
				item = new DataGeopoint(type,
						c.getString(c.getColumnIndex(GP_KEY_NAME)),
						c.getString(c.getColumnIndex(GP_KEY_ADDRESS)),
						c.getString(c.getColumnIndex(GP_KEY_WEBSITE)),
						c.getString(c.getColumnIndex(GP_KEY_CATEGROY)),
						c.getString(c.getColumnIndex(GP_KEY_UNIQUE_ADDRESS)),
						c.getInt(c.getColumnIndex(GP_KEY_X)),
						c.getInt(c.getColumnIndex(GP_KEY_Y)),
						c.getInt(c.getColumnIndex(GP_KEY_LAT)),
						c.getInt(c.getColumnIndex(GP_KEY_LON)));
				
				items.add(item);
				c.moveToNext();
			}
			c.close();
			return items;
		}
		
		return null;
	}

}