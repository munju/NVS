package com.jmuenster.map;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.jmuenster.gps.GPSHandler;

/**
 * @author Julian Munster
 *
 */
public class MyMapView extends MapActivity{

	//Instance of a MapView and GPSHandler object
	private MapView mMV;
	private GPSHandler gHandler;

	/** 
	 * Called when the activity is first created.
	 * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	/**
	 * Disregard this method unless you want to display
	 * a route.
	 */
	@Override
	protected boolean isRouteDisplayed() {return false;}
    
	/**
	 * This createMapView method reads in the apiKey for the 
	 * map view and then creates the default view.
	 * You may choose to give more options.
	 * 
	 * @param apiKey the google maps apiKey
	 * @return the MapView that was just created and initialised
	 */
	public MapView createMapView(String apiKey) {
		return createMapView(apiKey, true, true, 15);
	}

	/**
	 * This createMapView method reads in the apiKey for the
	 * map view and then creates the default view.
	 * You also need to enter if zoom is on/off, a zoom value,
	 * and if it is clickable or not.
	 * It also tries to load in the last known location of 
	 * the device in order to center the map.
	 *  
	 * @param apiKey the google maps apiKey
	 * @param clickable is the map clickable
	 * @param zoom is the map zoomable
	 * @param zoomValue the initial zoom value
	 * @return the MapView that was just created and initialised
	 */
    public MapView createMapView(String apiKey, boolean clickable,  boolean zoom, int zoomValue) {
		gHandler = new GPSHandler(this);
		//gHandler.checkGPSstatus();
        mMV = new MapView(this, apiKey);
        mMV.setEnabled(true);
        mMV.setClickable(clickable);
        mMV.setBuiltInZoomControls(zoom);
		mMV.getController().setZoom(zoomValue);
    	//mMV.getController().setCenter(gHandler.getLastKnownLocation().getGeoPoint());
		setContentView(mMV);
		return mMV;
    }
}
