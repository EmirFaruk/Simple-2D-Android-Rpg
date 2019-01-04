package com.ngdroidapp.levels;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.ngdroidapp.Object;
import com.ngdroidapp.Animal;
import com.ngdroidapp.NgApp;

public class Level0 implements ILevel {

    private NgApp root;

    public Level0(NgApp root) {
        this.root = root;
    }

    @Override
    public void setup() {
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public boolean isCollision(Rect obj) {
        return false;
    }

    @Override
    public Animal[] getAnimals() {
        return null;
    }

    @Override
    public Object[] getObjects() {
        return null;
    }
}
