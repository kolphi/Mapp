package at.fhooe.mc.linzbrowser.android;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBGeopoints {

	// Database fields
	public static final String HORTE_DATABASE_TABLE = "horte";
	public static final String HORTE_KEY_ROWID = "_id";
	public static final String HORTE_KEY_NAME = "name";
	public static final String HORTE_KEY_ADDRESS = "address";
	public static final String HORTE_KEY_WEBSITE = "website";
	public static final String HORTE_KEY_CATEGROY = "category";
	public static final String HORTE_KEY_UNIQUE_ADDRESS = "unique_address";
	public static final String HORTE_KEY_X = "x";
	public static final String HORTE_KEY_Y = "y";
	public static final String HORTE_KEY_LAT = "lat";
	public static final String HORTE_KEY_LON = "lon";

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

	/**
	 * Create a new todo If the todo is successfully created return the new
	 * rowId for that note, otherwise return a -1 to indicate failure.
	 */
	public long startNewGame() {
		werte = new ContentValues();

		werte.put(PG_KEY_NAME, "");
		werte.put(PG_KEY_START_TIME, 0);
		
		open();
		return db.insert(PG_DATABASE_TABLE, null, werte);
	}

	public Cursor fetchRunningGame() {
		open();
		Cursor runningGame = db.query(PG_DATABASE_TABLE, new String[] {
				PG_KEY_ROWID, PG_KEY_NAME, PG_KEY_SAVED }, PG_KEY_SAVED
				+ "='0'", null, null, null, null);
		
		if (runningGame != null) {
			runningGame.moveToFirst();
		}
		return runningGame;
	}

	public void saveHort(String name, String address, String website, String category, String uniqueAddress, int x, int y, int lat, int lon) {
		werte = new ContentValues();

		werte.put(HORTE_KEY_NAME, name);
		werte.put(HORTE_KEY_ADDRESS, address);
		werte.put(HORTE_KEY_WEBSITE, website);
		werte.put(HORTE_KEY_CATEGROY, category);
		werte.put(HORTE_KEY_UNIQUE_ADDRESS, uniqueAddress);
		werte.put(HORTE_KEY_X, x);
		werte.put(HORTE_KEY_Y, y);
		werte.put(HORTE_KEY_LAT, lat);
		werte.put(HORTE_KEY_LON, lon);
		
		open();
		db.insert(HORTE_DATABASE_TABLE, null, werte);
	}
	
	public void saveHort(DataHort hort) {
		saveHort(hort.name, hort.address, hort.website, hort.category, hort.uniqueAddress, hort.x, hort.y, hort.lat, hort.lon);
	}
	
	public void deleteAllHort() {
		open();
		db.delete(HORTE_DATABASE_TABLE, null, null);
	}
	
	public ArrayList<DataHort> getAllHort() {
		open();
		Cursor c = db.query(HORTE_DATABASE_TABLE, new String[] {
				HORTE_KEY_ADDRESS, HORTE_KEY_CATEGROY, HORTE_KEY_LAT, HORTE_KEY_LON, HORTE_KEY_NAME, HORTE_KEY_ROWID, HORTE_KEY_UNIQUE_ADDRESS, HORTE_KEY_WEBSITE, HORTE_KEY_X, HORTE_KEY_Y },
				null, null, null, null, null);
		
		if (c != null) {
			c.moveToFirst();
			
			ArrayList<DataHort> items = new ArrayList<DataHort>();
			DataHort item;
			
			while(!c.isAfterLast()){
				
				item = new DataHort(
						c.getString(c.getColumnIndex(HORTE_KEY_NAME)),
						c.getString(c.getColumnIndex(HORTE_KEY_ADDRESS)),
						c.getString(c.getColumnIndex(HORTE_KEY_WEBSITE)),
						c.getString(c.getColumnIndex(HORTE_KEY_CATEGROY)),
						c.getString(c.getColumnIndex(HORTE_KEY_UNIQUE_ADDRESS)),
						c.getInt(c.getColumnIndex(HORTE_KEY_X)),
						c.getInt(c.getColumnIndex(HORTE_KEY_Y)),
						c.getInt(c.getColumnIndex(HORTE_KEY_LAT)),
						c.getInt(c.getColumnIndex(HORTE_KEY_LON)));
				
				items.add(item);
				c.moveToNext();
			}
			
			return items;
		}
		
		return null;
	}
	
	public boolean isRunningGame() {
		open();
		Cursor c = db.query(PG_DATABASE_TABLE, new String[] { AA_KEY_ROWID, PG_KEY_SAVED },
				PG_KEY_SAVED + "='0'", null, null, null, null);

		if (c.getCount() == 0) {
			return false;
		} else {
			return true;
		}

	}

	public boolean isAlcoholAvailable() {
		open();
		Cursor c = db.query(AA_DATABASE_TABLE, new String[] { AA_KEY_ROWID },
				null, null, null, null, null);

		if (c.getCount() == 0) {
			return false;
		} else {
			return true;
		}

	}

	public boolean isAlcoholAvailable(long rowId) {
		open();
		Cursor c = db.query(AA_DATABASE_TABLE, new String[] { AA_KEY_ROWID },
				AA_KEY_ROWID + "='" + rowId + "'", null, null, null, null);

		if (c.getCount() == 0) {
			return false;
		} else {
			return true;
		}

	}

	public Cursor fetchAllAvailableAlc() {
		open();
		return db.query(AA_DATABASE_TABLE, new String[] { AA_KEY_ROWID,
				AA_KEY_NAME, AA_KEY_PERCENT, AA_KEY_MIXING }, null, null, null,
				null, AA_KEY_PERCENT + " desc, " + AA_KEY_NAME + " asc");
	}

	public Cursor fetchAvailableAlc(long rowId) throws SQLException {
		open();
		Cursor mCursor = db.query(AA_DATABASE_TABLE, new String[] {
				AA_KEY_ROWID, AA_KEY_NAME, AA_KEY_PERCENT, AA_KEY_MIXING },
				AA_KEY_ROWID + "='" + rowId + "'", null, null, null, null);
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public String getAlcName(long rowId) {
		Cursor c = fetchAvailableAlc(rowId);

		int nameIdx = c.getColumnIndex(AA_KEY_NAME);

		return c.getString(nameIdx);
	}
	
	public float getAlcPercent(long rowId) {
		Cursor c = fetchAvailableAlc(rowId);

		int percentIdx = c.getColumnIndex(AA_KEY_PERCENT);

		return c.getFloat(percentIdx);
	}

	public void addAvailableAlcohol(String name, int mixing, float percent) {

		werte = new ContentValues();
		werte.put(AA_KEY_NAME, name);
		werte.put(AA_KEY_MIXING, mixing);
		werte.put(AA_KEY_PERCENT, percent);

		open();
		db.insert(AA_DATABASE_TABLE, null, werte);
	}

	public void updateMixing(String name, int mixing) {
		werte = new ContentValues();
		werte.put(AA_KEY_MIXING, mixing);

		open();
		db.update(AA_DATABASE_TABLE, werte, AA_KEY_NAME + "=?",
				new String[] { name });
	}

	public boolean editAlcohol(long rowId, String name, float percent,
			int mixing) {
		werte = new ContentValues();
		werte.put(AA_KEY_NAME, name);
		werte.put(AA_KEY_PERCENT, percent);
		werte.put(AA_KEY_MIXING, mixing);

		open();
		return db.update(AA_DATABASE_TABLE, werte, AA_KEY_ROWID + "=?",
				new String[] { String.valueOf(rowId) }) > 0;
	}

	public boolean deleteAlcohol(long rowId) {

		open();
		return db.delete(AA_DATABASE_TABLE, AA_KEY_ROWID + "=?",
				new String[] { String.valueOf(rowId) }) > 0;
	}

	public boolean deleteAllAlcohol() {
		open();
		return db.delete(AA_DATABASE_TABLE, null, null) > 0;
	}

	public Cursor fetchPrevGame(long rowId) {
		open();
		Cursor c = db.query(PG_DATABASE_TABLE, new String[] { PG_KEY_ROWID,
				PG_KEY_NAME, PG_KEY_START_TIME, PG_KEY_END_TIME,
				PG_KEY_PERSON_COUNT, PG_KEY_SAVED, PG_KEY_AV_GRAM },
				PG_KEY_ROWID + "='" + rowId + "'", null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public String getPrevName(long rowId) {
		Cursor c = fetchPrevGame(rowId);
		return c.getString(c.getColumnIndex(PG_KEY_NAME));
	}

	public void deletePrevGame(long rowId) {
		open();
		db.delete(PG_DATABASE_TABLE, PG_KEY_ROWID + "=?",
				new String[] { String.valueOf(rowId) });
	}

	public void renamePrevGame(long rowId, String name) {
		werte = new ContentValues();
		werte.put(PG_KEY_NAME, name);

		open();
		db.update(PG_DATABASE_TABLE, werte, PG_KEY_ROWID + "=?",
				new String[] { String.valueOf(rowId) });
	}
	
	public void editPrevGame(long rowId, long firstHistoryEntry, long lastHistoryEntry) {
		werte = new ContentValues();
		if(firstHistoryEntry != -1){
			werte.put(PG_KEY_START_TIME, firstHistoryEntry);
			werte.put(PG_KEY_END_TIME, lastHistoryEntry);
		}else{
			Cursor c = fetchPrevGame(rowId);
			werte.put(PG_KEY_END_TIME, c.getLong(c.getColumnIndex(DBGeopoints.PG_KEY_START_TIME))+1);
		}

		open();
		db.update(PG_DATABASE_TABLE, werte, PG_KEY_ROWID + "=?",
				new String[] { String.valueOf(rowId) });
	}

	public Cursor fetchAllPrevGames() {
		open();
		return db.query(PG_DATABASE_TABLE, new String[] { PG_KEY_ROWID,
				PG_KEY_NAME, PG_KEY_START_TIME, PG_KEY_END_TIME,
				PG_KEY_PERSON_COUNT, PG_KEY_AV_GRAM, PG_KEY_SAVED },
				PG_KEY_SAVED + "='1'", null, // WHERE-Argumente (f�r
				// "?")
				null, // GROUP BY
				null, // HAVING
				PG_KEY_START_TIME + " desc" // ORDER BY
		);
	}

	public Cursor fetchAllPrevGamesByName(boolean isASC) {
		String sort = "";
		if (isASC) {
			sort = " asc";
		} else {
			sort = " desc";
		}
		open();
		return db.query(PG_DATABASE_TABLE, new String[] { PG_KEY_ROWID,
				PG_KEY_NAME, PG_KEY_START_TIME, PG_KEY_END_TIME,
				PG_KEY_PERSON_COUNT, PG_KEY_AV_GRAM, PG_KEY_SAVED },
				PG_KEY_SAVED + "='1'", null, // WHERE-Argumente (f�r
				// "?")
				null, // GROUP BY
				null, // HAVING
				PG_KEY_NAME + sort // ORDER BY
		);
	}

	public Cursor fetchAllPrevGamesSorted(int numSort) {
		String field = null;
		String sort = null;

		switch (numSort) {
		case 0:
			field = PG_KEY_NAME;
			sort = "desc";
			break;
		case 1:
			field = PG_KEY_NAME;
			sort = "asc";
			break;
		case 2:
			field = PG_KEY_START_TIME;
			sort = "desc";
			break;
		case 3:
			field = PG_KEY_START_TIME;
			sort = "asc";
			break;
		case 4:
			field = PG_KEY_PERSON_COUNT;
			sort = "desc";
			break;
		case 5:
			field = PG_KEY_PERSON_COUNT;
			sort = "asc";
			break;
		}

		open();
		return db.query(PG_DATABASE_TABLE, new String[] { PG_KEY_ROWID,
				PG_KEY_NAME, PG_KEY_START_TIME, PG_KEY_END_TIME,
				PG_KEY_PERSON_COUNT, PG_KEY_AV_GRAM, PG_KEY_SAVED },
				PG_KEY_SAVED + "='1'", null, // WHERE-Argumente (f�r
				// "?")
				null, // GROUP BY
				null, // HAVING
				field + " " + sort // ORDER BY
		);
	}

	public Cursor fetchAllPrevGamesByGram() {
		open();
		return db.query(PG_DATABASE_TABLE, new String[] { PG_KEY_ROWID,
				PG_KEY_NAME, PG_KEY_START_TIME, PG_KEY_END_TIME,
				PG_KEY_PERSON_COUNT, PG_KEY_AV_GRAM, PG_KEY_SAVED },
				PG_KEY_SAVED + "='1'", null, // WHERE-Argumente (f�r
				// "?")
				null, // GROUP BY
				null, // HAVING
				PG_KEY_AV_GRAM + " desc" // ORDER BY
		);
	}



	public void updateEnteredAlcohol(String name, float percent, int mixing) {
		werte = new ContentValues();

		werte.put(EA_KEY_NAME, name);
		werte.put(EA_KEY_PERCENT, percent);
		werte.put(EA_KEY_MIXING, mixing);
		
		open();
		if (db.update(EA_DATABASE_TABLE, werte, EA_KEY_NAME + "=?",
				new String[] { name }) == 0) {
			db.insert(EA_DATABASE_TABLE, null, werte);
		}
	}

	

	public boolean isSavedGame(long gameId) {
		open();
		Cursor c = db.query(PG_DATABASE_TABLE, new String[] { PG_KEY_ROWID },
				PG_KEY_ROWID + "='" + gameId + "'", null, null, null, null);

		if (c.getCount() == 0) {
			return false;
		}
		return true;
	}

}