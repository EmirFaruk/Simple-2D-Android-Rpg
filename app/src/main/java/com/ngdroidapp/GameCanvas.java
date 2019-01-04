package com.ngdroidapp;

import android.graphics.Canvas;

import istanbul.gamelab.ngdroid.base.BaseCanvas;

/**
 * Created by noyan on 24.06.2016.
 * Nitra Games Ltd.
 */

public class GameCanvas extends BaseCanvas {

    private static final int LOADING_STATE = 0;
    private static final int PLAYING_STATE = 1;
    private static final int PAUSE_STATE = 2;
    private static final int GAMEOVER_STATE = 3;

    private Player player;

    private int touchDownX, touchDownY;
    private int touchUpX, touchUpY;
    private boolean multiTouch;

    public GameCanvas(NgApp ngApp) {
        super(ngApp);
    }

    public void setup() {
        player = new Player(root);
        player.setup();
        root.currentLevel = 1;
    }

    public void update() {
        player.update();
        root.levels.get(root.currentLevel).update();

        for(int i = 0; i < root.levels.get(root.currentLevel).getAnimals().length; i++) {
            if(Math.abs(player.getPosition()[0] - root.levels.get(root.currentLevel).getAnimals()[i].getPosition().first) < 100 &&
                    Math.abs(player.getPosition()[1] - root.levels.get(root.currentLevel).getAnimals()[i].getPosition().second) < 100) {
                root.levels.get(root.currentLevel).getAnimals()[i].move(player.getPosition()[0], player.getPosition()[1], player.getDirection());
            }
        }
    }

    public void draw(Canvas canvas) {
        root.levels.get(root.currentLevel).draw(canvas);
        player.draw(canvas);
    }

    public void keyPressed(int key) {

    }

    public void keyReleased(int key) {

    }

    public boolean backPressed() {
        return true;
    }

    public void surfaceChanged(int width, int height) {

    }

    public void surfaceCreated() {

    }

    public void surfaceDestroyed() {

    }

    public void touchDown(int x, int y, int id) {
        touchDownX = x;
        touchDownY = y;

        if(id == 1) {
            multiTouch = true;
            player.setTarget(touchDownX, touchDownY);
            player.fire();
        }
    }

    public void touchMove(int x, int y, int id) {
    }

    public void touchUp(int x, int y, int id) {
        touchUpX = x;
        touchUpY = y;

        if(id == 0) {
            if(multiTouch) {
                player.setTarget(player.getPosition()[0], player.getPosition()[1]);
                multiTouch = false;
            } else player.setTarget(touchUpX, touchUpY);
        }
        else player.setTarget(player.getPosition()[0], player.getPosition()[1]);
    }

    public void pause() {

    }

    public void resume() {

    }

    public void reloadTextures() {

    }

    public void showNotify() {
    }

    public void hideNotify() {
    }
}
