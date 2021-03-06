package at.fhooe.mc.linzguide.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import at.fhooe.mc.linzguide.android.util.RawDataProvider;

public class DBGeopointsHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "geopoints.db";
	private static final int DB_VERSION = 1;
	
	private static final String CREATE_PART1 =
		"CREATE TABLE ";
	private static final String CREATE_PART2 =
			" (" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"name TEXT NOT NULL, " +
			"address TEXT NOT NULL, " +
			"website TEXT NOT NULL, " +
			"category TEXT NOT NULL, " +
			"unique_address TEXT NOT NULL, " +
			"x TEXT NOT NULL, " +
			"y TEXT NOT NULL, " +
			"lat TEXT NOT NULL, " +
			"lon TEXT NOT NULL" +
			")";
	
	
	private static final String DROP = 
		"DROP TABLE IF EXISTS ";

	
	public DBGeopointsHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for(int i = 0; i<RawDataProvider.prefNames.length; i++){
			db.execSQL(CREATE_PART1 + RawDataProvider.prefNames[i] + CREATE_PART2);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for(int i = 0; i<RawDataProvider.prefNames.length; i++){
			db.execSQL(DROP + RawDataProvider.prefNames[i]);
		}
		onCreate(db);
	}
	
}
