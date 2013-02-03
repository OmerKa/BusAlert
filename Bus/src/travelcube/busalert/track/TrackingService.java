package travelcube.busalert.track;

import travelcube.busalert.R;
import travelcube.busalert.alert.AlertActivity;
import travelcube.busalert.common.BusIntent;
import travelcube.busalert.common.ICallBackWithError;
import travelcube.busalert.model.BusStop;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class TrackingService extends Service implements LocationListener {

    private BusStop         dest;
    private LocationManager locationManager;
    private Intent          intent;

    @Override
    public final IBinder onBind(final Intent arg0) {
        return null;
    }

    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        Log.w("Bus", "onStartCommand");

        this.intent = intent;
        dest = BusIntent.getDestinationPoint(intent);

        locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(
                BusIntent.getProviderName(intent), 0, 0, this);

        new ICallBackWithError() {

            @Override
            public void onError(final Exception ex) {
                // TODO Auto-generated method stub

            }

            @Override
            public void callback(final Object... arg0) {
                startActivity((Location) arg0[0]);
                stopSelf();
            }
        };

        serviceNotification(intent.getExtras(), dest.getName());

        return START_REDELIVER_INTENT;
    }

    /**
     * @param extraData
     * @param pointName
     * 
     */
    private void serviceNotification(final Bundle extraData,
            final String pointName) {
        Notification notification = new Notification(R.drawable.bus_icon,
                "bus", System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, TrackActivity.class);
        notificationIntent.putExtras(extraData);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(this, "bus", pointName, pendingIntent);
        startForeground(1, notification);
    }

    private void startActivity(final Location current) {
        Log.w("Bus", "StartActivity");
        Intent i = new Intent();
        i.setClass(this, AlertActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtras(intent.getExtras());
        i = BusIntent.setCurrentLocation(i, current);
        startActivity(i);
    }

    @Override
    public final void onDestroy() {
        Log.w("Bus", "serviceOnDestroy");
        locationManager.removeUpdates(this);
        super.onDestroy();

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.distanceTo(dest) - location.getAccuracy() <= dest
                .getAccuracy()) {
            locationManager.removeUpdates(this);
            startActivity(location);
            stopSelf();
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}
