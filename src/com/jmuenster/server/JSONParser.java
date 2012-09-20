package com.jmuenster.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jmuenster.extras.Constants;
import com.jmuenster.extras.StickyNote;

/**
 * @author Julian Munster
 *
 */
public class JSONParser {
	
	// This field is a list of all returned notes
	private static ArrayList<StickyNote> notesList = new ArrayList<StickyNote>();
	
	/**
	 * This method returns the array list with all the 
	 * returned notes.
	 * 
	 * @return notesList
	 */
	public static ArrayList<StickyNote> getNotesList() {
		return notesList;
	}
	
	/**
	 * This method creates a new JSONObject that can be send via an 
	 * HTTP call. This object has a JSONArray of JSONObjects inside 
	 * itself. All parameters must be included or set to null for the
	 * Strings, -1 for the longs and three flags for the point data 
	 * fields, if they should not be included.
	 * 
	 * @param stickyNote
	 * @param altModified
	 * @param latModified
	 * @param lngModified
	 * @param radPresent
	 * @param radius
	 * @return j the completed JSONObject
	 */
	public static JSONObject makeNewNote(StickyNote stickyNote, boolean altModified, 
			boolean latModified, boolean lngModified, boolean radPresent, int radius) {
		JSONObject j = new JSONObject();
		JSONObject note = new JSONObject();
		JSONArray a = new JSONArray();
		
		try {
			if (stickyNote.getTitle() != null) {
				note.put(Constants.JSON_TITLE, stickyNote.getTitle());}
			if (stickyNote.getSnippet() != null) {
				note.put(Constants.JSON_BODY, stickyNote.getSnippet());}
			if (stickyNote.getCategory() != null) {
				note.put(Constants.JSON_CATEGORY, stickyNote.getCategory());}
			if (altModified) {
				note.put(Constants.JSON_ALTITUDE, stickyNote.getAltitude());}
			if (latModified) {
				note.put(Constants.JSON_LATITUDE, stickyNote.getPoint().getLatitudeE6());}
			if (lngModified) {
				note.put(Constants.JSON_LONGITUDE, stickyNote.getPoint().getLongitudeE6());}
			if (radPresent) {
				note.put(Constants.JSON_RADIUS, radius);}
			if (stickyNote.getOwner() != null) {
				note.put(Constants.JSON_OWNER, stickyNote.getOwner());}
			if (stickyNote.getCreation_time() != -1) {
				note.put(Constants.JSON_CREATION_TIME, stickyNote.getCreation_time());}
			if (stickyNote.getValid_for() != -1) {
				note.put(Constants.JSON_VALID_FOR, stickyNote.getValid_for());}
			if (stickyNote.getVisibility() != null) {
				note.put(Constants.JSON_VISIBILITY, stickyNote.getVisibility());}
			note.put(Constants.JSON_PROTOCOL_VERSION, Constants.CURRENT_PROTOCOL);
			a.put(note);
			j.put("data", a);
		} catch (JSONException e) {e.printStackTrace();}
		
		return j;
	}

	public static boolean retrieveObjects(HttpEntity result) {
		notesList.clear();
		String stream = null;
		InputStream instream;
    	if (result != null) {
    		try {
    			instream = result.getContent();
    			stream= convertStreamToString(instream);
    			instream.close();
    		} catch (Exception e) {
    			e.printStackTrace();
    			return false;
    		}
    	}
    	if (stream != null) {
    		jsonArraySplitter(handleJSON(stream));
    		return true;
    	} else {
    		return false;
    	}
	}
	
    private static void jsonArraySplitter(JSONArray array) {
		for (int i = 0; i < array.length(); i++) {
			try {
				JSONObject temp = array.getJSONObject(i);
				
				String title = temp.getString(Constants.JSON_TITLE);
				String body = temp.getString(Constants.JSON_BODY);
				int lat = temp.getInt(Constants.JSON_LATITUDE);
				int lng = temp.getInt(Constants.JSON_LONGITUDE);
				StickyNote note = new StickyNote(title, body, lat, lng);
				note.setUnique_id(temp.getString(Constants.JSON_UNIQUE_ID));
				note.setCategory(temp.getString(Constants.JSON_CATEGORY));
				note.setAltitude(temp.getInt(Constants.JSON_ALTITUDE));
				note.setOwner(temp.getString(Constants.JSON_OWNER));
				note.setCreation_time(temp.getLong(Constants.JSON_CREATION_TIME));
				note.setValid_for(temp.getLong(Constants.JSON_VALID_FOR));
				note.setVisibility(temp.getString(Constants.JSON_VISIBILITY));
				
				notesList.add(note);
			} catch (JSONException e) {e.printStackTrace();}
		}
	}

	private static JSONArray handleJSON(String stream) {
    	JSONObject jObject = null;
    	JSONArray dataObject = null;
		try {
			jObject = new JSONObject(stream);
			dataObject = jObject.getJSONArray("data");
		} catch (JSONException e) {e.printStackTrace();}
		return dataObject;
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
