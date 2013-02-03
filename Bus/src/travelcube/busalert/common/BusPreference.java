package travelcube.busalert.common;

import travelcube.busalert.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class BusPreference extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preference);
    }

}
