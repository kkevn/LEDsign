package com.kkevn.ledsign.ui.create;

public class Effect {

    final static String EFFECT_TEXT_SCROLL = "Scrolling Text";
    final static String EFFECT_COLOR_SOLID = "Solid Color";
    final static String EFFECT_COLOR_RAINBOW = "Rainbow Wave";

    private String type;
    private String param;

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
