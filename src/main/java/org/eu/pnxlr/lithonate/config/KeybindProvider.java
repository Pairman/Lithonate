package org.eu.pnxlr.lithonate.config;

import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import org.eu.pnxlr.lithonate.Lithonate;

import java.util.List;
import java.util.stream.Collectors;

public class KeybindProvider implements IKeybindProvider {
    private static final List<IHotkey> ALL_CUSTOM_HOTKEYS = LithonateConfigs.getAllConfigOptionStream().
            filter(option -> option instanceof IHotkey).
            map(option -> (IHotkey) option).
            collect(Collectors.toList());

    @Override
    public void addKeysToMap(IKeybindManager manager) {
        ALL_CUSTOM_HOTKEYS.forEach(iHotkey -> manager.addKeybindToMap(iHotkey.getKeybind()));
    }

    @Override
    public void addHotkeys(IKeybindManager manager) {
        manager.addHotkeysForCategory(Lithonate.MOD_NAME, "lithonate.hotkeys.category.main", ALL_CUSTOM_HOTKEYS);
    }
}
