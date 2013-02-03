package travelcube.busalert.lines;

import android.app.Activity;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.view.KeyEvent;

/**
 * @author omer
 * 
 */
public class BasicOnKeyboardActionListener implements OnKeyboardActionListener {

    private final Activity mTargetActivity;

    public BasicOnKeyboardActionListener(final Activity targetActivity) {
        mTargetActivity = targetActivity;
    }

    @Override
    public final void onKey(final int primaryCode, final int[] keyCodes) {
        long eventTime = System.currentTimeMillis();
        KeyEvent event;
        switch (primaryCode) {
            case 10:
                event = new KeyEvent(eventTime, "א", 0, 0);
                break;
            case -5:
                event = new KeyEvent(eventTime, eventTime,
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                        KeyEvent.FLAG_SOFT_KEYBOARD
                                | KeyEvent.FLAG_KEEP_TOUCH_MODE);
                break;

            default:
                event = new KeyEvent(eventTime, String.valueOf(primaryCode), 0,
                        0);
                break;
        }

        mTargetActivity.dispatchKeyEvent(event);
    }

    @Override
    public void onPress(final int arg0) {

    }

    @Override
    public void onRelease(final int arg0) {

    }

    @Override
    public void onText(final CharSequence arg0) {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeUp() {

    }
}
