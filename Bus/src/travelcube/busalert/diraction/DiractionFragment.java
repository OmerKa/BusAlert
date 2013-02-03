package travelcube.busalert.diraction;

import travelcube.busalert.R;
import travelcube.busalert.common.BusIntent;
import travelcube.busalert.common.ICallBackWithError;
import travelcube.busalert.common.jsonDataList;
import travelcube.busalert.model.BusLine;
import travelcube.busalert.stops.FindStop;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Fragment let the user to chose the direction of the bus. after the setData()
 * is called, the server is been query for lines, and then the list is shown for
 * the user to pick one line.
 * 
 * @author Omer
 */
public class DiractionFragment extends Fragment {

    private Activity       context;
    private BusLineAdapter adapter;
    private ListView       listview;

    @Override
    public final View onCreateView(final LayoutInflater inflater,
            final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diraction, container,
                false);
        context = getActivity();
        return view;
    }

    @Override
    public final void onActivityCreated(final Bundle savedInstanceState) {

        ImageButton button = (ImageButton) findViewById(R.id.help);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                helpClick(v);
            }
        });

        initListView();

        super.onActivityCreated(savedInstanceState);
    };

    /**
     * wrapper so the syntax in activity and in the fragment will by the same.
     * 
     * @param id
     *            the id of the view
     * 
     * @return the View object
     */
    private View findViewById(final int id) {
        return getView().findViewById(id);
    }

    /**
     * Initialize the list view.
     */
    private void initListView() {
        listview = (ListView) findViewById(R.id.dircetionsList);
        adapter = new BusLineAdapter(context);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> arg0, final View arg1,
                    final int index, final long arg3) {
                startFindStopActivity((BusLine) listview
                        .getItemAtPosition(index));
            }

            /**
             * Start FindStop Activity.
             * 
             * @param line
             *            the requested line
             */
            private void startFindStopActivity(BusLine line) {
                Intent i = new Intent(context, FindStop.class);
                i = BusIntent.setBusLine(i, line);
                startActivity(i);
            }
        });
    }

    /**
     * Set the data so the fragment can start working.
     * 
     * @param busNumber
     *            the bus number
     * @param location
     *            the current location
     */
    public final void setData(final String busNumber, final Location location) {
        final View lineProgressBar = findViewById(R.id.dircetionProgressBar);
        lineProgressBar.setVisibility(View.VISIBLE);

        // Query the server
        GetLines.getLines(busNumber, location, new ICallBackWithError() {
            @SuppressWarnings("unchecked")
            @Override
            public void callback(final Object... arg0) {
                processDataToView((jsonDataList<BusLine>) arg0[0]);
                lineProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(final Exception ex) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Error: " + (ex.getMessage()))
                        .setCancelable(true).setPositiveButton("ok", null);
                AlertDialog alert = builder.create();
                alert.show();
                lineProgressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * adds the items in lines to the ListView.
     * 
     * @param lines
     *            list of lines to show in UI
     */
    private void processDataToView(final jsonDataList<BusLine> lines) {
        if (lines.getData().isEmpty()) {
            findViewById(R.id.noLineLayout).setVisibility(View.VISIBLE);
        } else {
            ((TextView) findViewById(R.id.choosedirectionText))
                    .setVisibility(View.VISIBLE);
            adapter.addItems(lines);
            findViewById(R.id.noLineLayout).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * @param view
     */
    public final void helpClick(final View view) {
        new AlertDialog.Builder(context).setTitle(R.string.help_title)
                .setMessage(R.string.help_no_lines)
                .setPositiveButton(R.string.help_ok, null).show();
    }

}
