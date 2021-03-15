/**
 * LinkedMatrixContainer object contains a reference to each individual
 * linkedmatrix that it is given to create a single object to easily
 * track the status of each effect a matrix requires.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.processing;

import com.kkevn.ledsign.processing.ledsign.Matrix;

class LinkedMatrixContainer {

    LinkedMatrix linked_matrix;
    int matrixCount;
    String selections, params, effect;

    /**
     * Constructor for the linked matrix container object. Creates a linked
     * matrix based on the matrix selections provided and stores its effect
     * with custom parameters.
     *
     * @param {String} selections: Ordered string of matrices in this object.
     * @param {String} effect: Name of effect for these matrices to apply.
     * @param {String} params: Custom parameters for the supplied effect.
     */
    LinkedMatrixContainer(String selections, String effect, String params) {

        this.selections = selections;
        this.effect = effect;
        this.params = params;

        this.matrixCount = 0;

        // count matrices in this container
        for (int i = 0; i < selections.length(); i++) {
            if (selections.charAt(i) == '1') {
                this.matrixCount++;
            }
        }

        Matrix[] temp = new Matrix[this.matrixCount];

        /*for (int i = 0; i < selections.length(); i++) {
          if (selections.charAt(i) == '1') {
            temp[i] = matrices[i];
          }
        }*/

        int temp_i = 0;
        for (int i = 0; i < selections.length(); i++) {
            if (selections.charAt(i) == '1') {
                //temp[temp_i++] = matrices[i];
                temp[temp_i++] = ledsign.matrices[i];
            }
        }

        //this.linked_matrix = new ledsign.LinkedMatrix(temp);
        this.linked_matrix = new LinkedMatrix(temp);
    }

    /**
     * Returns the amount of matrices linked together.
     *
     * @return {int} Total amount of matrices present.
     */
    public int getMatrixCount() {
        return this.matrixCount;
    }

    /**
     * Returns true if the specified selections is equal to this
     * object's selections string.
     *
     * @param {String} s: Ordered string of matrices in this object.
     *
     * @return {boolean} Result of comparison from the matrix selections.
     */
    public boolean isSelectionMatch(String s) {
        return this.selections.equals(s);
    }

    /**
     * Returns the Matrix selections applied to this linked matrix.
     *
     * @return {String} Matrices applied to this object.
     */
    public String getSelections() {
        return this.selections;
    }

    /**
     * Returns the type of effect applied to this linked matrix.
     *
     * @return {String} Effect applied to this object.
     */
    public String getEffectType() {
        return this.effect;
    }

    /**
     * Returns the effect parameters applied to this linked matrix.
     *
     * @return {String} Parameters for effect applied to this object.
     */
    public String getParams() {
        return this.params;
    }
}