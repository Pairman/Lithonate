package org.eu.pnxlr.lithonate.config.options;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;

public class LithonateConfigHotkey extends ConfigHotkey implements LithonateIConfigBase {
    public LithonateConfigHotkey(String name, String defaultStorageString) {
        super(name, defaultStorageString, LITHONATE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX);
    }

    public LithonateConfigHotkey(String name, String defaultStorageString, KeybindSettings settings) {
        super(name, defaultStorageString, KeybindSettings.DEFAULT, LITHONATE_NAMESPACE_PREFIX + name + COMMENT_SUFFIX);
    }
}
