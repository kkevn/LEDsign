/**
 * LEDsign is the class that contains the Processing sketch for rendering the 3D preview sketch. It
 * includes the setup, draw, and other helping functions or logic for managing the sketch. The
 * sketch will obtain a live parsed version of the effects list made in profile creation. This data
 * will update the sketch to reflect the changes of the effects list.
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

import processing.core.*;

public class LEDsign extends PApplet {

    // initialize the type codes for each effect
    public final static String TEXT_SCROLL = "ST";
    public final static String COLOR_SOLID = "SC";
    public final static String COLOR_RAINBOW = "RW";
    public final static String COLOR_FADE = "F";
    public final static String COLOR_WIPE = "CW";
    public final static String THEATER_CHASE = "TC";

    // contains maps for the letters that can appear when scrolled
    String[] txt__, txt_0, txt_1, txt_2, txt_3, txt_4, txt_5, txt_6, txt_7, txt_8, txt_9, txt_A,
            txt_B, txt_C, txt_D, txt_E, txt_F, txt_G, txt_H, txt_I, txt_J, txt_K, txt_L, txt_M,
            txt_N, txt_O, txt_P, txt_Q, txt_R, txt_S, txt_T, txt_U, txt_V, txt_W, txt_X, txt_Y, txt_Z;

    // sketch rotation variables
    float sensitivity = 2.0f;
    float yr = 0f;
    float xr = 0f;
    boolean do_lerp = false;
    float yr_adjuster = 0f;
    float xr_adjuster = 0f;
    int last_click_id = -1;
    int first_mx = 0;
    int first_my = 0;

    // sketch size variables
    final int BOX_SIZE = 1000;
    final int DELAY = 100;
    double SCALE_FACTOR;
    int SIZE;

    // miscellaneous sketch variables
    String parsedEffects = ",", previousEffects = ",";
    PImage backgroundImage;
    int lastPrint;

    // Matrix related variables
    Matrix m0, m1, m2, m3, m4;
    static Matrix[] matrices;
    LinkedMatrixContainer[] containers;

    // preference related variables
    Color accentColor;
    HUDElement[] hud;
    boolean auto_rotate = false;

    // --- Effect variables below

    // Fade variables
    int fadeR = 255, fadeG = 0, fadeB = 0;

    // Crawl variables
    int x_crawl = 0;
    int y_crawl = 0;
    int crawl_direction = 1;
    boolean crawl_back = false;

    // Color Wipe variables
    int i_wipe = 0;

    // Theater Chase variables
    int i_chase = 0;
    int chase_dir = 1;
    int frames_rainbow_chase = 0;

    // Wave variables
    int xy_rainbow = 0;
    int max_rainbow = 100;
    int frames_rainbow = 0;

    // Scrolling Text variables
    int letter_pos = 0;
    int char_index = 0;

    /**
     * Runs once prior to drawing the sketch. Initializes all necessary objects such as sketch size
     * and Matrix sides.
     */
    public void setup() {

        // adjust box to fit current screen height
        SCALE_FACTOR = height / 2.0f / (double) BOX_SIZE;
        SIZE = (int) (BOX_SIZE * SCALE_FACTOR);

        // initialize Matrix objects
        m0 = new Matrix(SIZE, 0, 200, 0, 0); // front face
        m1 = new Matrix(SIZE, 1, 200, 0, 200); // right face
        m2 = new Matrix(SIZE, 2, 200, 200, 200); // back face
        m3 = new Matrix(SIZE, 3, 0, 200, 0); // left face
        m4 = new Matrix(SIZE, 4, 0, 0, 200); // top face
        matrices = new Matrix[]{m0, m1, m2, m3, m4};

        // initialize HUD elements
        hud = new HUDElement[4];
        hud[0] = new HUDButton((int) (width * 0.025), (int) (height * 0.025), "  R  ");
        hud[1] = new HUDButton((int) (width * 0.025), (int) (height * 0.8), "  < ");
        hud[2] = new HUDButton((int) (width * 0.875), (int) (height * 0.8), "  > ");
        hud[3] = new HUDToggleButton((int) (width * 0.875), (int) (height * 0.025), "  A  ");

        // background image
        //backgroundImage = loadImage("cube-grid.png");

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

    /**
     * Runs once prior to drawing the sketch. Initializes the sketch canvas.
     */
    public void settings() {
        size(displayWidth, displayHeight / 2, P3D);
    }

    /**
     * Reads the preference settings from the Settings fragment and updates the sketch to reflect
     * those preferences.
     */
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

    /**
     * Draws the sketch. Each call renders one frame. Handles all rendering of the sketch.
     */
    public void draw() {

        // draw the background and update preference settings
        background(128);
        readSettings();

        // update the last amount of time passed
        int timeElapsed = millis() - lastPrint;

        // draw the HUD
        drawHUD();

        // translate the skech to be centered on screen
        translate(width / 2, height / 2);

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

        // draw the hardware cube sketch
        drawCube();

        // only render the next frame of the hardware every DELAY amount
        if (timeElapsed > DELAY) {

            // update the parsed effects list trackers
            previousEffects = parsedEffects;
            parsedEffects = CreateFragment.parseList();

            // parse the effects list into objects that the sketch can understand
            parseEffectList(parsedEffects);

            // if changes detected in effects list
            if (!previousEffects.equals(parsedEffects)) {
                //println("change made!");
                //clearCube();
                //for (Matrix m : matrices) {
                //    m.clear();
                //}
            }

            // clear effects if none to render
            if (parsedEffects == ",") {
                clearCube();
            }

            // iterate over each container from the parsed effects
            for (LinkedMatrixContainer container : containers) {

                // obtain the parameters from current container
                String[] params = container.params.split(";");

                // render the current container's specified effect with the parameters
                switch (container.getEffectType()) {
                    case COLOR_SOLID:
                        solidColor(container.linked_matrix, Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]));
                        break;
                    case COLOR_RAINBOW:
                        rainbow(container.linked_matrix, 4);
                        break;
                    case COLOR_FADE:
                        fade(container.linked_matrix, Integer.parseInt(params[0]));
                        break;
                    case COLOR_WIPE:
                        colorWipe(container.linked_matrix, Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]));
                        break;
                    case THEATER_CHASE:
                        theaterChase(container.linked_matrix, Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]));
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
     * Renders each available 2D HUD element on the canvas without being affected by the 3D canvas'
     * depth dimension.
     */
    void drawHUD() {
        hint(DISABLE_DEPTH_TEST);
        for (HUDElement e : hud) {
            if (!e.isHidden())
                e.display();
        }
        hint(ENABLE_DEPTH_TEST);
    }

    /**
     * Clears each face of the cube that does not currently have an effect it needs to render.
     */
    void clearCube() {

        // initialize counters for each face with no selections on it
        int[] xyz = {0, 0, 0, 0, 0};

        // iterate over each container
        for (LinkedMatrixContainer lmc : containers) {
            String x = lmc.getSelections();

            // mark which Matrix faces have an effect to render
            for (int i = 0; i < x.length(); i++) {
                if (x.charAt(i) == '1') {
                    xyz[i]++;
                }
            }
        }

        // clear faces with no Matrix selection on that face
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
     * Renders each matrix or 'side' of the cube by rotating and translating the origin of the
     * current view.
     *
     * @source https://stackoverflow.com/questions/43741561/how-do-i-is-it-posssible-to-keep-different-picture-in-each-side-of-box-in-p5
     */
    public void drawCube() {

        // initialize the size of the cube size
        int size = (int) (BOX_SIZE * SCALE_FACTOR);

        // render a slightly smaller cube within the LED cube to hide gaps between edges
        // (which strangely do not appear in Processing IDE but do in Android)
        fill(20);
        box(size - 1);

        // center the origin/view
        translate(-size / 2, -size / 2, size / 2);

        // render the front face of cube
        m0.display();
        push();
            fill(200);
            textSize(32);
            translate(0, 0, 1);
            text("Front", size / 2, size + 32);
        pop();

        // render the right face of cube
        push();
            translate(size, 0, 0);
            rotateY(radians(90));
            m1.display();
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
            m2.display();
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
            m3.display();
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
            m4.display();
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

    /**
     * Parses the given input and neatly separates each effect into the LinkedMatrixContainer array.
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
                    containers[i] = new LinkedMatrixContainer(selections, COLOR_SOLID, params);
                    break;
                case COLOR_RAINBOW:
                    containers[i] = new LinkedMatrixContainer(selections, COLOR_RAINBOW, params);
                    break;
                case COLOR_FADE:
                    containers[i] = new LinkedMatrixContainer(selections, COLOR_FADE, params);
                    break;
                case COLOR_WIPE:
                    containers[i] = new LinkedMatrixContainer(selections, COLOR_WIPE, params);
                    break;
                case THEATER_CHASE:
                    containers[i] = new LinkedMatrixContainer(selections, THEATER_CHASE, params);
                    break;
                case TEXT_SCROLL:
                    containers[i] = new LinkedMatrixContainer(selections, TEXT_SCROLL, params);
                    break;
            }
        }
    }

    /**
     * Detects touch actions on the sketch. Used to identify if a HUD elements if pressed or the
     * sketch is simply being rotated
     */
    public void touchStarted() {

        // stop lerp rotation when user interacts again
        do_lerp = false;

        // determine what was pressed
        if (hud[0].isHovered()) {

            // rotate back to reset position
            println("==> Resetting Position");
            last_click_id = hud[0].getID();
            //yr = 0f;
            //xr = 0f;
            do_lerp = true;
            yr_adjuster = xr_adjuster = 0f;

        } else if (hud[1].isHovered()) {

            // rotate leftwards pressed
            println("==> Rotating Left");
            last_click_id = hud[1].getID();
            //yr = 45f;
            do_lerp = true;
            yr_adjuster = yr + 90f;

        } else if (hud[2].isHovered()) {

            // rotate rightwards pressed
            println("==> Rotating Right");
            last_click_id = hud[2].getID();
            //yr = -45f;
            do_lerp = true;
            yr_adjuster = yr - 90f;

        } else if (hud[3].isHovered()) {

            // auto rotate toggle pressed
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

    /**
     * Detects touch movement on the sketch. Used to rotate the hardware cube with drag gestures.
     */
    public void touchMoved() {

        // stop lerp rotation when user interacts again
        do_lerp = false;

        // determine slope from start and end of touch swipe
        double slope = getSlope(first_mx, first_my, mouseX, mouseY);

        // swipe was in x-direction
        if (slope <= 1.0) {

            // swipe was towards the right, rotate cube rightwards
            if (mouseX > pmouseX) {
                yr += sensitivity;

            } else if (mouseX <= pmouseX) {

                // swipe was towards the left, rotate cube leftwards
                yr -= sensitivity;
            }
        } else {

            // swipe was in y-direction

            // swipe was towards the top, rotate cube upwards
            if (mouseY > pmouseY) {
                xr -= sensitivity;

            } else if (mouseY <= pmouseY) {

                // swipe was towards the base, rotate cube downwards
                xr += sensitivity;
            }
        }
    }

    /* ------------------------------ EFFECTS ------------------------------ */

    /**
     * Fill the matrix with a single color.
     *
     * @param {MatrixTemplate} matrix: Matrix object to animate.
     * @param {int} r: The desired red RGB value.
     * @param {int} g: The desired green RGB value.
     * @param {int} b: The desired blue RGB value.
     */
    public void solidColor(MatrixTemplate matrix, int r, int g, int b) {
        for (int i = 0; i < matrix.getLEDCount(); i++) {
            if (matrix.getPriorityAt(i) == 0)// added
                matrix.setLEDAtIndex(i, r, g, b, 0);
        }
    }

    /**
     * Animate a single LED scrolling across the matrix, reversing its direction between each row.
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
    }

    /**
     * Animates a single LED trailing across the entirety of its parent Matrix.
     *
     * @param {MatrixTemplate} matrix: Matrix object to animate.
     * @param {int} r: The desired red RGB value.
     * @param {int} g: The desired green RGB value.
     * @param {int} b: The desired blue RGB value.
     * @param {int} runs: The amount of times to iterate this effect.
     */
    public void colorWipe(MatrixTemplate matrix, int r, int g, int b, int runs) {

        // clear the trail when reaching the end to start over
        if (i_wipe >= matrix.getLEDCount()) {
            matrix.clear();
            i_wipe = 0;
        } else {

            // draw the LED at this position if low enough priority
            if (matrix.getPriorityAt(i_wipe) == 0) {
                matrix.setLEDAtIndex(i_wipe++, r, g, b);
            }
        }
    }

    /**
     * Fade the matrix across the RGB color spectrum.
     *
     * @param {MatrixTemplate} matrix: Matrix object to animate.
     * @param {int} speed: The increment or decrement value for the RGB values.
     */
    public void fade(MatrixTemplate matrix, int speed) {

        // determine which RGB value to adjust based on current position in spectrum
        if (fadeR == 255 && fadeG != 255 && fadeB == 0) {
            fadeG += speed;
        } else if (fadeR != 0 && fadeG == 255 && fadeB == 0) {
            fadeR -= speed;
        } else if (fadeR == 0 && fadeG == 255 && fadeB != 255) {
            fadeB += speed;
        } else if (fadeR == 0 && fadeG != 0 && fadeB == 255) {
            fadeG -= speed;
        } else if (fadeR != 255 && fadeG == 0 && fadeB == 255) {
            fadeR += speed;
        } else if (fadeR == 255 && fadeG == 0 && fadeB != 0) {
            fadeB -= speed;
        }

        // fill this Matrix with the current color
        for (int i = 0; i < matrix.getLEDCount(); i++) {
            if (matrix.getPriorityAt(i) == 0)
                matrix.setLEDAtIndex(i, fadeR, fadeG, fadeB, 0);
        }
    }

    /**
     * Animates a theater chase effect of several LEDs dancing back and forth.
     *
     * @param {MatrixTemplate} matrix: Matrix object to animate.
     * @param {int} r: The desired red RGB value.
     * @param {int} g: The desired green RGB value.
     * @param {int} b: The desired blue RGB value.
     * @param {int} doRainbow: Determines whether or not to use rainbow coloring instead.
     */
    public void theaterChase(MatrixTemplate matrix, int r, int g, int b, int doRainbow) {

        int tempR = r, tempG = g, tempB = b;

        // reset rainbow value
        if (frames_rainbow_chase > max_rainbow)
            frames_rainbow_chase = 0;

        // iterate over each LED and clear between each iteration to prevent LEDs from overwriting
        for (int col = 0; col < matrix.getRowSize(); col++) {
            matrix.clear();
            for (int row = 0; row < matrix.getMatrixCount() * matrix.getRowSize(); row++) {

                // draw LEDs at low priority positions only
                if (matrix.getPriorityAt(i_chase) == 0) {

                    // update RGB values if using rainbow colors is specified
                    if (doRainbow != 0) {
                        push();
                            colorMode(HSB, 100);
                            float p = map(frames_rainbow_chase, 0, max_rainbow, 0, 100);
                            int c = color(ceil(p), 100, 100);
                        pop();
                        tempR = (int) red(c);
                        tempG = (int) green(c);
                        tempB = (int) blue(c);
                    }

                    // draw two LEDs per row at alternating positions
                    if (/*row % 8 == 0 || row % 8 == 1 || row % 8 == 4 || row % 8 == 5*/row % 2 == 0) {
                        matrix.setLEDAtIndex(i_chase + (8 * row), tempR, tempG, tempB);
                        matrix.setLEDAtIndex(i_chase + (8 * row) + 4, tempR, tempG, tempB);
                    } else {
                        matrix.setLEDAtIndex((8 * (row + 1)) - (i_chase + 1), tempR, tempG, tempB);
                        matrix.setLEDAtIndex((8 * (row + 1)) - i_chase - 5, tempR, tempG, tempB);
                    }
                }
            }
        }

        // if an LED reaches the end, reverse its direction
        if (i_chase >= 3)
            chase_dir = -1;
        else if (i_chase <= 0)
            chase_dir = 1;

        // update position and rainbow value
        i_chase += chase_dir;
        frames_rainbow_chase++;
    }

    /**
     * Returns a diagram of the specified character so that it can be interpreted onto a matrix
     * object. It retrieves the appropriate diagram by using the preloaded diagrams labeled by the
     * character they correspond to.
     *
     * @param {char} character: Character to create diagram of.
     *
     * @return {String} Diagram of specified character as string.
     */
    public String toMatrix(char character) {

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

        // format the text to scroll across
        String formatted_text = text.replace(" ", "_").toUpperCase();
        String[] chars = new String[formatted_text.length() + 1];

        // obtain a map of each letter in the text to scroll
        for (int i = 0; i < formatted_text.length(); i++) {
            chars[i] = toMatrix(formatted_text.charAt(i));
        }

        // add a space at end of given text to separate it from the next scroll across
        chars[chars.length - 1] = toMatrix('_');

        // manage edge cases for positions
        if (letter_pos >= 8) {
            letter_pos = 0;
            char_index++;

            if (char_index >= chars.length - 1) {
                char_index = 0;
            }
        }

        // scroll the Matrix across once
        matrix.scroll(1, r, g, b);

        // add the next letter to the Matrix
        matrix.addLetter(chars[char_index], letter_pos++, r, g, b);
    }

    /**
     * Animates a rainbow effect pulsating from a specified corner or center. The hue slightly
     * changes each row or column the rainbow moves and the color changes with each new "pulse" that
     * is released.
     *
     * @param {MatrixTemplate} matrix: Matrix object to animate.
     * @param {int} origin: Effect will start from this quadrant's corner (0 for center).
     */
    public void rainbow(MatrixTemplate matrix, int origin) {

        // initialize some size variables
        int length = matrix.getLEDCount() / matrix.getRowSize();
        int matrices = matrix.getMatrixCount();

        // reset rainbow value
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
            int c = color(ceil(p), 100, 100);
        pop();

        // start the wave based on specified origin
        switch(origin) {

            // center of matrix
            case 0:

                int offset = (matrices - 1) * 4;

                for (int y = length / 2; y <= xy_rainbow; y++) {
                    matrix.setLEDAtCoord((length - 1) - y, xy_rainbow - offset, (int) red(c), (int) green(c), (int) blue(c));
                    matrix.setLEDAtCoord((length - 1) - xy_rainbow, y - offset, (int) red(c), (int) green(c), (int) blue(c));

                    matrix.setLEDAtCoord(y, xy_rainbow - offset, (int) red(c), (int) green(c), (int) blue(c));
                    matrix.setLEDAtCoord(xy_rainbow, y - offset, (int) red(c), (int) green(c), (int) blue(c));

                    matrix.setLEDAtCoord(xy_rainbow, (length - offset) - y - 1, (int) red(c), (int) green(c), (int) blue(c));
                    matrix.setLEDAtCoord(y, (length - offset) - xy_rainbow - 1, (int) red(c), (int) green(c), (int) blue(c));

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
                    matrix.setLEDAtCoord(xy_rainbow, (length / matrices) - y - 1, (int) red(c), (int) green(c), (int) blue(c));
                    matrix.setLEDAtCoord(y, (length / matrices) - xy_rainbow - 1, (int) red(c), (int) green(c), (int) blue(c));
                }
                break;

            // 4th quadrant, bottom right
            case 4:

                for (int y = 0; y <= xy_rainbow; y++) {
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
     * LED object contains information about its state such as position and color to allow for it to
     * be individually addressed.
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
         * Visually turns off this LED by setting its RGB value and previous state to a dark gray.
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
     * Matrix object contains an array of all the individually addressable LEDs on its face.
     *
     * @author Kevin Kowalski
     */

    final int MATRIX_ROWS = 8;
    final int MATRIX_COLS = 8;
    final int MATRIX_LEDS = MATRIX_ROWS * MATRIX_COLS;

    class Matrix implements MatrixTemplate {

        int size, id, r, g, b;
        boolean doCircular;

        LEDsign.LED[] matrix;

        /**
         * Constructor for the matrix object. Creates a grid of LEDs according to the MATRIX_LEDS
         * variable above at the specified render size.
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
            this.matrix = new LEDsign.LED[MATRIX_LEDS];

            // populate the matrix with the appropriate amount of LEDs
            for (int i = 0; i < MATRIX_LEDS; i++) {
                this.matrix[i] = new LEDsign.LED(i, this.size, this.r, this.g, this.b);
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
        @Override
        public int getMatrixCount() {
            return 1;
        }

        /**
         * Returns this matrix's LED count.
         *
         * @return {int} Amount of LEDs present in matrix.
         */
        @Override
        public int getLEDCount() {
            return this.matrix.length;
        }

        /**
         * Returns the row dimension of this matrix.
         *
         * @return {int} Total amount of rows present in this matrix.
         */
        @Override
        public int getRowSize() {
            return MATRIX_ROWS;
        }

        /**
         * Returns the LED priority at the specified index.
         *
         * @param {int} index: The index of the LED to fetch from.
         *
         * @return {int} LED priority at specified index.
         */
        @Override
        public int getPriorityAt(int index) {
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
        public LEDsign.LED getLEDAt(int x, int y) {
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
        @Override
        public void identify() {

            // clear the matrix before rendering to it
            this.disable();

            // TODO
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
        @Override
        public void setLEDAtIndex(int index, int r, int g, int b) {
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
        @Override
        public void setLEDAtIndex(int index, int r, int g, int b, int p) {
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
        @Override
        public void setLEDAtCoord(int x, int y, int r, int g, int b) {
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
        @Override
        public void setLEDAtCoord(int x, int y, int r, int g, int b, int p) {
            if (x >= 0 && x < MATRIX_ROWS && y >= 0 && y < MATRIX_COLS)
                this.matrix[x + (y * MATRIX_COLS)].setColor(r, g, b);
            this.matrix[x + (y * MATRIX_COLS)].setPriority(p);
        }

        /**
         * Sets the RGB value of the LED at the specified index to its previous RGB state.
         *
         * @param {int} index: The index of the requested LED.
         */
        @Override
        public void undoLEDAtIndex(int index) {
            if (index >= 0 && index < MATRIX_LEDS)
                this.matrix[index].undo();
        }

        /**
         * Sets the RGB value of the LED at the specified coordinate to its previous RGB state.
         *
         * @param {int} x: The column coordinate of the requested LED.
         * @param {int} y: The row coordinate of the requested LED.
         */
        @Override
        public void undoLEDAtCoord(int x, int y) {
            if (x >= 0 && x < MATRIX_ROWS && y >= 0 && y < MATRIX_COLS)
                this.matrix[x + (y * MATRIX_COLS)].undo();
        }

        /**
         * Visually turns off all of the LEDs on this matrix.
         */
        @Override
        public void disable() {
            for (int i = 0; i < MATRIX_LEDS; i++) {
                this.matrix[i].disable();
            }
        }

        /**
         * Clears all of the LEDs on this matrix.
         */
        @Override
        public void clear() {
            for (int i = 0; i < MATRIX_LEDS; i++) {
                this.matrix[i].clear();
            }
        }

        /**
         * Scrolls the contents of this matrix to the left by the specified amount of columns.
         *
         * @param {int} offset: The amount of columns to scroll to.
         * @param {int} bg_r: The background red RGB value.
         * @param {int} bg_g: The background green RGB value.
         * @param {int} bg_b: The background blue RGB value.
         */
        @Override
        public void scroll(int offset, int bg_r, int bg_g, int bg_b) {
            for (int i = 0; i < MATRIX_LEDS; i++) {

                int[] led_color = this.matrix[i].getColor();

                // move over each LED to the left one offset
                if (i % MATRIX_ROWS != 0 && this.matrix[i].getPriority() == 1) {
                    this.setLEDAtIndex(i - offset, led_color[0], led_color[1], led_color[2], 1);
                    this.matrix[i].setPriority(0);
                }
                //else
                //  this.setLEDAtIndex(i, bg_r, bg_g, bg_b);
            }
        }

        /**
         * Renders a single portion (column) of the given letter at the specified RGB value.
         *
         * @param {String} letter: Diagram for the letter to render.
         * @param {int} column: The column of the diagram currently being rendered.
         * @param {int} r: The desired red RGB value of this letter.
         * @param {int} g: The desired green RGB value of this letter.
         * @param {int} b: The desired blue RGB value of this letter.
         */
        @Override
        public void addLetter(String letter, int column, int r, int g, int b) {
            for (int i = 0; i < MATRIX_ROWS; i++) {
                if (letter.charAt((i * MATRIX_COLS) + column) == '1')
                    this.setLEDAtCoord(MATRIX_COLS - 2, i, r, g, b, 1);
            }
        }
    }

    /* ------------------------------ HUD CLASSES ------------------------------ */

    /**
     * HUDElement contains a template for any HUD element to be derived from this class. At a
     * minimum, any HUD element requires an (X,Y) coordinate for it to be rendered at any point on
     * the canvas.
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
         * Detects whether or not this element is disabled and if the mouse
         * pointer is currently hovering over the coordinate of this element.
         *
         * @return {boolean} Hover status of this element.
         */
        boolean isHovered() {
            return (!isHidden() && mouseX == this.x && mouseY == this.y);
        }
    }

    /**
     * HUDButton is a HUDElement object that acts like a button. It has bounds and when clicked, it
     * will reflect a visual change. The listener for buttons is found in the touchStarted function.
     *
     * @author Kevin Kowalski
     */

    class HUDButton extends HUDElement {

        int w, d = 128;
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

            this.text = text;

            this.w = text.length() * 24;
        }

        /**
         * Renders this HUDButton based on its parameters (including position,size, label, shape
         * and color).
         */
        @Override
        void display() {
            if (this.isHovered()) {
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
            text(this.text, (float) (this.x - this.d / 2.75), (float) (this.y + this.d / 1.15));
        }

        /**
         * Detects whether or not this button is disabled and if the mouse pointer is currently
         * hovering over the bounds of this button.
         *
         * @return {boolean} Hover status of this button.
         */
        boolean isHovered() {
            return (!isHidden() &&
                    mouseX > this.x && mouseX < (this.x + this.w) &&
                    mouseY > this.y && mouseY < (this.y + this.d));
        }
    }

    /**
     * HUDToggleButton is a HUDButton object that acts like a toggle button. It has bounds and when
     * clicked, it will reflect a visual change like that of a typical toggle button.
     *
     * @author Kevin Kowalski
     */

    class HUDToggleButton extends HUDButton {

        int w, d = 128;
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

            this.w = text.length() * 24;
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
            text(this.text, (float) (this.x - this.d / 2.75), (float) (this.y + this.d / 1.15));
        }

        /**
         * Detects whether or not the mouse pointer is currently hovering over the bounds of this
         * button.
         */
        void toggle() {
            this.b = isToggled ? 200 : 100;
            isToggled = !isToggled;
        }
    }
}