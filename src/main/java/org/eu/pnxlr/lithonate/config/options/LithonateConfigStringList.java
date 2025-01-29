package org.eu.pnxlr.lithonate.config.options;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigStringList;

import java.util.List;

public class LithonateConfigStringList extends ConfigStringList implements LithonateIConfigBase {
    public LithonateConfigStringList(String name, ImmutableList<String> defaultValue) {
        super(name, defaultValue, LITHONATE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        List<String> oldValue = Lists.newArrayList(this.getStrings());

        super.setValueFromJsonElement(element);

        if (!oldValue.equals(this.getStrings())) {
            this.onValueChanged();
        }
    }
}
