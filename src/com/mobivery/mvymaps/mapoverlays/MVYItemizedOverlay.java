package com.mobivery.mvymaps.mapoverlays;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.google.android.maps.MapView;
import com.mobivery.mvymaps.MVYMapView;
import com.mobivery.mvymaps.MVYMapViewConfig;
import com.mobivery.mvymaps.MVYMapViewConfig.POIDefines.POIBalloonMode;
import com.mobivery.mvymaps.MVYMapViewConfig.POIDefines.POIBounds;
import com.mobivery.mvymaps.MVYMapViewPoiDelegate;
import com.mobivery.mvymaps.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;

/**
 * MVYItemizedOverlay
 * Class needed to paint a layer with POIs in map.
 * @author Pepe
 *
 */
public class MVYItemizedOverlay extends BalloonItemizedOverlay<MVYOverlayItem>{
	
	private ArrayList<MVYOverlayItem> overlayItemsArray = new ArrayList<MVYOverlayItem>();
	private int selectedPOI = -1;
	private MVYMapView mMapView;
	private MVYMapViewPoiDelegate poiDelegate;
	private boolean balloonEnabled = true;
	private boolean clickEnabled = true;

	/**
	 * Constructor. Needs a default image for POIs and the MVYMapView.
	 * @param defaultMarker
	 * @param mapView
	 */
	public MVYItemizedOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		
		// poi delegate for open/close events
		if( mapView != null ){
			mMapView = (MVYMapView) mapView;
			poiDelegate = mMapView.getPoiDelegate();
		}
		
		// balloon config
		setShowDisclosure(false);
		setShowClose(false);
		if (MVYMapViewConfig.POIDefines.BALLOON_MODE == POIBalloonMode.DISCLOSURE) {
			setShowDisclosure(true);
		}
		else if (MVYMapViewConfig.POIDefines.BALLOON_MODE == POIBalloonMode.CLOSE) {
			setShowClose(true);
		}
		setBalloonBottomOffset(MVYMapViewConfig.POIDefines.BALLOON_BOTTOM_OFFSET);
		setBalloonLeftOffset(MVYMapViewConfig.POIDefines.BALLOON_LEFT_OFFSET);
	}

	/**
	 * To obtain the overlayItem at index i
	 */
	@Override
	protected MVYOverlayItem createItem(int i) {
		
		MVYOverlayItem item = overlayItemsArray.get(i);
		
		// adjust custom marker bounds
		if (item.getCustomMarker() != null) {
			if (MVYMapViewConfig.POIDefines.BOUNDS == POIBounds.CENTER_BOTTOM) {
				item.setMarker(boundCenterBottom(item.getCustomMarker()));
			}
			else if(MVYMapViewConfig.POIDefines.BOUNDS == POIBounds.CENTER) {
				item.setMarker(boundCenter(item.getCustomMarker()));
			}
		}
		
		return overlayItemsArray.get(i);
	}

	/**
	 * To obtain the overlayItems count
	 */
	@Override
	public int size() {
		return overlayItemsArray.size();
	}
	
	/**
	 * To add functionality on map drawing
	 */
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// to hide automatic shadow
		shadow = false;
		super.draw(canvas, mapView, shadow);
	}
	
	/**
	 * To reorder the POIs to draw
	 */
	@Override
	protected int getIndexToDraw(int drawingOrder) {
		
		if( selectedPOI == -1 || overlayItemsArray.size() == 0 ){
			return super.getIndexToDraw(drawingOrder);
		}
		
		// algorithm to bring to front the POI selected 	
		if( drawingOrder == (overlayItemsArray.size()-1) ){
			return selectedPOI;
		}
		else if ( drawingOrder < selectedPOI ){
			return drawingOrder;
		}
		else if ( drawingOrder >= selectedPOI ){
			return drawingOrder + 1;
		}
		return 0;
	}
	
	/**
	 * To add one new POI
	 * @param item
	 */
	public void addOverlayItem( MVYOverlayItem item ) {
		overlayItemsArray.add(item);
		this.populate();
	}
	
	/**
	 * Actions to do when user selects a POI and balloon opens
	 * @param index
	 */
	@Override
	protected void onBalloonOpen(int index) {

		// re-populate overlay when user selects POI
		// to bring marker view to front
		selectedPOI = index;
		populate();
		
		// to hide balloons when user touches the map
		if( mMapView != null ){
			
			mMapView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					hideBalloon();
					return false;
				}
			});
		}
	}

	/**
	 * Actions to do when user clicks on balloon
	 */
	@Override
	protected boolean onBalloonTap(int index, MVYOverlayItem item) {

		// pass the event to delegate from map view
		if(poiDelegate != null) {
			poiDelegate.MVYMapViewPoiOpenClicked(item.getCustomObject());
		}
		return true;
	}
	
	/**
	 * Actions to do when user clicks on POI
	 */
	public final boolean onTap(int index) {
		
		if (clickEnabled == false) {
			return false;
		}
		if (balloonEnabled == false) {
			
			// pass open event to 
			MVYOverlayItem item = overlayItemsArray.get(index);
			if (item != null && poiDelegate != null) {
				poiDelegate.MVYMapViewPoiOpenClicked(item.getCustomObject());
			}
			return false;
		}
		return super.onTap(index);
	}
	
	/**
	 * Actions to do when balloon disappears
	 */
	@Override
	public void hideBalloon() {
		
		if (balloonView != null) {
			
			// pass close event to POI delegate
			MVYOverlayItem item = (MVYOverlayItem) currentFocusedItem;
			if (item != null && poiDelegate != null) {
//				poiDelegate.MVYMapViewPoiCloseClicked(item.getCustomObject());
			}
			
		}
		super.hideBalloon();
	}
	
	/**
	 * To clean the overlay removing all POIs
	 */
	public void cleanAllPOIs() {
		overlayItemsArray = new ArrayList<MVYOverlayItem>();
		selectedPOI = -1;
		hideBalloon();
		populate();
	}
	
	public boolean isBalloonEnabled() {
		return balloonEnabled;
	}

	public void setBalloonEnabled(boolean enabled) {
		this.balloonEnabled = enabled;
	}

	public boolean isClickEnabled() {
		return clickEnabled;
	}

	public void setClickEnabled(boolean clickEnabled) {
		this.clickEnabled = clickEnabled;
	}

}
