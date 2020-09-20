package com.kkevn.ledsign.processing;

import android.util.Log;

import com.kkevn.ledsign.ui.create.CreateFragment;
import com.kkevn.ledsign.ui.create.Effect;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import processing.core.PApplet;

public class Sketch extends PApplet {

    //int yr = 0;
    //int xr = 0;

    String[] txt__, txt_0, txt_1, txt_2, txt_3, txt_4, txt_5, txt_6, txt_7, txt_8, txt_9, txt_A,
            txt_B, txt_C, txt_D, txt_E, txt_F, txt_G, txt_H, txt_I, txt_J, txt_K, txt_L, txt_M,
            txt_N, txt_O, txt_P, txt_Q, txt_R, txt_S, txt_T, txt_U, txt_V, txt_W, txt_X, txt_Y, txt_Z;

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

        //String[] lines = loadStrings("_.txt");
        //println(txt__[0] + "-" + txt__.length);
        //txt__ = loadStrings("_.txt");

        // empty space
        txt__ = loadStrings("characters/_.txt");

        // numbers
        txt_0 = loadStrings("characters/0.txt");
        txt_1 = loadStrings("characters/1.txt");
        txt_2 = loadStrings("characters/2.txt");
        txt_3 = loadStrings("characters/3.txt");
        txt_4 = loadStrings("characters/4.txt");
        txt_5 = loadStrings("characters/5.txt");
        txt_6 = loadStrings("characters/6.txt");
        txt_7 = loadStrings("characters/7.txt");
        txt_8 = loadStrings("characters/8.txt");
        txt_9 = loadStrings("characters/9.txt");

        // letters
        txt_A = loadStrings("characters/A.txt");
        txt_B = loadStrings("characters/B.txt");
        txt_C = loadStrings("characters/C.txt");
        txt_D = loadStrings("characters/D.txt");
        txt_E = loadStrings("characters/E.txt");
        txt_F = loadStrings("characters/F.txt");
        txt_G = loadStrings("characters/G.txt");
        txt_H = loadStrings("characters/H.txt");
        txt_I = loadStrings("characters/I.txt");
        txt_J = loadStrings("characters/J.txt");
        txt_K = loadStrings("characters/K.txt");
        txt_L = loadStrings("characters/L.txt");
        txt_M = loadStrings("characters/M.txt");
        txt_N = loadStrings("characters/N.txt");
        txt_O = loadStrings("characters/O.txt");
        txt_P = loadStrings("characters/P.txt");
        txt_Q = loadStrings("characters/Q.txt");
        txt_R = loadStrings("characters/R.txt");
        txt_S = loadStrings("characters/S.txt");
        txt_T = loadStrings("characters/T.txt");
        txt_U = loadStrings("characters/U.txt");
        txt_V = loadStrings("characters/V.txt");
        txt_W = loadStrings("characters/W.txt");
        txt_X = loadStrings("characters/X.txt");
        txt_Y = loadStrings("characters/Y.txt");
        txt_Z = loadStrings("characters/Z.txt");
    }

    public void draw() {
        background(0);
        translate(width / 2, height / 2);
        rotateY(degrees(yr));
        rotateX(degrees(xr));
        fill(10, 200, 10);
        box(500);

        //parseEffectList(CreateFragment.parseList());

    }

    // SCROLL{xxxxx;string;int;int;int},SOLID{xxxxx;int;int;int},RAINBOW{...}
    private void parseEffectList(String input) {

        String[] isolated_inputs = input.split(",");

        for (String s : isolated_inputs) {

            String params = s.substring(s.indexOf('{') + 1, s.length() - 1);

            switch (s.substring(0, s.indexOf('{'))) {

                case Effect.TEXT_SCROLL:

                    break;
            }
        }

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
