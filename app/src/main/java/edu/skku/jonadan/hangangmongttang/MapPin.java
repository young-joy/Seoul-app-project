package edu.skku.jonadan.hangangmongttang;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

public class MapPin {
    float X, Y;
    int id;

    public MapPin(float X, float Y, int id) {
        this.X = X;
        this.Y = Y;
        this.id = id;
    }

    public MapPin() {
    }

    public float getX() {
        return X;
    }

    public void setX(float X) {
        this.X = X;
    }

    public float getY() {
        return Y;
    }

    public void setY(float Y) {
        this.Y = Y;
    }

    public PointF getPoint() {
        return new PointF(this.X, this.Y);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
