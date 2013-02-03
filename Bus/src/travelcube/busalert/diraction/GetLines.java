package travelcube.busalert.diraction;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import travelcube.busalert.common.Client;
import travelcube.busalert.common.ICallBackWithError;
import travelcube.busalert.common.jsonDataList;
import travelcube.busalert.model.BusLine;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Handles the get lines query.
 * 
 * @author omer
 * 
 */
public class GetLines extends Client {

    /**
     * @param cb
     */
    public GetLines(final ICallBackWithError cb) {
        super(cb);
    }

    @Override
    protected final void onPostExecute(final String result) {
        jsonDataList<BusLine> res = null;
        try {
            Gson gson = new Gson();
            Type test = new TypeToken<jsonDataList<BusLine>>() {}.getType();
            res = gson.fromJson(result, test);
            _cb.callback(res);
        } catch (Exception ex) {
            _cb.onError(new RuntimeException(result));
        }
    }

    /**
     * Query the server for lines.
     * 
     * @param userBusNumber
     *            the bus number
     * @param location
     *            the current location
     * @param callback
     *            a callback object
     */
    public static void getLines(final String userBusNumber,
            final Location location, final ICallBackWithError callback) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("bus", userBusNumber));
        nameValuePairs.add(new BasicNameValuePair("lat", String
                .valueOf(location.getLatitude())));
        nameValuePairs.add(new BasicNameValuePair("lon", String
                .valueOf(location.getLongitude())));
        nameValuePairs.add(new BasicNameValuePair("acc", String
                .valueOf(location.getAccuracy())));
        nameValuePairs.add(new BasicNameValuePair("hour", String
                .valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))));
        nameValuePairs.add(new BasicNameValuePair("day", String
                .valueOf(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))));
        GetLines t = new GetLines(callback);
        t.execute(serverAddress + "/android/lines/", nameValuePairs);

    }
}
