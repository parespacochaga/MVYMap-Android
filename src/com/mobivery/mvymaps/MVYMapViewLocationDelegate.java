package com.mobivery.mvymaps;

import com.google.android.maps.GeoPoint;
/**
 * Implement this interface to receive events on location changes
 * @author Pepe
 *
 */
public interface MVYMapViewLocationDelegate {

	/**
	 * To implement actions when location listener receives a user position change
	 * @param point
	 */
	public void MVYMapViewLocationChanged(GeoPoint point);
}
