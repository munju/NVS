package com.jmuenster.map;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.jmuenster.extras.Constants;
import com.jmuenster.extras.StickyNote;
import com.jmuenster.poi.AddEditDelNote;
import com.jmuenster.poi.ViewNote;

public class MyItemizedOverlay extends BalloonItemizedOverlay<StickyNote> {

	private static boolean longPressed = false;
	private ArrayList<StickyNote> m_overlays = new ArrayList<StickyNote>();
	
	public MyItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker), mapView);
	}

	@Override
	protected StickyNote createItem(int i) {
		return m_overlays.get(i);
	}

	@Override
	public int size() {
		return m_overlays.size();
	}
	
	@Override
	protected boolean onBalloonTap(int index, StickyNote item) {
		if ((m_overlays.get(index).getOwner()).compareTo("juls")==0) {
			checkForMode(index, false);
		} else {
			checkForMode(index, true);
		}
		hideBalloon();
		return true;
	}
	
	public static void setLongPressed(boolean pressed) {
		longPressed = pressed;
	}
	
	public void addOverlay(StickyNote overlay) {
	    m_overlays.add(overlay);
	    setLastFocusedIndex(-1);
	    populate();
	    getMapView().invalidate();
	}
	
	public void clearOverlays() {
		m_overlays.clear();
	    setLastFocusedIndex(-1);
	}
	
	/**
	 * Checking if the user wants to "Edit" or "Delete" the focused overlay
	 * item. It passes the item values and unique id to the AddEditPoint 
	 * activity. It also allows the user to just view the note.
	 * 
	 * @param index
	 * @param viewOnly
	 */
	private void checkForMode(final int index, boolean viewOnly) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getMapView().getContext());
		if (viewOnly) {
			showView(index);
		} else {
			builder = createEditDeleteViewDialog(builder, index);
		}

		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void showView(int index) {
		Intent i = new Intent(getMapView().getContext(), ViewNote.class);
		
		Bundle item = new Bundle();
		item = grabbingWholeNote(index);
		i.putExtras(item);
		
		getMapView().getContext().startActivity(i);	
	}

	private Builder createEditDeleteViewDialog(Builder builder, final int index) {
		builder.setMessage("Do you want to edit or delete?")
		
	       .setCancelable(true)
	       .setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   handlePoint(index, null, Constants.EDIT);
	        	   dialog.cancel();
	           }
	       })
	       .setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   handlePoint(index, null, Constants.DELETE);
	               dialog.cancel();
	           }
	       })
	       .setNegativeButton("VIEW", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   showView(index);
	               dialog.cancel();
	           }
	       });
		return builder;
	}

	private void handlePoint(int index, GeoPoint p, String mode) {
		if (mode.compareTo("")==0) {return;}
		Intent i = new Intent(getMapView().getContext(), AddEditDelNote.class);
		
		Bundle item = new Bundle();

		if (mode == Constants.ADD) {
			item.putInt(Constants.OI_LAT, p.getLatitudeE6());
			item.putInt(Constants.OI_LNG, p.getLongitudeE6());
		} else {
			item = grabbingWholeNote(index);
		}
		i.putExtra(Constants.MODE, mode);
		i.putExtras(item);
	
		getMapView().getContext().startActivity(i);
	}
	
	private Bundle grabbingWholeNote(final int index) {
		Bundle item = new Bundle();
		item.putInt(Constants.OI_LAT, m_overlays.get(index).getPoint().getLatitudeE6());
		item.putInt(Constants.OI_LNG, m_overlays.get(index).getPoint().getLongitudeE6());
		item.putInt(Constants.OI_ALT, m_overlays.get(index).getAltitude());
		item.putLong(Constants.OI_CREATION_TIME, m_overlays.get(index).getCreation_time());
		item.putLong(Constants.OI_VALID_FOR, m_overlays.get(index).getValid_for());
		item.putString(Constants.OI_CATEGORY, m_overlays.get(index).getCategory());
		item.putString(Constants.OI_OWNER, m_overlays.get(index).getOwner());
		item.putString(Constants.OI_ID,m_overlays.get(index).getUnique_id());
		item.putString(Constants.OI_VISIBILITY,m_overlays.get(index).getVisibility());
		item.putString(Constants.OI_TITLE,m_overlays.get(index).getTitle());
		item.putString(Constants.OI_DESCRIPTION, m_overlays.get(index).getSnippet());
		return item;
	}

	public void onLongPress(MotionEvent e) {
		if (!longPressed) {
			longPressed = true;
			GeoPoint p = getMapView().getProjection().fromPixels((int)e.getX(), (int)e.getY());
			handlePoint(-1, p, Constants.ADD);
		}
	}

	public boolean onDoubleTap(MotionEvent e) {
		getMapView().getController().zoomIn();
		return true;
	}

	public boolean onSingleTapConfirmed(MotionEvent e) {
		if (!getBalloon()) {
			hideBalloon();
		}
		setBalloon(false);
		return true;
	}

	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {return false;}

	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {return false;}

	public void onShowPress(MotionEvent e) {}

	public boolean onDown(MotionEvent e) {return false;}

	public boolean onSingleTapUp(MotionEvent e) {return false;}

	public boolean onDoubleTapEvent(MotionEvent e) {return false;}
}
