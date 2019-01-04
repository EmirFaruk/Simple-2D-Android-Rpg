package com.ngdroidapp.levels;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.ngdroidapp.Object;
import com.ngdroidapp.Animal;

public interface ILevel {

    void setup();

    void update();

    void draw(Canvas canvas);

    boolean isCollision(Rect obj);

    Animal[] getAnimals();

    Object[] getObjects();
}
