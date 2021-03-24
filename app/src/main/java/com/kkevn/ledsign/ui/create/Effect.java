/**
 * Effect is the object used to represent a single customizable effect that is displayed through the
 * buildable effects list and then rendered on the 3D preview sketch.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.create;

import com.google.gson.annotations.Expose;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Effect {

    // initialize all possible effect types (transient for JSON object)
    public final static transient String TEXT_SCROLL = "Scrolling Text";
    public final static transient String COLOR_SOLID = "Solid Color";
    public final static transient String COLOR_RAINBOW = "Rainbow Wave";
    public final static transient String COLOR_FADE = "Fade";
    public final static transient String COLOR_WIPE = "Color Wipe";
    public final static transient String THEATER_CHASE = "Theater Chase";
    public final static transient String RETRO = "Retro (WIP)";

    // store all possible effect types in enum
    public enum Effect_Types {
        EFFECT_COLOR_SOLID(COLOR_SOLID), EFFECT_TEXT_SCROLL(TEXT_SCROLL), EFFECT_COLOR_RAINBOW(COLOR_RAINBOW),
        EFFECT_FADE(COLOR_FADE), EFFECT_WIPE(COLOR_WIPE), EFFECT_THEATER_CHASE(THEATER_CHASE),
        EFFECT_RETRO(RETRO);

        private final String stringValue;

        Effect_Types(final String s) {
            stringValue = s;
        }

        public String toString() {
            return stringValue;
        }
    }

    // declare relevant variables (exposed for JSON object)
    @Expose
    private String type;
    @Expose
    private String param;

    /**
     * Constructor for this Effect.
     *
     * @param {String} type: Name of this effect.
     */
    public Effect(String type) {

        // initialize this effect's variables
        this.type = type;
        this.param = "n/a";
    }

    /**
     * Constructor for this Effect.
     *
     * @param {String} type: Name of this effect.
     * @param {String} param: Customized parameters of this effect.
     */
    public Effect(String type, String param) {

        // initialize this effect's variables
        this.type = type;
        this.param = param;
    }

    /**
     * Returns the Effect object as a String with its type and parameters concatenated.
     *
     * @return {String} Effect type and parameters concatenated.
     */
    public String getEffectID() {
        return getType() + getParam();
    }

    /**
     * Returns the Effect object's type or name.
     *
     * @return {String} Effect type.
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns the Effect object's type code as used in the parsing for both the 3D sketch and
     * Arduino. The type code is simply the first letter of each word in the name of the effect.
     *
     * @return {String} Effect type code.
     */
    public String getTypeCode() {
        String typeCode = "";

        // split each word of this effect's type/name
        for (String s : getType().split(" ")) {

            // add the first letter of this word
            typeCode += s.charAt(0);
        }

        return typeCode;
    }

    /**
     * Returns the Effect object's parameters.
     *
     * @return {String} Effect parameters.
     */
    public String getParam() {
        return this.param;
    }

    /**
     * Returns a single parameter of the Effect object at the specified position.
     *
     * @param {int} pos: Position of parameter in effect's parameter list.
     *
     * @return {String} Parameter at specified position.
     */
    public String getParamAt(int pos) {

        // isolate parameters from the outside curly braces and split into array on delimiter
        String params = getParam().substring(1, getParam().length() - 1);
        String[] isolated_params = params.split(";");

        // return the parameter at the position if it exists
        return pos < isolated_params.length ? isolated_params[pos] : "-";
    }

    /**
     * Returns the Effect object's RGB color values if present. Returned as integer array, with
     * first value a boolean on whether the effect has RGB values. If non-zero, final three indexes
     * contain the RGB values of this effect.
     *
     * @return {int[]} Flag and RGB values of this effect's parameters.
     */
    public int[] getColor() {

        // initialize empty array of size 4, first value as flag and remainder as RGB values
        int[] colors = new int[4];

        // check if current effect has any RGB values using regex
        if (getParam().matches(".*(;[0-9]{1,3}){3}[;}].*")) {

            // extract the section of the effect parameters containing RGB values using regex
            String extractedInputs = "";
            Pattern p = Pattern.compile("(;[0-9]{1,3}){3}[;}]");
            Matcher m = p.matcher(getParam());
            if (m.find())
                extractedInputs = m.group(0);

            // flag colors array as containing RGB values
            colors[0] = 1;

            // initialize temporary variables
            String val = "";
            int pos = 1;

            // parse the RGB integers into the colors array
            for (int i = 1; i < extractedInputs.length(); i++) {

                // get the current character to observe
                char curr = extractedInputs.charAt(i);

                // if delimiter found, begin building next parameter as integer
                if (curr != ';') {
                    val += curr;
                } else {
                    colors[pos++] = Integer.parseInt(val);
                    val = "";
                }

                // all RGB values found, no need to keep iterating further
                if (pos > 3)
                    break;
            }
        }

        // return the array
        return colors;
    }

    /**
     * Returns the Effect object's list of matrices its effect is applied to. Can return in either
     * default binary values or as words representing each face of the 3D preview.
     *
     * @param {boolean} asText: Whether or not to return the effects as default integers or text.
     *
     * @return {String} List of matrices effects are to be applied to.
     */
    public String getMatrices(boolean asText) {

        // define words for each face of the 3D preview (order is same as binary parameter values)
        final String[] matrices = {"Front", "Right", "Back", "Left", "Top"};

        // fetch the binary list of matrices from the effect's parameters
        String sides = getParam().substring(1, getParam().indexOf(';'));

        // return the matrices in the format requested
        if (asText) {

            // update the list only where the binary parameter is true
            String sidesAsText = "";
            for (int i = 0; i < sides.length(); i++) {
                if (sides.charAt(i) == '1') {
                    sidesAsText += (matrices[i] + " + ");
                }
            }

            // return list removing unnecessary '+' suffix
            return "[ " + sidesAsText.substring(0, sidesAsText.length() - 3) + " ]";

        } else {
            return sides;
        }
    }
}