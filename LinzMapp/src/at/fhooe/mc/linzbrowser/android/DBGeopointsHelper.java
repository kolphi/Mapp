package at.fhooe.mc.linzbrowser.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBGeopointsHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "geopoints.db";
	private static final int DB_VERSION = 1;
	
	private static final String HORTE_CREATE =
		"CREATE TABLE horte (" +
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
	private static final String ENTERED_ALC_CREATE =
		"CREATE TABLE entered_alc (" +
		"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		"name TEXT NOT NULL, " +
		"percent INTEGER NOT NULL, " +
		"mixing INTEGER NOT NULL" +
		")";
	private static final String AVAILABLE_ALC_CREATE =
		"CREATE TABLE available_alc (" +
		"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		"name TEXT NOT NULL, " +
		"percent INTEGER NOT NULL, " +
		"mixing INTEGER NOT NULL" +
		")";
	private static final String PREV_GAMES_CREATE =
		"CREATE TABLE prev_games (" +
		"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		"name TEXT NOT NULL, " +
		"start_time INTEGER NOT NULL, " +
		"end_time INTEGER NULL, " +
		"person_count INTEGER DEFAULT 0, " +
		"saved INTEGER DEFAULT 0, " +
		"av_gram INTEGER DEFAULT 0" +
		")";
	
	private static final String HORTE_DROP = 
		"DROP TABLE IF EXISTS horte";
	private static final String ENTERED_ALC_DROP = 
		"DROP TABLE IF EXISTS entered_alc";
	private static final String AVAILABLE_ALC_DROP = 
		"DROP TABLE IF EXISTS available_alc";
	private static final String PREV_GAMES_DROP = 
		"DROP TABLE IF EXISTS prev_games";

	
	public DBGeopointsHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(HORTE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(HORTE_DROP);
		onCreate(db);
	}
	
}
