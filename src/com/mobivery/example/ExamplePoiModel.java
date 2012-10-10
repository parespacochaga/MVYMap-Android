package com.mobivery.example;

import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;

/**
 * ExamplePoiModel
 * Object that contains data of one POI.
 * @author Pepe
 *
 */
public class ExamplePoiModel {
	
	private GeoPoint geoPoint;
	private Drawable markerDrawable;
	private String title;
	private String snippet;
	
	public GeoPoint getGeoPoint() {
		return geoPoint;
	}
	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}
	public Drawable getMarkerDrawable() {
		return markerDrawable;
	}
	public void setMarkerDrawable(Drawable markerDrawable) {
		this.markerDrawable = markerDrawable;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSnippet() {
		return snippet;
	}
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
}
