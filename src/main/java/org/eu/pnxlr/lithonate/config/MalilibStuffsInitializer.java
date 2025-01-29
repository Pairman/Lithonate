package org.eu.pnxlr.lithonate.config;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.event.InputEventHandler;
import org.eu.pnxlr.lithonate.Lithonate;

public class MalilibStuffsInitializer {
    public static void init() {
        InitializationHandler.getInstance().registerInitializationHandler(() -> {
            ConfigManager.getInstance().registerConfigHandler(Lithonate.MOD_ID, new LithonateConfigStorage());

            InputEventHandler.getKeybindManager().registerKeybindProvider(new KeybindProvider());

            LithonateConfigs.initCallbacks();
        });
    }
}
