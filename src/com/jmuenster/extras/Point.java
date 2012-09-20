package com.jmuenster.extras;

import com.google.android.maps.GeoPoint;

/**
 * @author Julian Muenster
 *
 */
public class Point {
	
	private GeoPoint geoPoint;
	private double altitude;

	public Point(GeoPoint gp, double al) {
		this.geoPoint = gp;
		this.altitude = al;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
}
