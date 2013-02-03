package travelcube.busalert.alert;

import travelcube.busalert.R;
import travelcube.busalert.common.ActivityBase;
import travelcube.busalert.common.BusIntent;
import travelcube.busalert.model.BusStop;
import travelcube.busalert.track.TrackActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Alert the user when he is approaching the station start sound and vibrate
 * until the user click on a button.
 * 
 * @author omer
 */
public class AlertActivity extends ActivityBase {

    private final MediaPlayer mp = new MediaPlayer();
    private VibrateThread     vibrateThread;
    private BusStop           dest;
    private boolean           isAlertOn;
    private Location          current;

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_alert, menu);
        return true;
    }

    @Override
    protected void onNewIntent(final Intent intent) {

    }

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowFlags();
        setContentView(R.layout.activity_alert);

        Intent intent = getIntent();
        dest = BusIntent.getDestinationPoint(intent);
        current = BusIntent.getCurrentLocation(intent);

        ((TextView) findViewById(R.id.textView2)).setText(dest.getName());
        if (BusIntent.getProviderName(intent).endsWith(
                LocationManager.GPS_PROVIDER))
            findViewById(R.id.startGPSButton).setVisibility(View.GONE);

        AlertLog.alertLog(dest, current);

        startAlert();
    }

    /**
     */
    private void setWindowFlags() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    /**
     * start the sound and vibrate.
     */
    private void startAlert() {
        isAlertOn = true;
        startSound();
        startvibrate();
    }

    /**
     * Start sound on loop.
     */
    private void startSound() {
        Uri uri = getSoundPath();

        try {
            mp.setDataSource(this, uri);
            mp.setAudioStreamType(AudioManager.STREAM_RING);
            mp.setLooping(true);
            mp.prepare();
            mp.start();
            Log.w("Bus", "PlaySound");

        } catch (Exception e) {
            Log.w("Bus", "PlaySoundException");
        }
    }

    private void startvibrate() {
        vibrateThread = new VibrateThread();
        vibrateThread.start();
    }

    /**
     * extract the sound from settings. If the user didn't set sound, return
     * Default sound
     * 
     * @return a uri for the alert sound
     */
    private Uri getSoundPath() {
        final String defaultSoundPath = RingtoneManager
                .getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM)
                .toString();

        return Uri.parse(sharedPref.getString("ringtone", defaultSoundPath));
    }

    @Override
    public final void onBackPressed() {
        if (!isAlertOn) {
            stop();
            moveTaskToBack(true);
            finish();
        }
    }

    /**
     * Stop the sound and vibrate.
     */
    private void stop() {
        Log.w("Bus", "stop");
        isAlertOn = false;
        mp.stop();
        vibrateThread.interrupt();
    }

    /**
     * Called from UI.
     * 
     * @param view
     *            view
     */
    public final void stopClick(final View view) {
        stop();
    }

    /**
     * Called from UI.
     * 
     * @param view
     *            view
     */
    public final void startGpsClick(final View view) {
        stop();
        startGPSTracking();
        finish();
    }

    /**
     * Start the Tracking Activity with GPS provider.
     */
    private void startGPSTracking() {
        Intent i = new Intent(AlertActivity.this, TrackActivity.class);
        dest.setAccuracy(Float.valueOf(sharedPref.getString("gps", "0")));
        i.putExtras(getIntent().getExtras());
        i = BusIntent.setDestinationPoint(i, dest);
        i = BusIntent.setProviderName(i, LocationManager.GPS_PROVIDER);
        startActivity(i);
    }

    /**
     * a new thread for the vibrate, so it will not stack the UI.
     * 
     * @author omer
     * 
     */
    class VibrateThread extends Thread {

        @Override
        public void run() {
            Log.w("Bus", "VibrateThread");
            int i = 0;
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            while (i < 1000 * 60) {
                i = i + 100;
                v.vibrate(100);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    i = 1000 * 60;
                    Log.w("Bus", "VibrateThreadInterruptedException");
                }
            }
        }
    }
}
