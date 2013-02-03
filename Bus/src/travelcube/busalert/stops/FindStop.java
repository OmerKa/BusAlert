package travelcube.busalert.stops;

import java.util.ArrayList;
import java.util.List;

import travelcube.busalert.R;
import travelcube.busalert.common.ActivityBase;
import travelcube.busalert.common.BusIntent;
import travelcube.busalert.common.ICallBackWithError;
import travelcube.busalert.model.BusLine;
import travelcube.busalert.model.BusStop;
import travelcube.busalert.track.TrackActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * @author omer
 * 
 *         Activity for selecting the bus stop for the alarm.
 */
public class FindStop extends ActivityBase {
    private BusStopAdapter adapter;
    private ListView       listview;
    private Boolean        isLoading;

    /**
     * @see travelcube.busalert.common.ActivityBase#onCreate(android.os.Bundle)
     * @param savedInstanceState
     *            need to have a string "stopFile"
     **/
    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_stop);

        adapter = new BusStopAdapter(this);
        listview = (ListView) findViewById(R.id.StopList);
        listview.setAdapter(adapter);

        BusLine busLine = BusIntent.getBusLine(getIntent());

        setStopList(busLine.getId());
        setListItemClick();

    }

    private void setListItemClick() {

        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> arg0, final View arg1,
                    final int arg2, final long arg3) {
                BusStop selected = (BusStop) listview.getItemAtPosition(arg2);
                Intent i = new Intent(FindStop.this, TrackActivity.class);
                i.putExtras(getIntent().getExtras());
                selected.setAccuracy(Float.valueOf(sharedPref.getString(
                        "network", "0")));
                i = BusIntent.setDestinationPoint(i, selected);
                i = BusIntent.setProviderName(i,
                        LocationManager.NETWORK_PROVIDER);
                startActivity(i);
            }
        });
    }

    /**
     * get the station list form the server and display it.
     */
    private void setStopList(final String stopFile) {
        setIsLoading(true);
        GetStops.getStops(stopFile, new ICallBackWithError() {

            @Override
            public void onError(final Exception ex) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        FindStop.this);
                builder.setMessage("Error: " + ex.getMessage())
                        .setCancelable(true).setPositiveButton("ok", null);
                AlertDialog alert = builder.create();
                alert.show();
                setIsLoading(false);
            }

            @Override
            public void callback(final Object... arg0) {
                @SuppressWarnings("unchecked")
                ArrayList<BusStop> data = (ArrayList<BusStop>) arg0[0];
                adapter.addItems(data);
                listview.setSelection(data.indexOf(getClosest(data)));
                setIsLoading(false);
            }
        });
    }

    private BusStop getClosest(final List<BusStop> data) {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        Location loc = locationManager
                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        BusStop closest = data.get(0);
        double closestDis = Math.sqrt(Math.pow(
                loc.getLatitude() - closest.getLatitude(), 2)
                + Math.pow(closest.getLongitude() - loc.getLongitude(), 2));
        for (BusStop stop : data) {
            double dis = Math.sqrt(Math.pow(
                    loc.getLatitude() - stop.getLatitude(), 2)
                    + Math.pow(stop.getLongitude() - loc.getLongitude(), 2));
            if (dis < closestDis) {
                closestDis = dis;
                closest = stop;
            }
        }
        return closest;
    }

    /**
     * @return the isLoading
     */
    public final Boolean getIsLoading() {
        return isLoading;
    }

    /**
     * @param isLoading
     *            the isLoading to set
     */
    public final void setIsLoading(final Boolean isLoading) {
        this.isLoading = isLoading;
        ProgressBar bar = (ProgressBar) findViewById(R.id.ProgressBar);
        if (isLoading) {
            bar.setVisibility(View.VISIBLE);
        } else {
            bar.setVisibility(View.GONE);
        }
    }
}
