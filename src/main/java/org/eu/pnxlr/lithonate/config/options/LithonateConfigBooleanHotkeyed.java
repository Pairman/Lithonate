package org.eu.pnxlr.lithonate.config.options;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;

public class LithonateConfigBooleanHotkeyed extends ConfigBooleanHotkeyed implements LithonateIConfigBase {
    public LithonateConfigBooleanHotkeyed(String name, boolean defaultValue, String defaultHotkey) {
        super(name, defaultValue, defaultHotkey, LITHONATE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX, LITHONATE_NAMESPACE_PREFIX + name + PRETTY_NAME_SUFFIX);
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
