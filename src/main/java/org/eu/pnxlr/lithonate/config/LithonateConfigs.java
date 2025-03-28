package org.eu.pnxlr.lithonate.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.interfaces.IValueChangeCallback;

import org.eu.pnxlr.lithonate.Lithonate;
import org.eu.pnxlr.lithonate.config.options.*;
import org.eu.pnxlr.lithonate.gui.LithonateConfigGui;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class LithonateConfigs {
    /**
     * ============================
     * Config Declarations
     * ============================
     */

    ////////////////////
    //    Features    //
    ////////////////////

    @Config(type = Config.Type.TWEAK, category = Config.Category.FEATURES)
    public static final LithonateConfigBooleanHotkeyed TWEAK_AUTO_ANTI_GHOST_BLOCKS = ConfigFactory.newConfigBooleanHotkeyed("tweakAutoAntiGhostBlocks", false, "");

    @Config(type = Config.Type.TWEAK, category = Config.Category.FEATURES)
    public static final LithonateConfigBooleanHotkeyed TWEAK_BETTER_BLOCK_PLACEMENT_AGAINST_SIGNS = ConfigFactory.newConfigBooleanHotkeyed("tweakBetterBlockPlacementAgainstSigns", false, "");

    @Config(type = Config.Type.TWEAK, category = Config.Category.FEATURES)
    public static final LithonateConfigBooleanHotkeyed TWEAK_BETTER_DUSTS_RAILS_PLACEMENT_ON_TRAPDOORS = ConfigFactory.newConfigBooleanHotkeyed("tweakBetterDustsRailsPlacementOnTrapdoors", false, "");

    @Config(type = Config.Type.TWEAK, category = Config.Category.FEATURES)
    public static final LithonateConfigBooleanHotkeyed TWEAK_BIGGER_COCOA_BLOCK_HITBOXES = ConfigFactory.newConfigBooleanHotkeyed("tweakBiggerCocoaBlockHitboxes", true, "");

    @Config(type = Config.Type.TWEAK, category = Config.Category.FEATURES)
    public static final LithonateConfigBooleanHotkeyed TWEAK_BIGGER_BANNER_SIGN_BLOCK_HITBOXES = ConfigFactory.newConfigBooleanHotkeyed("tweakBiggerBannerSignBlockHitboxes", true, "");

    @Config(type = Config.Type.TWEAK, category = Config.Category.FEATURES)
    public static final LithonateConfigBooleanHotkeyed TWEAK_HIGHLIGHT_BLOCK_OUTLINES = ConfigFactory.newConfigBooleanHotkeyed("tweakHighlightBlockOutlines", true, "");
    @Config(type = Config.Type.TWEAK, category = Config.Category.FEATURES)
    public static final LithonateConfigBooleanHotkeyed TWEAK_HIGHLIGHT_BLOCK_OUTLINES_THROUGH = ConfigFactory.newConfigBooleanHotkeyed("tweakHighlightBlockOutlinesThrough", false, "");
    @Config(type = Config.Type.GENERIC, category = Config.Category.FEATURES)
    public static final LithonateConfigInteger TWEAK_HIGHLIGHT_BLOCK_OUTLINES_WIDTH = ConfigFactory.newConfigInteger("tweakHighlightBlockOutlinesWidth", 4, 1, 20);

    @Config(type = Config.Type.TWEAK, category = Config.Category.FEATURES)
    public static final LithonateConfigBooleanHotkeyed TWEAK_TRANSPARENT_BLOCK_CLICKS = ConfigFactory.newConfigBooleanHotkeyed("tweakTransparentBlockClicks", true, "");
    @Config(type = Config.Type.GENERIC, category = Config.Category.FEATURES)
    public static final LithonateConfigBoolean TWEAK_TRANSPARENT_BLOCK_CLICKS_BANNER_SIGN = ConfigFactory.newConfigBoolean("tweakTransparentBlockClicksBannerSign", false);
    @Config(type = Config.Type.GENERIC, category = Config.Category.FEATURES)
    public static final LithonateConfigBoolean TWEAK_TRANSPARENT_BLOCK_CLICKS_NO_OPENING_TRAPDOORS = ConfigFactory.newConfigBoolean("tweakTransparentBlockClicksNoOpeningTrapdoors", true);

    @Config(type = Config.Type.GENERIC, category = Config.Category.FEATURES)
    public static final LithonateConfigBoolean TWEAK_TRANSPARENT_WITHER_ROSE_HITBOXES = ConfigFactory.newConfigBoolean("tweakTransparentWitherRoseHitboxes", false);

    ////////////////////
    //    Disables    //
    ////////////////////

    @Config(type = Config.Type.TWEAK, category = Config.Category.DISABLES)
    public static final LithonateConfigBooleanHotkeyed YEET_NO_BREAKING_NETHER_BRICKS_UNDER_WITHER_ROSES = ConfigFactory.newConfigBooleanHotkeyed("yeetNoBreakingNetherBricksUnderWitherRoses", true, "");

    @Config(type = Config.Type.TWEAK, category = Config.Category.DISABLES)
    public static final LithonateConfigBooleanHotkeyed YEET_NO_CLICKING_NETHER_PORTAL_SIDES = ConfigFactory.newConfigBooleanHotkeyed("yeetNoClickingNetherPortalSides", true, "");

    @Config(type = Config.Type.TWEAK, category = Config.Category.DISABLES)
    public static final LithonateConfigBooleanHotkeyed YEET_NO_CLICKING_WITHER_ROSES = ConfigFactory.newConfigBooleanHotkeyed("yeetNoClickingWitherRoses", true, "");

    ////////////////////
    //    Settings    //
    ////////////////////

    @Config(type = Config.Type.GENERIC, category = Config.Category.SETTINGS)
    public static final LithonateConfigBoolean SETTING_ENABLE = ConfigFactory.newConfigBoolean("settingEnable", true);

    @Config(type = Config.Type.HOTKEY, category = Config.Category.SETTINGS)
    public static final LithonateConfigHotkey SETTING_OPEN_CONFIG_GUI = ConfigFactory.newConfigHotKey("settingOpenConfigGui", "L,C");

    @Config(type = Config.Type.TWEAK, category = Config.Category.SETTINGS)
    public static final LithonateConfigBooleanHotkeyed SETTING_DEBUG_MODE = ConfigFactory.newConfigBooleanHotkeyed("settingDebugMode");

    /**
     * ============================
     * Implementation Details
     * ============================
     */

    public static void initCallbacks() {
        // common callbacks
        IValueChangeCallback<ConfigBoolean> redrawConfigGui = newValue -> LithonateConfigGui.getCurrentInstance().ifPresent(LithonateConfigGui::reDraw);

        // hotkeys
        SETTING_OPEN_CONFIG_GUI.getKeybind().setCallback(LithonateConfigGui::onOpenGuiHotkey);

        // debugs
        SETTING_DEBUG_MODE.setValueChangeCallback(redrawConfigGui);
    }

    private static final List<LithonateOption> OPTIONS = Lists.newArrayList();
    private static final Map<Config.Category, List<LithonateOption>> CATEGORY_TO_OPTION = Maps.newLinkedHashMap();
    private static final Map<Config.Type, List<LithonateOption>> TYPE_TO_OPTION = Maps.newLinkedHashMap();
    private static final Map<IConfigBase, LithonateOption> CONFIG_TO_OPTION = Maps.newLinkedHashMap();

    static {
        for (Field field : LithonateConfigs.class.getDeclaredFields()) {
            Config annotation = field.getAnnotation(Config.class);
            if (annotation != null) {
                try {
                    Object config = field.get(null);
                    if (!(config instanceof LithonateIConfigBase)) {
                        Lithonate.LOGGER.warn("[Lithonate] {} is not a subclass of LithonateIConfigBase", config);
                        continue;
                    }
                    LithonateOption lithonateOption = new LithonateOption(annotation, (LithonateIConfigBase) config);
                    OPTIONS.add(lithonateOption);
                    CATEGORY_TO_OPTION.computeIfAbsent(lithonateOption.getCategory(), k -> Lists.newArrayList()).add(lithonateOption);
                    TYPE_TO_OPTION.computeIfAbsent(lithonateOption.getType(), k -> Lists.newArrayList()).add(lithonateOption);
                    CONFIG_TO_OPTION.put(lithonateOption.getConfig(), lithonateOption);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<LithonateOption> getOptions(Config.Category categoryType) {
        if (categoryType == Config.Category.ALL) {
            return OPTIONS;
        }
        return CATEGORY_TO_OPTION.getOrDefault(categoryType, Collections.emptyList());
    }

    public static List<LithonateOption> getOptions(Config.Type optionType) {
        return TYPE_TO_OPTION.getOrDefault(optionType, Collections.emptyList());
    }

    public static Stream<LithonateIConfigBase> getAllConfigOptionStream() {
        return OPTIONS.stream().map(LithonateOption::getConfig);
    }

    public static Optional<LithonateOption> getOptionFromConfig(IConfigBase iConfigBase) {
        return Optional.ofNullable(CONFIG_TO_OPTION.get(iConfigBase));
    }

    public static boolean hasConfig(IConfigBase iConfigBase) {
        return getOptionFromConfig(iConfigBase).isPresent();
    }
}
