package com.jmuenster.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jmuenster.extras.Constants;
import com.jmuenster.extras.StickyNote;

public class PointDbAdapter {
	private static final String TAG = "NoteDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "note";
	private static final int DATABASE_VERSION = 1;
	
	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table "+DATABASE_TABLE+" (_id integer primary key autoincrement, "
			+ Constants.KEY_MYID + " text not null, " + Constants.KEY_TITLE + " text not null, " 
			+ Constants.KEY_BODY + " text not null, " + Constants.KEY_CATEGORY + " text not null, "
			+ Constants.KEY_ALT + " text not null, " + Constants.KEY_LAT + " text not null, " 
			+ Constants.KEY_LNG + " text not null, " + Constants.KEY_OWNER + " text not null, "
			+ Constants.KEY_CREATION_TIME + " text not null, " + Constants.KEY_VALID_FOR + " text not null, "
			+ Constants.KEY_VISIBILITY + " text not null);";

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS points");
			onCreate(db);
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param context the Context within which to work
	 */
	public PointDbAdapter(Context c) {
		mDbHelper = new DatabaseHelper(c);
	}

	/**
	 * Open the note database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public PointDbAdapter open() throws SQLException {
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Closes the note database.
	 */
	public void close() {
		mDbHelper.close();
	}

	/**
	 * Create a new sticky note using the one provided. If the note is
	 * successfully created return the new rowId for that note, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @param sn the sticky note to be created
	 * @return rowId or -1 if failed
	 */
	public long createPoint(StickyNote sn) {
		ContentValues args = new ContentValues();
		args.put(Constants.KEY_MYID, sn.getUnique_id());
		args.put(Constants.KEY_TITLE, sn.getTitle());
		args.put(Constants.KEY_BODY, sn.getSnippet());
		args.put(Constants.KEY_CATEGORY, sn.getCategory());
		args.put(Constants.KEY_ALT, sn.getAltitude());
		args.put(Constants.KEY_LAT, sn.getPoint().getLatitudeE6());
		args.put(Constants.KEY_LNG, sn.getPoint().getLongitudeE6());
		args.put(Constants.KEY_OWNER, sn.getOwner());
		args.put(Constants.KEY_CREATION_TIME, sn.getCreation_time());
		args.put(Constants.KEY_VALID_FOR, sn.getValid_for());
		args.put(Constants.KEY_VISIBILITY, sn.getVisibility());

		return mDb.insert(DATABASE_TABLE, null, args);
	}

	/**
	 * Return a Cursor over the list of all notes in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor getAllPoints() {

		return mDb.query(DATABASE_TABLE, new String[] { 
				Constants.KEY_ROWID,
				Constants.KEY_MYID,
				Constants.KEY_TITLE,
				Constants.KEY_BODY,
				Constants.KEY_CATEGORY,
				Constants.KEY_ALT,
				Constants.KEY_LAT, 
				Constants.KEY_LNG,
				Constants.KEY_OWNER,
				Constants.KEY_CREATION_TIME,
				Constants.KEY_VALID_FOR,
				Constants.KEY_VISIBILITY }, 
				null, null, null, null, null);
	}
	
	/**
	 * Return a Cursor over a particular note in the database.
	 * 
	 * @return Cursor over one note
	 */
    public Cursor getTitle(String myID) throws SQLException {
        Cursor mCursor =
        	mDb.query(true, DATABASE_TABLE, new String[] {
                		Constants.KEY_ROWID,
                		Constants.KEY_MYID,
                		Constants.KEY_TITLE,
        				Constants.KEY_BODY, 
        				Constants.KEY_CATEGORY,
        				Constants.KEY_ALT,
        				Constants.KEY_LAT, 
        				Constants.KEY_LNG,
        				Constants.KEY_OWNER,
        				Constants.KEY_CREATION_TIME,
        				Constants.KEY_VALID_FOR,
        				Constants.KEY_VISIBILITY }, 
                		Constants.KEY_MYID + "=?", 
                		new String[] {myID}, 
                		null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
	/**
	 * Updates a note in the DB.
	 * 
	 * @param rowId row id of the old note
	 * @param sn the new note
	 * @return true for success and false for failure
	 */
    public boolean updateTitle(long rowId, StickyNote sn) {
        ContentValues args = new ContentValues();
        args.put(Constants.KEY_MYID, sn.getUnique_id());
        args.put(Constants.KEY_TITLE, sn.getTitle());
        args.put(Constants.KEY_BODY, sn.getSnippet());
        args.put(Constants.KEY_CATEGORY, sn.getCategory());
        args.put(Constants.KEY_ALT, sn.getAltitude());
        args.put(Constants.KEY_LAT, sn.getPoint().getLatitudeE6());
        args.put(Constants.KEY_LNG, sn.getPoint().getLongitudeE6());
        args.put(Constants.KEY_OWNER, sn.getOwner());
        args.put(Constants.KEY_CREATION_TIME, sn.getCreation_time());
        args.put(Constants.KEY_VALID_FOR, sn.getValid_for());
        args.put(Constants.KEY_VISIBILITY, sn.getVisibility());
        return mDb.update(DATABASE_TABLE, args, Constants.KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    /**
     * Deletes a note in the DB.
     * 
     * @param myID of the note to be deleted
     * @return true for success and false for failure
     */
    
    public boolean deleteTitle(String myID) {
        return mDb.delete(DATABASE_TABLE, Constants.KEY_MYID + "=?", new String[] {myID}) > 0;
    }
}