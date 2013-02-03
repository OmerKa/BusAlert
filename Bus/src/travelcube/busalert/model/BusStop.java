package travelcube.busalert.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

public class BusStop extends Location {

    private String name;
    int            order;
    public String  id;

    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public final void setName(final String name) {
        this.name = name;
    }

    public BusStop(final String name, final String id, final Float lat,
            final Float lon, final int order) {
        super("server");
        setLatitude(lat);
        setLongitude(lon);
        this.name = name;
        this.id = id;
        this.order = order;
    }

    public BusStop(final String name, final Location location) {
        super(location);
        this.name = name;
    }

    @Override
    public final void writeToParcel(final Parcel parcel, final int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeString(name);
    }

    public static final Parcelable.Creator<BusStop> CREATOR = new Parcelable.Creator<BusStop>() {
                                                                @Override
                                                                public BusStop createFromParcel(
                                                                        final Parcel in) {
                                                                    Location location = Location.CREATOR
                                                                            .createFromParcel(in);
                                                                    String name = in
                                                                            .readString();
                                                                    return new BusStop(
                                                                            name,
                                                                            location);
                                                                }

                                                                @Override
                                                                public BusStop[] newArray(
                                                                        final int size) {
                                                                    return new BusStop[size];
                                                                }
                                                            };

}
