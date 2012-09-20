package com.jmuenster.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.jmuenster.extras.StickyNote;

public class DBHelper {
	
	PointDbAdapter db;
	
	public DBHelper (Context c) {
		db = new PointDbAdapter(c); 	
	}
	
	private boolean checkUniqueness(String myID) {
		ArrayList<String> al = getAllMyIDs();
		for (int i = 0; i < al.size(); i++) {
			if (myID.compareTo(al.get(i))==0) {
				Log.d("DB", "Title already in DB");
				return true;
			}
		}
		return false;
	}
	
	public int insert(StickyNote sn) {
		long id;
		if (checkUniqueness(sn.getUnique_id())) {return 2;}
		db.open();
		id = db.createPoint(sn);
		db.close();
		if (id == -1) {
			Log.d("DB", "Insertion unsuccessful");
			return 1;
		}
		else {return 0;}
	}
	
	public boolean delete(String myID) {
		db.open();
		boolean answer = db.deleteTitle(myID);
		db.close();
		if (!answer) {
			Log.d("DB","Deletion unsuccessful");
		}
		return answer;
	}
	
	public boolean edit(String myIDold, StickyNote sn) {
		db.open();
		Cursor c = db.getTitle(myIDold);
		if (c == null) {
			Log.d("DB","Modification unsuccessful Phase 1");
			return false;
		}
		long row = c.getLong(0);
		boolean answer = db.updateTitle(row, sn);
		db.close();
		if (!answer) {
			Log.d("DB","Modification unsuccessful Phase 2");
		}
		return answer;
	}
	
	public ArrayList<String> getAllMyIDs() {
		ArrayList<String> al = new ArrayList<String>();
		db.open();
        Cursor c = db.getAllPoints();
        if (c.moveToFirst()) {
            do {          
                al.add(c.getString(1));
            } while (c.moveToNext());
        }
		db.close();
		return al;
	}
	
	public ArrayList<StickyNote> getAllNotes() {
		ArrayList<StickyNote> al = new ArrayList<StickyNote>();
		db.open();
        Cursor c = db.getAllPoints();
        if (c.moveToFirst()) {
            do {
            	StickyNote newTemp = new StickyNote(c.getString(2),c.getString(3),c.getInt(6),c.getInt(7));
            	newTemp.setUnique_id(c.getString(1));
            	newTemp.setCategory(c.getString(4));
            	newTemp.setAltitude(c.getInt(5));
            	newTemp.setOwner(c.getString(8));
            	newTemp.setCreation_time(c.getLong(9));
            	newTemp.setValid_for(c.getLong(10));
            	newTemp.setVisibility(c.getString(11));
            	
                al.add(newTemp);
            } while (c.moveToNext());
        }
		db.close();
		return al;
	}
}
