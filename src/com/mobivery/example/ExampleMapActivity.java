package com.mobivery.example;


import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.mobivery.mvymaps.MVYMapView;
import com.mobivery.mvymaps.MVYMapViewLocationDelegate;
import com.mobivery.mvymaps.MVYMapViewPoiDelegate;

/**
 * ExampleMapActivity
 * Activity class that represents a example of use of MVYMapView component
 * @author Pepe
 *
 */
public class ExampleMapActivity extends MapActivity implements MVYMapViewPoiDelegate, MVYMapViewLocationDelegate {

	MVYMapView mapView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_example_map);
		mapView = (MVYMapView) findViewById(R.id.mapview);
		
		// delegate interfaces
		mapView.setPoiDelegate(this);
		mapView.setLocationDelegate(this);
		
		// user location POI
		mapView.setLocationCustomEnabled(true);
		
		// request POIs through DAO
		ArrayList<ExamplePoiModel> blueArray = ExampleDAO.getInstance().getBlueArray(this);
		ArrayList<ExamplePoiModel> redArray = ExampleDAO.getInstance().getRedArray(this);
		
		// add overlay items for each POI
		for (ExamplePoiModel bluePoiModel : blueArray) {
			mapView.addPoi(bluePoiModel.getGeoPoint(), bluePoiModel.getMarkerDrawable(), bluePoiModel.getTitle(), bluePoiModel.getSnippet(), bluePoiModel);
		}
		for (ExamplePoiModel redPoiModel : redArray) {
			mapView.addPoi(redPoiModel.getGeoPoint(), redPoiModel.getMarkerDrawable(), redPoiModel.getTitle(), redPoiModel.getSnippet(), redPoiModel);
		}
		
		// zoom
		mapView.setZoomWithAllPois();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void MVYMapViewPoiOpenClicked(Object customObject) {
		
		ExamplePoiModel poiObject = (ExamplePoiModel) customObject;
		Log.v("MVYMap", "POI Open " + poiObject.getTitle());
	}

	@Override
	public void MVYMapViewLocationChanged(GeoPoint point) {
		
		Log.v("MVYMap", "Location Changed: " + point.getLatitudeE6() / 1E6 + ", " + point.getLongitudeE6() / 1E6);
		mapView.setLocationListenerEnabled(false);
	}

}
