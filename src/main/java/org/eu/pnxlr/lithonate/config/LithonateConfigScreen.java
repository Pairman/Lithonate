package org.eu.pnxlr.lithonate.config;

import me.lambdaurora.spruceui.Position;
import me.lambdaurora.spruceui.SpruceTexts;
import me.lambdaurora.spruceui.background.SimpleColorBackground;
import me.lambdaurora.spruceui.option.SpruceBooleanOption;
import me.lambdaurora.spruceui.screen.SpruceScreen;
import me.lambdaurora.spruceui.widget.SpruceButtonWidget;
import me.lambdaurora.spruceui.widget.SpruceSeparatorWidget;
import me.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import me.lambdaurora.spruceui.wrapper.VanillaButtonWrapper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class LithonateConfigScreen extends SpruceScreen {
    private final Screen parent;

    public LithonateConfigScreen(Screen parent) {
        super(new TranslatableText("config.lithonate.title"));
        this.parent = parent;
    };

    @Override
    protected void init() {
        super.init();
        LithonateConfig config = LithonateConfig.getInstance();
        SpruceOptionListWidget list = new SpruceOptionListWidget(
                Position.of(0, 32), this.width, this.height - 36 - 32);

        list.setRenderTransition(false);
        list.setBackground(new SimpleColorBackground(0, 0, 0, 0));
        list.addSingleOptionEntry(new SpruceBooleanOption(
                "config.lithonate.enabled",
                config::isEnabled,
                config::setEnabled,
                new TranslatableText("config.lithonate.enabled.tooltip")
        ));
        list.addSingleOptionEntry(new SpruceBooleanOption(
                "config.lithonate.transparent_clicks",
                config::isTransparentClicks,
                config::setTransparentClicks,
                new TranslatableText("config.lithonate.transparent_clicks.tooltip")
        ));
        list.addSingleOptionEntry(new SpruceBooleanOption(
                "config.lithonate.transparent_clicks_banner_sign",
                config::isTransparentClicksBannerSign,
                config::setTransparentClicksBannerSign,
                new TranslatableText("config.lithonate.transparent_clicks_banner_sign.tooltip")
        ));
        this.children.add(list);

        this.addButton(new SpruceButtonWidget(
                Position.of(this, this.width / 2 - 75, this.height - 28),
                150, 20, SpruceTexts.GUI_DONE,
                btn -> { this.renderParent(); }
        ).asVanilla());
    }

    @Override
    public void renderTitle(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 16, 16777215);
    }

    public void renderParent() {
        this.client.method_29970(this.parent);
    }

    @Override
    public void onClose() {
        this.renderParent();
        LithonateConfig.saveConfig();
    }
}
