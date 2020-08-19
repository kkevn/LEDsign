package com.kkevn.ledsign.processing;

import processing.core.PApplet;

public class Sketch extends PApplet {

    public Sketch() {

    }

    public void settings() {
        size(displayWidth, displayHeight / 2);
        //fullScreen();
    }

    public void preload() {
        //
    }

    public void setup() {

    }

    public void draw() {
        if (mousePressed) {
            ellipse(mouseX, mouseY, 50, 50);
        }
    }
}
