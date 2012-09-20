package com.jmuenster.extras;

/**
 * @author Julian Muenster
 *
 */
public class Constants {
	// Overlay Item Constants
	public static final String OI_ID = "ioID";
	public static final String OI_TITLE = "ioTitle";
	public static final String OI_DESCRIPTION = "ioDescription";
	public static final String OI_CATEGORY = "ioCategory";
	public static final String OI_LAT = "ioLat";
	public static final String OI_LNG = "ioLng";
	public static final String OI_ALT = "ioAlt";
	public static final String OI_OWNER = "ioOwner";
	public static final String OI_VALID_FOR = "ioValid_for";
	public static final String OI_CREATION_TIME = "ioCreationTime";
	public static final String OI_VISIBILITY = "ioVis";
	
	// Mode
	public static final String MODE = "mode";
	public static final String EDIT = "EDIT";
	public static final String ADD = "ADD";
	public static final String DELETE = "DELETE";
	
	// PointDB
	public static final String KEY_ROWID = "_id";
	public static final String KEY_MYID = "myID";
	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_CATEGORY = "cat";
	public static final String KEY_ALT = "alt";
	public static final String KEY_LAT = "lat";
	public static final String KEY_LNG = "lng";
	public static final String KEY_OWNER = "owner";
	public static final String KEY_CREATION_TIME = "time";
	public static final String KEY_VALID_FOR = "valid_for";
	public static final String KEY_VISIBILITY = "vis";
	public static final String KEY_PROTOCOL_VERSION = "ver";
	
	// GPS
	public static final int REQUEST_CODE = 0;
	
	// HTTP Request Type
	public static final String GET_REQUEST = "GET";
	public static final String PUT_REQUEST = "PUT";
	public static final String POST_REQUEST = "POST";
	public static final String DELETE_REQUEST = "DELTE";
	
	// Server URL
	public static final String SERVER_IP = "192.168.1.4";//192.168.1.3 
	public static final String SERVER_URL = "http://"+SERVER_IP+":8080";
	public static final String SEARCH_EXTENSION = "/api/search/";
	public static final String SERVER_EXTENSION = "/api/note/";
	public static final int CURRENT_PROTOCOL = 1;
	
	// Visibility
	public static final String VIS_PUBLIC = "Public";
	public static final String VIS_PRIVATE = "Private";
	public static final String VIS_FRIENDS = "Friends";
	
	// Category
	public static final String CAT_GENERAL = "General";
	public static final String CAT_MEDICAL = "Medical";
	public static final String CAT_FOOD = "Food";
	public static final String CAT_ENTERTAINMENT = "Entertainment";
	
	// Variable Names JSON
	public static final String JSON_OBJECT_NAME = "data";
	public static final String JSON_UNIQUE_ID = "id";
	public static final String JSON_TITLE = "title";
	public static final String JSON_BODY = "body";
	public static final String JSON_CATEGORY = "category";
	public static final String JSON_ALTITUDE = "altitude";
	public static final String JSON_LATITUDE = "latitude";
	public static final String JSON_LONGITUDE = "longitude";
	public static final String JSON_RADIUS = "radius";
	public static final String JSON_OWNER = "owner";
	public static final String JSON_CREATION_TIME = "creation_time";
	public static final String JSON_VALID_FOR = "valid_for";
	public static final String JSON_VISIBILITY = "visibility";
	public static final String JSON_PROTOCOL_VERSION = "protocol_version";
	
	// Toast Messages
	public static final String TOAST_INTERNET = "Please turn on your Wifi/3G!";
	public static final String TOAST_HOST = "Server is unreachable!";
}
