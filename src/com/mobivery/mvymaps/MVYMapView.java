package com.mobivery.mvymaps;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.mobivery.mvymaps.mapoverlays.MVYItemizedOverlay;
import com.mobivery.mvymaps.mapoverlays.MVYOverlayItem;
import com.mobivery.mvymaps.utils.MVYMapLimits;

/**
 * MVYMapView
 * Extended MapView to simplify management of POIs, Balloons and Location Listener
 * @author Pepe
 *
 */
public class MVYMapView extends MapView {

	private Context mContext;
	private MVYItemizedOverlay itemizedOverlay;
	private MyLocationOverlay myLocationOverlay;
	private MVYItemizedOverlay customLocationOverlay;
	private GeoPoint userGeoPoint;
	private Drawable customUserMarker;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private boolean locationCustomEnabled = false;
	private boolean locationCustomRefreshEnabled = false;
	private boolean locationListenerEnabled = false;
	private MVYMapLimits mapLimits;
	private MVYMapViewPoiDelegate poiDelegate;
	private MVYMapViewLocationDelegate locationDelegate;

	/**
	 * Constructor with API Key
	 * @param context
	 * @param apiKey
	 */
	public MVYMapView(Context context, String apiKey) {
		super(context, apiKey);
		initMVYMapView(context);
	}
	
	/**
	 * Constructor with attributes
	 * @param context
	 * @param attributes
	 */
	public MVYMapView(Context context, AttributeSet attributes) {
		super(context, attributes);
		initMVYMapView(context);
	}
	
	/**
	 * Constructor with attributes and style
	 * @param context
	 * @param attributes
	 * @param defStyle
	 */
	public MVYMapView(Context context, AttributeSet attributes, int defStyle) {
		super(context, attributes, defStyle);
		initMVYMapView(context);
	}
	
	/**
	 * Initializations. Called from all constructors
	 * @param context
	 */
	private void initMVYMapView(Context context) {
		mContext = context;
	}
	
	/** ------------------------ PUBLIC METHODS ------------------------ **/
	
	/**
	 * Add a POI marker to this map with a marker image, title, snnipet and a custom Object.
	 * Use this method and the MVYMapViewPoiDelegate to work with balloons
	 * @param point
	 * @param customMarker
	 * @param title
	 * @param snnipet
	 * @param customObject use this to store information associated to this POI, 
	 * 						returned through methods of MVYMapViewPoiDelegate
	 */
	public void addPoi(GeoPoint point, Drawable customMarker, String title, String snnipet, Object customObject) {
		
		if(itemizedOverlay == null) {
			// default marker for POIs
			Drawable defaultPoiMarker = getResources().getDrawable(MVYMapViewConfig.MarkerDefines.RES_ID_POI_DEFAULT);
			defaultPoiMarker.setBounds(0, 0, defaultPoiMarker.getIntrinsicWidth(), defaultPoiMarker.getIntrinsicHeight());
			
			// itemized overlay
			itemizedOverlay = new MVYItemizedOverlay(defaultPoiMarker, this);
			this.getOverlays().add(itemizedOverlay);
			
			// limits to zoom and center
			mapLimits = new MVYMapLimits();
		}
		
		MVYOverlayItem overlayItem = new MVYOverlayItem(point, customMarker, title, snnipet, customObject);
		itemizedOverlay.addOverlayItem(overlayItem);
		mapLimits.addGeoPoint(point);
	}
	
	/**
	 * Add a POI marker to this map with a marker image, title and snnipet 
	 * @param point
	 * @param customMarker
	 * @param title
	 * @param snnipet
	 */
	public void addPoi(GeoPoint point, Drawable customMarker, String title, String snnipet) {
		addPoi(point, customMarker, title, snnipet, null);
	}
	
	/**
	 * Add a POI marker to this map only with a marker image
	 * @param point
	 * @param customMarker
	 */
	public void addPoi(GeoPoint point, Drawable customMarker) {
		addPoi(point, customMarker, "", "", null);
	}
	
	/**
	 * To remove all POIs added with methods 'addPoi'
	 */
	public void cleanPois() {
		itemizedOverlay.cleanAllPOIs();
		mapLimits = new MVYMapLimits();
	}
	
	/**
	 * Enable or disable balloons on POIs
	 * @param enabled
	 */
	public void setPOIBalloonsEnabled(boolean enabled) {
		itemizedOverlay.setBalloonEnabled(enabled);
	}
	
	/**
	 * Move the center of the map to given point
	 * @param point
	 */
	public void setCenterInPoint(GeoPoint point) {
		this.getController().animateTo(point);
	}
	
	/**
	 * Change zoom of map
	 * @param zoomLevel
	 */
	public void setZoom(int zoomLevel){
		this.getController().setZoom(zoomLevel);
	}
	
	/**
	 * Zoom and center map in region with all POIs 
	 */
	public void setZoomWithAllPois() {
		mapLimits.applyLimitsToMap(this);
	}
	
	/**
	 * Show or hide the automatic system marker of user location
	 * @param enabled
	 */
	public void setLocationStandardEnabled(boolean enabled) {
		
		if (enabled == true) {
			
			if( myLocationOverlay == null ) {
				// create a standard location overlay
				myLocationOverlay = new MyLocationOverlay(mContext, this);
				this.getOverlays().add(myLocationOverlay);
			}
			myLocationOverlay.enableMyLocation();
		}
		else {

			if( myLocationOverlay != null ) {
				myLocationOverlay.disableMyLocation();
			}
		}	
	}
	
	/**
	 * Show or hide the marker of user position
	 * @param enabled
	 */
	public void setLocationCustomEnabled(boolean enabled) {
		
		if (enabled == true) {
			
			if (userGeoPoint == null) {
				listenToLocations();
			}
			else {
				addUserLocation();
			}
			locationCustomEnabled = true;
		}
		else {
			
			customLocationOverlay.cleanAllPOIs();
		}
	}
	
	/**
	 * Enables or disables location listener to auto-refresh custom user position
	 * @param enabled
	 */
	public void setLocationCustomAutoRefreshEnabled(boolean enabled) {
		
		if (enabled == true && locationCustomRefreshEnabled == false && locationListenerEnabled == false) {
			listenToLocations();
		}
		else if (enabled == false && (locationCustomRefreshEnabled == true || locationListenerEnabled == true)) {
			disableListenToLocations();
		}
		locationCustomRefreshEnabled = enabled;
	}
	
	/**
	 * Enables or disables location listener to notify MVYMapViewPoiDelegate
	 * @param enabled
	 */
	public void setLocationListenerEnabled(boolean enabled) {
		
		if (enabled == true && locationCustomRefreshEnabled == false && locationListenerEnabled == false) {
			listenToLocations();
		}
		else if (enabled == false && (locationCustomRefreshEnabled == true || locationListenerEnabled == true)) {
			disableListenToLocations();
		}
		locationListenerEnabled = enabled;
	}
	
	/** ------------------------ GETTERS & SETTERS ------------------------ **/
	
	/**
	 * Getter for POI delegate interface
	 * @return POI delegate
	 */
	public MVYMapViewPoiDelegate getPoiDelegate() {
		return poiDelegate;
	}
	
	/**
	 * Setter for POI delegate interface
	 * @param delegate
	 */
	public void setPoiDelegate(MVYMapViewPoiDelegate delegate) {
		poiDelegate = delegate;
	}

	/**
	 * Getter for location delegate interface
	 * @return location delegate
	 */
	public MVYMapViewLocationDelegate getLocationDelegate() {
		return locationDelegate;
	}

	/**
	 * Setter for location delegate interface.
	 * Enables location listener
	 * @param locationDelegate
	 */
	public void setLocationDelegate(MVYMapViewLocationDelegate locationDelegate) {
		this.locationDelegate = locationDelegate;
		setLocationListenerEnabled(true);
	}
	
	/**
	 * Getter for custom marker image of user position
	 * @return
	 */
	public Drawable getCustomUserMarker() {
		return customUserMarker;
	}

	/**
	 * Setter for custom marker image of user position
	 * @param customUserMarker
	 */
	public void setCustomUserMarker(Drawable customUserMarker) {
		this.customUserMarker = customUserMarker;
	}
	
	/** ------------------------ PRIVATE METHODS ------------------------ **/

	/**
	 * Method that register a listener to user location changes
	 */
	private void listenToLocations() {
		
		// acquire a reference to the system Location Manager
		if(locationManager == null) {
			locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		}

		// define a listener that responds to location updates
		if(locationListener == null) {
			locationListener = new MVYLocationListener();
		}

		// register the listener with the Location Manager to receive location updates
		try {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 
					MVYMapViewConfig.LocationListenerDefines.MIN_TIME_CHANGE, 
					MVYMapViewConfig.LocationListenerDefines.MIN_DISTANCE_CHANGE, 
					locationListener);
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 
					MVYMapViewConfig.LocationListenerDefines.MIN_TIME_CHANGE, 
					MVYMapViewConfig.LocationListenerDefines.MIN_DISTANCE_CHANGE, 
					locationListener);
		} 
		catch (Exception e) {
			Log.v("MVYMap", "MVYMapView location manager error: " + e);
		}
	}
	
	/**
	 * Method to remove the listener of location changes
	 */
	private void disableListenToLocations() {
		try {
			locationManager.removeUpdates(locationListener);
		}
		catch (Exception e) {
			Log.v("MVYMap", "MVYMapView location manager error: " + e);
		}
		
	}
	
	/**
	 * MVYLocationListener
	 * Class that receives location changes
	 * @author Pepe
	 *
	 */
	private class MVYLocationListener implements LocationListener{
		
		@Override
		public void onLocationChanged(Location location) {
			
			Log.v("MVYMap", "MVYMapView location changed: " + location);
			
			boolean firstLocation = (userGeoPoint == null);
			userGeoPoint = new GeoPoint( (int)(location.getLatitude() * 1E6), 
					(int)(location.getLongitude() * 1E6));
			
			if (locationListenerEnabled && locationDelegate != null) {
				// notify location delegate
				locationDelegate.MVYMapViewLocationChanged(userGeoPoint);
			}
			
			if (locationCustomRefreshEnabled || (firstLocation && locationCustomEnabled)) {
				// refresh custom marker of user location 
				addUserLocation();
			}
			
			if (locationCustomRefreshEnabled == false && locationListenerEnabled == false) {
				// remove listener
				disableListenToLocations();
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
			Log.v("MVYMap", "MVYMapView location provider disabled: " + provider);
		}

		@Override
		public void onProviderEnabled(String provider) {
			Log.v("MVYMap", "MVYMapView location provider enabled: " + provider);
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			Log.v("MVYMap", "MVYMapView location status changed: " + provider + " " + status);
		}
	}
	
	/**
	 * Adds POI with user geolocation
	 */
	private void addUserLocation() {
		
		if (customLocationOverlay == null) {
			// default marker
			Drawable defaultUserMarker = getResources().getDrawable(MVYMapViewConfig.MarkerDefines.RES_ID_USER_DEFAULT);
			defaultUserMarker.setBounds(0, 0, defaultUserMarker.getIntrinsicWidth(), defaultUserMarker.getIntrinsicHeight());
			
			// itemized overlay with balloon disabled
			customLocationOverlay = new MVYItemizedOverlay(defaultUserMarker, this);
			customLocationOverlay.setClickEnabled(false);
			
			// add custom location overlay
			if (itemizedOverlay != null) {
				// bring POIs overlay to front
				this.getOverlays().remove(itemizedOverlay);
				this.getOverlays().add(customLocationOverlay);
				this.getOverlays().add(itemizedOverlay);
			}
			else {
				this.getOverlays().add(customLocationOverlay);
			}
		}
		else {
			customLocationOverlay.cleanAllPOIs();
		}
		
		// add item to overlay
		MVYOverlayItem overlayItem = new MVYOverlayItem(userGeoPoint, customUserMarker, "User Position", "", null);
		customLocationOverlay.addOverlayItem(overlayItem);
	}
	
}
