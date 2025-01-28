package org.eu.pnxlr.lithonate;

import org.eu.pnxlr.lithonate.config.LithonateConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Lithonate implements ClientModInitializer {

    private static Lithonate INSTANCE;

    public static Lithonate getInstance() {
        return INSTANCE;
    }

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        LithonateConfig.loadConfig();
    }
}


