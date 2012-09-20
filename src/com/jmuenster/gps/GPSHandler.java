package com.jmuenster.gps;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.jmuenster.extras.Point;

public class GPSHandler {
	
//	private Context c;
	private LocationManager lm;
	
	public GPSHandler(Context c) {
//		this.c = c;
	    lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
	}
	
	public void checkGPSstatus() {
		
	}
	
	public Point getLastKnownLocation() {
		List<String> providers = lm.getProviders(true);
		Location loc = null;
        for (int i=providers.size()-1; i>=0; i--) {
        	loc = lm.getLastKnownLocation(providers.get(i));
            if (loc != null) break;
        }
        GeoPoint geoPoint = new GeoPoint((int)(loc.getLatitude()*1E6), 
        		(int)(loc.getLongitude()*1E6));
        
        Point point = new Point(geoPoint, loc.getAltitude());
        
		return point;
	}

	public LocationListener startLocationListener(MapView mv) {
		LocationListener locationListener = new MyLocationListener(mv);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 30, locationListener);
		// every 35 seconds = 35000
		return locationListener;
	}
	
	public void stopLocationListener(LocationListener locationListener) {
		lm.removeUpdates(locationListener);
	}
	
	/**
	 * This getGPS method reads in the application 
	 * context and then determines the last known
	 * location of the device.
	 * 
	 * @param c the application context
	 * @return an array of doubles with the location information
	 */
	@SuppressWarnings("static-access")
	public static double[] getLKGPS(Context c) {  
	    LocationManager lm = (LocationManager) c.getSystemService(c.LOCATION_SERVICE);    
	    List<String> providers = lm.getProviders(true);  
	  
	    /* Loop over the array backwards, and if you get an accurate location, then break out the loop*/  
	    Location l = null;  
	      
	    for (int i=providers.size()-1; i>=0; i--) {  
	        l = lm.getLastKnownLocation(providers.get(i));  
	        if (l != null) break;  
	    }  
	      
	    double[] gps = new double[3];  
	    if (l != null) {  
	        gps[0] = l.getLatitude();  
	        gps[1] = l.getLongitude();
	        gps[2] = l.getAltitude();
	    }  
	    return gps;  
	}

//	public boolean checkGPSonline() {
//	    LocationManager lm = (LocationManager) this.getSystemService(MyMapView.LOCATION_SERVICE); 
//		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
//	          createGpsDisabledAlert();  
//	    }
//		return true;
//	}
//	
//	private void openGPSsettings() {
//		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivityForResult(intent, Constants.REQUEST_CODE);
//	}
//	
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        if(requestCode == Constants.REQUEST_CODE && resultCode == 0){
//    	    LocationManager lm = (LocationManager) this.getSystemService(MyMapView.LOCATION_SERVICE); 
//    		if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){  
//    	          createGpsDisabledAlert();
//    	    }
//        }
//    }
//    
//    private void createGpsDisabledAlert(){  
//    	AlertDialog.Builder builder = new AlertDialog.Builder(this);  
//    	builder.setMessage("Your GPS is disabled! Would you like to enable it?")  
//    	     .setCancelable(false)  
//    	     .setPositiveButton("Enable GPS",  
//    	          new DialogInterface.OnClickListener(){  
//    	          public void onClick(DialogInterface dialog, int id){  
//    	        	  openGPSsettings();
//    	        	  dialog.dismiss();
//    	          }  
//    	     });  
//    	     builder.setNegativeButton("Quit Application",  
//    	          new DialogInterface.OnClickListener(){  
//    	          public void onClick(DialogInterface dialog, int id){  
//    	               quitTheApp();
//    	          }  
//    	     });  
//    	AlertDialog alert = builder.create();  
//    	alert.show();  
//    } 
//    
//    private void quitTheApp() {
//    	this.finish();
//    }
}
