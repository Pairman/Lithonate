package org.eu.pnxlr.lithonate.config.options;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBoolean;

public class LithonateConfigBoolean extends ConfigBoolean implements LithonateIConfigBase {
    public LithonateConfigBoolean(String name, boolean defaultValue) {
        super(name, defaultValue, LITHONATE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        boolean oldValue = this.getBooleanValue();

        super.setValueFromJsonElement(element);

        if (oldValue != this.getBooleanValue()) {
            this.onValueChanged();
        }
    }
}
