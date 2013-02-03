package travelcube.busalert.lines;

import travelcube.busalert.common.ICallBackWithError;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationHandler {

    private final LocationManager    locationManager;
    private Location                 current;
    private final LocationListener   locationListener;
    private final ICallBackWithError call;

    public LocationHandler(final Context context,
            final ICallBackWithError callBack) {
        call = callBack;
        locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                // Called when a new location is found by the network location
                // provider.
                if (current == null) {
                    current = location;
                    call.callback(current);
                } else if (isBetterLocation(location)) {
                    current = location;
                    call.callback(current);
                }
            }

            @Override
            public void onProviderDisabled(final String arg0) {
                call.onError(new Exception(arg0 + " provider is Disable"));

            }

            @Override
            public void onProviderEnabled(final String arg0) {

            }

            @Override
            public void onStatusChanged(final String arg0, final int arg1,
                    final Bundle arg2) {

            }
        };
    }

    public final void start() {
        current = null;
        locationManager.removeUpdates(locationListener);
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    private boolean isBetterLocation(final Location newLocation) {
        if (newLocation.distanceTo(current) != 0) { return true; }
        if (newLocation.getAccuracy() < current.getAccuracy()) { return true; }
        return false;
    }

    public final Location getCurrentLocation() {
        locationManager.removeUpdates(locationListener);
        return current;

    }
}
