package edu.skku.jonadan.hangangmongttang;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PinView extends SubsamplingScaleImageView {
    private PointF sPin;

    ArrayList<MapPin> mapPins;
    ArrayList<DrawPin> drawnPins;
    ArrayList<PointF> deeplinkCoordinates = new ArrayList<>();
    ArrayList<PointF> modifiedCoordinates = new ArrayList<>();
    Context context;
    FragmentManager fragmentManager;
    Bundle dialog_args;

    float pin_width;
    float pin_height;

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

    public void setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    public PointF getPin() {
        return sPin;
    }

    private void initialise() {
        Log.d("touch_event","set");
        dialog_args = new Bundle();
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
        pin_width = (density / 160f) * 30f;
        pin_height = (density / 160f) * 30f;
        Log.d("get_size",sizePoint.toString());

        modifiedCoordinates.clear();

        for (int i = 0; i < mapPins.size(); i++) {
            MapPin mPin = mapPins.get(i);
            deeplinkCoordinates.add(mPin.getPoint());
            //Bitmap bmpPin = Utils.getBitmapFromAsset(context, mPin.getPinImgSrc());
            Bitmap bmpPin = MainActivity.getBitmapFromVectorDrawable(context, R.drawable.marker);

            bmpPin = Bitmap.createScaledBitmap(bmpPin, (int) pin_width, (int) pin_height, true);

            //PointF vPin = sourceToViewCoord(mPin.getPoint());
            PointF vPin = mPin.getPoint();
            //in my case value of point are at center point of pin image, so we need to adjust it here

            Log.d("touch_event",vPin.toString());

            PointF newPin = new PointF();
            float vX = (vPin.x - (bmpPin.getWidth() / 2)) * width / 1440f;
            float vY = (vPin.y - bmpPin.getHeight()/2 -40f) * height / 2560f;
            newPin.x = vX;
            newPin.y = vY;
            modifiedCoordinates.add(newPin);
            Log.d("touch_event","dp: "+new Float(vX).toString()+", "+new Float(vY).toString());
            canvas.drawBitmap(bmpPin, vX, vY, paint);
            //add added pin to an Array list to get touched pin
            DrawPin dPin = new DrawPin();
            dPin.setStartX(newPin.x - pin_width/2);
            dPin.setEndX(newPin.x + pin_width/2);
            dPin.setStartY(newPin.y + pin_height/2);
            dPin.setEndY(newPin.y + pin_height/ 2);
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
                if (isReady() && modifiedCoordinates != null) {
                    PointF tappedCoordinate = new PointF(e.getX(), e.getY());
                    Log.d("touch_event", tappedCoordinate.toString());

                    int pin_num = modifiedCoordinates.size();
                    for (int i=0;i<pin_num;i++) {
                        PointF pinCoord = modifiedCoordinates.get(i);
                        int pinX = (int) pinCoord.x;
                        int pinY = (int) pinCoord.y;

                        if (tappedCoordinate.x >= pinX - pin_width*2/3 && tappedCoordinate.x <= pinX + pin_width*2/3 &&
                                tappedCoordinate.y >= pinY - pin_height*2/3 && tappedCoordinate.y <= pinY + pin_height*2/3) {
                            Log.d("touch_event","pin : "+pinCoord.toString());
                            Log.d("touch_event",new Integer(i).toString() + "pin touched");

                            ParkInfoDialog park_info_dialog = new ParkInfoDialog();
                            dialog_args.putInt("PARK_INDEX",i);
                            park_info_dialog.setArguments(dialog_args);
                            park_info_dialog.show(fragmentManager, "PARK_INFO_DIALOG");
                            break;
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
