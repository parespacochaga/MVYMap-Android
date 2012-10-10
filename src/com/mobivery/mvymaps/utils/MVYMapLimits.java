package com.mobivery.mvymaps.utils;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

/**
 * MVYMapLimits
 * Class to center and zoom a map to region that includes 
 * all geopoints added
 * @author Pepe
 *
 */
public class MVYMapLimits {
	
	private long latMax;
	private long latMin;
	private long lonMax;
	private long lonMin;
	private int pointsCount;
	private static final int MAP_LIMITS_DEFAULT_ZOOM = 14;
	
	public MVYMapLimits() {
		
		latMax = -1000000000;
		latMin = 1000000000;
		lonMax = -1000000000;
		lonMin = 1000000000;
		pointsCount = 0;
	}
	
	public void addGeoPoint(GeoPoint point) {
		
		if (point.getLatitudeE6() > latMax)
			latMax = point.getLatitudeE6();
		if (point.getLatitudeE6() < latMin)
			latMin = point.getLatitudeE6();
		if (point.getLongitudeE6() > lonMax)
			lonMax = point.getLongitudeE6();
		if (point.getLongitudeE6() < lonMin)
			lonMin = point.getLongitudeE6();
		pointsCount++;
	}
	
	public void applyLimitsToMap(MapView mapView) {
		
		if (pointsCount < 2) {
			mapView.getController().setZoom(MAP_LIMITS_DEFAULT_ZOOM);
		} else {
			mapView.getController().zoomToSpan((int) ((latMax - latMin) * 1.1),(int) ((lonMax - lonMin) * 1.1));
		}
		mapView.getController().animateTo(getCenter());
	}
	
	public GeoPoint getCenter() {
		
		double centerLatitude = (latMax + latMin) / 2;
		double centerLongitude = (lonMax + lonMin) / 2;
		return new GeoPoint((int) centerLatitude, (int) centerLongitude);
	}

	public long getLatMax() {
		return latMax;
	}

	public long getLatMin() {
		return latMin;
	}

	public long getLonMax() {
		return lonMax;
	}

	public long getLonMin() {
		return lonMin;
	}

	public int getPointsCount() {
		return pointsCount;
	}

	public static int getMapLimitsDefaultZoom() {
		return MAP_LIMITS_DEFAULT_ZOOM;
	}

}
