package travelcube.busalert.alert;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import travelcube.busalert.common.Client;
import travelcube.busalert.common.ICallBackWithError;
import android.location.Location;

/**
 * Handles the Alert log.
 * 
 * @author omer
 * 
 */
public class AlertLog extends Client {

    /**
     * @param cb
     */
    public AlertLog(final ICallBackWithError cb) {
        super(cb);
    }

    /**
     * Sends a Alert log massage to the server.
     * 
     * @param current
     *            the current location
     * @param dest
     *            the destination location
     */
    public static void alertLog(final Location current, final Location dest) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("current_lat", String
                .valueOf(current.getLatitude())));
        nameValuePairs.add(new BasicNameValuePair("current_lon", String
                .valueOf(current.getLongitude())));
        nameValuePairs.add(new BasicNameValuePair("current_acc", String
                .valueOf(current.getAccuracy())));
        nameValuePairs.add(new BasicNameValuePair("dest_lat", String
                .valueOf(dest.getLatitude())));
        nameValuePairs.add(new BasicNameValuePair("dest_lon", String
                .valueOf(dest.getLongitude())));
        nameValuePairs.add(new BasicNameValuePair("dest_acc", String
                .valueOf(dest.getAccuracy())));

        new AlertLog(null).execute(serverAddress + "/android/log/alert",
                nameValuePairs);
    }
}
