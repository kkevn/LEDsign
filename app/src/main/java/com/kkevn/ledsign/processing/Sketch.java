package com.kkevn.ledsign.processing;

import processing.core.PApplet;

public class Sketch extends PApplet {

    //int yr = 0;
    //int xr = 0;

    float yr = 0f;
    float xr = 0f;

    public Sketch() {

    }

    public void settings() {
        size(displayWidth, displayHeight / 2, P3D);
        //smooth(aa);
        //fullScreen();
    }

    public void preload() {
        //
    }

    public void setup() {
        //size(width, height, P3D);
    }

    public void draw() {
        background(0);
        translate(width / 2, height / 2);
        rotateY(degrees(yr));
        rotateX(degrees(xr));
        fill(10, 200, 10);
        box(500);

    }

    public void touchMoved() {

        // to right
        if (mouseX > pmouseX) {
            yr += 0.0002f;
            if (yr > 360f) {
                yr = 0f;
            }
        } else if (mouseX <= pmouseX) {
            yr -= 0.0002f;
            if (yr < 0f) {
                yr = 360f;
            }
        }

        if (mouseY > pmouseY) {
            //xr-=1;
            if (xr > -45f) {
                xr -= 0.0002f;
            }
        } else if (mouseY <= pmouseY) {
            //xr+=1;
            if (xr < 0f) {
                xr += 0.0002f;
            }
        }
    }

    /*public void touchMoved() {

        // to right
        if (mouseX > pmouseX) {
            yr += 2;
            if (yr > 360) {
                yr = 0;
            }
        } else if (mouseX <= pmouseX) {
            yr -= 2;
            if (yr < 0) {
                yr = 360;
            }
        }

        if (mouseY > pmouseY) {
            //xr-=1;
            if (xr > -45) {
                xr -= 1;
            }
        } else if (mouseY <= pmouseY) {
            //xr+=1;
            if (xr < 0) {
                xr += 1;
            }
        }
    }*/
}
