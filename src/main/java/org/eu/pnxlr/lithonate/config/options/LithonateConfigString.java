package org.eu.pnxlr.lithonate.config.options;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigString;

import java.util.Objects;

public class LithonateConfigString extends ConfigString implements LithonateIConfigBase {
    public LithonateConfigString(String name, String defaultValue) {
        super(name, defaultValue, LITHONATE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        String oldValue = this.getStringValue();

        super.setValueFromJsonElement(element);

        if (!Objects.equals(oldValue, this.getStringValue())) {
            this.onValueChanged();
        }
    }
}
