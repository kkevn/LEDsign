package com.kkevn.ledsign.ui.create;

import com.google.gson.annotations.Expose;

public class Effect {

    public final static transient String TEXT_SCROLL = "Scrolling Text";
    public final static transient String COLOR_SOLID = "Solid Color";
    public final static transient String COLOR_RAINBOW = "Rainbow Wave";

    public enum Effect_Types {
        EFFECT_TEXT_SCROLL(TEXT_SCROLL), EFFECT_COLOR_SOLID(COLOR_SOLID), EFFECT_COLOR_RAINBOW(COLOR_RAINBOW);

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
}
