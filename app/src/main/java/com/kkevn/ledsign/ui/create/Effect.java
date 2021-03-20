package com.kkevn.ledsign.ui.create;

import com.google.gson.annotations.Expose;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Effect {

    public final static transient String TEXT_SCROLL = "Scrolling Text";
    public final static transient String COLOR_SOLID = "Solid Color";
    public final static transient String COLOR_RAINBOW = "Rainbow Wave";
    public final static transient String COLOR_FADE = "Fade";
    public final static transient String COLOR_WIPE = "Color Wipe";
    public final static transient String THEATER_CHASE = "Theater Chase";
    public final static transient String RETRO = "Retro";

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

    @Expose
    private String type;
    @Expose
    private String param;

    public Effect(String type) {
        this.type = type;
        this.param = "n/a";
    }

    public Effect(String type, String param) {
        this.type = type;
        this.param = param;
    }

    public String getType() {
        return this.type;
    }

    public String getTypeCode() {
        String typeCode = "";
        for (String s : this.type.split(" ")) {
            typeCode += s.charAt(0);
        }
        return typeCode;
    }

    public String getParam() {

        // sort our readable params here maybe with switch?
        return this.param;
    }

    public String getParamAt(int i) {

        String params = this.param.substring(1, this.param.length() - 1);
        String[] isolated_params = params.split(";");
        if (i < isolated_params.length)
            return isolated_params[i];
        else
            return "-";
    }

    public int[] getColor() {
        int[] colors = new int[4];

        // check if current effect has any RGB values using regex
        if (param.matches(".*(;[0-9]{1,3}){3}[;}].*")) {
            //String extractedInputs = param.substring(param.indexOf(';') + 1, param.length() - 1);
            //String extractedInputs = param.split("(;[0-9]{1,3}){3}[;}]", 1)[1];

            // extract the section of the effect parameters containing RGB values using regex
            String extractedInputs = "";
            Pattern p = Pattern.compile("(;[0-9]{1,3}){3}[;}]");
            Matcher m = p.matcher(param);
            if (m.find())
                extractedInputs = m.group(0);

            // mark colors array as having RGB
            colors[0] = 1;

            // initialize variables
            String val = "";
            int pos = 1;

            // parse the RGB integers into the colors array
            for (int i = 1; i < extractedInputs.length(); i++) {
                char curr = extractedInputs.charAt(i);
                if (curr != ';') {
                    val += curr;
                } else {
                    colors[pos++] = Integer.parseInt(val);
                    val = "";
                }
                if (pos > 3)
                    break;
            }
        }
        return colors;
    }

    public String getMatrices(boolean asText) {

        String[] matrices = {"Front", "Right", "Back", "Left", "Top"};

        String sides = this.param.substring(1, this.param.indexOf(';'));

        if (asText) {
            String sidesAsText = "";

            for (int i = 0; i < sides.length(); i++) {
                if (sides.charAt(i) == '1') {
                    sidesAsText += (matrices[i] + " + ");
                }
            }
            return "[ " + sidesAsText.substring(0, sidesAsText.length() - 3) + " ]";
        } else {
            return sides;
        }
    }
}