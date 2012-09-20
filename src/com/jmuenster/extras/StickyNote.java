package com.jmuenster.extras;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class StickyNote extends OverlayItem{

	private String unique_id;
	private String category;
	private int altitude;
	private String owner;
	private long creation_time;
	private long valid_for;
	private String visibility;
	
	public StickyNote(String title, String body, int lat, int lng) {
		super(new GeoPoint(lat, lng), title, body);
		unique_id = category = owner = visibility =  "";
		altitude = 0;
		creation_time = valid_for = 0;
	}

	public StickyNote(String unique_id, String title, String body, String category, int altitude,
			int latitude, int longitude, String owner, long creationTime,
			long validFor, String visibility) {
		super(new GeoPoint(latitude, longitude), title, body);
		this.unique_id = unique_id;
		this.category = category;
		this.altitude = altitude;
		this.owner = owner;
		creation_time = creationTime;
		valid_for = validFor;
		this.visibility = visibility;
	}
	
	/**
	 * @return the unique_id
	 */
	public String getUnique_id() {
		return unique_id;
	}

	/**
	 * @param uniqueId the unique_id to set
	 */
	public void setUnique_id(String uniqueId) {
		unique_id = uniqueId;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the altitude
	 */
	public int getAltitude() {
		return altitude;
	}

	/**
	 * @param altitude the altitude to set
	 */
	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the creation_time
	 */
	public long getCreation_time() {
		return creation_time;
	}

	/**
	 * @param creationTime the creation_time to set
	 */
	public void setCreation_time(long creationTime) {
		creation_time = creationTime;
	}

	/**
	 * @return the valid_for
	 */
	public long getValid_for() {
		return valid_for;
	}

	/**
	 * @param validFor the valid_for to set
	 */
	public void setValid_for(long validFor) {
		valid_for = validFor;
	}

	/**
	 * @return the visibility
	 */
	public String getVisibility() {
		return visibility;
	}

	/**
	 * @param visibility the visibility to set
	 */
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StickyNote [ID=" + unique_id + ", altitude=" + altitude + ", body=" + getSnippet()
				+ ", category=" + category + ", creation_time=" + creation_time
				+ ", latitude=" + getPoint().getLatitudeE6() + ", longitude=" + getPoint().getLongitudeE6()
				+ ", owner=" + owner + ", title=" + getTitle() + ", valid_for=" + valid_for
				+ ", visibility=" + visibility + "]";
	}
}
