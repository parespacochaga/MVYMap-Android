package com.mobivery.mvymaps;

import com.mobivery.mvymaps.MVYMapViewConfig.POIDefines.POIBalloonMode;
import com.mobivery.mvymaps.MVYMapViewConfig.POIDefines.POIBounds;

/**
 * MVYMapViewConfig
 * Configuration values for MVYMapView
 * @author Pepe
 *
 */
public class MVYMapViewConfig {
	
	public static class POIDefines {
		
		public static final POIBounds BOUNDS = POIBounds.CENTER_BOTTOM;
		public static final POIBalloonMode BALLOON_MODE = POIBalloonMode.DISCLOSURE;
		public static final int BALLOON_BOTTOM_OFFSET = 25;
		public static final int BALLOON_LEFT_OFFSET = 0; //DEVELOPING
		
		public enum POIBounds {
		    CENTER, 
		    CENTER_BOTTOM,
		    NULL
		}
		
		public enum POIBalloonMode {
			DISCLOSURE,
			CLOSE,
			NULL
		}
	}

	public static class LocationListenerDefines {
		
		public static final long MIN_TIME_CHANGE = 10;
		public static final float MIN_DISTANCE_CHANGE = 100;
	}
	
	public static class MarkerDefines {
		
		public static final int RES_ID_POI_DEFAULT = android.R.drawable.ic_menu_compass;
		public static final int RES_ID_USER_DEFAULT = android.R.drawable.ic_menu_mylocation;
	}
	
	public static class ZoomDefines {
		
		public static final int MAP_LIMITS_DEFAULT_ZOOM = 14;
	}

}
