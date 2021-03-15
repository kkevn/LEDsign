/**
 * MatrixTemplate object contains a template for the Matrix and
 * LinkedMatrix objects. It simply contains the necessary common
 * functions between the two so that effects can be easily
 * applied referencing the same functions regardless of whether
 * or not each matrix is linked.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.processing;

class MatrixTemplate {

    /**
     * Returns the amount of matrices linked together.
     *
     * @return {int} Total amount of matrices present.
     */
    public int getMatrixCount() {
        return 0;
    }

    /**
     * Returns the total LED count from each matrix.
     *
     * @return {int} Total amount of LEDs present from each matrix.
     */
    public int getLEDCount() {
        return 0;
    }

    /**
     * Returns the row dimension of each matrix.
     *
     * @return {int} Total amount of rows present in a matrix.
     */
    public int getRowSize() {
        return 0;
    }

    /**
     * Returns the LED priority at the specified index.
     *
     * @param {int} index: The index of the LED to fetch from.
     *
     * @return {int} LED priority at specified index..
     */
    public int getPriorityAt(int index) {
        return 0;
    }

    /**
     * Displays each matrix's unique ID value using its LEDs.
     */
    public void identify() {

    }

    /**
     * Sets the RGB value of the LED at the specified index.
     *
     * @param {int} index: The index of the requested LED.
     * @param {int} r: The desired red RGB value.
     * @param {int} g: The desired green RGB value.
     * @param {int} b: The desired blue RGB value.
     */
    public void setLEDAtIndex(int index, int r, int g, int b) {

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
    public void setLEDAtIndex(int index, int r, int g, int b, int p) {

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
    public void setLEDAtCoord(int x, int y, int r, int g, int b) {

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
    public void setLEDAtCoord(int x, int y, int r, int g, int b, int p) {

    }

    /**
     * Sets the RGB value of the LED at the specified index to
     * its previous RGB state.
     *
     * @param {int} index: The index of the requested LED.
     */
    public void undoLEDAtIndex(int index) {

    }

    /**
     * Sets the RGB value of the LED at the specified coordinate
     * to its previous RGB state.
     *
     * @param {int} x: The column coordinate of the requested LED.
     * @param {int} y: The row coordinate of the requested LED.
     */
    public void undoLEDAtCoord(int x, int y) {

    }

    /**
     * Visually turns off all of the LEDs in each matrix.
     */
    public void disable() {

    }

    /**
     * Clears all of the LEDs in each matrix.
     */
    public void clear() {

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
    public void scroll(int offset, int bg_r, int bg_g, int bg_b) {

    }

    /**
     * Renders a single portion (column) of the given letter at the
     * specified RGB value.
     *
     * @param {string} letter: Diagram for the letter to render.
     * @param {int} column: The column of the diagram currently being rendered.
     * @param {int} r: The desired red RGB value of this letter.
     * @param {int} g: The desired green RGB value of this letter.
     * @param {int} b: The desired blue RGB value of this letter.
     */
    public void addLetter(String letter, int column, int r, int g, int b) {

    }
}