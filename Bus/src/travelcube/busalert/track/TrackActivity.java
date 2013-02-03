package travelcube.busalert.track;

import java.util.List;
import java.util.Locale;

import travelcube.busalert.R;
import travelcube.busalert.common.BusIntent;
import travelcube.busalert.stops.FindStop;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * @author omer
 * 
 *         Show a map with the current location and the destination location,
 *         uses the passive provider for the location
 * 
 */
public class TrackActivity extends MapActivity {

    private static final int DESTIANION_LOCATION_COLOR = 0x15FF0000;
    private static final int CURRENT_LOCATION_COLOR    = 0x154D2EFF;
    private LocationManager  locationManager;
    private Intent           serviceIntent;
    private LocationListener locationListener;
    private MapView          mapView;
    private String           provider;

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        mapView = (MapView) findViewById(R.id.mapview);

        locationListener = new LocationListener() {

            @Override
            public void onStatusChanged(final String provider,
                    final int status, final Bundle extras) {

            }

            @Override
            public void onProviderEnabled(final String provider) {

            }

            @Override
            public void onProviderDisabled(final String provider) {
                alert();
            }

            @Override
            public void onLocationChanged(final Location location) {
                showAndFocusCurrentLocationOnMap(location);

            }
        };

        locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);

        Intent intent = getIntent();

        startTrackingService(intent);
        provider = BusIntent.getProviderName(intent);

        initializeMap(BusIntent.getDestinationPoint(intent));
    }

    private void alert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("אנא אפשר את הגדרות המיקום")
                .setTitle("מיקום")
                .setCancelable(false)
                .setPositiveButton("הפעל כעת",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                    final int id) {
                                startActivity(new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton("בטל",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                    final int id) {
                                dialog.cancel();
                            }
                        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * initialize the map view.
     * 
     * @param location
     *            a location to show on the map as the destination point
     * @param string
     */
    private void initializeMap(final Location location) {
        Locale.setDefault(new Locale("iw", "IL")); // sets the map to be in
                                                   // Hebrew
        mapView.setBuiltInZoomControls(true);

        locationManager
                .requestLocationUpdates(provider, 0, 0, locationListener);

        mapView.getOverlays().add(
                getMapOverlayFromLocation(location, R.drawable.busstop,
                        DESTIANION_LOCATION_COLOR));
    }

    /**
     * create a MapOveraly from a location.
     * 
     * @param location
     *            the location to drew on the map
     * @param drawable
     *            an icon to use
     * @param color
     *            the color of the circle of the accuracy
     * @return a MapOvlay object of the location
     */
    private MapOverlay getMapOverlayFromLocation(final Location location,
            final int drawable, final int color) {
        GeoPoint p = new GeoPoint((int) (location.getLatitude() * 1E6),
                (int) (location.getLongitude() * 1E6));

        return new MapOverlay(p, location.getAccuracy(), this.getResources()
                .getDrawable(drawable), color);
    }

    /**
     * add the location to the map and focus the map on it, if there is an old
     * location on the map, remove it first.
     * 
     * @param location
     *            the current location
     */
    private void showAndFocusCurrentLocationOnMap(final Location location) {
        List<Overlay> mapOverlays = mapView.getOverlays();
        if (mapOverlays.size() == 2) {
            mapOverlays.remove(1);
        }

        MapOverlay mapOverlay = getMapOverlayFromLocation(location,
                R.drawable.bus1, CURRENT_LOCATION_COLOR);
        mapOverlays.add(mapOverlay);

        focusMapOnOverlay(mapOverlay.getPoint());
    }

    /**
     * focus the MapView on a location.
     * 
     * @param geoPoint
     *            the location
     */
    private void focusMapOnOverlay(final GeoPoint geoPoint) {
        MapController mc = mapView.getController();
        mc.animateTo(geoPoint);
        mc.setZoom(14);
        mapView.invalidate();
    }

    /**
     * Start the tracking service with the extra data from the intent.
     * 
     * @param intent
     *            the intent
     */
    private void startTrackingService(final Intent intent) {
        serviceIntent = new Intent(this, TrackingService.class);
        serviceIntent.putExtras(intent.getExtras());
        startService(serviceIntent);
    }

    @Override
    protected final void onPause() {
        locationManager.removeUpdates(locationListener);
        super.onPause();
    }

    @Override
    protected final void onResume() {
        locationManager
                .requestLocationUpdates(provider, 0, 0, locationListener);
        super.onResume();
    }

    /**
     * @param view
     */
    public final void helpClick(final View view) {
        new AlertDialog.Builder(this).setTitle(R.string.help_title)
                .setMessage(R.string.help_maps)
                .setPositiveButton(R.string.help_ok, null).show();
    }

    /**
     * @param view
     *            Handle click button event, close the service
     */
    public final void sendClick(final View view) {
        stopService(serviceIntent);
    }

    /**
     * @param view
     *            Handle click button, close the service and go back to the
     *            findStopActivity
     */
    public final void changeClick(final View view) {
        stopService(serviceIntent);
        Intent i = new Intent(this, FindStop.class);
        i.putExtras(getIntent().getExtras());
        startActivity(i);
        finish();
    }

    @Override
    public final void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected final boolean isRouteDisplayed() {
        return false;
    }
}
