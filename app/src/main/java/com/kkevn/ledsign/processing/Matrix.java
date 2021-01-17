//package com.kkevn.ledsign.processing;
///**
// * Matrix object contains an array of all the individually addressable
// * LEDs on its face.
// *
// * @author Kevin Kowalski
// */
//
//import com.kkevn.ledsign.processing.MatrixTemplate;
//import com.kkevn.ledsign.processing.ledsign;
//
//final int MATRIX_ROWS = 8;
//final int MATRIX_COLS = 8;
//final int MATRIX_LEDS = MATRIX_ROWS * MATRIX_COLS;
//
//class Matrix extends MatrixTemplate {
//
//    int size, id, r, g, b;
//
//    boolean doCircular;
//
//    ledsign.LED[] matrix;
//
//    /**
//     * Constructor for the matrix object. Creates a grid of LEDs according
//     * to the MATRIX_LEDS variable above at the specified render size.
//     *
//     * @param {int} size: The requested rendered size of the matrix.
//     * @param {int} id: The unique identity of this matrix.
//     * @param {int} r: The desired red RGB value.
//     * @param {int} g: The desired green RGB value.
//     * @param {int} b: The desired blue RGB value.
//     */
//    Matrix(int size, int id, int r, int g, int b) {
//
//        // calculate size (pixel width) of an LED based on the amount needed per row
//        this.size = size / MATRIX_ROWS;
//
//        // unique identity value for this matrix
//        this.id = id;
//
//        // initial color of matrix
//        this.r = r;
//        this.g = g;
//        this.b = b;
//
//        // shape of LEDs on matrix
//        this.doCircular = false;
//
//        // array to store each LED's state
//        this.matrix = new ledsign.LED[MATRIX_LEDS];
//
//        // populate the matrix with the appropriate amount of LEDs
//        for (int i = 0; i < MATRIX_LEDS; i++) {
//            //this.matrix.push(new LED(i, this.size, this.r, this.g, this.b));
//            //this.matrix = (LED[]) append(this.matrix, new LED(i, this.size, this.r, this.g, this.b));
//            this.matrix[i] = new ledsign.LED(i, this.size, this.r, this.g, this.b);
//        }
//    }
//
//    /**
//     * Renders each of the matrix's LEDs based on their current state.
//     */
//    public void display() {
//        for (int i = 0; i < MATRIX_LEDS; i++) {
//            this.matrix[i].display(this.doCircular);
//        }
//    }
//
//    /**
//     * Returns the amount of matrices linked together.
//     *
//     * @return {int} Single matrices always length of one.
//     */
//    public @Override
//    int getMatrixCount() {
//        return 1;
//    }
//
//    /**
//     * Returns this matrix's LED count.
//     *
//     * @return {int} Amount of LEDs present in matrix.
//     */
//    public @Override
//    int getLEDCount() {
//        return this.matrix.length;
//    }
//
//    /**
//     * Returns the row dimension of this matrix.
//     *
//     * @return {int} Total amount of rows present in this matrix.
//     */
//    public @Override
//    int getRowSize() {
//        return MATRIX_ROWS;
//    }
//
//    /**
//     * Returns the LED priority at the specified index.
//     *
//     * @param {int} index: The index of the LED to fetch from.
//     *
//     * @return {int} LED priority at specified index.
//     */
//    public @Override
//    int getPriorityAt(int index) {
//        return matrix[index].getPriority();
//    }
//
//    /**
//     * Returns the LED at the specified index.
//     *
//     * @param {int} x: The column coordinate of this LED.
//     * @param {int} y: The row coordinate of this LED.
//     *
//     * @return {LED} LED object at specified index.
//     */
//    public ledsign.LED getLEDAt(int x, int y) {
//        return this.matrix[((int) floor(x % MATRIX_ROWS)) + (y * MATRIX_COLS)];
//    }
//
//    /**
//     * Returns this matrix's unique ID value.
//     *
//     * @return {int} Unique ID passed during construction.
//     */
//    public int getID() {
//        return this.id;
//    }
//
//    /**
//     * Displays this matrix's unique ID value using its LEDs.
//     */
//    public @Override
//    void identify() {
//
//        // clear the matrix before rendering to it
//        this.disable();
//
//        // todo
//    }
//
//    /**
//     * Toggle the shape of the LEDs between circular or square.
//     */
//    public void toggleShape() {
//        this.doCircular = !this.doCircular;
//    }
//
//    /**
//     * Sets the RGB value of the LED at the specified index.
//     *
//     * @param {int} index: The index of the requested LED.
//     * @param {int} r: The desired red RGB value.
//     * @param {int} g: The desired green RGB value.
//     * @param {int} b: The desired blue RGB value.
//     */
//    public @Override
//    void setLEDAtIndex(int index, int r, int g, int b) {
//        if (index >= 0 && index < MATRIX_LEDS)
//            this.matrix[index].setColor(r, g, b);
//    }
//
//    /**
//     * Sets the RGB value and priority of the LED at the specified index.
//     *
//     * @param {int} index: The index of the requested LED.
//     * @param {int} r: The desired red RGB value.
//     * @param {int} g: The desired green RGB value.
//     * @param {int} b: The desired blue RGB value.
//     * @param {int} p: The desired priority level.
//     */
//    public @Override
//    void setLEDAtIndex(int index, int r, int g, int b, int p) {
//        if (index >= 0 && index < MATRIX_LEDS)
//            this.matrix[index].setColor(r, g, b);
//        this.matrix[index].setPriority(p);
//    }
//
//    /**
//     * Sets the RGB value of the LED at the specified coordinate.
//     *
//     * @param {int} x: The column coordinate of the requested LED.
//     * @param {int} y: The row coordinate of the requested LED.
//     * @param {int} r: The desired red RGB value.
//     * @param {int} g: The desired green RGB value.
//     * @param {int} b: The desired blue RGB value.
//     */
//    public @Override
//    void setLEDAtCoord(int x, int y, int r, int g, int b) {
//        if (x >= 0 && x < MATRIX_ROWS && y >= 0 && y < MATRIX_COLS)
//            this.matrix[x + (y * MATRIX_COLS)].setColor(r, g, b);
//    }
//
//    /**
//     * Sets the RGB value and priority of the LED at the specified coordinate.
//     *
//     * @param {int} x: The column coordinate of the requested LED.
//     * @param {int} y: The row coordinate of the requested LED.
//     * @param {int} r: The desired red RGB value.
//     * @param {int} g: The desired green RGB value.
//     * @param {int} b: The desired blue RGB value.
//     * @param {int} p: The desired priority level.
//     */
//    public @Override
//    void setLEDAtCoord(int x, int y, int r, int g, int b, int p) {
//        if (x >= 0 && x < MATRIX_ROWS && y >= 0 && y < MATRIX_COLS)
//            this.matrix[x + (y * MATRIX_COLS)].setColor(r, g, b);
//        this.matrix[x + (y * MATRIX_COLS)].setPriority(p);
//    }
//
//    /**
//     * Sets the RGB value of the LED at the specified index to
//     * its previous RGB state.
//     *
//     * @param {int} index: The index of the requested LED.
//     */
//    public @Override
//    void undoLEDAtIndex(int index) {
//        if (index >= 0 && index < MATRIX_LEDS)
//            this.matrix[index].undo();
//    }
//
//    /**
//     * Sets the RGB value of the LED at the specified coordinate
//     * to its previous RGB state.
//     *
//     * @param {int} x: The column coordinate of the requested LED.
//     * @param {int} y: The row coordinate of the requested LED.
//     */
//    public @Override
//    void undoLEDAtCoord(int x, int y) {
//        if (x >= 0 && x < MATRIX_ROWS && y >= 0 && y < MATRIX_COLS)
//            this.matrix[x + (y * MATRIX_COLS)].undo();
//    }
//
//    /**
//     * Visually turns off all of the LEDs on this matrix.
//     */
//    public @Override
//    void disable() {
//        for (int i = 0; i < MATRIX_LEDS; i++) {
//            this.matrix[i].disable();
//        }
//    }
//
//    /**
//     * Scrolls the contents of this matrix to the left by the
//     * specified amount of columns.
//     *
//     * @param {int} offset: The amount of columns to scroll to.
//     * @param {int} bg_r: The background red RGB value.
//     * @param {int} bg_g: The background green RGB value.
//     * @param {int} bg_b: The background blue RGB value.
//     */
//    public @Override
//    void scroll(int offset, int bg_r, int bg_g, int bg_b) {
//        for (int i = 0; i < MATRIX_LEDS; i++) {
//
//            int[] led_color = this.matrix[i].getColor();
//
//            if (i % MATRIX_ROWS != 0 && this.matrix[i].getPriority() == 1) {
//                this.setLEDAtIndex(i - offset, led_color[0], led_color[1], led_color[2], 1);
//                // maybe set old index to state zero
//                this.matrix[i].setPriority(0);
//            }
//            //else
//            //this.setLEDAtIndex(i, bg_r, bg_g, bg_b);
//        }
//    }
//
//  /*
//  00000000
//  00110000
//  00010000
//  00010000
//  00010000
//  00010000
//  01111110
//  00000000
//  */
//
//    /**
//     * Renders a single portion (column) of the given letter at the
//     * specified RGB value.
//     *
//     * @param {String} letter: Diagram for the letter to render.
//     * @param {int} column: The column of the diagram currently being rendered.
//     * @param {int} r: The desired red RGB value of this letter.
//     * @param {int} g: The desired green RGB value of this letter.
//     * @param {int} b: The desired blue RGB value of this letter.
//     */
//    public @Override
//    void addLetter(String letter, int column, int r, int g, int b) {
//        for (int i = 0; i < MATRIX_ROWS; i++) {
//            if (letter.charAt((i * MATRIX_COLS) + column) == '1')
//                this.setLEDAtCoord(MATRIX_COLS - 2, i, r, g, b, 1);
//        }
//    /*for (var i = 0; i < MATRIX_LEDS; i++) {
//      if (letter.charAt(i) == '1')
//        this.setLEDAtIndex(i, r, g, b);
//    }*/
//    }
//}