package edu.skku.jonadan.hangangmongttang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PinView extends SubsamplingScaleImageView {
    private PointF sPin;

    ArrayList<MapPin> mapPins;
    ArrayList<DrawPin> drawnPins;
    ArrayList<PointF> deeplinkCoordinates = new ArrayList<>();
    Context context;
    String tag = getClass().getSimpleName();

    public PinView(Context context) {
        this(context, null);
        this.context = context;
        initialise();
    }

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        initialise();
    }

    public void setPins(ArrayList<MapPin> mapPins) {
        this.mapPins = mapPins;
        initialise();
        invalidate();
    }

    public void setPin(PointF pin) {
        this.sPin = pin;
    }

    public PointF getPin() {
        return sPin;
    }

    private void initialise() {
        Log.d("touch_event","set");
        setTouchListener();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during       setup.
        if (!isReady()) {
            return;
        }

        drawnPins = new ArrayList<>();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        float density = getResources().getDisplayMetrics().densityDpi;

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point sizePoint = new Point();
        display.getSize(sizePoint);
        float width = sizePoint.x;
        float height = sizePoint.y;
        Log.d("get_size",sizePoint.toString());

        for (int i = 0; i < mapPins.size(); i++) {
            MapPin mPin = mapPins.get(i);
            deeplinkCoordinates.add(mPin.getPoint());
            //Bitmap bmpPin = Utils.getBitmapFromAsset(context, mPin.getPinImgSrc());
            Bitmap bmpPin = MainActivity.getBitmapFromVectorDrawable(context, R.drawable.marker);

            float w = (density / 160f) * 30;
            float h = (density / 160f) * 30;
            bmpPin = Bitmap.createScaledBitmap(bmpPin, (int) w, (int) h, true);

            //PointF vPin = sourceToViewCoord(mPin.getPoint());
            PointF vPin = mPin.getPoint();
            //in my case value of point are at center point of pin image, so we need to adjust it here

            //Log.d("touch_event",vPin.toString());
            Log.d("touch_event","density : "+new Float(density).toString());
            float x_px = (640f/160f)*(vPin.x - (bmpPin.getWidth() / 2));
            float y_px = (640f/160f)*(vPin.y - bmpPin.getHeight()/2 -40);
            Log.d("touch_event","px: "+new Float(x_px).toString() + ", "+ new Float(y_px).toString());

            float vX = (vPin.x - (bmpPin.getWidth() / 2)) * width / 1440f;
            float vY = (vPin.y - bmpPin.getHeight()/2 -40f) * height / 2560f;
            Log.d("touch_event","dp: "+new Float(vX).toString()+", "+new Float(vY).toString());
            canvas.drawBitmap(bmpPin, vX, vY, paint);
            //add added pin to an Array list to get touched pin
            DrawPin dPin = new DrawPin();
            dPin.setStartX(mPin.getX() - w / 2);
            dPin.setEndX(mPin.getX() + w / 2);
            dPin.setStartY(mPin.getY() - h / 2);
            dPin.setEndY(mPin.getY() + h / 2);
            dPin.setId(mPin.getId());
            drawnPins.add(dPin);
        }
    }

    public int getPinIdByPoint(PointF point) {

        for (int i = drawnPins.size() - 1; i >= 0; i--) {
            DrawPin dPin = drawnPins.get(i);
            if (point.x >= dPin.getStartX() && point.x <= dPin.getEndX()) {
                if (point.y >= dPin.getStartY() && point.y <= dPin.getEndY()) {
                    return dPin.getId();
                }
            }
        }
        return -1; //negative no means no pin selected
    }

    private void setTouchListener() {

        final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (isReady() && deeplinkCoordinates != null) {
                    PointF tappedCoordinate = new PointF(e.getX(), e.getY());
                    Log.d("touch_event", tappedCoordinate.toString());
                    int blockWidth = 30;
                    int blockHeight = 30;

                    // check if deepLinkBitmap is touched
                    for (PointF deeplink : deeplinkCoordinates) {
                        PointF deeplinkCoordinate = sourceToViewCoord(deeplink);
                        int deeplinkX = (int) (deeplinkCoordinate.x - (blockWidth / 2));
                        int deeplinkY = (int) (deeplinkCoordinate.y - blockHeight);

                        // center coordinate -/+ blockWidth actually sets touchable area to 2x icon size
                        if (tappedCoordinate.x >= deeplinkX - blockWidth && tappedCoordinate.x <= deeplinkX + blockWidth &&
                                tappedCoordinate.y >= deeplinkY - blockHeight && tappedCoordinate.y <= deeplinkY + blockHeight) {
                            Log.d("touch_event","pin touched");
                        }
                    }
                }
                return true;
            }
        });

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }
}
