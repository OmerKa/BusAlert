/**
 * 
 */
package travelcube.busalert.stops;

import java.util.ArrayList;

import travelcube.busalert.R;
import travelcube.busalert.model.BusStop;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author omer
 * 
 */
public class BusStopAdapter extends BaseAdapter {

    private ArrayList<BusStop> items = new ArrayList<BusStop>();
    private final Context      context;

    public BusStopAdapter(final Context context) {
        this.context = context;
        items.addAll(new ArrayList<BusStop>());
    }

    @Override
    public final int getCount() {
        return items.size();
    }

    @Override
    public final BusStop getItem(final int arg0) {
        return items.get(arg0);
    }

    @Override
    public final long getItemId(final int arg0) {
        return arg0;
    }

    @Override
    public final View getView(final int position, final View convertView,
            final ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.stop_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.name);
        textView.setText(getItem(position).getName());
        return rowView;
    }

    public final void addItems(final ArrayList<BusStop> itemList) {
        items = itemList;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView textView;
    }

}
