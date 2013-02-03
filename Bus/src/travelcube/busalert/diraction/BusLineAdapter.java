/**
 * 
 */
package travelcube.busalert.diraction;

import java.util.ArrayList;

import travelcube.busalert.R;
import travelcube.busalert.common.jsonDataList;
import travelcube.busalert.model.BusLine;
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
public class BusLineAdapter extends BaseAdapter {

    private jsonDataList<BusLine> items = new jsonDataList<BusLine>();
    private final Context         context;

    /**
	 * 
	 */
    public BusLineAdapter(Context context) {
        this.context = context;
        items.setData(new ArrayList<BusLine>());
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return items.getData().size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public BusLine getItem(int arg0) {
        return items.getData().get(arg0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.line_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.lastStop);
        textView.setText(getItem(position).getLastStop());
        return rowView;
    }

    public void addItems(jsonDataList<BusLine> itemList) {
        items = itemList;
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView textView;
    }

}
