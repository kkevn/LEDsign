package com.kkevn.ledsign.ui.configurators.widgets;

import com.kkevn.ledsign.ui.configurators.ConfiguratorFragment;

public abstract class ConfiguratorWidget {

    public ConfiguratorFragment.ConfiguratorListener cl = ConfiguratorFragment.getCl();

    public abstract void updateWidgetInputs(String ...inputs);
    public abstract String parseWidgetInputs();
}