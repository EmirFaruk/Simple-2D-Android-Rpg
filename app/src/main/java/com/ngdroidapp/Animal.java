package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Pair;

import istanbul.gamelab.ngdroid.util.Utils;

public class Animal extends GameObject {

    public enum Animals {
        Cow(0, 2, 4, 4),
        Lama(2, 2, 4, 4),
        Pig(3, 2, 4, 4),
        Sheep(4, 2, 4, 4);

        public int num, variety, positionCount, animationCount;

        Animals(int num, int variety, int positionCount, int animationCount) {
            this.num = num;
            this.variety = variety;
            this.positionCount = positionCount;
            this.animationCount = animationCount;
        }
    }

    public enum Direction {
        TOP(0),
        LEFT(1),
        DOWN(2),
        RIGHT(3);

        private int num;

        Direction(int num) {
            this.num = num;
        }
    }

    public enum State {
        WALK(0),
        EAT(1);

        private int num;

        State(int num) {
            this.num = num;
        }
    }

    private final Animals animalType;
    private Bitmap[] images;
    private Rect source[][][], destination;
    private int sourceX, sourceY, sourceW, sourceH;
    private int destinationX, destinationY, destinationW, destinationH;
    private int speed;
    private double dx, dy;
    private int targetX, targetY;
    private int currentState, currentPosition, animationNo;
    private int animCounter;

    public boolean isAlive;

    public Animal(NgApp ngApp, Animals type) {
        super(ngApp);
        animalType = type;
        images = new Bitmap[animalType.variety];

        switch (animalType) {
            case Cow:
                images[0] = Utils.loadImage(root, "Animals/cow_walk.png");
                images[1] = Utils.loadImage(root, "Animals/cow_eat.png");
                speed = 10;
                break;
            case Lama:
                images[0] = Utils.loadImage(root, "Animals/lama_walk.png");
                images[1] = Utils.loadImage(root, "Animals/lama_eat.png");
                speed = 30;
                break;
            case Pig:
                images[0] = Utils.loadImage(root, "Animals/pig_walk.png");
                images[1] = Utils.loadImage(root, "Animals/pig_eat.png");
                speed = 15;
                break;
            case Sheep:
                images[0] = Utils.loadImage(root, "Animals/sheep_walk.png");
                images[1] = Utils.loadImage(root, "Animals/sheep_eat.png");
                speed = 25;
                break;
        }

        source = new Rect[animalType.variety][][];
        sourceX = 0;
        sourceY = 0;
        sourceW = 128;
        sourceH = 128;
        destination = new Rect();
        destinationX = 0;
        destinationY = 0;
        destinationH = 160;
        destinationW = 160;
        dx = 0;
        dy = 0;
        targetX = 0;
        targetY = 0;
        animCounter = 10;
        isAlive = true;

        for(int i = 0; i < Animals.values()[i].variety; i++) {
            source[i] = new Rect[Animals.values()[i].positionCount][];
            for(int j = 0; j < Animals.values()[i].positionCount; j++){
                source[i][j] = new Rect[Animals.values()[i].animationCount];
                for(int k = 0; k < Animals.values()[i].animationCount; k++) {
                    source[i][j][k] = new Rect(sourceX + (sourceW * k), sourceY + (sourceH * j), sourceW + (sourceW * k + 1), sourceH + (sourceH * j + 1));
                }
            }
        }
        destinationX = 960;
        destinationY = 540;
        currentState = 1;
        currentPosition = 3;
    }

    @Override
    public void update() {
        if(!isAlive) return;
        playAnimation();
        if (currentState == State.WALK.num) move();
    }

    @Override
    public void draw(Canvas canvas) {
        if(!isAlive) return;
        drawAnimal(canvas);
    }

    private void playAnimation() {
        if(--animCounter <= 0) {
            if (++animationNo >= animalType.animationCount) animationNo = 0;
            animCounter = 10;
        }
    }

    private void move() {
        if((Math.abs(targetX - getPosition().first) < 65 && Math.abs(targetY - getPosition().second) < 65) ||
                (Math.abs(targetX - getPosition().first) < 65 && Math.abs(targetY - getPosition().second) < 65)) {
            dx = 0;
            dy = 0;
            setState(State.EAT);
            return;
        }

        double radyan = Math.atan2(destinationY - targetY, destinationX - targetX);
        double angle = (450 + Math.toDegrees(radyan)) % 360;

        dx = -(Math.sin(Math.toRadians(angle)));
        dy = (Math.cos(Math.toRadians(angle)));

        if((dx >= 0.7 && dx <= 1.2) && currentPosition != Direction.RIGHT.num) setDirection(Direction.RIGHT);
        else if((dx >= -1.2 && dx <= -0.7) && currentPosition != Direction.LEFT.num) setDirection(Direction.LEFT);
        else if((dy >= -1.2 && dy <= -0.7) && currentPosition != Direction.TOP.num) setDirection(Direction.TOP);
        else if((dy >= 0.7 && dy <= 1.2) && currentPosition != Direction.DOWN.num) setDirection(Direction.DOWN);

        destinationX += dx * speed;
        destinationY += dy * speed;
    }

    private void drawAnimal(Canvas canvas) {
        destination.set(destinationX, destinationY, destinationW + destinationX, destinationH + destinationY);
        canvas.drawBitmap(images[currentState], source[currentState][currentPosition][animationNo], destination, null);
    }

    public void move(int x, int y, Pair<Integer, Integer> direction) {
        if(direction != null) {
            targetX = x + 350 * direction.first;
            targetY = y + 350 * direction.second;
        } else {
            targetX = x;
            targetY = y;
        }
        setState(State.WALK);
    }

    public Rect getRect() {
        return destination;
    }

    public Pair<Integer, Integer> getPosition() {
        return new Pair<>(destinationX + (source[0][0][0].width() >> 1), destinationY + (source[0][0][0].height() >> 1));
    }

    public void setPosition(int x, int y) {
        destinationX = x;
        destinationY = y;
    }

    public void setDirection(Direction direction) {
        currentPosition = direction.num;
    }

    public void setState(State state) {
        currentState = state.num;
        animationNo = 0;
        animCounter = 10;
    }
}
