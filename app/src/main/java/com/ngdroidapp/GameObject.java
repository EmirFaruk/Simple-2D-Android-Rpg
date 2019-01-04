package com.ngdroidapp;

import android.graphics.Canvas;

public abstract class GameObject {

    protected final NgApp root;

    GameObject(NgApp ngApp) {
        root = ngApp;
    }

    abstract void update();

    abstract void draw(Canvas canvas);
}
