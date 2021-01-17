//package com.kkevn.ledsign.processing;
//
////import static processing.core.PApplet.floor;
//
///**
// * LED object contains information about its state such as position and
// * color to allow for it to be individually addressed.
// *
// * @author Kevin Kowalski
// */
//
////final int LED_DISABLED_RGB_VALUE = 16;
//
//class LED {
//
//    final int LED_DISABLED_RGB_VALUE = 16;
//
//    int x, y, size, r, g, b, prev_r, prev_g, prev_b, state;
//
//    /**
//     * Constructor for the LED object.
//     *
//     * @param {int} index: The index of this LED.
//     * @param {int} size: The pixel size of this LED.
//     * @param {int} r: The desired red RGB value.
//     * @param {int} g: The desired green RGB value.
//     * @param {int} b: The desired blue RGB value.
//     */
//    LED(int index, int size, int r, int g, int b) {
//        this.x = index % 8;
//        this.y = floor(index / 8);
//
//        this.size = size;
//
//        this.r = r;
//        this.g = g;
//        this.b = b;
//
//        this.prev_r = r;
//        this.prev_g = g;
//        this.prev_b = b;
//
//        this.state = 0;  // priority of LED, the higher it is the less likely it is to be overwritten by another effect/function
//    }
//
//    /**
//     * Alternative constructor for the LED object.
//     *
//     * @param {int} x: The column coordinate of this LED.
//     * @param {int} y: The row coordinate of this LED.
//     * @param {int} size: The pixel size of this LED.
//     * @param {int} r: The desired red RGB value.
//     * @param {int} g: The desired green RGB value.
//     * @param {int} b: The desired blue RGB value.
//     */
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
//    /**
//     * Renders this LED based on its current state (meaning position,
//     * size, shape and color).
//     *
//     * @param {boolean} isCircular: Decides LED shape.
//     */
//    public void display(boolean isCircular) {
//        if (isCircular) {
//
//            // render the PCB or background of the LED
//            fill(8, 8, 8);
//            rect(this.x * this.size, this.y * this.size, this.size, this.size);
//
//            // circles drawn from center, so adjust origin (and on top of background)
//            push();
//            translate(this.size / 2, this.size / 2, 1);
//            fill(this.r, this.g, this.b);
//            circle(this.x * this.size, this.y * this.size, this.size / 2);
//            pop();
//
//        } else {
//
//            // render the PCB or background of the LED
//      /*fill(8, 8, 8);
//      rect(this.x * this.size, this.y * this.size, this.size, this.size);
//
//      push();
//        translate(this.size / 6, this.size / 6, 1);
//        fill(this.r, this.g, this.b);
//        rect(this.x * this.size, this.y * this.size, this.size / 1.5, this.size / 1.5);
//      pop();
//      */
//            // render a simple square for this LED
//            fill(this.r, this.g, this.b);
//            rect(this.x * this.size, this.y * this.size, this.size, this.size);
//        }
//    }
//
//    /**
//     * Returns an array of this LED's current RGB values.
//     *
//     * @return {int array} An integer array of this LED's current RGB values.
//     */
//    public int[] getColor() {
//        //int[] colors = {this.r, this.g, this.b};
//        return new int[] {this.r, this.g, this.b};
//    }
//
//    /**
//     * Returns an array of this LED's previous RGB values.
//     *
//     * @return {int array} An integer array of this LED's previous RGB values.
//     */
//    public int[] getPrevColor() {
//        return new int[] {this.prev_r, this.prev_g, this.prev_b};
//    }
//
//    /**
//     * Returns the priority state of the current LED.
//     *
//     * @return {int} The priority state of this LED.
//     */
//    public int getPriority() {
//        return this.state;
//    }
//
//    /**
//     * Sets the RGB value of this LED and stores its previous color.
//     *
//     * @param {int} r: The desired red RGB value.
//     * @param {int} g: The desired green RGB value.
//     * @param {int} b: The desired blue RGB value.
//     */
//    public void setColor(int r, int g, int b) {
//        this.prev_r = this.r;
//        this.prev_g = this.g;
//        this.prev_b = this.b;
//
//        this.r = r;
//        this.g = g;
//        this.b = b;
//    }
//
//    /**
//     * Updates the priority of the current LED to the given rank.
//     *
//     * @param {int} newState: The new rank/priority of this LED.
//     */
//    public void setPriority(int newState) {
//        this.state = newState;
//    }
//
//    /**
//     * Sets the RGB value of this LED to its previous state.
//     */
//    public void undo() {
//        this.r = this.prev_r;
//        this.g = this.prev_g;
//        this.b = this.prev_b;
//    }
//
//    /**
//     * Visually turns off this LED by setting its RGB value and
//     * previous state to a dark gray.
//     */
//    public void disable() {
//        this.prev_r = LED_DISABLED_RGB_VALUE;
//        this.prev_g = LED_DISABLED_RGB_VALUE;
//        this.prev_b = LED_DISABLED_RGB_VALUE;
//
//        this.r = LED_DISABLED_RGB_VALUE;
//        this.g = LED_DISABLED_RGB_VALUE;
//        this.b = LED_DISABLED_RGB_VALUE;
//
//        this.state = 0;
//    }
//}
