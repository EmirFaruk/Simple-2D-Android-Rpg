package com.ngdroidapp;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;

public class Bullet extends GameObject {
    private final Rect bullet;
    private boolean hasTarget;
    private int positionX, positionY;
    private int speed;
    private double angle;

    //Bullet lifetime
    private int lifeTimeSec;
    private float lifeTime;

    public Bullet(NgApp ngApp) {
        super(ngApp);
        bullet = new Rect();
        speed = 50;
        lifeTimeSec = 5;
        lifeTime = 5;
    }

    @Override
    public void update() {
        if(!hasTarget) return;
        lifeTime -= (1.0f / root.appManager.getFrameRateTarget());
        if (lifeTime <= 0.0f) {
            hasTarget = false;
            lifeTime = lifeTimeSec;
        }
        positionX += -(Math.sin(Math.toRadians(angle))) * speed;
        positionY += (Math.cos(Math.toRadians(angle)))  * speed;

        bullet.set(positionX, positionY, positionX + 10, positionY + 10);
    }

    @Override
    public void draw(Canvas canvas) {
        //Don't need to draw anything
    }

    public Rect getRect() {
        return bullet;
    }

    public boolean getHasTarget() {
        return hasTarget;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void setHasTarget(boolean hasTarget) {
        this.hasTarget = hasTarget;
    }

    public Pair<Integer, Integer> getPosition() {
        return new Pair<>(positionX, positionY);
    }

    public void setPosition(int x, int y) {
        positionX = x;
        positionY = y;
    }
}
