package com.ngdroidapp;

import android.graphics.Canvas;

import com.ngdroidapp.levels.ILevel;
import com.ngdroidapp.levels.Level0;
import com.ngdroidapp.levels.Level1;

import java.util.ArrayList;
import java.util.List;

import istanbul.gamelab.ngdroid.base.BaseActivity;
import istanbul.gamelab.ngdroid.core.AppManager;
import istanbul.gamelab.ngdroid.base.BaseApp;
import istanbul.gamelab.ngdroid.util.Log;


/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */

public class NgApp extends BaseApp {

    public List<ILevel> levels;
    public int currentLevel;

    public NgApp(BaseActivity nitraBaseActivity, AppManager appManager) {
        super(nitraBaseActivity, appManager);
    }


    public void setup() {
        appManager.setUnitResolution(AppManager.RESOLUTION_QHD);
        appManager.setFrameRateTarget(20);

        levels = new ArrayList<>();
        levels.add(new Level0(this));
        levels.add(new Level1(this));
        levels.get(0).setup();
        levels.get(1).setup();
        currentLevel = 0;

        MenuCanvas mc = new MenuCanvas(this);
        canvasManager.setCurrentCanvas(mc);
    }


    public void update() {

    }

    public void draw(Canvas canvas) {

    }

    public void keyPressed(int key) {

    }

    public void keyReleased(int key) {

    }

    public boolean backPressed() {
        return true;
    }

    public void touchDown(int x, int y, int id) {

    }

    public void touchMove(int x, int y, int id) {

    }

    public void touchUp(int x, int y, int id) {

    }

    public void surfaceChanged(int width, int height) {

    }

    public void surfaceCreated() {

    }

    public void surfaceDestroyed() {

    }

    public void pause() {
        Log.i("NGAPP", "pause");
    }

    public void resume() {
        Log.i("NGAPP", "resume");
    }

    public void reloadTextures() {
        Log.i("NGAPP", "reloadTextures");
    }
}
