/**
 * TODO
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.processing;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.preference.PreferenceManager;

import com.kkevn.ledsign.R;
import com.kkevn.ledsign.ui.create.CreateFragment;

import java.util.HashSet;
import java.util.Set;

import processing.core.*;

public class ledsign extends PApplet {

    public final static String TEXT_SCROLL = "Scrolling Text";
    public final static String COLOR_SOLID = "Solid Color";
    public final static String COLOR_RAINBOW = "Rainbow Wave";

    PImage grid;
    PShape globe;

    String[] txt__, txt_0, txt_1, txt_2, txt_3, txt_4, txt_5, txt_6, txt_7, txt_8, txt_9, txt_A,
            txt_B, txt_C, txt_D, txt_E, txt_F, txt_G, txt_H, txt_I, txt_J, txt_K, txt_L, txt_M,
            txt_N, txt_O, txt_P, txt_Q, txt_R, txt_S, txt_T, txt_U, txt_V, txt_W, txt_X, txt_Y, txt_Z;

    float sensitivity = 2.0f;
    float yr = 0f, yr2 = 0f;
    float xr = 0f, xr2 = 0f;
    float rot = 0f;

    boolean do_lerp = false;
    float yr_adjuster = 0f;
    float xr_adjuster = 0f;
    int last_click_id = -1;

    int first_mx = 0;
    int first_my = 0;

    final int BOX_SIZE = 1000;
    final int DELAY = 100;
    double SCALE_FACTOR;
    int SIZE;

    int lastPrint;

    boolean disable = false;

    Matrix m0, m1, m2, m3, m4;
    static Matrix[] matrices;

    LinkedMatrix lm1, lm2, lm3, lm4, lm5;
    LinkedMatrix[] linked_matrices;

    LinkedMatrixContainer[] containers;

    Color accentColor;
    HUDElement[] hud;
    boolean auto_rotate = false;

    boolean b_solid = true;

    boolean b_crawl = false;
    int x_crawl = 0;
    int y_crawl = 0;
    int crawl_direction = 1;
    boolean crawl_back = false;

    boolean b_wipe = false;

    boolean b_rainbow = false;
    int xy_rainbow = 0;
    int max_rainbow = 100;
    int frames_rainbow = 0;

    boolean b_text = false;
    boolean setup_done = false;
    int letter_pos = 0;
    int char_index = 0;

    public void setup() {

        //orientation(LANDSCAPE);

        // adjust box to fit current screen height
        SCALE_FACTOR = height / 2.0f / (double) BOX_SIZE;
        SIZE = (int) (BOX_SIZE * SCALE_FACTOR);
        //print("BOX= " + BOX_SIZE + "\nSCALE " + SCALE_FACTOR + "\nSIZE= " + SIZE);

        // initialize matrix objects
        m0 = new Matrix(SIZE, 0, 200, 0, 0); // front face
        m1 = new Matrix(SIZE, 1, 200, 0, 200); // right face
        m2 = new Matrix(SIZE, 2, 200, 200, 200); // back face
        m3 = new Matrix(SIZE, 3, 0, 200, 0); // left face
        m4 = new Matrix(SIZE, 4, 0, 0, 200); // top face

        matrices = new Matrix[]{m0, m1, m2, m3, m4};
        linked_matrices = new LinkedMatrix[5];

        lm1 = new LinkedMatrix(m0, m1);
        lm2 = new LinkedMatrix(m1, m2, m3, m4);
        linked_matrices[0] = lm1;
        linked_matrices[1] = lm2;
        //containers = new LinkedMatrixContainer[2];
        //containers[0] = new LinkedMatrixContainer("11000", COLOR_SOLID, "150;150;50");
        //containers[1] = new LinkedMatrixContainer("11000", TEXT_SCROLL, "hello there;50;50;50");
        //containers = (LinkedMatrixContainer[]) append(containers, new LinkedMatrixEffect_SolidColor("10001", "50;250;50"));
        //containers = (LinkedMatrixContainer[]) append(containers, new LinkedMatrixEffect_TextScroll("10001", "hello there;50;50;50"));

        hud = new HUDElement[4];
        hud[0] = new HUDButton(width / 32, height / 24, "  R  ");
        hud[1] = new HUDButton(width / 32, height / 10 * 8, "  < ");
        hud[2] = new HUDButton(width / 10 * 9, height / 10 * 8, "  > ");
        hud[3] = new HUDToggleButton(width / 10 * 9, height / 24, "  A  ");
        //hud[4] = new HUDButton(int(width / 10 * 8.5), height / 10 * 8, "^");
        //hud[5] = new HUDButton(int(width / 10 * 8.5), height / 10 * 9, "v");

        //grid = loadImage("cube-grid.png");
        //grid = loadImage("unnamed.jpg");
        globe = createShape(SPHERE, SIZE * 4);

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

        lastPrint = millis() - DELAY;

    }

    public void settings() {
        //size(1080, 960, P3D);
        size(displayWidth, displayHeight / 2, P3D);
    }

    /**/
    public void readSettings() {

        // get references to all relevant preference keys
        String SENSITIVITY_KEY = getContext().getResources().getString(R.string.pref_sensitivity_key);
        String AUTO_ROTATE_KEY = getContext().getResources().getString(R.string.pref_auto_rotate_key);
        String COLOR_KEY = getContext().getResources().getString(R.string.pref_color_key);
        String HUD_KEY = getContext().getResources().getString(R.string.pref_hud_key);

        // create the SharedPreferences object to get preferences from
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        // update the sensitivity value to what is found in the settings
        sensitivity = (float) (sharedPreferences.getInt(SENSITIVITY_KEY, 0) + 1.0f) / 2.0f;

        // update the auto rotate status according to what is found in the settings
        if (sharedPreferences.getBoolean(AUTO_ROTATE_KEY, false)) {
            ((HUDToggleButton) hud[3]).toggle();
            auto_rotate = true;
        }

        // get the accent color found in the settings
        int color = sharedPreferences.getInt(COLOR_KEY, 0);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        accentColor = Color.valueOf(r, g, b);

        // "reset" hide selections by revealing all HUD elements and apply accent color to them
        for (HUDElement e : hud) {
            e.setHidden(false);
            e.setColor(r, g, b);
        }

        // for each selection in Settings, hide the specified selections
        for (String s : sharedPreferences.getStringSet(HUD_KEY, new HashSet<>())) {
            switch (s) {
                case "CENTER":
                    hud[0].setHidden(true);
                    break;
                case "LEFT":
                    hud[1].setHidden(true);
                    break;
                case "RIGHT":
                    hud[2].setHidden(true);
                    break;
                case "AUTO":
                    hud[3].setHidden(true);
                    break;
            }
        }
    }

    public void draw() {
        background(128);
        readSettings();
        int timeElapsed = millis() - lastPrint;

        //push();
        //hint(DISABLE_DEPTH_TEST);
        drawHUD();
        //hint(ENABLE_DEPTH_TEST);
        //pop();

        translate(width / 2, height / 2);

          /*push();
            //fill(32);
            //noStroke();
            rotate(rot);
            globe.setTexture(grid);
            noStroke();
            shape(globe);
          pop();
          rot += 0.001f;*/

        // hud was here

        // constrain x rotation only up to top of cube
        xr = constrain(xr, -90, 0);
        rotateX(radians(xr));

        // rotate y rotation according to auto_rotate status
        if (!auto_rotate) {
            rotateY(radians(yr));
        } else {
            //rotateY(radians(yr -= 0.5f));
            rotateY(radians(yr -= sensitivity));
            //rotateX(radians(xr -= 0.5f));
        }

        // x rotation was here


        // lerp rotate cube until new angle reached
        if (do_lerp) {

            // used to check if reset pressed to also reset x-rotation
            if (last_click_id == 0)
                xr = lerp(xr, xr_adjuster, 0.05f);

            // lerp rotate into new angle
            yr = lerp(yr, yr_adjuster, 0.05f);

            // stop lerp rotation after reaching within 0.5 degrees of new angle
            if (abs(yr - yr_adjuster) <= 0.5f && abs(xr - xr_adjuster) <= 0.5f)
                do_lerp = false;
        }

        //rotateY(degrees(yr));
        //rotateX(degrees(xr));
        //yr2 += sensitivity / 2;
        //rotateY(degrees(yr2));
        //rotateX(degrees(xr2 + 15f));
        drawCube();


        if (timeElapsed > DELAY) {

    /*if (b_solid == true) {
      //solidColor(lm2, 200, 200, 10);
      //b_solid = false;
    }

    if (b_crawl == true) {
      crawl(lm1, true, 10, 200, 20);
    }

    if (b_wipe == true) {
        m0.disable();
        //theaterChase(m0, 127, 127, 127);
        //theaterChase(m0, 127, 0, 0);
        //theaterChase(m0, 0, 0, 127);
        //colorWipe(m0, map(timeElapsed, 0, 1000, 0, 255), 0, 0);
      }

      if (b_rainbow == true) {
        //m0.disable();
        //rainbow(m0, 0);
        rainbow(lm2, 2);
      }

      if (b_text == true) {
        //m0.disable();
        //textScroll(m0, "AAAA" + " ", 24, 24, 24);
        textScroll(lm1, ("test "), 200, 200, 200);
      }*/

            String parsedEffects = CreateFragment.parseList();

            parseEffectList(parsedEffects);
            //parseEffectList("Scrolling Text{11000;my name is kev;200;200;200},Solid Color{10001;200;200;0},"); good one
            //parseEffectList("Solid Color{10001;200;200;0},Scrolling Text{11000;my name is kev;200;200;200},");
            //parseEffectList("Scrolling Text{11000;my name;200;200;200},");

            // build container array first by parsing Android inputs
            // DONT check for dupes before adding new container

            //for (Matrix m : matrices) {
            //    m.clear();
            //}

            // clear effects if none to render
            if (parsedEffects == ",") {
                clearCube();
            }

            // TODO
            for (LinkedMatrixContainer container : containers) {

                // if (lm != null) ??
                //println(idx++);
                //println(container.getEffectType());
                //println(container.getParams());

                String[] params = container.params.split(";");

                switch (container.getEffectType()) {
                    case COLOR_SOLID:

                        // only draw bg once, not every frame to prevent overwriting
                        if (b_solid == true) {
                            solidColor(container.linked_matrix, Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                            //b_solid = false;
                        }
                        break;
                    case COLOR_RAINBOW:
                        rainbow(container.linked_matrix, 4); // was 3
                        break;
                    case TEXT_SCROLL:
                        textScroll(container.linked_matrix, params[0] + " ", Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]));
                        break;
                }
            }
            lastPrint = millis();
        }
    }

    /**
     * Renders each available 2D HUD element on the canvas without
     * being affected by the 3D canvas' depth dimension.
     */
    void drawHUD() {
        hint(DISABLE_DEPTH_TEST);
        for (HUDElement e : hud) {
            if (!e.isHidden())
                e.display();
        }
        hint(ENABLE_DEPTH_TEST);
    }

    void clearCube() {
        // 10101
        //int m0, m1 = 0, m2, m3, m4;
        int[] xyz = {0, 0, 0, 0, 0};
        for (LinkedMatrixContainer lmc : containers) {
            String x = lmc.getSelections();

            for (int i = 0; i < x.length(); i++) {
                if (x.charAt(i) == '1') {
                    xyz[i]++;
                }
            }
        }

        if (xyz[0] == 0) {
            m0.clear();
        }
        if (xyz[1] == 0) {
            m1.clear();
        }
        if (xyz[2] == 0) {
            m2.clear();
        }
        if (xyz[3] == 0) {
            m3.clear();
        }
        if (xyz[4] == 0) {
            m4.clear();
        }
    }

    /**
     * https://stackoverflow.com/questions/43741561/how-do-i-is-it-posssible-to-keep-different-picture-in-each-side-of-box-in-p5
     *
     * Renders each matrix or 'side' of the cube by rotating
     * and translating the origin of the current view.
     */
    public void drawCube() {
        //angleMode(DEGREES); used radians(deg) instead

        int size = (int) (BOX_SIZE * SCALE_FACTOR);

        // render a slightly smaller cube within the LED cube to hide gaps between edges
        // (which strangely do not appear in Processing but do in Android)
        fill(20);
        box(size - 1);

        //push();
        // center the origin/view
        translate(-size / 2, -size / 2, size / 2);

        // render the front face of cube
        m0.display(); // red
        push();
            fill(200);
            textSize(32);
            translate(0, 0, 1);
            text("Front", size / 2, size + 32);
        pop();
        //pop();

        // render the right face of cube
        push();
            translate(size, 0, 0);
            rotateY(radians(90));
            m1.display(); // purple
            push();
                fill(200);
                textSize(32);
                translate(0, 0, 1);
                text("Right", size / 2, size + 32);
            pop();
        pop();

        // render the back face of cube
        push();
            rotateY(radians(180));
            translate(-size, 0, size);
            m2.display(); // white
            push();
                fill(200);
                textSize(32);
                translate(0, 0, 1);
                text("Back", size / 2, size + 32);
            pop();
        pop();

        // render the left face of cube
        push();
            translate(0, 0, -size);
            rotateY(radians(-90));
            m3.display(); // green
            push();
                fill(200);
                textSize(32);
                translate(0, 0, 1);
                text("Left", size / 2, size + 32);
            pop();
        pop();

        // render the top face of cube
        push();
            rotateX(radians(90));
            rotateZ(radians(270));
            //translate(0, 0, -size);
            //rotateX(radians(90));
            m4.display(); // blue
            push();
                fill(200);
                textSize(32);
                translate(0, 0, 1);
                //text("Top", size / 2, size + 32);
            pop();
        pop();

        // render the inactive bottom face of cube as dark gray
        push();
            fill(20, 20, 20);
            translate(0, size, 0);
            rotateX(radians(-90));
            rect(0, 0, size, size);
        pop();
    }

// Scrolling Text{xxxxx;string;int;int;int},Solid Color{xxxxx;int;int;int},RAINBOW{...},
    /**
     * Parses the given input and neatly separates each effect into the
     * LinkedMatrixContainer array.
     *
     * @param {String} input: Encoded comma-separated list of all effects.
     **
     *   Expected Effect Format:
     *     Foo{10010;bar; 0; 255; 123},
     *   where:
     *        Foo = the name/type of effect this is
     *      {...} = between brackets, the parameters for this effect
     *      10010 = the matrices to apply this effect to, where each digit corresponds to a matrix by index
     *               A '1' implies this matrix will have this effect whereas a '0' implies it will not.
     *               In this case, the first and fourth matrices will apply this effect; here are the indices for matrices:
     *                 0 - front of cube
     *                 1 - right of cube
     *                 2 - back of cube
     *                 3 - left of cube
     *                 4 - top of cube
     *      remaining values in brackets = the specified effect's parameters that vary by effect, usually contain RGB values
     *      trailing comma = marks end of effect to separate from next effect in input list
     */
    private void parseEffectList(String input) {

        // get individual list of each effect
        String[] isolated_inputs = input.split(",");

        // clear effect container to accomodate the new inputs
        containers = new LinkedMatrixContainer[isolated_inputs.length];

        // parse each effect from the list
        for (int i = 0; i < isolated_inputs.length; i++) {

            // extract current effect name, matrix selections and parameters
            String s = isolated_inputs[i];
            String params = s.substring(s.indexOf('{') + 1, s.length() - 1);
            String selections = params.substring(0, 5);
            params = params.substring(params.indexOf(';') + 1, params.length());

            // store the decoded effect into a container object and place it in the array
            switch (s.substring(0, s.indexOf('{'))) {

                case COLOR_SOLID:
                    //containers = (LinkedMatrixContainer[]) append(containers, new LinkedMatrixEffect_SolidColor(selections, params));
                    containers[i] = new LinkedMatrixContainer(selections, COLOR_SOLID, params);
                    break;
                case COLOR_RAINBOW:
                    //containers = (LinkedMatrixContainer[]) append(containers, new LinkedMatrixEffect_Rainbow(selections, params));
                    containers[i] = new LinkedMatrixContainer(selections, COLOR_RAINBOW, params);
                    break;
                case TEXT_SCROLL:
                    //containers = (LinkedMatrixContainer[]) append(containers, new LinkedMatrixEffect_TextScroll(selections, params));
                    containers[i] = new LinkedMatrixContainer(selections, TEXT_SCROLL, params);
                    break;
            }
            //println(containers[i].effect);
        }
        //println(containers[0].effect);
    }

/*Matrix[] getMatrices(String selections) {
  // 10001

  int count = 0;

  for (int i = 0; i < selections.length(); i++) {
    if (selections.charAt(i) == '1') {
      count++;
    }
  }

  Matrix[] temp = new Matrix[count];

  for (int i = 0; i < selections.length(); i++) {
    if (selections.charAt(i) == '1') {
      temp[i] = matrices[i];
      //linked_matrices[i] = new LinkedMatrix(new Matrix[]{m0, m2});
    }
  }

  return temp;
}

 //SCROLL{11111;my name is kev;200;0;200},SOLID{10001;0;200;0},

/*
linked_matrices[]
lm1 -> {m0, m1, m2, m3, m4}
lm2 -> {m1, m4}
lm3 -> {}
lm4 -> {}
lm5 -> {}
*/


/*void customizeMatrices(String selections) {


  for (int i = 0; i < linked_matrices.length; i++) {
    linked_matrices[i] = new LinkedMatrix(getMatrices(selections));
  }


}*/
    // was mousePressed
    public void touchStarted() {

        // stop lerp rotation when user interacts again
        do_lerp = false;

        // rotate back to reset position
        if (hud[0].isHovered()) {
            println("==> Resetting Position");
            last_click_id = hud[0].getID();
            //yr = 0f;
            //xr = 0f;
            do_lerp = true;
            yr_adjuster = xr_adjuster = 0f;
        }
        // rotate leftwards pressed
        else if (hud[1].isHovered()) {
            println("==> Rotating Left");
            last_click_id = hud[1].getID();
            //yr = 45f;
            do_lerp = true;
            yr_adjuster = yr + 90f;
        }
        // rotate rightwards pressed
        else if (hud[2].isHovered()) {
            println("==> Rotating Right");
            last_click_id = hud[2].getID();
            //yr = -45f;
            do_lerp = true;
            yr_adjuster = yr - 90f;
        }
        // rotate up
 /*else if (hud[4].isHovered()) {
   println("==> rotating up");
   last_click_id = hud[4].getID();
   xr = -45f;
 }
 // rotate down
 else if (hud[5].isHovered()) {
   last_click_id = hud[5].getID();
   println("==> rotating down");
   xr = 45f;
 }*/
        // auto rotate toggle pressed
        else if (hud[3].isHovered()) {
            println("==> Auto Rotate");
            last_click_id = hud[3].getID();
            ((HUDToggleButton) hud[3]).toggle();
            auto_rotate = !auto_rotate;
        } else {
            // record non-button/hover location click
            first_mx = mouseX;
            first_my = mouseY;
        }
    }

    /**
     * Calculates magnitude of slope between the specified coordinates.
     *
     * @param {int} x0: Initial x-location press.
     * @param {int} y0: Initial y-location press.
     * @param {int} x1: Final x-location release.
     * @param {int} y1: Final y-location release.
     *
     * @return {double} Calculated slope magnitude.
     */
    private double getSlope(int x0, int y0, int x1, int y1) {
        // check base case for perfectly vertical swipe
        if (x1 != x0) {
            return abs((float)((y1 - y0) / (x1 - x0)));
        } else {
            return 2;  // arbitrary value returned, as long as it's larger than 1
        }
    }

    // was touchMoved -> mouseDragged
    public void touchMoved() {

        // stop lerp rotation when user interacts again
        do_lerp = false;

        // determine slope from start and end of touch swipe
        double slope = getSlope(first_mx, first_my, mouseX, mouseY);

        // swipe was in x-direction
        if (slope <= 1.0) {

            // swipe was towards the right
            if (mouseX > pmouseX) {
                yr += sensitivity;
            }
            // swipe was towards the left
            else if (mouseX <= pmouseX) {
                yr -= sensitivity;
            }
        }
        // swipe was in y-direction
        else {
            // swipe was towards the top
            if (mouseY > pmouseY) {
                xr -= sensitivity;
            }
            // swipe was towards the base
            else if (mouseY <= pmouseY) {
                xr += sensitivity;
            }
        }
    }

    // smoother rotate
//    public void touchMoved() {
//
//        // quadrants with touch start/end?
//
//        // moving mouse to right
//        if (mouseX > pmouseX) {
//            //yr = lerp(yr, yr + radians(90), 0.05f);
//            //yr = radians(lerp(degrees(yr), degrees(yr) + 1, 0.05f));
//            //yr = radians(lerp(degrees(yr), degrees(yr) + radians(45), 0.05f));
//            yr = radians(lerp(degrees(yr), degrees(yr) + 2f, 0.005f));
//            //print(degrees(yr));
//            /*yr += sensitivity;
//            if (yr > radians(720f)) {
//                yr = 0f;
//            }*/
//        } else if (mouseX <= pmouseX) {
//            //yr = lerp(yr, yr + degrees(90), 0.05f);
//            //xr = lerp(xr, mouseY, 0.05f);
//            yr = radians(lerp(degrees(yr), degrees(yr) - 2f, 0.005f));
//            /*yr -= sensitivity;
//            if (yr < radians(0f)) {
//                yr = 720f;
//            }*/
//        }
//
//        // moving mouse down
//        if (mouseY > pmouseY) {
//            //xr-=1;
//            //xr -= sensitivity;
//            //if (xr > radians(-0.75f)) {
//                //xr -= sensitivity;
//            //}
//            xr = radians(lerp(degrees(xr), degrees(xr) - 2f, 0.005f));
//        } else if (mouseY <= pmouseY) {
//            //xr+=1;
//            //xr += sensitivity;
//            //if (xr < radians(0f)) {
//                //xr += sensitivity;
//            //}
//            xr = radians(lerp(degrees(xr), degrees(xr) + 2f, 0.005f));
//        }
//    }

    // was touchMoved
    /*public void touchMoved() {

        // to right
        if (mouseX > pmouseX) {
            yr += sensitivity;
            if (yr > radians(720f)) {
                yr = 0f;
            }
        } else if (mouseX <= pmouseX) {
            yr -= sensitivity;
            if (yr < radians(0f)) {
                yr = 720f;
            }
        }

        // moving mouse down
        if (mouseY > pmouseY) {
            //xr-=1;
            //xr -= sensitivity;
            if (xr > radians(-0.75f)) {
                xr -= sensitivity;
            }
        } else if (mouseY <= pmouseY) {
            //xr+=1;
            //xr += sensitivity;
            if (xr < radians(0f)) {
                xr += sensitivity;
            }
        }
    }*/

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

    /**
     * Fill the matrix with a single color.
     *
     * @param {MatrixTemplate} matrix: Matrix object to animate.
     * @param {int} r: The desired red RGB value.
     * @param {int} g: The desired green RGB value.
     * @param {int} b: The desired blue RGB value.
     */
    public void  solidColor(MatrixTemplate matrix, int r, int g, int b) {
        for (int i = 0; i < matrix.getLEDCount(); i++) {
            if (matrix.getPriorityAt(i) == 0)// added
                matrix.setLEDAtIndex(i, r, g, b, 0);
                //matrix.setLEDAtIndex(i, r, g, b, pos);
        }
    }

    /**
     * Animate a single LED scrolling across the matrix, reversing its
     * direction between each row.
     *
     * @param {MatrixTemplate} matrix: Matrix object to animate.
     * @param {boolean} backtrack: Reverse the animation upon completion.
     * @param {int} r: The desired red RGB value.
     * @param {int} g: The desired green RGB value.
     * @param {int} b: The desired blue RGB value.
     */
    public void crawl(MatrixTemplate matrix, boolean backtrack, int r, int g, int b) {

        // reverse y-direction when matrix y-axis borders reached
        if (y_crawl < 0 || y_crawl > (matrix.getRowSize() - 1)) {
            crawl_direction *= -1;
        }

        // reverse x-direction when matrix x-axis borders reached
        if (x_crawl <= -1 || x_crawl >= (matrix.getRowSize() * matrix.getMatrixCount())) {

            // reverse direction
            crawl_back = !crawl_back;

            // update next row to crawl
            y_crawl += crawl_direction;
        }

        // undo the previous LED and draw the current one
        if (crawl_back == false) {

            // undo previous LED in column, row below, or row above, respectively
            matrix.undoLEDAtCoord(x_crawl - 1, y_crawl);
            matrix.undoLEDAtCoord(x_crawl, y_crawl - 1);
            matrix.undoLEDAtCoord(x_crawl, y_crawl + 1);

            // draw current LED
            matrix.setLEDAtCoord(x_crawl++, y_crawl, r, g, b);
        } else {

            // undo previous LED in column, row above, or row below, respectively
            matrix.undoLEDAtCoord(x_crawl + 1, y_crawl);
            matrix.undoLEDAtCoord(x_crawl, y_crawl + 1);
            matrix.undoLEDAtCoord(x_crawl, y_crawl - 1);

            // draw current LED
            matrix.setLEDAtCoord(x_crawl--, y_crawl, r, g, b);
        }

        //print("x: " + x_crawl + "\n" + "y: " + y_crawl + "\n\n");
    }

    /**
     * ???
     *
     * @param {MatrixTemplate} matrix: Matrix object to animate.
     * @param {int} r: The desired red RGB value.
     * @param {int} g: The desired green RGB valuet.
     * @param {int} b: The desired blue RGB value.
     */
    public void  colorWipe(MatrixTemplate matrix, int r, int g, int b) {
        for (int i = 0; i < 64; i++) {
            matrix.setLEDAtIndex(i, r, g, b);
        }
    }

    /**
     * Returns a diagram of the specified character so that it
     * can be interpreted onto a matrix object. It retrieves
     * the appropriate diagram by using the preloaded diagrams
     * labeled by the character they correspond to.
     *
     * @param {char} character: Character to create diagram of.
     *
     * @return {String} Diagram of specified character as string.
     */
    public String toMatrix(char character) {
        //return eval("txt_" + character + ".join('');");

        String result = "";

        switch(character) {
            case '_':
                result = String.join("", txt__);
                break;
            case 'A':
                result = String.join("", txt_A);
                break;
            case 'B':
                result = String.join("", txt_B);
                break;
            case 'C':
                result = String.join("", txt_C);
                break;
            case 'D':
                result = String.join("", txt_D);
                break;
            case 'E':
                result = String.join("", txt_E);
                break;
            case 'F':
                result = String.join("", txt_F);
                break;
            case 'G':
                result = String.join("", txt_G);
                break;
            case 'H':
                result = String.join("", txt_H);
                break;
            case 'I':
                result = String.join("", txt_I);
                break;
            case 'J':
                result = String.join("", txt_J);
                break;
            case 'K':
                result = String.join("", txt_K);
                break;
            case 'L':
                result = String.join("", txt_L);
                break;
            case 'M':
                result = String.join("", txt_M);
                break;
            case 'N':
                result = String.join("", txt_N);
                break;
            case 'O':
                result = String.join("", txt_O);
                break;
            case 'P':
                result = String.join("", txt_P);
                break;
            case 'Q':
                result = String.join("", txt_Q);
                break;
            case 'R':
                result = String.join("", txt_R);
                break;
            case 'S':
                result = String.join("", txt_S);
                break;
            case 'T':
                result = String.join("", txt_T);
                break;
            case 'U':
                result = String.join("", txt_U);
                break;
            case 'V':
                result = String.join("", txt_V);
                break;
            case 'W':
                result = String.join("", txt_W);
                break;
            case 'X':
                result = String.join("", txt_X);
                break;
            case 'Y':
                result = String.join("", txt_Y);
                break;
            case 'Z':
                result = String.join("", txt_Z);
                break;
            case '0':
                result = String.join("", txt_0);
                break;
            case '1':
                result = String.join("", txt_1);
                break;
            case '2':
                result = String.join("", txt_2);
                break;
            case '3':
                result = String.join("", txt_3);
                break;
            case '4':
                result = String.join("", txt_4);
                break;
            case '5':
                result = String.join("", txt_5);
                break;
            case '6':
                result = String.join("", txt_6);
                break;
            case '7':
                result = String.join("", txt_7);
                break;
            case '8':
                result = String.join("", txt_8);
                break;
            case '9':
                result = String.join("", txt_9);
                break;
            default:
                result = String.join("", txt__);
        }

        return result;
    }

    /**
     * Animates the specified text to scroll across the matrix.
     *
     * @param {MatrixTemplate} matrix: Matrix object to animate.
     * @param {String} text: Text to scroll across the matrix.
     * @param {int} r: The desired red RGB value of this text.
     * @param {int} g: The desired green RGB value of this text.
     * @param {int} b: The desired blue RGB value of this text.
     */
    public void textScroll(MatrixTemplate matrix, String text, int r, int g, int b) {

        String formatted_text = text.replace(" ", "_").toUpperCase();
        String[] chars = new String[formatted_text.length() + 1];

        for (int i = 0; i < formatted_text.length(); i++) {
            //chars.push(toMatrix(formatted_text.charAt(i)));
            chars[i] = toMatrix(formatted_text.charAt(i));
        }

        //print(formatted_text);
        //if (formatted_text.charAt(formatted_text.length - 1) != '_')
        //chars.push(toMatrix('_'));
        chars[chars.length - 1] = toMatrix('_');

        if (letter_pos >= 8) {
            letter_pos = 0;
            char_index++;

            if (char_index >= chars.length - 1) {
                char_index = 0;
            }
        }

        matrix.scroll(1, r, g, b);

        matrix.addLetter(chars[char_index], letter_pos++, r, g, b);
    }

    /**
     * Animates a rainbow effect pulsating from a specified corner or center.
     * The hue slightly changes each row or column the rainbow moves and the
     * color changes with each new "pulse" that is released.
     *
     * @param {MatrixTemplate} matrix: Matrix object to animate.
     * @param {int} origin: Effect will start from this quadrant's corner (0 for center).
     */
    public void rainbow(MatrixTemplate matrix, int origin) {

        // M - x
        // 1 - 0
        // 2 - 4
        // 3 - 8
        // 4 - 12

        int length = matrix.getLEDCount() / matrix.getRowSize();
        int matrices = matrix.getMatrixCount();

        // reset ??????
        if (frames_rainbow > max_rainbow)
            frames_rainbow = 0;

        // reset position if it exceeds matrix size
        if (xy_rainbow >= length) {
            xy_rainbow = 0;
        }

        // generate a color for the current hue
        push();
        colorMode(HSB, 100);
        float p = map(frames_rainbow, 0, max_rainbow, 0, 100/*350*/);
        //print(ceil(p));
        //color c = color("hsb(" + ceil(p) + ", 100%, 100%)");
        int c = color(ceil(p), 100, 100);
        pop();

        switch(origin) {

            // center of matrix
            case 0:

                int offset = (matrices - 1) * 4;

                for (int y = length / 2; y <= xy_rainbow; y++) {

                    //print("y: " + y + "\n");
                    //print("xy_rainbow: " + xy_rainbow + "\n");

                    // old
                    //matrix.setLEDAtCoord((length - 1) - y, xy_rainbow, red(c), green(c), blue(c));
                    //matrix.setLEDAtCoord((length - 1) - xy_rainbow, y, red(c), green(c), blue(c));

                    matrix.setLEDAtCoord((length - 1) - y, xy_rainbow - offset, (int) red(c), (int) green(c), (int) blue(c));
                    matrix.setLEDAtCoord((length - 1) - xy_rainbow, y - offset, (int) red(c), (int) green(c), (int) blue(c));

                    // old
                    //matrix.setLEDAtCoord(y, xy_rainbow, red(c), green(c), blue(c));
                    //matrix.setLEDAtCoord(xy_rainbow, y, red(c), green(c), blue(c));

                    matrix.setLEDAtCoord(y, xy_rainbow - offset, (int) red(c), (int) green(c), (int) blue(c));
                    matrix.setLEDAtCoord(xy_rainbow, y - offset, (int) red(c), (int) green(c), (int) blue(c));

                    // old
                    //matrix.setLEDAtCoord(xy_rainbow, (length - 1) - y, red(c), green(c), blue(c));
                    //matrix.setLEDAtCoord(y, (length - 1) - xy_rainbow, red(c), green(c), blue(c));

                    matrix.setLEDAtCoord(xy_rainbow, (length - offset) - y - 1, (int) red(c), (int) green(c), (int) blue(c));
                    matrix.setLEDAtCoord(y, (length - offset) - xy_rainbow - 1, (int) red(c), (int) green(c), (int) blue(c));

                    // old
                    //matrix.setLEDAtCoord((length - 1) - y, (length - 1) - xy_rainbow, red(c), green(c), blue(c));
                    //matrix.setLEDAtCoord((length - 1) - xy_rainbow, (length - 1) - y, red(c), green(c), blue(c));

                    matrix.setLEDAtCoord((length ) - y - 1, (length - offset) - xy_rainbow - 1, (int) red(c), (int) green(c), (int) blue(c));
                    matrix.setLEDAtCoord((length ) - xy_rainbow - 1, (length - offset) - y -1, (int) red(c), (int) green(c), (int) blue(c));
                }
                break;

            // 1st quadrant, top right
            case 1:

                for (int y = 0; y <= xy_rainbow; y++) {
                    matrix.setLEDAtCoord((length - 1) - y, xy_rainbow, (int) red(c), (int) green(c), (int) blue(c));
                    matrix.setLEDAtCoord((length - 1) - xy_rainbow, y, (int) red(c), (int) green(c), (int) blue(c));
                }
                break;

            // 2nd quadrant, top left
            case 2:
                if (xy_rainbow >= length) {
                    xy_rainbow = 0;
                }

                for (int y = 0; y <= xy_rainbow; y++) {
                    matrix.setLEDAtCoord(y, xy_rainbow, (int) red(c), (int) green(c), (int) blue(c));
                    matrix.setLEDAtCoord(xy_rainbow, y, (int) red(c), (int) green(c), (int) blue(c));
                }
                break;

            // 3rd quadrant, bottom left
            case 3:

                for (int y = 0; y <= xy_rainbow; y++) {
                    //matrix.setLEDAtCoord(xy_rainbow, (length - 1) - y, red(c), green(c), blue(c));
                    //matrix.setLEDAtCoord(y, (length - 1) - xy_rainbow, red(c), green(c), blue(c));

                    matrix.setLEDAtCoord(xy_rainbow, (length / matrices) - y - 1, (int) red(c), (int) green(c), (int) blue(c));
                    matrix.setLEDAtCoord(y, (length / matrices) - xy_rainbow - 1, (int) red(c), (int) green(c), (int) blue(c));
                }
                break;

            // 4th quadrant, bottom right
            case 4:

                for (int y = 0; y <= xy_rainbow; y++) {
                    //matrix.setLEDAtCoord((length - 1) - y, (length - 1) - xy_rainbow, red(c), green(c), blue(c));
                    //matrix.setLEDAtCoord((length - 1) - xy_rainbow, (length - 1) - y, red(c), green(c), blue(c));

                    matrix.setLEDAtCoord((length ) - y - 1, (length / matrices) - xy_rainbow - 1, (int) red(c), (int) green(c), (int) blue(c));
                    matrix.setLEDAtCoord((length ) - xy_rainbow - 1, (length / matrices) - y -1, (int) red(c), (int) green(c), (int) blue(c));
                }
                break;
        }

        // increment hue and position values
        frames_rainbow++;
        xy_rainbow++;
    }

    /* ------------------------------ CLASSES ------------------------------ */

    /**
     * LED object contains information about its state such as position and
     * color to allow for it to be individually addressed.
     *
     * @author Kevin Kowalski
     */

    final int LED_DISABLED_RGB_VALUE = 16;

    class LED {

        int x, y, size, r, g, b, prev_r, prev_g, prev_b, state;

        /**
         * Constructor for the LED object.
         *
         * @param {int} index: The index of this LED.
         * @param {int} size: The pixel size of this LED.
         * @param {int} r: The desired red RGB value.
         * @param {int} g: The desired green RGB value.
         * @param {int} b: The desired blue RGB value.
         */
        LED(int index, int size, int r, int g, int b) {
            this.x = index % 8;
            this.y = floor(index / 8);

            this.size = size;

            this.r = r;
            this.g = g;
            this.b = b;

            this.prev_r = r;
            this.prev_g = g;
            this.prev_b = b;

            this.state = 0;  // priority of LED, the higher it is the less likely it is to be overwritten by another effect/function
        }

        /**
         * Alternative constructor for the LED object.
         *
         * @param {int} x: The column coordinate of this LED.
         * @param {int} y: The row coordinate of this LED.
         * @param {int} size: The pixel size of this LED.
         * @param {int} r: The desired red RGB value.
         * @param {int} g: The desired green RGB value.
         * @param {int} b: The desired blue RGB value.
         */
          /*LED2(int x, int y, int size, int r, int g, int b) {
            this.x = x;
            this.y = y;
            this.size = size;

            this.r = r;
            this.g = g;
            this.b = b;

            this.prev_r = r;
            this.prev_g = g;
            this.prev_b = b;
          }*/

        /**
         * Renders this LED based on its current state (meaning position,
         * size, shape and color).
         *
         * @param {boolean} isCircular: Decides LED shape.
         */
        public void display(boolean isCircular) {
            if (isCircular) {

                // render the PCB or background of the LED
                fill(8, 8, 8);
                rect(this.x * this.size, this.y * this.size, this.size, this.size);

                // circles drawn from center, so adjust origin (and on top of background)
                push();
                translate(this.size / 2, this.size / 2, 1);
                fill(this.r, this.g, this.b);
                circle(this.x * this.size, this.y * this.size, this.size / 2);
                pop();

            } else {

                // render the PCB or background of the LED
                /*fill(8, 8, 8);
                rect(this.x * this.size, this.y * this.size, this.size, this.size);

                push();
                    translate(this.size / 6, this.size / 6, 1);
                    fill(this.r, this.g, this.b);
                    rect(this.x * this.size, this.y * this.size, this.size / 1.5, this.size / 1.5);
                pop();
                */
                // render a simple square for this LED
                fill(this.r, this.g, this.b);
                rect(this.x * this.size, this.y * this.size, this.size, this.size);
            }
        }

        /**
         * Returns an array of this LED's current RGB values.
         *
         * @return {int array} An integer array of this LED's current RGB values.
         */
        public int[] getColor() {
            //int[] colors = {this.r, this.g, this.b};
            return new int[] {this.r, this.g, this.b};
        }

        /**
         * Returns an array of this LED's previous RGB values.
         *
         * @return {int array} An integer array of this LED's previous RGB values.
         */
        public int[] getPrevColor() {
            return new int[] {this.prev_r, this.prev_g, this.prev_b};
        }

        /**
         * Returns the priority state of the current LED.
         *
         * @return {int} The priority state of this LED.
         */
        public int getPriority() {
            return this.state;
        }

        /**
         * Sets the RGB value of this LED and stores its previous color.
         *
         * @param {int} r: The desired red RGB value.
         * @param {int} g: The desired green RGB value.
         * @param {int} b: The desired blue RGB value.
         */
        public void setColor(int r, int g, int b) {
            this.prev_r = this.r;
            this.prev_g = this.g;
            this.prev_b = this.b;

            this.r = r;
            this.g = g;
            this.b = b;
        }

        /**
         * Updates the priority of the current LED to the given rank.
         *
         * @param {int} newState: The new rank/priority of this LED.
         */
        public void setPriority(int newState) {
            this.state = newState;
        }

        /**
         * Sets the RGB value of this LED to its previous state.
         */
        public void undo() {
            this.r = this.prev_r;
            this.g = this.prev_g;
            this.b = this.prev_b;
        }

        /**
         * Visually turns off this LED by setting its RGB value and
         * previous state to a dark gray.
         */
        public void disable() {
            this.prev_r = LED_DISABLED_RGB_VALUE;
            this.prev_g = LED_DISABLED_RGB_VALUE;
            this.prev_b = LED_DISABLED_RGB_VALUE;

            this.r = LED_DISABLED_RGB_VALUE;
            this.g = LED_DISABLED_RGB_VALUE;
            this.b = LED_DISABLED_RGB_VALUE;

            this.state = 0;
        }

        /**
         * Clears this LED by setting its RGB value to a dark gray.
         */
        public void clear() {
            this.r = LED_DISABLED_RGB_VALUE;
            this.g = LED_DISABLED_RGB_VALUE;
            this.b = LED_DISABLED_RGB_VALUE;
        }
    }

    /**
     * Matrix object contains an array of all the individually addressable
     * LEDs on its face.
     *
     * @author Kevin Kowalski
     */

    final int MATRIX_ROWS = 8;
    final int MATRIX_COLS = 8;
    final int MATRIX_LEDS = MATRIX_ROWS * MATRIX_COLS;

    class Matrix extends MatrixTemplate {

        int size, id, r, g, b;
        boolean doCircular;

        ledsign.LED[] matrix;

        /**
         * Constructor for the matrix object. Creates a grid of LEDs according
         * to the MATRIX_LEDS variable above at the specified render size.
         *
         * @param {int} size: The requested rendered size of the matrix.
         * @param {int} id: The unique identity of this matrix.
         * @param {int} r: The desired red RGB value.
         * @param {int} g: The desired green RGB value.
         * @param {int} b: The desired blue RGB value.
         */
        Matrix(int size, int id, int r, int g, int b) {

            // calculate size (pixel width) of an LED based on the amount needed per row
            this.size = size / MATRIX_ROWS;

            // unique identity value for this matrix
            this.id = id;

            // initial color of matrix
            this.r = r;
            this.g = g;
            this.b = b;

            // shape of LEDs on matrix
            this.doCircular = false;

            // array to store each LED's state
            this.matrix = new ledsign.LED[MATRIX_LEDS];

            // populate the matrix with the appropriate amount of LEDs
            for (int i = 0; i < MATRIX_LEDS; i++) {
                //this.matrix.push(new LED(i, this.size, this.r, this.g, this.b));
                //this.matrix = (LED[]) append(this.matrix, new LED(i, this.size, this.r, this.g, this.b));
                this.matrix[i] = new ledsign.LED(i, this.size, this.r, this.g, this.b);
            }

            // set initial color of LEDs as disabled color
            this.disable();
        }

        /**
         * Renders each of the matrix's LEDs based on their current state.
         */
        public void display() {
            for (int i = 0; i < MATRIX_LEDS; i++) {
                this.matrix[i].display(this.doCircular);
            }
        }

        /**
         * Returns the amount of matrices linked together.
         *
         * @return {int} Single matrices always length of one.
         */
        public @Override
        int getMatrixCount() {
            return 1;
        }

        /**
         * Returns this matrix's LED count.
         *
         * @return {int} Amount of LEDs present in matrix.
         */
        public @Override
        int getLEDCount() {
            return this.matrix.length;
        }

        /**
         * Returns the row dimension of this matrix.
         *
         * @return {int} Total amount of rows present in this matrix.
         */
        public @Override
        int getRowSize() {
            return MATRIX_ROWS;
        }

        /**
         * Returns the LED priority at the specified index.
         *
         * @param {int} index: The index of the LED to fetch from.
         *
         * @return {int} LED priority at specified index.
         */
        public @Override
        int getPriorityAt(int index) {
            return matrix[index].getPriority();
        }

        /**
         * Returns the LED at the specified index.
         *
         * @param {int} x: The column coordinate of this LED.
         * @param {int} y: The row coordinate of this LED.
         *
         * @return {LED} LED object at specified index.
         */
        public ledsign.LED getLEDAt(int x, int y) {
            return this.matrix[((int) floor(x % MATRIX_ROWS)) + (y * MATRIX_COLS)];
        }

        /**
         * Returns this matrix's unique ID value.
         *
         * @return {int} Unique ID passed during construction.
         */
        public int getID() {
            return this.id;
        }

        /**
         * Displays this matrix's unique ID value using its LEDs.
         */
        public @Override
        void identify() {

            // clear the matrix before rendering to it
            this.disable();

            // todo
        }

        /**
         * Toggle the shape of the LEDs between circular or square.
         */
        public void toggleShape() {
            this.doCircular = !this.doCircular;
        }

        /**
         * Sets the RGB value of the LED at the specified index.
         *
         * @param {int} index: The index of the requested LED.
         * @param {int} r: The desired red RGB value.
         * @param {int} g: The desired green RGB value.
         * @param {int} b: The desired blue RGB value.
         */
        public @Override
        void setLEDAtIndex(int index, int r, int g, int b) {
            if (index >= 0 && index < MATRIX_LEDS)
                this.matrix[index].setColor(r, g, b);
        }

        /**
         * Sets the RGB value and priority of the LED at the specified index.
         *
         * @param {int} index: The index of the requested LED.
         * @param {int} r: The desired red RGB value.
         * @param {int} g: The desired green RGB value.
         * @param {int} b: The desired blue RGB value.
         * @param {int} p: The desired priority level.
         */
        public @Override
        void setLEDAtIndex(int index, int r, int g, int b, int p) {
            if (index >= 0 && index < MATRIX_LEDS)
                this.matrix[index].setColor(r, g, b);
            this.matrix[index].setPriority(p);
        }

        /**
         * Sets the RGB value of the LED at the specified coordinate.
         *
         * @param {int} x: The column coordinate of the requested LED.
         * @param {int} y: The row coordinate of the requested LED.
         * @param {int} r: The desired red RGB value.
         * @param {int} g: The desired green RGB value.
         * @param {int} b: The desired blue RGB value.
         */
        public @Override
        void setLEDAtCoord(int x, int y, int r, int g, int b) {
            if (x >= 0 && x < MATRIX_ROWS && y >= 0 && y < MATRIX_COLS)
                this.matrix[x + (y * MATRIX_COLS)].setColor(r, g, b);
        }

        /**
         * Sets the RGB value and priority of the LED at the specified coordinate.
         *
         * @param {int} x: The column coordinate of the requested LED.
         * @param {int} y: The row coordinate of the requested LED.
         * @param {int} r: The desired red RGB value.
         * @param {int} g: The desired green RGB value.
         * @param {int} b: The desired blue RGB value.
         * @param {int} p: The desired priority level.
         */
        public @Override
        void setLEDAtCoord(int x, int y, int r, int g, int b, int p) {
            if (x >= 0 && x < MATRIX_ROWS && y >= 0 && y < MATRIX_COLS)
                this.matrix[x + (y * MATRIX_COLS)].setColor(r, g, b);
            this.matrix[x + (y * MATRIX_COLS)].setPriority(p);
        }

        /**
         * Sets the RGB value of the LED at the specified index to
         * its previous RGB state.
         *
         * @param {int} index: The index of the requested LED.
         */
        public @Override
        void undoLEDAtIndex(int index) {
            if (index >= 0 && index < MATRIX_LEDS)
                this.matrix[index].undo();
        }

        /**
         * Sets the RGB value of the LED at the specified coordinate
         * to its previous RGB state.
         *
         * @param {int} x: The column coordinate of the requested LED.
         * @param {int} y: The row coordinate of the requested LED.
         */
        public @Override
        void undoLEDAtCoord(int x, int y) {
            if (x >= 0 && x < MATRIX_ROWS && y >= 0 && y < MATRIX_COLS)
                this.matrix[x + (y * MATRIX_COLS)].undo();
        }

        /**
         * Visually turns off all of the LEDs on this matrix.
         */
        public @Override
        void disable() {
            for (int i = 0; i < MATRIX_LEDS; i++) {
                this.matrix[i].disable();
            }
        }

        /**
         * Clears all of the LEDs on this matrix.
         */
        public @Override
        void clear() {
            for (int i = 0; i < MATRIX_LEDS; i++) {
                this.matrix[i].clear();
            }
        }

        /**
         * Scrolls the contents of this matrix to the left by the
         * specified amount of columns.
         *
         * @param {int} offset: The amount of columns to scroll to.
         * @param {int} bg_r: The background red RGB value.
         * @param {int} bg_g: The background green RGB value.
         * @param {int} bg_b: The background blue RGB value.
         */
        public @Override
        void scroll(int offset, int bg_r, int bg_g, int bg_b) {
            for (int i = 0; i < MATRIX_LEDS; i++) {

                int[] led_color = this.matrix[i].getColor();

                if (i % MATRIX_ROWS != 0 && this.matrix[i].getPriority() == 1) {
                    this.setLEDAtIndex(i - offset, led_color[0], led_color[1], led_color[2], 1);
                    // maybe set old index to state zero
                    this.matrix[i].setPriority(0);
                }
                //else
                //this.setLEDAtIndex(i, bg_r, bg_g, bg_b);
            }
        }

        /**
         * Renders a single portion (column) of the given letter at the
         * specified RGB value.
         *
         * @param {String} letter: Diagram for the letter to render.
         * @param {int} column: The column of the diagram currently being rendered.
         * @param {int} r: The desired red RGB value of this letter.
         * @param {int} g: The desired green RGB value of this letter.
         * @param {int} b: The desired blue RGB value of this letter.
         */
        public @Override
        void addLetter(String letter, int column, int r, int g, int b) {
            for (int i = 0; i < MATRIX_ROWS; i++) {
                if (letter.charAt((i * MATRIX_COLS) + column) == '1')
                    this.setLEDAtCoord(MATRIX_COLS - 2, i, r, g, b, 1);
                    //this.setLEDAtCoord(MATRIX_COLS - 2, i, r, g, b, pos);
            }
            /*for (var i = 0; i < MATRIX_LEDS; i++) {
              if (letter.charAt(i) == '1')
                this.setLEDAtIndex(i, r, g, b);
            }*/
        }
    }

    /**
     * LinkedMatrixEffect_SolidColor object contains a reference to an individual
     * linked matrix that tracks which matrices require the specified effect.
     *
     * @author Kevin Kowalski
     */

    class LinkedMatrixEffect_SolidColor extends LinkedMatrixContainer {

        int r, g, b;

        /**
         * Constructor for the linked matrix effect solid color object. Creates a
         * linked matrix based on the selections provided and parses the parameters
         * associated with this effect.
         *
         * @param {String} selections: Ordered string of matrices in this object.
         * @param {String} params: Parameters for the effect applied to this object.
         */
        LinkedMatrixEffect_SolidColor(String selections, String params) {
            super(selections, COLOR_SOLID, params);

            this.effect = COLOR_SOLID;
            this.params = params;

            String[] isolated_params = params.split(";");

            r = Integer.parseInt(isolated_params[0]);
            g = Integer.parseInt(isolated_params[1]);
            b = Integer.parseInt(isolated_params[2]);
        }
    }
    /**
     * LinkedMatrixEffect_TextScroll object contains a reference to an individual
     * linked matrix that tracks which matrices require the specified effect.
     *
     * @author Kevin Kowalski
     */

    class LinkedMatrixEffect_TextScroll extends LinkedMatrixContainer {

        String text;
        int r, g, b;

        /**
         * Constructor for the linked matrix effect text scroll object. Creates a
         * linked matrix based on the selections provided and parses the parameters
         * associated with this effect.
         *
         * @param {String} selections: Ordered string of matrices in this object.
         * @param {String} params: Parameters for the effect applied to this object.
         */
        LinkedMatrixEffect_TextScroll(String selections, String params) {
            super(selections, TEXT_SCROLL, params);

            this.effect = TEXT_SCROLL;
            this.params = params;

            String[] isolated_params = params.split(";");

            text = isolated_params[0];

            r = Integer.parseInt(isolated_params[1]);
            g = Integer.parseInt(isolated_params[2]);
            b = Integer.parseInt(isolated_params[3]);
        }
    }

    /* ------------------------------ HUD CLASSES ------------------------------ */

    /**
     * HUDElement contains a template for any HUD element to be derived
     * from this class. At a minimum, any HUD element requires an (X,Y)
     * coordinate for it to be rendered at any point on the canvas.
     *
     * Note: Only child classes of HUDElement are intended to be drawn.
     *
     * @author Kevin Kowalski
     */

    static int element_count = 0;

    class HUDElement {

        int x, y, id, r, g, b;
        boolean hide;

        /**
         * Constructor for the HUDElement object.
         *
         * @param {int} x: The x coordinate of this element.
         * @param {int} y: The y coordinate of this element.
         */
        HUDElement(int x, int y) {
            this.x = x;
            this.y = y;

            this.r = this.g = this.b = 200;

            this.id = element_count++;

            this.hide = false;
        }

        /**
         * Renders this default HUDElement at its specified
         * coordinates and displays its unique ID.
         */
        void display() {
            fill(255);
            textSize(12);
            text("HUDElement=" + this.id, this.x, this.y);
        }

        /**
         * Returns the unique value of this element assigned
         * at construction.
         *
         * @return {int} Unique ID value of this element.
         */
        int getID() {
            return this.id;
        }

        /**
         * Returns the hide status of this element.
         *
         * @return {boolean} Hide status of this element.
         */
        boolean isHidden() {
            return this.hide;
        }

        /**
         * Updates the hide status of this element.
         *
         * @param {boolean} New hide status.
         */
        void setHidden(boolean hide) {
            this.hide = hide;
        }

        /**
         * Updates the hide status of this element.
         *
         * @param {int} New red RGB value for this element.
         * @param {int} New green RGB value for this element.
         * @param {int} New blue RGB value for this element.
         */
        void setColor(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        /**
         * Detects whether or not the mouse pointer is currently hovering
         * over the coordinate of this element.
         *
         * @return {boolean} Hover status of this element.
         */
        boolean isHovered() {
            return (mouseX == this.x && mouseY == this.y);
        }
    }

    /**
     * HUDButton ...
     *
     * @author Kevin Kowalski
     */

    class HUDButton extends HUDElement {

        int w, d = 128;//, r = 100, g = 100, b = 200;
        String text;

        /**
         * Constructor for the HUDButton object.
         *
         * @param {int} x: The x coordinate of this button.
         * @param {int} y: The y coordinate of this button.
         * @param {String} text: The label on this button.
         */
        HUDButton(int x, int y, String text) {
            super(x, y);
            //this.x = x;
            //this.y = y;

            this.text = text;

            this.w = text.length() * 24;

            //this.id = element_count++;
        }

        /**
         * Renders this HUDButton based on its parameters (including position,
         * size, label, shape and color).
         */
        @Override
        void display() {
            if (this.isHovered()) {
                this.r = this.g = this.b = 64;
            }
            else {
                this.r = (int) accentColor.red();
                this.g = (int) accentColor.green();
                this.b = (int) accentColor.blue();
            }
            fill(this.r, this.g, this.b);
            rect(this.x, this.y, this.w, this.d, 24);
            fill(200);
            textSize(128);
            text(this.text, (float) (this.x - this.d / 2.75), (float) (this.y + this.d / 1.15));
        }

        /**
         * Detects whether or not the mouse pointer is currently hovering
         * over the bounds of this button.
         *
         * @return {boolean} Hover status of this button.
         */
        boolean isHovered() {
            return (mouseX > this.x && mouseX < (this.x + this.w) &&
                    mouseY > this.y && mouseY < (this.y + this.d));
        }
    }

    /**
     * HUDToggleButton ...
     *
     * @author Kevin Kowalski
     */

    class HUDToggleButton extends HUDButton {

        int w, d = 128;//, r = 100, g = 100, b = 200;
        boolean isToggled = false;

        /**
         * Constructor for the HUDButton object.
         *
         * @param {int} x: The x coordinate of this button.
         * @param {int} y: The y coordinate of this button.
         * @param {String} text: The label on this button.
         */
        HUDToggleButton(int x, int y, String text) {
            super(x, y, text);
            //this.x = x;
            //this.y = y;

            //this.text = text;

            this.w = text.length() * 24;

            //this.id = element_count++;
        }

        /**
         * Renders this HUDButton based on its parameters (including position,
         * size, label, shape and color).
         */
        @Override
        void display() {
            if (!isToggled) {
                this.r = this.g = this.b = 64;
            } else {
                this.r = (int) accentColor.red();
                this.g = (int) accentColor.green();
                this.b = (int) accentColor.blue();
            }
            fill(this.r, this.g, this.b);
            rect(this.x, this.y, this.w, this.d, 24);
            fill(200);
            textSize(128);
            //text(this.text, this.x + 1, (float) (this.y + this.d / 1.5));
            text(this.text, (float) (this.x - this.d / 2.75), (float) (this.y + this.d / 1.15));
        }

        /**
         * Detects whether or not the mouse pointer is currently hovering
         * over the bounds of this button.
         */
        void toggle() {
            this.b = isToggled ? 200 : 100;
            isToggled = !isToggled;
        }
    }
}
