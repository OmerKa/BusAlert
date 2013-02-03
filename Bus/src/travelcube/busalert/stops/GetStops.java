/**
 * 
 */
package travelcube.busalert.stops;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;

import travelcube.busalert.common.Client;
import travelcube.busalert.common.ICallBackWithError;
import travelcube.busalert.model.BusStop;

/**
 * @author omer
 * 
 */
public final class GetStops extends Client {

    /**
     * @param cb
     */
    private GetStops(final ICallBackWithError cb) {
        super(cb);
    }

    @Override
    protected void onPostExecute(final String result) {
        try {
            ArrayList<BusStop> stops = new ArrayList<BusStop>();
            BufferedReader reader = new BufferedReader(new StringReader(result));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                stops.add(new BusStop(rowData[3], rowData[0], Float
                        .valueOf(rowData[1]), Float.valueOf(rowData[2]), 0));
            }
            _cb.callback(stops);
        } catch (Exception ex) {
            _cb.onError(new RuntimeException(result));
        }
    }

    public static void getStops(final String stopFile,
            final ICallBackWithError callBack) {
        GetStops t = new GetStops(callBack);
        t.execute("http://stopfiles.s3-website-us-east-1.amazonaws.com/"
                + stopFile);
    }

}
