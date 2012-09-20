package com.jmuenster.extras;

import java.net.InetAddress;
import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.jmuenster.db.DBHelper;
import com.jmuenster.server.ServerHandler;

/**
 * This class decides and organises calls between the server and the local DB. If the
 * server is available it is the preferred way to gather note information. All notes 
 * owned by this user will also be stored locally.
 * 
 * 
 * @author Julian Munster
 *
 */
public class Syncronisation {

	private Context context;
	private DBHelper localDB;
	private ServerHandler client;
	private boolean gotInternet;

	/**
	 * Constructor, initialises all fields.
	 * 
	 * @param c
	 */
	public Syncronisation (Context c) {
		this.context = c;
		localDB = new DBHelper(c);
		client = new ServerHandler();
	}
	
	/**
	 * Setter for the gotInternet boolean field.
	 * 
	 * @param doWe
	 */
	public void setGotInternet(boolean doWe) {
		gotInternet = doWe;
	}

	/**
	 * Checks if the device can connect to the Internet.
	 * 
	 * @param context
	 * @return true for success false for failures
	 */
	public static boolean checkConnectivity(Context context) {
		String message = Constants.TOAST_INTERNET;
	    ConnectivityManager cm = 
	    	(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	    	message = Constants.TOAST_HOST;
	    	try {
				if (InetAddress.getByName(Constants.SERVER_IP).isReachable(3000)) {
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	    return false;
	}
	
	/**
	 * This method gatherers all notes in a radius of 2000 meters.
	 * It is only for the start of the application without any 
	 * user input.
	 * 
	 * @return a list of Sticky Notes
	 */
	public ArrayList<StickyNote> getAllNotes() {
		ArrayList<StickyNote> stickies = new ArrayList<StickyNote>();
		
		if (gotInternet) {
			stickies = client.getAllNotes();
		} else {
			stickies = localDB.getAllNotes();
		}
		
		return stickies;
	}
	
	/**
	 * This method looks up a specific group of sticky notes. It 
	 * filters them by the parameters passed in as a note itself.
	 * 
	 * @param sn
	 * @return a list of Sticky Notes
	 */
	public ArrayList<StickyNote> getNotes(StickyNote sn) {
		ArrayList<StickyNote> stickies = new ArrayList<StickyNote>();
		
		if (gotInternet) {
			stickies = client.getAllNotes();
		} else {
			stickies = localDB.getAllNotes();
		}
		
		return stickies;
	}
	
	/**
	 * Creating a new Note.
	 * 
	 * @param sn
	 * @return true for success or false for failure
	 */
//	public boolean createNote(StickyNote sn) {
//		if (gotInternet) {
//			StickyNote newNote = client.addNewNote(sn);
//			if (newNote.getUnique_id() == null) {return false;}
//			localDB.insert(newNote);
//			return true;
//		} else {
//			Toast.makeText(context, "You must be connected to the " +
//					"Internet to create a note.", Toast.LENGTH_LONG).show();
//			return false;
//		}
//	}
	
	/**
	 * Deleting a Note.
	 * 
	 * @param id
	 * @return true for success or false for failure
	 */
	public boolean deleteNote(String id) {
		if (id == null || id.equals("")) {return false;}
		if (gotInternet) {
			if (client.deleteNote(id)) {
				localDB.delete(id);
				return true;
			}
			return false;
		} else {
			Toast.makeText(context, "You must be connected to the " +
					"Internet to delete a note.", Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	// will do later
	public boolean updateNote(StickyNote sn) {
		return false;
	}
}
