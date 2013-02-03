package travelcube.busalert.lines;

import travelcube.busalert.R;
import travelcube.busalert.common.ActivityBase;
import travelcube.busalert.common.ICallBackWithError;
import travelcube.busalert.diraction.DiractionFragment;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author omer
 * 
 *         Activity for selecting the bus. The main activity let the user to
 *         insert the bus number, and the fragment let the user choose the
 *         direction.
 * 
 */
public class FindBusActivity extends ActivityBase {

    private Object          lockObject;
    private LocationHandler locationHandler;
    private EditText        editText;
    private TextView        locationWait;
    private Boolean         isFixedLocation;
    private Keyboard        mKeyboard;
    private KeyboardView    mKeyboardView;

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_bus);

        editText = (EditText) findViewById(R.id.userBusNumber);

        initKeyboard();

        lockObject = new Object();

        locationHandler = new LocationHandler(this, new ICallBackWithError() {

            @Override
            public void onError(Exception ex) {
                locationDialog();

            }

            @Override
            public void callback(Object... arg0) {
                synchronized (lockObject) {
                    try {
                        lockObject.notify();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                isFixedLocation = true;
                locationWait.setVisibility(View.GONE);

            }
        });
        getLocation();
    }

    /**
     * Open a dialog for the user, so he can enable the network location.
     */
    public void locationDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("אנא אפשר את הגדרות המיקום")
                .setTitle("מיקום")
                .setCancelable(false)
                .setPositiveButton("הפעל כעת",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                    final int id) {
                                startActivity(new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton("בטל",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog,
                                    final int id) {
                                dialog.cancel();
                            }
                        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Start {@link LocationHandler}, when there is a fix location notifies the
     * lockObject, and set the isFixedLocation to true.
     */
    private void getLocation() {
        isFixedLocation = false;
        locationWait = (TextView) findViewById(R.id.locationWait);
        locationHandler.start();
    }

    /**
     * replace the android keyboard with a new one. the enable it
     */
    private void initKeyboard() {
        mKeyboard = new Keyboard(this, R.layout.keybord);
        mKeyboardView = (KeyboardView) findViewById(R.id.keyboard_view);

        mKeyboardView.setKeyboard(mKeyboard);

        mKeyboardView
                .setOnKeyboardActionListener(new BasicOnKeyboardActionListener(
                        this));

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View arg0, final boolean arg1) {
                if (arg1) {
                    mKeyboardView.setVisibility(View.VISIBLE);
                } else {
                    mKeyboardView.setVisibility(View.GONE);
                }
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                editText.setText("");
                mKeyboardView.setVisibility(View.VISIBLE);
                return true;
            }
        });

        mKeyboardView.setVisibility(View.VISIBLE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onResume() When one is
     * coming back to the app, he might be in a different location
     */
    @Override
    protected final void onResume() {
        getLocation();
        super.onResume();
    }

    /**
     * @param view
     *            Handle click button event: wait for location fix and call
     *            send()
     */
    public final void sendClick(final View view) {

        findViewById(R.id.clickProgressBar).setVisibility(View.VISIBLE);

        // close keyboard
        editText.clearFocus();

        if (!isFixedLocation) {
            // open a new thread so the UI will not stuck while waiting for
            // location
            new Thread() {
                @Override
                public void run() {
                    synchronized (lockObject) {
                        try {
                            if (!isFixedLocation) {
                                lockObject.wait();
                            }
                        } catch (InterruptedException e) {}
                    }
                    runOnUiThread(new Runnable() {
                        // the send command has to be from the UI.
                        @Override
                        public void run() {
                            send();
                        }
                    });
                }
            }.start();
        } else {
            send();
        }
    }

    /**
     * set the bus number and location to the diraction fragment.
     * */
    private void send() {
        findViewById(R.id.clickProgressBar).setVisibility(View.GONE);
        ((DiractionFragment) getSupportFragmentManager().findFragmentById(
                R.id.fragment)).setData(editText.getText().toString(),
                locationHandler.getCurrentLocation());
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.FragmentActivity#onBackPressed() Beacuse
     * there is a new keyboard, the first back hides it - just like the android
     * keyboard
     */
    @Override
    public final void onBackPressed() {
        if (mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

}
