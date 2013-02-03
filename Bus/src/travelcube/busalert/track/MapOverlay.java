package travelcube.busalert.track;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapOverlay extends Overlay {
    private static final int BORDER_TRANSPRTY = 0xee000000;
    private final GeoPoint   point;
    private final Float      acc;
    private final Drawable   img;
    private final int        color;

    public MapOverlay(final GeoPoint point, final Float acccc,
            final Drawable img, final int color) {
        this.point = point;
        this.acc = acccc;
        this.img = img;
        this.color = color;
    }

    @Override
    public final void draw(final Canvas canvas, final MapView mapView,
            final boolean shadow) {
        super.draw(canvas, mapView, shadow);

        // convert point to pixels
        Point screenPts = new Point();
        mapView.getProjection().toPixels(point, screenPts);

        drawIcon(canvas, screenPts);

        drawCircle(canvas, mapView, screenPts);

    }

    private void drawCircle(final Canvas canvas, final MapView mapView,
            final Point screenPts) {
        Paint mPaintBorder = new Paint();
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setAntiAlias(true);
        mPaintBorder.setColor(color + BORDER_TRANSPRTY);
        Paint mPaintFill = new Paint();
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(color);

        int radius = (int) mapView.getProjection().metersToEquatorPixels(acc);
        /** Draw the boundary of the circle */
        canvas.drawCircle(screenPts.x, screenPts.y, radius, mPaintBorder);
        /** Fill the circle with semitransparent color */
        canvas.drawCircle(screenPts.x, screenPts.y, radius, mPaintFill);
    }

    private void drawIcon(final Canvas canvas, final Point screenPts) {
        Bitmap markerImage = ((BitmapDrawable) img).getBitmap();
        canvas.drawBitmap(markerImage,
                screenPts.x - markerImage.getWidth() / 2, screenPts.y
                        - markerImage.getHeight() / 2, null);
    }

    /**
     * @return the point
     */
    public final GeoPoint getPoint() {
        return point;
    }

}
