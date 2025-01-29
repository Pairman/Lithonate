package org.eu.pnxlr.lithonate.config.options;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.StringUtils;
import org.eu.pnxlr.lithonate.Lithonate;
import org.eu.pnxlr.lithonate.config.LithonateConfigs;
import org.eu.pnxlr.lithonate.config.LithonateOption;

import java.util.function.Function;

public interface LithonateIConfigBase extends IConfigBase {
    String LITHONATE_NAMESPACE_PREFIX = Lithonate.MOD_ID + ".config.";
    String COMMENT_SUFFIX = ".comment";
    String PRETTY_NAME_SUFFIX = ".pretty_name";

    @Override
    default String getConfigGuiDisplayName() {
        return StringUtils.translate(LITHONATE_NAMESPACE_PREFIX + this.getName());
    }

    default LithonateOption getLithonateOption() {
        return LithonateConfigs.getOptionFromConfig(this).orElseThrow(() -> new RuntimeException("ConfigBase " + this + " not in LithonateConfigs"));
    }

    default Function<String, String> getGuiDisplayLineModifier() {
        LithonateOption lithonateOption = this.getLithonateOption();
        if (!lithonateOption.isEnabled()) {
            return line -> GuiBase.TXT_DARK_RED + line + GuiBase.TXT_RST;
        }
        return line -> line;
    }
}
