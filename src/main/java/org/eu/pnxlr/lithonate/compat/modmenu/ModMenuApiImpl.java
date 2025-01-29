package org.eu.pnxlr.lithonate.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.eu.pnxlr.lithonate.gui.LithonateConfigGui;

@Environment(EnvType.CLIENT)
public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            LithonateConfigGui gui = new LithonateConfigGui();
            gui.setParent(screen);
            return gui;
        };
    }
}
