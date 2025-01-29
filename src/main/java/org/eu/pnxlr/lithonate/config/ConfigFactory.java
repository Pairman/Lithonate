package org.eu.pnxlr.lithonate.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import org.eu.pnxlr.lithonate.config.options.*;

public abstract class ConfigFactory {
    public static LithonateConfigHotkey newConfigHotKey(String name, String defaultStorageString) {
        return new LithonateConfigHotkey(name, defaultStorageString);
    }

    public static LithonateConfigHotkey newConfigHotKey(String name, String defaultStorageString, KeybindSettings settings) {
        return new LithonateConfigHotkey(name, defaultStorageString, settings);
    }

    public static LithonateConfigBooleanHotkeyed newConfigBooleanHotkeyed(String name) {
        return newConfigBooleanHotkeyed(name, false, "");
    }

    public static LithonateConfigBooleanHotkeyed newConfigBooleanHotkeyed(String name, boolean defaultValue, String defaultStorageString) {
        return new LithonateConfigBooleanHotkeyed(name, defaultValue, defaultStorageString);
    }

    public static LithonateConfigBoolean newConfigBoolean(String name, boolean defaultValue) {
        return new LithonateConfigBoolean(name, defaultValue);
    }

    public static LithonateConfigInteger newConfigInteger(String name, int defaultValue) {
        return new LithonateConfigInteger(name, defaultValue);
    }

    public static LithonateConfigInteger newConfigInteger(String name, int defaultValue, int minValue, int maxValue) {
        return new LithonateConfigInteger(name, defaultValue, minValue, maxValue);
    }

    public static LithonateConfigDouble newConfigDouble(String name, double defaultValue) {
        return new LithonateConfigDouble(name, defaultValue);
    }

    public static LithonateConfigDouble newConfigDouble(String name, double defaultValue, double minValue, double maxValue) {
        return new LithonateConfigDouble(name, defaultValue, minValue, maxValue);
    }

    public static LithonateConfigString newConfigString(String name, String defaultValue) {
        return new LithonateConfigString(name, defaultValue);
    }

    public static LithonateConfigStringList newConfigStringList(String name, ImmutableList<String> defaultValue) {
        return new LithonateConfigStringList(name, defaultValue);
    }

    public static LithonateConfigOptionList newConfigOptionList(String name, IConfigOptionListEntry defaultValue) {
        return new LithonateConfigOptionList(name, defaultValue);
    }
}
