package com.jmuenster.gps;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class MyLocationListener implements LocationListener {

	private MapView mv;
	public MyLocationListener (MapView mv) {
		this.mv = mv;
	}
	
	public void onLocationChanged(Location loc) {
		mv.getController().animateTo(new GeoPoint((int)(loc.getLatitude()*1E6), (int)(loc.getLongitude()*1E6)));
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
