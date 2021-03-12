package com.kkevn.ledsign.ui.create;

import com.google.gson.annotations.Expose;

public class Effect {

    public final static transient String TEXT_SCROLL = "Scrolling Text";
    public final static transient String COLOR_SOLID = "Solid Color";
    public final static transient String COLOR_RAINBOW = "Rainbow Wave";
    public final static transient String COLOR_FADE = "Fade";
    public final static transient String THEATER_CHASE = "Theater Chase";
    public final static transient String MARQUEE = "Marquee";
    public final static transient String RETRO = "Retro";

    public enum Effect_Types {
        EFFECT_TEXT_SCROLL(TEXT_SCROLL), EFFECT_COLOR_SOLID(COLOR_SOLID), EFFECT_COLOR_RAINBOW(COLOR_RAINBOW),
        EFFECT_FADE(COLOR_FADE), EFFECT_THEATER_CHASE(THEATER_CHASE), EFFECT_MARQUEE(MARQUEE),
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

    public String getParam() {

        // sort our readable params here maybe with switch?
        return this.param;
    }

    public String getMatrices(boolean asText) {

        String[] matrices = {"Front", "Right", "Back", "Left", "Top"};

        String sides = this.param.substring(1, this.param.indexOf(';'));

        if (asText) {
            String sidesAsText = "";

            for (int i = 0; i < sides.length(); i++) {
                if (sides.charAt(i) == '1') {
                    sidesAsText += (matrices[i] + " - ");
                }
            }
            return "[ " + sidesAsText.substring(0, sidesAsText.length() - 3) + " ]";
        } else {
            return sides;
        }
    }
}
