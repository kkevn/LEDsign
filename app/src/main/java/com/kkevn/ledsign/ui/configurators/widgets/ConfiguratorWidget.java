/**
 * ConfiguratorWidget is the template for any child ConfiguratorWidgets. A ConfiguratorWidget is a
 * UI object that builds up a ConfiguratorFragment. These widgets accept inputs for a specific item
 * and these inputs are later parsed to obtain the full customization from a given effect
 * configurator.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.configurators.widgets;

import com.kkevn.ledsign.ui.configurators.ConfiguratorFragment;

public abstract class ConfiguratorWidget {

    // obtain reference to the ConfiguratorListener
    public final ConfiguratorFragment.ConfiguratorListener cl = ConfiguratorFragment.getCl();

    /**
     * Determines whether or not to disable this ConfiguratorWidget based on the given enabled flag.
     *
     * @param {boolean} enabled: Enable flag.
     */
    public abstract void setEnabled(boolean enabled);

    /**
     * Updates the default values of each customizable item with the given inputs.
     *
     * @param {String ...} inputs: List of inputs to update with.
     */
    public abstract void updateWidgetInputs(String ...inputs);

    /**
     * Returns the parsed inputs of each customizable item in this ConfiguratorWidget.
     *
     * @return {String} Parsed inputs of this widget.
     */
    public abstract String parseWidgetInputs();
}