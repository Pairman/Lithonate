package org.eu.pnxlr.lithonate;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eu.pnxlr.lithonate.config.MalilibStuffsInitializer;

@Environment(EnvType.CLIENT)
public class Lithonate implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_NAME = "Lithonate";
    public static final String MOD_ID = "lithonate";
    public static String VERSION = "unknown";

    @Override
    public void onInitializeClient() {
        VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).orElseThrow(RuntimeException::new).getMetadata().getVersion().getFriendlyString();

        MalilibStuffsInitializer.init();
    }
}
