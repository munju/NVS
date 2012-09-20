/***
 * Copyright (c) 2010 readyState Software Ltd
 * 
 * modified by Julian Munster
 * 
 * The following license only applies to this file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.jmuenster.map;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.jmuenster.extras.StickyNote;
import com.jmuenster.poi.R;

/**
 * An abstract extension of ItemizedOverlay for displaying an information balloon
 * upon screen-tap of each marker overlay.
 * 
 * @author Jeff Gilfelt, Julian Munster
 */
public abstract class BalloonItemizedOverlay<Item extends StickyNote> extends ItemizedOverlay<Item> 
implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener{

	private int viewOffset;
	private MapView mapView;
	private View clickRegion;
	private boolean balloon;
	private Item currentFocussedItem;
	private int currentFocussedIndex;
    private GestureDetector gesturedetector;
	private BalloonOverlayView<Item> balloonView;

	/**
	 * Create a new BalloonItemizedOverlay
	 * 
	 * @param defaultMarker - A bounded Drawable to be drawn on the map for each item in the overlay.
	 * @param mapView - The view upon which the overlay items are to be drawn.
	 */
	public BalloonItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(defaultMarker);
		viewOffset = 0;
		setBalloon(false);
		this.mapView = mapView;
        gesturedetector = new GestureDetector(mapView.getContext(), this);
	}
	
	/**
	 * Creates the balloon view. Override to create a sub-classed view that
	 * can populate additional sub-views.
	 */
	protected BalloonOverlayView<Item> createBalloonOverlayView() {
		return new BalloonOverlayView<Item>(mapView.getContext(), getBalloonBottomOffset());
	}
	
	/**
	 * Sets the visibility of this overlay's balloon view to GONE. 
	 */
	protected void hideBalloon() {
		if (balloonView != null) {
			balloonView.setVisibility(View.GONE);
			setBalloon(false);
		}
	}
	
	/**
	 * Hides the balloon view for any other BalloonItemizedOverlay instances
	 * that might be present on the MapView.
	 * 
	 * @param overlays - list of overlays (including this) on the MapView.
	 */
	private void hideOtherBalloons(List<Overlay> overlays) {
		
		for (Overlay overlay : overlays) {
			if (overlay instanceof BalloonItemizedOverlay<?> && overlay != this) {
				((BalloonItemizedOverlay<?>) overlay).hideBalloon();
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see com.google.android.maps.ItemizedOverlay#onTap(int)
	 */
	@Override
	protected final boolean onTap(int index) {
		
		currentFocussedIndex = index;
		currentFocussedItem = createItem(index);
		
		boolean isRecycled;
		if (balloonView == null) {
			balloonView = createBalloonOverlayView();
			clickRegion = (View) balloonView.findViewById(R.id.balloon_inner_layout);
			clickRegion.setOnTouchListener(createBalloonTouchListener());
			isRecycled = false;
		} else {
			isRecycled = true;
		}
	
		balloonView.setVisibility(View.GONE);
		
		List<Overlay> mapOverlays = mapView.getOverlays();
		if (mapOverlays.size() > 1) {
			hideOtherBalloons(mapOverlays);
		}
		
		balloonView.setData(currentFocussedItem);
		
		GeoPoint point = currentFocussedItem.getPoint();
		MapView.LayoutParams params = new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, point,
				MapView.LayoutParams.BOTTOM_CENTER);
		params.mode = MapView.LayoutParams.MODE_MAP;
		
		balloonView.setVisibility(View.VISIBLE);

		if (isRecycled) {
			balloonView.setLayoutParams(params);
		} else {
			mapView.addView(balloonView, params);
		}
		
		mapView.getController().animateTo(point);
		setBalloon(true);
		return true;
	}
	
	/**
	 * Override this method to handle a "tap" on a balloon. By default, does nothing 
	 * and returns false.
	 * 
	 * @param index - The index of the item whose balloon is tapped.
	 * @param item - The item whose balloon is tapped.
	 * @return true if you handled the tap, otherwise false.
	 */
	protected boolean onBalloonTap(int index, Item item) {
		return false;
	}
	
	/**
	 * Sets the onTouchListener for the balloon being displayed, calling the
	 * overridden {@link #onBalloonTap} method.
	 */
	private OnTouchListener createBalloonTouchListener() {
		return new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				
				View l =  ((View) v.getParent()).findViewById(R.id.balloon_main_layout);
				Drawable d = l.getBackground();
				
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					int[] states = {android.R.attr.state_pressed};
					if (d.setState(states)) {
						d.invalidateSelf();
					}
					return true;
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					int newStates[] = {};
					if (d.setState(newStates)) {
						d.invalidateSelf();
					}
					// call overridden method
					onBalloonTap(currentFocussedIndex, currentFocussedItem);
					return true;
				} else {
					return false;
				}
				
			}
		};
	}
	
	/**
	 * Setting up gesture events.
	 * 
	 * @param event
	 * @param mapView
	 * @return boolean
	 */
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		return gesturedetector.onTouchEvent(event);
	}
	
	/**
	 * Set the horizontal distance between the marker and the bottom of the information
	 * balloon. The default is 0 which works well for center bounded markers. If your
	 * marker is center-bottom bounded, call this before adding overlay items to ensure
	 * the balloon hovers exactly above the marker. 
	 * 
	 * @param pixels - The padding between the center point and the bottom of the
	 * information balloon.
	 */
	public void setBalloonBottomOffset(int pixels) {
		viewOffset = pixels;
	}
	public int getBalloonBottomOffset() {
		return viewOffset;
	}
	
	public void setBalloon(boolean balloon) {
		this.balloon = balloon;
	}

	public boolean getBalloon() {
		return balloon;
	}
	
	/**
	 * Expose map view to subclasses.
	 * Helps with creation of balloon views. 
	 * 
	 * @return mapView
	 */
	public MapView getMapView() {
		return mapView;
	}
	/**
	 * Re-populates the overlays.
	 */
	public void refreshOverlays(){
	    populate();
	}
}
