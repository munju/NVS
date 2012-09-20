package com.jmuenster.poi;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.jmuenster.extras.Constants;
import com.jmuenster.extras.StickyNote;
import com.jmuenster.extras.Syncronisation;
import com.jmuenster.map.MyItemizedOverlay;
import com.jmuenster.map.MyMapView;

public class PointOfInterest extends MyMapView {
	
	private MapView myMapView;
	private Syncronisation sync;
	private List<Overlay> mapOverlays;
	private MyItemizedOverlay food, entertainment, medical, general;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myMapView = createMapView("0d5YpthRw0mtPyxS0t7OvTnDqqVGDMonimT4pVQ");
        
        sync = new Syncronisation(this);
        
        setUpOverlays();	
    	mapOverlays = myMapView.getOverlays();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	standardTest();
    	refreshCycle();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
  
    private void refreshCycle() {
    	general.clearOverlays();
    	food.clearOverlays();
    	medical.clearOverlays();
    	entertainment.clearOverlays();
    	loadAllPoints();
    	mapOverlays.add(medical);
    	mapOverlays.add(entertainment);
    	mapOverlays.add(food);
    	mapOverlays.add(general);
    	general.refreshOverlays();
    	food.refreshOverlays();
    	medical.refreshOverlays();
    	entertainment.refreshOverlays();
    }
    
    private void standardTest() {
    	if (!Syncronisation.checkConnectivity(this)) {
    		sync.setGotInternet(false);
    	} else {
    		sync.setGotInternet(true);
    	}
    }
    
    private void setUpOverlays() {
    	Drawable ent_drawable = getResources().getDrawable(R.drawable.entertainment);
    	Drawable med_drawable = getResources().getDrawable(R.drawable.red_cross);
    	Drawable food_drawable = getResources().getDrawable(R.drawable.chillimarker);
    	Drawable general_drawable = getResources().getDrawable(R.drawable.general);
    	
    	general = new MyItemizedOverlay(general_drawable, myMapView);
    	general.setBalloonBottomOffset(45);
    	
    	food = new MyItemizedOverlay(food_drawable, myMapView);
    	food.setBalloonBottomOffset(30);

    	entertainment = new MyItemizedOverlay(ent_drawable, myMapView);
    	entertainment.setBalloonBottomOffset(30);
    	
    	medical = new MyItemizedOverlay(med_drawable, myMapView);
    	medical.setBalloonBottomOffset(35);   	   	
    }
    
    private void loadAllPoints() {
    	ArrayList<StickyNote> al = sync.getAllNotes();
    	for (int i = 0; i < al.size(); i++) {
    		Log.i("List of notes", al.get(i).toString());
    		String cat = al.get(i).getCategory();
    		if (cat.compareTo(Constants.CAT_FOOD)==0) {
    			food.addOverlay(al.get(i));
    		} else if (cat.compareTo(Constants.CAT_ENTERTAINMENT)==0) {
    			entertainment.addOverlay(al.get(i));
    		} else if (cat.compareTo(Constants.CAT_MEDICAL)==0) {
    			medical.addOverlay(al.get(i));
    		} else {
    			general.addOverlay(al.get(i));
    		}
    	}
    }
}

// -45.868017,170.515441
// -45.863072,170.517495
// 1308728879452