package com.mobivery.mvymaps.mapoverlays;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

/**
 * MVYOverlayItem
 * Class to have a POI with custom marker in map.
 * @author Pepe
 *
 */
public class MVYOverlayItem extends OverlayItem{
	
	private Object mObject;
	private Drawable mMarker;

	public MVYOverlayItem(GeoPoint point, Drawable customMarker, String title, String snippet, Object customObject) {
		super(point, title, snippet);
		setCustomMarker(customMarker);
		setCustomObject(customObject); 
	}

	public Object getCustomObject() {
		return mObject;
	}

	public void setCustomObject(Object object) {
		this.mObject = object;
	}
	
	public Drawable getCustomMarker() {
		return mMarker;
	}
	
	public void setCustomMarker(Drawable customMarker) {
		this.mMarker = customMarker;
	}
}

