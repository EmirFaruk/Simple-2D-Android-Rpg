package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Pair;

public class Object extends GameObject {

    private Bitmap image;
    private Rect source, destination;
    private int x, y, width, height;

    public Object(NgApp root) {
        super(root);
    }

    @Override
    public void update() {
        destination.set(x, y, x + width, y + height);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, source, destination, null);
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setSource(Rect source) {
        this.source = source;
        width = source.right - source.left;
        height = source.bottom - source.top;
    }

    public void setDestination(Rect destination) {
        this.destination = destination;
        x = destination.left;
        y = destination.top;
    }

    public Pair<Integer, Integer> getPosition() {
        return destination == null ? null : new Pair<>(destination.left, destination.top);
    }

    public Pair<Integer, Integer> getSize() {
        return source == null ? null : new Pair<>(source.right - source.left, source.bottom - source.top);
    }

    public Rect getRect() {
        return destination;
    }
}
