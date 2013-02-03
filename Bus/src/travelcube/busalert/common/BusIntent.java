/**
 * 
 */
package travelcube.busalert.common;

import travelcube.busalert.model.BusLine;
import travelcube.busalert.model.BusStop;
import android.content.Intent;
import android.location.Location;

/**
 * Extends the Intent class, for hiding the extra data.
 * 
 * @author omer
 * 
 */
public class BusIntent {

    /**
     * Save a location with it name.
     * 
     * @param location
     *            the location
     */
    public static Intent setDestinationPoint(Intent intent, BusStop location) {
        intent.putExtra("destinationLocation", location);
        return intent;
    }

    /**
     * gets the location that was save, if there is no location return 0.
     * 
     * @return a Location object
     */
    public static BusStop getDestinationPoint(Intent intent) {
        return (BusStop) intent.getParcelableExtra("destinationLocation");

    }

    public static Intent setCurrentLocation(Intent intent, Location location) {
        intent.putExtra("currentLocation", location);
        return intent;

    }

    public static Location getCurrentLocation(Intent intent) {
        return (Location) intent.getParcelableExtra("currentLocation");

    }

    public static Intent setProviderName(Intent intent, String provider) {
        intent.putExtra("provider", provider);
        return intent;
    }

    public static String getProviderName(Intent intent) {
        return intent.getStringExtra("provider");
    }

    public static Intent setBusLine(Intent intent, BusLine busLine) {
        intent.putExtra("busLine", busLine);
        return intent;
    }

    public static BusLine getBusLine(Intent intent) {
        return (BusLine) intent.getParcelableExtra("busLine");
    }

}
