package travelcube.busalert.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author omer
 * 
 *         Represent single bus line.
 * 
 */
public class BusLine implements Parcelable {

    public BusLine(final String lastStop, final String id) {
        this.lastStop = lastStop;
        this.id = id;
    }

    private String lastStop;
    private String id;

    /**
     * @return the lastStop
     */
    public final String getLastStop() {
        return lastStop;
    }

    /**
     * @param lastStop
     *            the lastStop to set
     */
    public final void setLastStop(final String lastStop) {
        this.lastStop = lastStop;
    }

    /**
     * @return the id
     */
    public final String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public final void setId(final String id) {
        this.id = id;
    }

    @Override
    public final int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(lastStop);
        dest.writeString(id);
    }

    public static final Parcelable.Creator<BusLine> CREATOR = new Parcelable.Creator<BusLine>() {
                                                                @Override
                                                                public BusLine createFromParcel(
                                                                        final Parcel in) {
                                                                    String lastStation = in
                                                                            .readString();
                                                                    String id = in
                                                                            .readString();
                                                                    return new BusLine(
                                                                            lastStation,
                                                                            id);
                                                                }

                                                                @Override
                                                                public BusLine[] newArray(
                                                                        final int size) {
                                                                    return new BusLine[size];
                                                                }
                                                            };

}
