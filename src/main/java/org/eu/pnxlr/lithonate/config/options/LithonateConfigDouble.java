package org.eu.pnxlr.lithonate.config.options;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigDouble;

public class LithonateConfigDouble extends ConfigDouble implements LithonateIConfigBase {
    public LithonateConfigDouble(String name, double defaultValue) {
        super(name, defaultValue, LITHONATE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX);
    }

    public LithonateConfigDouble(String name, double defaultValue, double minValue, double maxValue) {
        super(name, defaultValue, minValue, maxValue, LITHONATE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        double oldValue = this.getDoubleValue();

        super.setValueFromJsonElement(element);

        if (oldValue != this.getDoubleValue()) {
            this.onValueChanged();
        }
    }
}
