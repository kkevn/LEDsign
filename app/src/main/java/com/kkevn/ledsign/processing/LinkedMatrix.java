/**
 * LinkedMatrix object contains a reference to each individual matrix
 * that it is linked with to create a single, larger matrix so that
 * multiple matrices can easily be treated as one.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.processing;

import com.kkevn.ledsign.processing.ledsign.Matrix;

import static processing.core.PApplet.append;
import static processing.core.PApplet.floor;
import static processing.core.PApplet.sqrt;

class LinkedMatrix extends MatrixTemplate {

    Matrix[] linkedmatrix;
    int gridSize, rowSize, LEDcount;

    /**
     * Constructor for the linked matrix object. Creates an array
     * to track references to each matrix object it contains.
     *
     * @param {Matrix} ...m: List of matrices in this object.
     */
    LinkedMatrix(Matrix ...m) {

        // array to store reference to each matrix
        this.linkedmatrix = new Matrix[m.length];

        // populate the matrix with the appropriate amount of LEDs
        //args.forEach(arg => this.linkedmatrix.push(arg));
        int index = 0;
        for (Matrix matrix : m) {
            //append(this.linkedmatrix, matrix);
            this.linkedmatrix[index++] = matrix;
        }

        // retrieves the LED count per matrix
        this.gridSize = this.linkedmatrix[0].getLEDCount();

        // calculates the row dimension of a square matrix
        this.rowSize = (int) sqrt(this.gridSize);

        // calculates the total LEDs among the listed matrices
        this.LEDcount = this.linkedmatrix.length * this.gridSize;
    }

    /**
     * Returns the amount of matrices linked together.
     *
     * @return {int} Total amount of matrices present.
     */
    public @Override
    int getMatrixCount() {
        return this.linkedmatrix.length;
    }

    /**
     * Returns the total LED count from each matrix.
     *
     * @return {int} Total amount of LEDs present from each matrix.
     */
    public @Override
    int getLEDCount() {
        return this.LEDcount;
    }

    /**
     * Returns the row dimension of each matrix.
     *
     * @return {int} Total amount of rows present in a matrix.
     */
    public @Override
    int getRowSize() {
        return this.rowSize;
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
        return this.linkedmatrix[floor(index / this.gridSize)].getPriorityAt(index % this.gridSize);
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
        return this.linkedmatrix[floor(x / this.rowSize)].getLEDAt(x, y);
    }

    /**
     * Returns each matrix's unique ID value.
     *
     * @return {int array} Contains ID of each Matrix in this LinkedMatrix.
     */
    public int[] getID() {

        // create empty array
        int[] arr_ids = new int[this.getMatrixCount()];

        // fill array with ID from each matrix
        for (int i = 0; i < this.linkedmatrix.length; i++) {
            //arr_ids.push(this.linkedmatrix[i].getID());
            append(arr_ids, this.linkedmatrix[i].getID());

            //TODO
        }

        return arr_ids;
    }

    /**
     * Displays each matrix's unique ID value using its LEDs.
     */
    public @Override
    void identify() {
        for (int i = 0; i < this.linkedmatrix.length; i++) {
            this.linkedmatrix[i].identify();
        }
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
        if (index >= 0 && index < this.LEDcount)
            this.linkedmatrix[floor(index / this.gridSize)].setLEDAtIndex(index % this.gridSize, r, g, b);
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
        if (index >= 0 && index < this.LEDcount)
            this.linkedmatrix[floor(index / this.gridSize)].setLEDAtIndex(index % this.gridSize, r, g, b, p);
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
        if (x >= 0 && x < (this.linkedmatrix.length * this.rowSize) && y >= 0 && y < this.rowSize)
            this.linkedmatrix[floor(x / this.rowSize)].setLEDAtCoord(x % this.rowSize, y, r, g, b);
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
        if (x >= 0 && x < (this.linkedmatrix.length * this.rowSize) && y >= 0 && y < this.rowSize)
            this.linkedmatrix[floor(x / this.rowSize)].setLEDAtCoord(x % this.rowSize, y, r, g, b, p);
    }

    /**
     * Sets the RGB value of the LED at the specified index to
     * its previous RGB state.
     *
     * @param {int} index: The index of the requested LED.
     */
    public @Override
    void undoLEDAtIndex(int index) {
        if (index >= 0 && index < this.LEDcount)
            this.linkedmatrix[floor(index / this.gridSize)].undoLEDAtIndex(index % this.gridSize);
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
        if (x >= 0 && x < (this.linkedmatrix.length * this.rowSize) && y >= 0 && y < this.rowSize)
            this.linkedmatrix[floor(x / this.rowSize)].undoLEDAtCoord(x % this.rowSize, y);
    }

    /**
     * Visually turns off all of the LEDs in each matrix.
     */
    public @Override
    void disable() {
        for (int i = 0; i < this.linkedmatrix.length; i++) {
            this.linkedmatrix[i].disable();
        }
    }

    /**
     * Clears all of the LEDs in each matrix.
     */
    public @Override
    void clear() {
        for (int i = 0; i < this.linkedmatrix.length; i++) {
            this.linkedmatrix[i].clear();
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

        // used to adjust indices across matrices at the connecting edges
        int edge_offset = this.gridSize - this.rowSize;

        //int[] color = {0, 100, 0};

        for (int x = 0; x < (this.linkedmatrix.length * this.rowSize); x++) {
            for (int y = 0; y < this.rowSize; y++) {
                int [] clr = this.linkedmatrix[floor(x / this.rowSize)].getLEDAt(x, y).getColor();

                if (this.linkedmatrix[floor(x / this.rowSize)].getLEDAt(x, y).getPriority() == 1) {
                    this.setLEDAtCoord(x - 1, y, clr[0], clr[1], clr[2], 1);
                    this.undoLEDAtCoord(x, y);
                }

                this.linkedmatrix[floor(x / this.rowSize)].getLEDAt(x, y).setPriority(0);
            }
        }


    /*for (int i = 0; i < this.LEDcount; i++) {

      // retrieve the color at the current index
      //int[] color = this.linkedmatrix[floor(i / this.gridSize)].matrix[i % this.gridSize].getColor();
      int[] led_color = this.linkedmatrix[floor(i / this.gridSize)].matrix[i % this.gridSize].getColor();
      //Matrix x = this.linkedmatrix[floor(i / this.gridSize)];
      //LED y = x.matrix[i % this.gridSize];
      //int[] z = y.getColor();

      // for first columns of a matrix, adjust indices to move towards last column of next matrix
      if (i % this.rowSize == 0 && this.linkedmatrix[floor(i / this.gridSize)].matrix[i % this.gridSize].getPriority() == 1) {
        this.setLEDAtIndex(i - edge_offset - offset, led_color[0], led_color[1], led_color[2], 1);
        //this.linkedmatrix[floor(i / this.gridSize)].matrix[i % this.gridSize].setPriority(0);
        //this.undoLEDAtIndex(i - edge_offset - offset);
      }
      else if (i % this.rowSize != 0 && this.linkedmatrix[floor(i / this.gridSize)].matrix[i % this.gridSize].getPriority() == 1) {
        this.setLEDAtIndex(i - offset, led_color[0], led_color[1], led_color[2], 1);
        //this.linkedmatrix[floor(i / this.gridSize)].matrix[i % this.gridSize].setPriority(0);
        //this.undoLEDAtIndex(i - offset);
      }

      this.linkedmatrix[floor(i / this.gridSize)].matrix[i % this.gridSize].setPriority(0);
    }*/
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

        int cols = (this.gridSize / this.rowSize) * this.linkedmatrix.length;

        for (int i = 0; i < this.rowSize; i++) {
            if (letter.charAt((i * this.rowSize) + column) == '1')
                this.setLEDAtCoord(cols - 2, i, r, g, b, 1);
                //this.setLEDAtCoord(cols - 2, i, r, g, b, pos);
            //else // added
            //this.getLEDAt(cols - 2, i).setPriority(0);
        }
    }
}