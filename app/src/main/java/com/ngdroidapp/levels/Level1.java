package com.ngdroidapp.levels;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.ngdroidapp.Object;
import com.ngdroidapp.Animal;
import com.ngdroidapp.NgApp;

import java.util.Random;

import istanbul.gamelab.ngdroid.util.Log;
import istanbul.gamelab.ngdroid.util.Utils;

public class Level1 implements ILevel {

    private final NgApp root;

    private Bitmap background;
    private Rect bgSource, bgDestination;
    private int bgSourceW, bgSourceH, bgSourceX, bgSourceY;
    private int bgDestinationW, bgDestinationH, bgDestinationX, bgDestinationY;

    private int animalCount;
    private Animal[] animal;
    private int fenceCount;
    private Bitmap fenceImage;
    private Object[] fences;

    public Level1(NgApp root) {
        this.root = root;
    }

    @Override
    public void setup() {
        background = Utils.loadImage(root, "tilea2.png");
        bgSourceX = 0;
        bgSourceY = 0;
        bgSourceW = 32;
        bgSourceH = 96;
        bgSource = new Rect(bgSourceX, bgSourceY, bgSourceW, bgSourceH);

        bgDestinationX = 0;
        bgDestinationY = 0;
        bgDestinationW = 128 + bgDestinationX;
        bgDestinationH = 128 + bgDestinationY;
        bgDestination = new Rect(bgDestinationX, bgDestinationY, bgDestinationW, bgDestinationH);

        animalCount = 4;
        animal = new Animal[animalCount];
        animal[0] = new Animal(root, Animal.Animals.Lama);
        animal[1] = new Animal(root, Animal.Animals.Cow);
        animal[2] = new Animal(root, Animal.Animals.Sheep);
        animal[3] = new Animal(root, Animal.Animals.Pig);

        Random rnd = new Random();
        for(int i = 0; i < animalCount; i++) animal[i].setPosition(rnd.nextInt(1700) + 100, rnd.nextInt(800) + 100);

        // region Fences
        fenceImage = Utils.loadImage(root, "Objects/fence.png");
        fenceCount = 30;
        fences = new Object[fenceCount];
        for(int i = 0; i < fenceCount; i++) {
            if(i < 5) {
                fences[i] = new Object(root);
                fences[i].setImage(fenceImage);
                fences[i].setSource(new Rect(12, 3, 84, 26));
                fences[i].setDestination(new Rect(root.getWidth() / 2 + (i * fences[i].getSize().first), root.getHeight() / 2,
                        root.getWidth() / 2 + (i * fences[i].getSize().first) + fences[i].getSize().first, root.getHeight() / 2 + fences[i].getSize().second));
            } else if(i < 15) {
                fences[i] = new Object(root);
                fences[i].setImage(fenceImage);
                fences[i].setSource(new Rect(44, 32, 52, 64));
                fences[i].setDestination(new Rect(fences[4].getPosition().first + fences[4].getSize().first, fences[4].getPosition().second + ((i - 5) * fences[i].getSize().second),
                        fences[4].getPosition().first + fences[4].getSize().first + fences[i].getSize().first, fences[4].getPosition().second + ((i - 5) * fences[i].getSize().second) + fences[i].getSize().second));
            } else if(i < 20) {
                fences[i] = new Object(root);
                fences[i].setImage(fenceImage);
                fences[i].setSource(new Rect(12, 3, 84, 26));
                fences[i].setDestination(new Rect(root.getWidth() / 2 + ((i - 15) * fences[i].getSize().first), fences[14].getPosition().second,
                        root.getWidth() / 2 + ((i - 15) * fences[i].getSize().first) + fences[i].getSize().first,fences[14].getPosition().second + fences[i].getSize().second));
            } else if(i < 30) {
                fences[i] = new Object(root);
                fences[i].setImage(fenceImage);
                fences[i].setSource(new Rect(44, 32, 52, 64));
                fences[i].setDestination(new Rect(fences[0].getPosition().first, fences[0].getPosition().second + ((i - 20) * fences[i].getSize().second),
                        fences[0].getPosition().first + fences[i].getSize().first, fences[0].getPosition().second + ((i - 20) * fences[i].getSize().second) + fences[i].getSize().second));
            }
        }
        // endregion
    }

    @Override
    public void update() {
        for(int i = 0; i < animalCount; i++) animal[i].update();
        for(int i = 0; i < fenceCount; i++) fences[i].update();
    }

    @Override
    public void draw(Canvas canvas) {
        for(int i = 0; i < root.getWidth(); i += bgDestinationW) {
            for(int j = 0; j < root.getHeight(); j += bgDestinationH) {
                bgDestination.set(i, j, bgDestinationW + i, bgDestinationH + j);
                canvas.drawBitmap(background, bgSource, bgDestination, null);
            }
        }
        for(int i = 0; i < fenceCount; i++) fences[i].draw(canvas);
        for(int i = 0; i < animalCount; i++) animal[i].draw(canvas);
    }

    @Override
    public boolean isCollision(Rect obj) {
       for(int i = 0; i < animalCount; i++) {
            if(animal[i].getRect().intersect(obj) && animal[i].isAlive) {
                animal[i].isAlive = false;
                return true;
            }
        }
        for(int i = 0; i < fenceCount; i++) {
           if(fences[i].getRect().intersect(obj)) return true;
        }
        return false;
    }

    @Override
    public Animal[] getAnimals() {
        return animal;
    }

    @Override
    public Object[] getObjects() {
        // TODO FIND BETTER SOLUTION
        return fences;
    }
}
