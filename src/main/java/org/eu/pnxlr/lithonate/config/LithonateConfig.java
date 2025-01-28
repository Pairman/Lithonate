package org.eu.pnxlr.lithonate.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LithonateConfig {

    private static final LithonateConfig INSTANCE = new LithonateConfig();
    private static final Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir().resolve("lithonate.json");
    private static final transient Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private boolean isEnabled = true;
    private boolean isTransparentClicks = true;
    private boolean isTransparentClicksBannerSign = false;

    public static LithonateConfig getInstance() {
        return INSTANCE;
    }

    public static void loadConfig() {
        if (!Files.exists(CONFIG_PATH)) {
            saveConfig();
            return;
        }
        try {
            LithonateConfig configs = GSON.fromJson(
                    Files.readString(CONFIG_PATH), LithonateConfig.class);
            getInstance().isEnabled = configs.isEnabled;
            getInstance().isTransparentClicksBannerSign = configs.isTransparentClicksBannerSign;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        try {
            Files.writeString(CONFIG_PATH, GSON.toJson(INSTANCE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    public boolean isTransparentClicks() {
        return this.isTransparentClicks;
    }

    public void setTransparentClicks(boolean transparentClicks) {
        this.isTransparentClicks = transparentClicks;
    }

    public boolean isTransparentClicksBannerSign() {
        return this.isTransparentClicksBannerSign;
    }

    public void setTransparentClicksBannerSign(boolean transparentClicksBannerSign) {
        this.isTransparentClicksBannerSign = transparentClicksBannerSign;
    }
}
