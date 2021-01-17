//package com.kkevn.ledsign.processing;
//
//import android.os.SystemClock;
//import android.util.Log;
//
//import com.kkevn.ledsign.ui.create.CreateFragment;
//import com.kkevn.ledsign.ui.create.Effect;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import processing.core.PApplet;
//import processing.core.PGraphics;
//import processing.core.PImage;
//import processing.core.PShape;
//
//import static com.kkevn.ledsign.ui.create.Effect.COLOR_RAINBOW;
//import static com.kkevn.ledsign.ui.create.Effect.COLOR_SOLID;
//import static com.kkevn.ledsign.ui.create.Effect.TEXT_SCROLL;
//import static processing.core.PApplet.floor;
//
//public class Sketch extends PApplet {
//
//    //int yr = 0;
//    //int xr = 0;
//
//    PImage grid;
//    PShape globe;
//
//    String[] txt__, txt_0, txt_1, txt_2, txt_3, txt_4, txt_5, txt_6, txt_7, txt_8, txt_9, txt_A,
//            txt_B, txt_C, txt_D, txt_E, txt_F, txt_G, txt_H, txt_I, txt_J, txt_K, txt_L, txt_M,
//            txt_N, txt_O, txt_P, txt_Q, txt_R, txt_S, txt_T, txt_U, txt_V, txt_W, txt_X, txt_Y, txt_Z;
//
//    float sensitivity = 0.0004f;
//    float yr = 0f;
//    float xr = 0f;
//    float rot = 0f;
//
//    final int BOX_SIZE = 1000;
//    final int DELAY = 100;
//    double SCALE_FACTOR;
//    int SIZE;
//
//    int lastPrint;
//
//
//    boolean disable = false;
//
//    Matrix m0, m1, m2, m3, m4;
//    Matrix[] matrices;
//
//    LinkedMatrix lm1, lm2, lm3, lm4, lm5;
//    LinkedMatrix[] linked_matrices;
//
//    LinkedMatrixContainer[] containers;
//
//
//    boolean b_solid = true;
//
//    boolean b_crawl = false;
//    int x_crawl = 0;
//    int y_crawl = 0;
//    int crawl_direction = 1;
//    boolean crawl_back = false;
//
//    boolean b_wipe = false;
//
//    boolean b_rainbow = false;
//    int xy_rainbow = 0;
//    int max_rainbow = 100;
//    int frames_rainbow = 0;
//
//    boolean b_text = false;
//    boolean setup_done = false;
//    int letter_pos = 0;
//    int char_index = 0;
//
//    public Sketch() {
//
//    }
//
//    public void settings() {
//        size(displayWidth, displayHeight / 2, P3D);
//        //smooth(aa);
//        //fullScreen();
//    }
//
//    public void preload() {
//        //
//    }
//
//    public void setup() {
//        //size(width, height, P3D);
//
//        // adjust box to fit current screen height
//        SCALE_FACTOR = height / 2.0 / (double) BOX_SIZE;
//        SIZE = (int) (BOX_SIZE * SCALE_FACTOR);
//        //print("BOX= " + BOX_SIZE + "\nSCALE " + SCALE_FACTOR + "\nSIZE= " + SIZE);
//
//        // initialize matrix objects
//        m0 = new Matrix(SIZE, 0, 200, 0, 0); // front face
//        m1 = new Matrix(SIZE, 1, 200, 0, 200); // right face
//        m2 = new Matrix(SIZE, 2, 200, 200, 200); // back face
//        m3 = new Matrix(SIZE, 3, 0, 200, 0); // left face
//        m4 = new Matrix(SIZE, 4, 0, 0, 200); // top face
//
//        matrices = new Matrix[]{m0, m1, m2, m3, m4};
//        linked_matrices = new LinkedMatrix[5];
//
//        lm1 = new LinkedMatrix(m0, m1);
//        lm2 = new LinkedMatrix(m1, m2, m3, m4);
//        linked_matrices[0] = lm1;
//        linked_matrices[1] = lm2;
//        //containers = new LinkedMatrixContainer[2];
//        //containers[0] = new LinkedMatrixContainer("11000", COLOR_SOLID, "150;150;50");
//        //containers[1] = new LinkedMatrixContainer("11000", TEXT_SCROLL, "hello there;50;50;50");
//        //containers = (LinkedMatrixContainer[]) append(containers, new LinkedMatrixEffect_SolidColor("10001", "50;250;50"));
//        //containers = (LinkedMatrixContainer[]) append(containers, new LinkedMatrixEffect_TextScroll("10001", "hello there;50;50;50"));
//
//        //grid = loadImage("cube-grid.png");
//        grid = loadImage("unnamed.jpg");
//        globe = createShape(SPHERE, SIZE * 4);
//
//        //String[] lines = loadStrings("_.txt");
//        //println(txt__[0] + "-" + txt__.length);
//        //txt__ = loadStrings("_.txt");
//
//        // empty space
//        txt__ = loadStrings("characters/_.txt");
//
//        // numbers
//        txt_0 = loadStrings("characters/0.txt");
//        txt_1 = loadStrings("characters/1.txt");
//        txt_2 = loadStrings("characters/2.txt");
//        txt_3 = loadStrings("characters/3.txt");
//        txt_4 = loadStrings("characters/4.txt");
//        txt_5 = loadStrings("characters/5.txt");
//        txt_6 = loadStrings("characters/6.txt");
//        txt_7 = loadStrings("characters/7.txt");
//        txt_8 = loadStrings("characters/8.txt");
//        txt_9 = loadStrings("characters/9.txt");
//
//        // letters
//        txt_A = loadStrings("characters/A.txt");
//        txt_B = loadStrings("characters/B.txt");
//        txt_C = loadStrings("characters/C.txt");
//        txt_D = loadStrings("characters/D.txt");
//        txt_E = loadStrings("characters/E.txt");
//        txt_F = loadStrings("characters/F.txt");
//        txt_G = loadStrings("characters/G.txt");
//        txt_H = loadStrings("characters/H.txt");
//        txt_I = loadStrings("characters/I.txt");
//        txt_J = loadStrings("characters/J.txt");
//        txt_K = loadStrings("characters/K.txt");
//        txt_L = loadStrings("characters/L.txt");
//        txt_M = loadStrings("characters/M.txt");
//        txt_N = loadStrings("characters/N.txt");
//        txt_O = loadStrings("characters/O.txt");
//        txt_P = loadStrings("characters/P.txt");
//        txt_Q = loadStrings("characters/Q.txt");
//        txt_R = loadStrings("characters/R.txt");
//        txt_S = loadStrings("characters/S.txt");
//        txt_T = loadStrings("characters/T.txt");
//        txt_U = loadStrings("characters/U.txt");
//        txt_V = loadStrings("characters/V.txt");
//        txt_W = loadStrings("characters/W.txt");
//        txt_X = loadStrings("characters/X.txt");
//        txt_Y = loadStrings("characters/Y.txt");
//        txt_Z = loadStrings("characters/Z.txt");
//
//        lastPrint = millis() - DELAY;
//    }
//
//    public void draw() {
//        background(16);
//        /*translate(width / 2, height / 2);
//        rotateY(degrees(yr));
//        rotateX(degrees(xr));
//        fill(10, 200, 10);
//        box(500);*/
//
//        //parseEffectList(CreateFragment.parseList());
//
//        int timeElapsed = millis() - lastPrint;
//
//        translate(width / 2, height / 2);
//
//          /*push();
//            //fill(32);
//            //noStroke();
//            rotate(rot);
//            globe.setTexture(grid);
//            noStroke();
//            shape(globe);
//          pop();
//          rot += 0.001f;*/
//
//        rotateY(degrees(yr));
//        rotateX(degrees(xr));
//
//        drawCube();
//
//        if (timeElapsed > DELAY) {
//
//    /*if (b_solid == true) {
//      //solidColor(lm2, 200, 200, 10);
//      //b_solid = false;
//    }
//
//    if (b_crawl == true) {
//      crawl(lm1, true, 10, 200, 20);
//    }
//
//    if (b_wipe == true) {
//        m0.disable();
//        //theaterChase(m0, 127, 127, 127);
//        //theaterChase(m0, 127, 0, 0);
//        //theaterChase(m0, 0, 0, 127);
//        //colorWipe(m0, map(timeElapsed, 0, 1000, 0, 255), 0, 0);
//      }
//
//      if (b_rainbow == true) {
//        //m0.disable();
//        //rainbow(m0, 0);
//        rainbow(lm2, 2);
//      }
//
//      if (b_text == true) {
//        //m0.disable();
//        //textScroll(m0, "AAAA" + " ", 24, 24, 24);
//        textScroll(lm1, ("test "), 200, 200, 200);
//      }*/
//
//            parseEffectList("Scrolling Text{11000;my name is kev;200;200;200},Solid Color{10001;200;200;0},");
//            //parseEffectList("Solid Color{10001;200;200;0},Scrolling Text{11000;my name is kev;200;200;200},");
//            //parseEffectList("Scrolling Text{11000;my name;200;200;200},");
//
//            // build container array first by parsing Android inputs
//            // DONT check for dupes before adding new container
//
//            // TODO
//            for (LinkedMatrixContainer container : containers) {
//
//                // if (lm != null) ??
//                //println(idx++);
//                //println(container.getEffectType());
//                //println(container.getParams());
//
//                String[] params = container.params.split(";");
//
//                switch (container.getEffectType()) {
//                    case COLOR_SOLID:
//
//                        // only draw bg once, not every frame to prevent overwriting
//                        if (b_solid == true) {
//                            solidColor(container.linked_matrix, Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]));
//                            //b_solid = false;
//                        }
//                        break;
//                    case TEXT_SCROLL:
//                        textScroll(container.linked_matrix, params[0] + " ", Integer.parseInt(params[1]), Integer.parseInt(params[2]), Integer.parseInt(params[3]));
//                        break;
//                }
//            }
//
//            lastPrint = millis();
//        }
//
//    }
//
//    /**
//     * https://stackoverflow.com/questions/43741561/how-do-i-is-it-posssible-to-keep-different-picture-in-each-side-of-box-in-p5
//     *
//     * Renders each matrix or 'side' of the cube by rotating
//     * and translating the origin of the current view.
//     */
//    void drawCube() {
//        //angleMode(DEGREES); used radians(deg) instead
//
//        int size = (int) (BOX_SIZE * SCALE_FACTOR);
//
//        //push();
//        // center the origin/view
//        translate(-size / 2, -size / 2, size / 2);
//
//        // render the front face of cube
//        m0.display(); // red
//        push();
//            fill(200);
//            textSize(32);
//            translate(0, 0, 1);
//            text("Front", size / 2, size + 32);
//        pop();
//        //pop();
//
//
//        // render the right face of cube
//        push();
//            translate(size, 0, 0);
//            rotateY(radians(90));
//            m1.display(); // purple
//            push();
//                fill(200);
//                textSize(32);
//                translate(0, 0, 1);
//                text("Right", size / 2, size + 32);
//            pop();
//        pop();
//
//        // render the back face of cube
//        push();
//            rotateY(radians(180));
//            translate(-size, 0, size);
//            m2.display(); // white
//            push();
//                fill(200);
//                textSize(32);
//                translate(0, 0, 1);
//                text("Back", size / 2, size + 32);
//            pop();
//        pop();
//
//        // render the left face of cube
//        push();
//            translate(0, 0, -size);
//            rotateY(radians(-90));
//            m3.display(); // green
//            push();
//                fill(200);
//                textSize(32);
//                translate(0, 0, 1);
//                text("Left", size / 2, size + 32);
//            pop();
//        pop();
//
//        // render the top face of cube
//        push();
//            translate(0, 0, -size);
//            rotateX(radians(90));
//            m4.display(); // blue
//            push();
//                fill(200);
//                textSize(32);
//                translate(0, 0, 1);
//                text("Top", size / 2, size + 32);
//            pop();
//        pop();
//
//        // render the inactive bottom face of cube as dark gray
//        push();
//            fill(20, 20, 20);
//            translate(0, size, 0);
//            rotateX(radians(-90));
//            rect(0, 0, size, size);
//        pop();
//    }
//
//    // Scrolling Text{xxxxx;string;int;int;int},Solid Color{xxxxx;int;int;int},RAINBOW{...},
//    /**
//     * Parses the given input and neatly separates each effect into the
//     * LinkedMatrixContainer array.
//     *
//     * @param {String} input: Encoded comma-separated list of all effects.
//     **
//     *   Expected Effect Format:
//     *     Foo{10010;bar; 0; 255; 123},
//     *   where:
//     *        Foo = the name/type of effect this is
//     *      {...} = between brackets, the parameters for this effect
//     *      10010 = the matrices to apply this effect to, where each digit corresponds to a matrix by index
//     *               A '1' implies this matrix will have this effect whereas a '0' implies it will not.
//     *               In this case, the first and fourth matrices will apply this effect; here are the indices for matrices:
//     *                 0 - front of cube
//     *                 1 - right of cube
//     *                 2 - back of cube
//     *                 3 - left of cube
//     *                 4 - top of cube
//     *      remaining values in brackets = the specified effect's parameters that vary by effect, usually contain RGB values
//     *      trailing comma = marks end of effect to separate from next effect in input list
//     */
//    private void parseEffectList(String input) {
//
//        // get individual list of each effect
//        String[] isolated_inputs = input.split(",");
//
//        // clear effect container to accomodate the new inputs
//        containers = new LinkedMatrixContainer[isolated_inputs.length];
//
//        // parse each effect from the list
//        for (int i = 0; i < isolated_inputs.length; i++) {
//
//            // extract current effect name, matrix selections and parameters
//            String s = isolated_inputs[i];
//            String params = s.substring(s.indexOf('{') + 1, s.length() - 1);
//            String selections = params.substring(0, 5);
//            params = params.substring(params.indexOf(';') + 1, params.length());
//
//            // store the decoded effect into a container object and place it in the array
//            switch (s.substring(0, s.indexOf('{'))) {
//
//                case COLOR_SOLID:
//                    //containers = (LinkedMatrixContainer[]) append(containers, new LinkedMatrixEffect_SolidColor(selections, params));
//                    containers[i] = new LinkedMatrixContainer(selections, COLOR_SOLID, params);
//                    break;
//                case COLOR_RAINBOW:
//                    //containers = (LinkedMatrixContainer[]) append(containers, new LinkedMatrixEffect_Rainbow(selections, params));
//                    //containers[i] = new LinkedMatrixContainer(selections, COLOR_RAINBOW, params);
//                    break;
//                case TEXT_SCROLL:
//                    //containers = (LinkedMatrixContainer[]) append(containers, new LinkedMatrixEffect_TextScroll(selections, params));
//                    containers[i] = new LinkedMatrixContainer(selections, TEXT_SCROLL, params);
//                    break;
//            }
//            //println(containers[i].effect);
//        }
//        //println(containers[0].effect);
//    }
//
//    public void touchMoved() {
//
//        // to right
//        if (mouseX > pmouseX) {
//            yr += sensitivity;
//            if (yr > 360f) {
//                yr = 0f;
//            }
//        } else if (mouseX <= pmouseX) {
//            yr -= sensitivity;
//            if (yr < 0f) {
//                yr = 360f;
//            }
//        }
//
//        if (mouseY > pmouseY) {
//            //xr-=1;
//            if (xr > -45f) {
//                xr -= sensitivity;
//            }
//        } else if (mouseY <= pmouseY) {
//            //xr+=1;
//            if (xr < 0f) {
//                xr += sensitivity;
//            }
//        }
//    }
//
//    /*public void touchMoved() {
//
//        // to right
//        if (mouseX > pmouseX) {
//            yr += 2;
//            if (yr > 360) {
//                yr = 0;
//            }
//        } else if (mouseX <= pmouseX) {
//            yr -= 2;
//            if (yr < 0) {
//                yr = 360;
//            }
//        }
//
//        if (mouseY > pmouseY) {
//            //xr-=1;
//            if (xr > -45) {
//                xr -= 1;
//            }
//        } else if (mouseY <= pmouseY) {
//            //xr+=1;
//            if (xr < 0) {
//                xr += 1;
//            }
//        }
//    }*/
//
//    public class LED extends PApplet {
//
//        final int LED_DISABLED_RGB_VALUE = 16;
//
//        int x, y, size, r, g, b, prev_r, prev_g, prev_b, state;
//
//        /**
//         * Constructor for the LED object.
//         *
//         * @param {int} index: The index of this LED.
//         * @param {int} size: The pixel size of this LED.
//         * @param {int} r: The desired red RGB value.
//         * @param {int} g: The desired green RGB value.
//         * @param {int} b: The desired blue RGB value.
//         */
//        LED(int index, int size, int r, int g, int b) {
//            //super();
//            this.x = index % 8;
//            this.y = floor(index / 8);
//
//            this.size = size;
//
//            this.r = r;
//            this.g = g;
//            this.b = b;
//
//            this.prev_r = r;
//            this.prev_g = g;
//            this.prev_b = b;
//
//            this.state = 0;  // priority of LED, the higher it is the less likely it is to be overwritten by another effect/function
//        }
//
//        /**
//         * Alternative constructor for the LED object.
//         *
//         * @param {int} x: The column coordinate of this LED.
//         * @param {int} y: The row coordinate of this LED.
//         * @param {int} size: The pixel size of this LED.
//         * @param {int} r: The desired red RGB value.
//         * @param {int} g: The desired green RGB value.
//         * @param {int} b: The desired blue RGB value.
//         */
//  /*LED2(int x, int y, int size, int r, int g, int b) {
//    this.x = x;
//    this.y = y;
//    this.size = size;
//
//    this.r = r;
//    this.g = g;
//    this.b = b;
//
//    this.prev_r = r;
//    this.prev_g = g;
//    this.prev_b = b;
//  }*/
//
//        /**
//         * Renders this LED based on its current state (meaning position,
//         * size, shape and color).
//         *
//         * @param {boolean} isCircular: Decides LED shape.
//         */
//        void display(boolean isCircular) {
//            if (isCircular) {
//
//                // render the PCB or background of the LED
//                fill(8, 8, 8);
//                rect(this.x * this.size, this.y * this.size, this.size, this.size);
//
//                // circles drawn from center, so adjust origin (and on top of background)
//                push();
//                translate(this.size / 2, this.size / 2, 1);
//                fill(this.r, this.g, this.b);
//                circle(this.x * this.size, this.y * this.size, this.size / 2);
//                pop();
//
//            } else {
//
//                // render the PCB or background of the LED
//          /*fill(8, 8, 8);
//          rect(this.x * this.size, this.y * this.size, this.size, this.size);
//
//          push();
//            translate(this.size / 6, this.size / 6, 1);
//            fill(this.r, this.g, this.b);
//            rect(this.x * this.size, this.y * this.size, this.size / 1.5, this.size / 1.5);
//          pop();
//          */
//                // render a simple square for this LED
//
//                    fill(this.r, this.g, this.b);
//                    rect(this.x * this.size, this.y * this.size, this.size, this.size);
//            }
//        }
//
//        /**
//         * Returns an array of this LED's current RGB values.
//         *
//         * @return {int array} An integer array of this LED's current RGB values.
//         */
//        int[] getColor() {
//            //int[] colors = {this.r, this.g, this.b};
//            return new int[] {this.r, this.g, this.b};
//        }
//
//        /**
//         * Returns an array of this LED's previous RGB values.
//         *
//         * @return {int array} An integer array of this LED's previous RGB values.
//         */
//        int[] getPrevColor() {
//            return new int[] {this.prev_r, this.prev_g, this.prev_b};
//        }
//
//        /**
//         * Returns the priority state of the current LED.
//         *
//         * @return {int} The priority state of this LED.
//         */
//        int getPriority() {
//            return this.state;
//        }
//
//        /**
//         * Sets the RGB value of this LED and stores its previous color.
//         *
//         * @param {int} r: The desired red RGB value.
//         * @param {int} g: The desired green RGB value.
//         * @param {int} b: The desired blue RGB value.
//         */
//        void setColor(int r, int g, int b) {
//            this.prev_r = this.r;
//            this.prev_g = this.g;
//            this.prev_b = this.b;
//
//            this.r = r;
//            this.g = g;
//            this.b = b;
//        }
//
//        /**
//         * Updates the priority of the current LED to the given rank.
//         *
//         * @param {int} newState: The new rank/priority of this LED.
//         */
//        void setPriority(int newState) {
//            this.state = newState;
//        }
//
//        /**
//         * Sets the RGB value of this LED to its previous state.
//         */
//        void undo() {
//            this.r = this.prev_r;
//            this.g = this.prev_g;
//            this.b = this.prev_b;
//        }
//
//        /**
//         * Visually turns off this LED by setting its RGB value and
//         * previous state to a dark gray.
//         */
//        void disable() {
//            this.prev_r = LED_DISABLED_RGB_VALUE;
//            this.prev_g = LED_DISABLED_RGB_VALUE;
//            this.prev_b = LED_DISABLED_RGB_VALUE;
//
//            this.r = LED_DISABLED_RGB_VALUE;
//            this.g = LED_DISABLED_RGB_VALUE;
//            this.b = LED_DISABLED_RGB_VALUE;
//
//            this.state = 0;
//        }
//    }
//
//    /**
//     * Fill the matrix with a single color.
//     *
//     * @param {MatrixTemplate} matrix: Matrix object to animate.
//     * @param {int} r: The desired red RGB value.
//     * @param {int} g: The desired green RGB value.
//     * @param {int} b: The desired blue RGB value.
//     */
//    void solidColor(MatrixTemplate matrix, int r, int g, int b) {
//        for (int i = 0; i < matrix.getLEDCount(); i++) {
//            if (matrix.getPriorityAt(i) == 0)// added
//                matrix.setLEDAtIndex(i, r, g, b, 0);
//        }
//    }
//
//    /**
//     * Animate a single LED scrolling across the matrix, reversing its
//     * direction between each row.
//     *
//     * @param {MatrixTemplate} matrix: Matrix object to animate.
//     * @param {boolean} backtrack: Reverse the animation upon completion.
//     * @param {int} r: The desired red RGB value.
//     * @param {int} g: The desired green RGB value.
//     * @param {int} b: The desired blue RGB value.
//     */
//    void crawl(MatrixTemplate matrix, boolean backtrack, int r, int g, int b) {
//
//        // reverse y-direction when matrix y-axis borders reached
//        if (y_crawl < 0 || y_crawl > (matrix.getRowSize() - 1)) {
//            crawl_direction *= -1;
//        }
//
//        // reverse x-direction when matrix x-axis borders reached
//        if (x_crawl <= -1 || x_crawl >= (matrix.getRowSize() * matrix.getMatrixCount())) {
//
//            // reverse direction
//            crawl_back = !crawl_back;
//
//            // update next row to crawl
//            y_crawl += crawl_direction;
//        }
//
//        // undo the previous LED and draw the current one
//        if (crawl_back == false) {
//
//            // undo previous LED in column, row below, or row above, respectively
//            matrix.undoLEDAtCoord(x_crawl - 1, y_crawl);
//            matrix.undoLEDAtCoord(x_crawl, y_crawl - 1);
//            matrix.undoLEDAtCoord(x_crawl, y_crawl + 1);
//
//            // draw current LED
//            matrix.setLEDAtCoord(x_crawl++, y_crawl, r, g, b);
//        } else {
//
//            // undo previous LED in column, row above, or row below, respectively
//            matrix.undoLEDAtCoord(x_crawl + 1, y_crawl);
//            matrix.undoLEDAtCoord(x_crawl, y_crawl + 1);
//            matrix.undoLEDAtCoord(x_crawl, y_crawl - 1);
//
//            // draw current LED
//            matrix.setLEDAtCoord(x_crawl--, y_crawl, r, g, b);
//        }
//
//        //print("x: " + x_crawl + "\n" + "y: " + y_crawl + "\n\n");
//    }
//
//    /**
//     * ???
//     *
//     * @param {MatrixTemplate} matrix: Matrix object to animate.
//     * @param {int} r: The desired red RGB value.
//     * @param {int} g: The desired green RGB valuet.
//     * @param {int} b: The desired blue RGB value.
//     */
//    void  colorWipe(MatrixTemplate matrix, int r, int g, int b) {
//        for (int i = 0; i < 64; i++) {
//            matrix.setLEDAtIndex(i, r, g, b);
//        }
//    }
//
//    /**
//     * Returns a diagram of the specified character so that it
//     * can be interpreted onto a matrix object. It retrieves
//     * the appropriate diagram by using the preloaded diagrams
//     * labeled by the character they correspond to.
//     *
//     * @param {char} character: Character to create diagram of.
//     *
//     * @return {String} Diagram of specified character as string.
//     */
//    String toMatrix(char character) {
//        //return eval("txt_" + character + ".join('');");
//
//        String result = "";
//
//        switch(character) {
//            case '_':
//                result = String.join("", txt__);
//                break;
//            case 'A':
//                result = String.join("", txt_A);
//                break;
//            case 'B':
//                result = String.join("", txt_B);
//                break;
//            case 'C':
//                result = String.join("", txt_C);
//                break;
//            case 'D':
//                result = String.join("", txt_D);
//                break;
//            case 'E':
//                result = String.join("", txt_E);
//                break;
//            case 'F':
//                result = String.join("", txt_F);
//                break;
//            case 'G':
//                result = String.join("", txt_G);
//                break;
//            case 'H':
//                result = String.join("", txt_H);
//                break;
//            case 'I':
//                result = String.join("", txt_I);
//                break;
//            case 'J':
//                result = String.join("", txt_J);
//                break;
//            case 'K':
//                result = String.join("", txt_K);
//                break;
//            case 'L':
//                result = String.join("", txt_L);
//                break;
//            case 'M':
//                result = String.join("", txt_M);
//                break;
//            case 'N':
//                result = String.join("", txt_N);
//                break;
//            case 'O':
//                result = String.join("", txt_O);
//                break;
//            case 'P':
//                result = String.join("", txt_P);
//                break;
//            case 'Q':
//                result = String.join("", txt_Q);
//                break;
//            case 'R':
//                result = String.join("", txt_R);
//                break;
//            case 'S':
//                result = String.join("", txt_S);
//                break;
//            case 'T':
//                result = String.join("", txt_T);
//                break;
//            case 'U':
//                result = String.join("", txt_U);
//                break;
//            case 'V':
//                result = String.join("", txt_V);
//                break;
//            case 'W':
//                result = String.join("", txt_W);
//                break;
//            case 'X':
//                result = String.join("", txt_X);
//                break;
//            case 'Y':
//                result = String.join("", txt_Y);
//                break;
//            case 'Z':
//                result = String.join("", txt_Z);
//                break;
//            case '0':
//                result = String.join("", txt_0);
//                break;
//            case '1':
//                result = String.join("", txt_1);
//                break;
//            case '2':
//                result = String.join("", txt_2);
//                break;
//            case '3':
//                result = String.join("", txt_3);
//                break;
//            case '4':
//                result = String.join("", txt_4);
//                break;
//            case '5':
//                result = String.join("", txt_5);
//                break;
//            case '6':
//                result = String.join("", txt_6);
//                break;
//            case '7':
//                result = String.join("", txt_7);
//                break;
//            case '8':
//                result = String.join("", txt_8);
//                break;
//            case '9':
//                result = String.join("", txt_9);
//                break;
//            default:
//                result = String.join("", txt__);
//        }
//
//        return result;
//    }
//
//    /**
//     * Animates the specified text to scroll across the matrix.
//     *
//     * @param {MatrixTemplate} matrix: Matrix object to animate.
//     * @param {String} text: Text to scroll across the matrix.
//     * @param {int} r: The desired red RGB value of this text.
//     * @param {int} g: The desired green RGB value of this text.
//     * @param {int} b: The desired blue RGB value of this text.
//     */
//    void textScroll(MatrixTemplate matrix, String text, int r, int g, int b) {
//
//        String formatted_text = text.replace(" ", "_").toUpperCase();
//        String[] chars = new String[formatted_text.length() + 1];
//
//        for (int i = 0; i < formatted_text.length(); i++) {
//            //chars.push(toMatrix(formatted_text.charAt(i)));
//            chars[i] = toMatrix(formatted_text.charAt(i));
//        }
//
//        //print(formatted_text);
//        //if (formatted_text.charAt(formatted_text.length - 1) != '_')
//        //chars.push(toMatrix('_'));
//        chars[chars.length - 1] = toMatrix('_');
//
//        if (letter_pos >= 8) {
//            letter_pos = 0;
//            char_index++;
//
//            if (char_index >= chars.length - 1) {
//                char_index = 0;
//            }
//        }
//
//        matrix.scroll(1, r, g, b);
//
//        matrix.addLetter(chars[char_index], letter_pos++, r, g, b);
//    }
//
//    /**
//     * Animates a rainbow effect pulsating from a specified corner or center.
//     * The hue slightly changes each row or column the rainbow moves and the
//     * color changes with each new "pulse" that is released.
//     *
//     * @param {MatrixTemplate} matrix: Matrix object to animate.
//     * @param {int} origin: Effect will start from this quadrant's corner (0 for center).
//     */
//    void rainbow(MatrixTemplate matrix, int origin) {
//
//        // M - x
//        // 1 - 0
//        // 2 - 4
//        // 3 - 8
//        // 4 - 12
//
//        int length = matrix.getLEDCount() / matrix.getRowSize();
//        int matrices = matrix.getMatrixCount();
//
//        // reset ??????
//        if (frames_rainbow > max_rainbow)
//            frames_rainbow = 0;
//
//        // reset position if it exceeds matrix size
//        if (xy_rainbow >= length) {
//            xy_rainbow = 0;
//        }
//
//        // generate a color for the current hue
//        push();
//            colorMode(HSB, 100);
//            float p = map(frames_rainbow, 0, max_rainbow, 0, 350);
//            //print(ceil(p));
//            //color c = color("hsb(" + ceil(p) + ", 100%, 100%)");
//            int c = color(ceil(p), 100, 100);
//            //color c = color(ceil(p), 100, 100); TODO
//        pop();
//
//        switch(origin) {
//
//            // center of matrix
//            case 0:
//
//                int offset = (matrices - 1) * 4;
//
//                for (int y = length / 2; y <= xy_rainbow; y++) {
//
//                    //print("y: " + y + "\n");
//                    //print("xy_rainbow: " + xy_rainbow + "\n");
//
//                    // old
//                    //matrix.setLEDAtCoord((length - 1) - y, xy_rainbow, red(c), green(c), blue(c));
//                    //matrix.setLEDAtCoord((length - 1) - xy_rainbow, y, red(c), green(c), blue(c));
//
//                    matrix.setLEDAtCoord((length - 1) - y, xy_rainbow - offset, (int) red(c), (int) green(c), (int) blue(c));
//                    matrix.setLEDAtCoord((length - 1) - xy_rainbow, y - offset, (int) red(c), (int) green(c), (int) blue(c));
//
//                    // old
//                    //matrix.setLEDAtCoord(y, xy_rainbow, red(c), green(c), blue(c));
//                    //matrix.setLEDAtCoord(xy_rainbow, y, red(c), green(c), blue(c));
//
//                    matrix.setLEDAtCoord(y, xy_rainbow - offset, (int) red(c), (int) green(c), (int) blue(c));
//                    matrix.setLEDAtCoord(xy_rainbow, y - offset, (int) red(c), (int) green(c), (int) blue(c));
//
//                    // old
//                    //matrix.setLEDAtCoord(xy_rainbow, (length - 1) - y, red(c), green(c), blue(c));
//                    //matrix.setLEDAtCoord(y, (length - 1) - xy_rainbow, red(c), green(c), blue(c));
//
//                    matrix.setLEDAtCoord(xy_rainbow, (length - offset) - y - 1, (int) red(c), (int) green(c), (int) blue(c));
//                    matrix.setLEDAtCoord(y, (length - offset) - xy_rainbow - 1, (int) red(c), (int) green(c), (int) blue(c));
//
//                    // old
//                    //matrix.setLEDAtCoord((length - 1) - y, (length - 1) - xy_rainbow, red(c), green(c), blue(c));
//                    //matrix.setLEDAtCoord((length - 1) - xy_rainbow, (length - 1) - y, red(c), green(c), blue(c));
//
//                    matrix.setLEDAtCoord((length ) - y - 1, (length - offset) - xy_rainbow - 1, (int) red(c), (int) green(c), (int) blue(c));
//                    matrix.setLEDAtCoord((length ) - xy_rainbow - 1, (length - offset) - y -1, (int) red(c), (int) green(c), (int) blue(c));
//                }
//                break;
//
//            // 1st quadrant, top right
//            case 1:
//
//                for (int y = 0; y <= xy_rainbow; y++) {
//                    matrix.setLEDAtCoord((length - 1) - y, xy_rainbow, (int) red(c), (int) green(c), (int) blue(c));
//                    matrix.setLEDAtCoord((length - 1) - xy_rainbow, y, (int) red(c), (int) green(c), (int) blue(c));
//                }
//                break;
//
//            // 2nd quadrant, top left
//            case 2:
//                if (xy_rainbow >= length) {
//                    xy_rainbow = 0;
//                }
//
//                for (int y = 0; y <= xy_rainbow; y++) {
//                    matrix.setLEDAtCoord(y, xy_rainbow, (int) red(c), (int) green(c), (int) blue(c));
//                    matrix.setLEDAtCoord(xy_rainbow, y, (int) red(c), (int) green(c), (int) blue(c));
//                }
//                break;
//
//            // 3rd quadrant, bottom left
//            case 3:
//
//                for (int y = 0; y <= xy_rainbow; y++) {
//                    //matrix.setLEDAtCoord(xy_rainbow, (length - 1) - y, red(c), green(c), blue(c));
//                    //matrix.setLEDAtCoord(y, (length - 1) - xy_rainbow, red(c), green(c), blue(c));
//
//                    matrix.setLEDAtCoord(xy_rainbow, (length / matrices) - y - 1, (int) red(c), (int) green(c), (int) blue(c));
//                    matrix.setLEDAtCoord(y, (length / matrices) - xy_rainbow - 1, (int) red(c), (int) green(c), (int) blue(c));
//                }
//                break;
//
//            // 4th quadrant, bottom right
//            case 4:
//
//                for (int y = 0; y <= xy_rainbow; y++) {
//                    //matrix.setLEDAtCoord((length - 1) - y, (length - 1) - xy_rainbow, red(c), green(c), blue(c));
//                    //matrix.setLEDAtCoord((length - 1) - xy_rainbow, (length - 1) - y, red(c), green(c), blue(c));
//
//                    matrix.setLEDAtCoord((length ) - y - 1, (length / matrices) - xy_rainbow - 1, (int) red(c), (int) green(c), (int) blue(c));
//                    matrix.setLEDAtCoord((length ) - xy_rainbow - 1, (length / matrices) - y -1, (int) red(c), (int) green(c), (int) blue(c));
//                }
//                break;
//        }
//
//        // increment hue and position values
//        frames_rainbow++;
//        xy_rainbow++;
//    }
//}