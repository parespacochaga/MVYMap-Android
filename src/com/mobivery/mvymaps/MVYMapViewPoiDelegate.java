package com.mobivery.mvymaps;

/**
 * MVYMapViewPoiDelegate
 * Implement this interface to receive events from POIs 
 * @author Pepe
 *
 */
public interface MVYMapViewPoiDelegate {
	
	/**
	 * To implement actions when user clicks balloon
	 * @param customObject
	 */
	public void MVYMapViewPoiOpenClicked(Object customObject);

	/**
	 * To implement actions when user hides balloon
	 * @param customObject
	 */
	// TODO if you want this method, uncomment the next line, and the line #201 in MVYItemizedOverlay
	// public void MVYMapViewPoiCloseClicked(Object customObject);
	
}
