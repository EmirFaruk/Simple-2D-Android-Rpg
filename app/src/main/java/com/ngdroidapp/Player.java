package com.ngdroidapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Pair;

import istanbul.gamelab.ngdroid.util.Utils;

public class Player extends GameObject {
    public enum Animation {
        IDLE(0, 1, 8),
        WALK(1, 9, 8),
        FIRE(2, 3, 8);

        public int no;
        public int animationCount;
        public int positionCount;

        Animation(int no, int animationCount, int positionCount) {
            this.no = no;
            this.animationCount = animationCount;
            this.positionCount = positionCount;
        }
    }

    public enum Direction {
        SOUTHERN_EAST(0),
        EAST(1),
        NORTHEAST(2),
        NORTH(3),
        NORTHWEST(4),
        WEST(5),
        SOUTHERN_WEST(6),
        SOUTH(7);

        public int no;

        Direction(int no) {
            this.no = no;
        }
    }

    private Bitmap image;
    private Rect source[][][], destination;
    private Rect collisionRectX, collisionRectY;
    private int sourceX, sourceY, sourceW, sourceH;
    private int destinationX, destinationY, destinationW, destinationH;

    private int currentAnimation;
    private int currentPosition;
    private int animationNo;

    private int speedX, speedY;
    private double directionX, directionY;
    private int targetX, targetY;
    private double angle;

    private Bullet[] bulletPool;
    private int bulletPoolSize;
    private int fireAnimCounter;

    public Player(NgApp root) {
        super(root);
    }

    public void setup() {
        image = Utils.loadImage(root, "cowboy.png");
        sourceX = 0;
        sourceY = 0;
        sourceW = 128;
        sourceH = 128;
        source = new Rect[Animation.values().length][][];
        destinationX = 0;
        destinationY = 0;
        destinationW = 128;
        destinationH = 128;
        destination = new Rect(destinationX, destinationY, destinationW + destinationX, destinationH + destinationY);
        collisionRectX = new Rect();
        collisionRectY = new Rect();

        for(int i = 0; i < Animation.values().length; i++) {
            source[i] = new Rect[Animation.values()[i].positionCount][];
            for(int j = 0; j < Animation.values()[i].positionCount; j++) {
                source[i][j] = new Rect[Animation.values()[i].animationCount];
                for(int k = 0; k < Animation.values()[i].animationCount; k++) {
                    if (i == Animation.IDLE.no) {
                        source[i][j][k] = new Rect(sourceX, sourceY + (sourceH * (2 + j)), sourceW, sourceH + (sourceH * (2 + j)));
                    } else if (i == Animation.WALK.no) {
                        source[i][j][k] = new Rect(sourceX + (sourceW * (1 + k)), sourceY + (sourceH * (2 + j)), sourceW + (sourceW * (1 + k)), sourceH + (sourceH * (2 + j)));
                    } else if (i == Animation.FIRE.no) {
                        source[i][j][k] = new Rect(sourceX + (sourceW * (11 + k)), sourceY + (sourceH * (2 + j)), sourceW + (sourceW * (11 + k)), sourceH + (sourceH * (2 + j)));
                    }
                }
            }
        }
        currentAnimation = Animation.IDLE.no;
        currentPosition = Direction.NORTH.no;
        animationNo = 0;

        speedX = 15;
        speedY = 15;
        directionX = 0;
        directionY = 0;

        bulletPoolSize = 10;
        bulletPool = new Bullet[bulletPoolSize];
        for(int i = 0; i < bulletPoolSize; i++) bulletPool[i] = new Bullet(root);
        fireAnimCounter = 5;
    }

    @Override
    public void update() {
        if(Math.abs(targetX - (destinationX + (source[0][0][0].width() >> 1))) < 20 && Math.abs(targetY - (destinationY + (source[0][0][0].height() >> 1))) < 20) {
            if (currentAnimation != Animation.FIRE.no) {
                setAnimation(Animation.IDLE.no);
            }
        } else if(currentAnimation != Animation.FIRE.no) {
            if(currentAnimation != Animation.WALK.no) setAnimation(Animation.WALK.no);
            calculateDirection();
            collisionRectX.set(destinationX + (int)(speedX * directionX), destinationY, destinationX + (int)(speedX * directionX) + destinationW,destinationY + destinationH);
            collisionRectY.set(destinationX, destinationY + (int)(speedY * directionY), destinationX + destinationW, destinationY + (int)(speedY * directionY) + destinationH);
            if(root.levels.get(root.currentLevel).isCollision(collisionRectX) || root.levels.get(root.currentLevel).isCollision(collisionRectY)) {
                setAnimation(Animation.IDLE.no);
            } else {
                destinationX += speedX * directionX;
                destinationY += speedY * directionY;
            }
        }

        if(currentAnimation == Animation.FIRE.no) {
            if(--fireAnimCounter <= 0) {
                if (++animationNo >= Animation.values()[currentAnimation].animationCount) setAnimation(Animation.IDLE.no);
                fireAnimCounter = 5;
            }
        } else if (++animationNo >= Animation.values()[currentAnimation].animationCount) animationNo = 0;

        for(int i = 0; i < bulletPoolSize; i++) {
            bulletPool[i].update();
            if(root.levels.get(root.currentLevel).isCollision(bulletPool[i].getRect())) bulletPool[i].setHasTarget(false);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        destination.set(destinationX, destinationY, destinationW + destinationX, destinationH + destinationY);
        canvas.drawBitmap(image, source[currentAnimation][currentPosition][animationNo], destination, null);
    }

    public int[] getPosition() {
        return new int[]{destinationX + source[0][0][0].width() / 2, destinationY + source[0][0][0].height() / 2};
    }

    public void setTarget(int x, int y) {
        targetX = x;
        targetY = y;
    }

    public void fire() {
        if(currentAnimation != Animation.FIRE.no) {
            setAnimation(Animation.FIRE.no);
            calculateDirection();
        }
        for(int i = 0; i < bulletPoolSize; i++) {
            if(!bulletPool[i].getHasTarget()) {
                bulletPool[i].setPosition(destinationX, destinationY);
                bulletPool[i].setHasTarget(true);
                bulletPool[i].setAngle(angle);
                return;
            }
        }
    }

    private void calculateDirection() {
        double radyan = Math.atan2(getPosition()[1] - targetY, getPosition()[0]  - targetX);
        angle = (450 + Math.toDegrees(radyan)) % 360;

        directionX = -(Math.sin(Math.toRadians(angle)));
        directionY = (Math.cos(Math.toRadians(angle)));

        if((directionX >= 0.9 && directionX <= 1) && currentPosition != Direction.EAST.no) setPosition(Direction.EAST.no);
        else if((directionX >= -1 && directionX <= -0.9) && currentPosition != Direction.WEST.no) setPosition(Direction.WEST.no);
        else if((directionY >= -1 && directionY <= -0.9) && currentPosition != Direction.NORTH.no) setPosition(Direction.NORTH.no);
        else if((directionY >= 0.9 && directionY <= 1) && currentPosition != Direction.SOUTH.no) setPosition(Direction.SOUTH.no);
        else if((directionX >= 0 && directionX <= 0.9) && (directionY >= -0.9 && directionY <= 0) && currentPosition != Direction.NORTHEAST.no) setPosition(Direction.NORTHEAST.no);
        else if((directionX >= -0.9 && directionX <= 0) && (directionY >= 0 && directionY <= 0.9) && currentPosition != Direction.SOUTHERN_WEST.no) setPosition(Direction.SOUTHERN_WEST.no);
        else if((directionX >= -0.9 && directionX <= 0) && (directionY >= -0.9 && directionY <= 0) && currentPosition != Direction.NORTHWEST.no) setPosition(Direction.NORTHWEST.no);
        else if((directionX >= 0 && directionX <= 0.9) && (directionY >= 0 && directionY <= 0.9) && currentPosition != Direction.SOUTHERN_EAST.no) setPosition(Direction.SOUTHERN_EAST.no);
    }

    private void setAnimation(int animationNO) {
        currentAnimation = animationNO;
        animationNo = 0;
    }

    private void setPosition(int positionNo) {
        currentPosition = positionNo;
        animationNo = 0;
    }

    public Pair<Integer, Integer> getDirection() {
        return new Pair<>((directionX < 0 ? 0 : 1), (directionY < 0 ? 0 : 1));
    }
}