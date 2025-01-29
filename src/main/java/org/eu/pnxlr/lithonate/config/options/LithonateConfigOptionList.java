package org.eu.pnxlr.lithonate.config.options;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.ConfigOptionList;

public class LithonateConfigOptionList extends ConfigOptionList implements LithonateIConfigBase {
    public LithonateConfigOptionList(String name, IConfigOptionListEntry defaultValue) {
        super(name, defaultValue, LITHONATE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        IConfigOptionListEntry oldValue = this.getOptionListValue();

        super.setValueFromJsonElement(element);

        if (oldValue != this.getOptionListValue()) {
            this.onValueChanged();
        }
    }
}
