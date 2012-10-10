package com.mobivery.example;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;

/**
 * ExampleDAO
 * Singleton class to access POIs data
 * @author Pepe
 *
 */
public class ExampleDAO {
	
	private static ExampleDAO sInstance;
	private final static int NUM_POIS = 20;
	private final static double GEO_MAX = 60 * 1E6;
	
	/**
	 * Method to get the singleton instance
	 * @return shared instance
	 */
	public static ExampleDAO getInstance() {
		if (sInstance == null) {
			sInstance = new ExampleDAO();
		}
		return sInstance;
	}
	
	/**
	 * Method that generates POIs with blue markers and random geoposition
	 * @param context
	 * @return array list of POIs
	 */
	public ArrayList<ExamplePoiModel> getBlueArray( Context context ) {
		
		Random randomObject = new Random();
		ArrayList<ExamplePoiModel> poisArray = new ArrayList<ExamplePoiModel>();
		ExamplePoiModel poiObject;
		
		for( int i = 0; i < NUM_POIS/2; i++ ) {

			int latE6 = (int) ((randomObject.nextFloat() * GEO_MAX * 2) - GEO_MAX);
			int lonE6 = (int) ((randomObject.nextFloat() * GEO_MAX * 2) - GEO_MAX);
			GeoPoint randomGeoPoint = new GeoPoint(latE6, lonE6);
			
			Drawable customMarker = context.getResources().getDrawable(R.drawable.poi_blue);
			customMarker.setBounds(0, 0, customMarker.getIntrinsicWidth(), customMarker.getIntrinsicHeight());
			
			poiObject = new ExamplePoiModel();
			poiObject.setGeoPoint(randomGeoPoint);
			poiObject.setMarkerDrawable(customMarker);
			poiObject.setTitle("Blue #" + i);
			poiObject.setSnippet("Position: " + randomGeoPoint.getLatitudeE6()/1E6 + ", " + randomGeoPoint.getLongitudeE6()/1E6);

			poisArray.add(poiObject);
		}
		return poisArray;
	}
	
	/**
	 * Method that generates POIs with red markers and random geoposition
	 * @param context
	 * @return array list of POIs
	 */
	public ArrayList<ExamplePoiModel> getRedArray( Context context ) {
		
		Random randomObject = new Random();
		ArrayList<ExamplePoiModel> poisArray = new ArrayList<ExamplePoiModel>();
		ExamplePoiModel poiObject;
		
		for( int i = 0; i < NUM_POIS/2; i++ ) {

			int latE6 = (int) ((randomObject.nextFloat() * GEO_MAX * 2) - GEO_MAX);
			int lonE6 = (int) ((randomObject.nextFloat() * GEO_MAX * 2) - GEO_MAX);
			GeoPoint randomGeoPoint = new GeoPoint(latE6, lonE6);
			
			Drawable customMarker = context.getResources().getDrawable(R.drawable.poi_red);
			customMarker.setBounds(0, 0, customMarker.getIntrinsicWidth(), customMarker.getIntrinsicHeight());
			
			poiObject = new ExamplePoiModel();
			poiObject.setGeoPoint(randomGeoPoint);
			poiObject.setMarkerDrawable(customMarker);
			poiObject.setTitle("Red #" + i);
			poiObject.setSnippet("Position: " + randomGeoPoint.getLatitudeE6()/1E6 + ", " + randomGeoPoint.getLongitudeE6()/1E6);

			poisArray.add(poiObject);
		}
		return poisArray;
	}

}
