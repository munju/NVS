package com.jmuenster.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;

import android.util.Log;

import com.jmuenster.extras.Constants;
import com.jmuenster.extras.StickyNote;

public class ServerHandler {
	
	HTTPClient client;
	
	public ServerHandler() {
		client = new HTTPClient();
	}
	
	public ArrayList<StickyNote> getAllNotes() {
		ArrayList<StickyNote> al = new ArrayList<StickyNote>();

		// creating StickyNote - needs to be something meaningful
		// needs to have current GPS coordinates :S
//		StickyNote sn = new StickyNote(null, null, -45868017, 170515441);
		// creating new JSONObject
//		JSONObject newNote = JSONParser.makeNewNote(sn, false, false, false, true, 2000);
		
		// sending off httpGet
		HttpResponse response = client.httpPost(Constants.SERVER_URL+Constants.SEARCH_EXTENSION, "{\"data\": [{\"rad\":\"1000000\"}]}");
		
		// checking response for errors
		if (checkResponse(response)) {
			if (JSONParser.retrieveObjects(response.getEntity())) {
				// grabbing all Sticky Notes
				al = JSONParser.getNotesList();
			}
		}
		return al;
	}
	
//	public ArrayList<StickyNote> getNotes(StickyNote sn, int radius) {
//		ArrayList<StickyNote> al = new ArrayList<StickyNote>();
//		boolean radPresent = false;
//		if (radius > 0) {radPresent = true;}
//		
//		// creating new JSONObject
//		JSONObject newNote = JSONParser.makeNewNote(sn, false, false, false, radPresent,  radius);
//		
//		// sending off httpGet
//		HttpResponse response = client.httpPost(Constants.SERVER_URL+Constants.SEARCH_EXTENSION, newNote);
//		
//		// checking response for errors
//		if (checkResponse(response)) {
//			if (JSONParser.retrieveObjects(response.getEntity())) {
//				// grabbing all Sticky Notes
//				al = JSONParser.getNotesList();
//			}
//		}
//		return al;
//	}
	
//	public StickyNote addNewNote(StickyNote sn) {
//		// creating new JSONObject
//		JSONObject newNote = JSONParser.makeNewNote(sn, true, true, true, false, 0);
//		
//		HttpResponse response = client.httpPost(Constants.SERVER_URL+Constants.SERVER_EXTENSION, newNote);
//		
//		StickyNote newSticky = new StickyNote("", "", 0, 0);
//		newSticky.setUnique_id(null);
//		// checking response for errors
//		if (checkResponse(response)) {
//			ArrayList<StickyNote> al = JSONParser.getNotesList();
//			return al.get(0);
//		}
//		return newSticky;
//	}
	
	public boolean deleteNote(String id) {
		HttpResponse response = client.httpDelete(Constants.SERVER_URL+Constants.SERVER_EXTENSION, id + "/");
		
		// checking response for errors
		if (checkResponse(response)) {
			return true;
		}
		return false;
	}
	
	/////////////////////////////////////////////////////////////////////////
	// doing this last
	public boolean updateNote(StickyNote snNew) {
		return false;
	}
	/////////////////////////////////////////////////////////////////////////
	
	private boolean checkResponse(HttpResponse response) {
		if ((response.getStatusLine().getStatusCode()+"").matches("20\\d")) {
			return true;
		} else {
			String answer = "";
			try {
				answer = convertStreamToString(response.getEntity().getContent());
			} catch (Exception e) {e.printStackTrace();}
			Log.i("HTTPErrorCode", response.getStatusLine().getStatusCode()+"");
			Log.i("ServerResponse", answer);
			return false;
		}
	}
	
	private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
