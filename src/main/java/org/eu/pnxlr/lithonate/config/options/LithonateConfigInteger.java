package org.eu.pnxlr.lithonate.config.options;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigInteger;

public class LithonateConfigInteger extends ConfigInteger implements LithonateIConfigBase {
    public LithonateConfigInteger(String name, int defaultValue) {
        super(name, defaultValue, LITHONATE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX);
    }

    public LithonateConfigInteger(String name, int defaultValue, int minValue, int maxValue) {
        super(name, defaultValue, minValue, maxValue, LITHONATE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        int oldValue = this.getIntegerValue();

        super.setValueFromJsonElement(element);

        if (oldValue != this.getIntegerValue()) {
            this.onValueChanged();
        }
    }
}
