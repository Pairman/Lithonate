package org.eu.pnxlr.lithonate.gui;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetBase;
import fi.dy.masa.malilib.gui.widgets.WidgetSearchBar;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.interfaces.IStringValue;
import fi.dy.masa.malilib.util.StringUtils;
import org.eu.pnxlr.lithonate.Lithonate;
import org.eu.pnxlr.lithonate.config.Config;
import org.eu.pnxlr.lithonate.config.LithonateConfigs;
import org.eu.pnxlr.lithonate.config.LithonateOption;
import org.eu.pnxlr.lithonate.mixins.core.gui.WidgetSearchBarAccessor;
import org.eu.pnxlr.lithonate.util.FabricUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LithonateConfigGui extends GuiConfigsBase {
    @Nullable
    private static LithonateConfigGui currentInstance = null;
    private static Config.Category category = Config.Category.SETTINGS;
    @Nullable
    private Config.Type filteredType = null;

    private final List<WidgetBase> hoveringWidgets = Lists.newArrayList();
    private WidgetSearchBar searchBar = null;

    public LithonateConfigGui() {
        super(10, 50, Lithonate.MOD_ID, null, "lithonate.gui.title", Lithonate.VERSION);
        currentInstance = this;
    }

    @Override
    public void removed() {
        super.removed();
        currentInstance = null;
    }

    public static Optional<LithonateConfigGui> getCurrentInstance() {
        return Optional.ofNullable(currentInstance);
    }

    public static boolean onOpenGuiHotkey(KeyAction keyAction, IKeybind iKeybind) {
        GuiBase.openGui(new LithonateConfigGui());
        return true;
    }

    public void setSearchBar(WidgetSearchBar searchBar) {
        this.searchBar = searchBar;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.clearOptions();

        this.hoveringWidgets.clear();
        int x = 10;
        int y = 26;

        for (Config.Category category : Config.Category.values()) {
            x += this.createNavigationButton(x, y, category);
        }

        x = this.width - 11;
        x = this.createTypeFilterDropDownList(x) - 5;
        if (this.searchBar != null) {
            GuiTextFieldGeneric searchBox = ((WidgetSearchBarAccessor) this.searchBar).getSearchBox();
            int deltaWidth = Math.max(50, x - this.searchBar.getX()) - this.searchBar.getWidth();
            this.searchBar.setWidth(this.searchBar.getWidth() + deltaWidth);
            searchBox.setWidth(searchBox.getWidth() + deltaWidth);
        }
    }

    private int createNavigationButton(int x, int y, Config.Category category) {
        ButtonGeneric button = new ButtonGeneric(x, y, -1, 20, category.getDisplayName());
        button.setEnabled(LithonateConfigGui.category != category);
        button.setHoverStrings(category.getDescription());
        this.addButton(button, (b, mb) -> this.setDisplayParameter(LithonateConfigGui.category, category, () -> LithonateConfigGui.category = category));
        return button.getWidth() + 2;
    }

    private <T extends IStringValue> int createDropDownList(int x, List<T> entries, T defaultValue, Supplier<T> valueGetter, Consumer<T> valueSetter, String hoverTextKey, Consumer<SelectorDropDownList<T>> postProcessor) {
        int y = this.getListY() + 3;
        int height = 16;
        int maxTextWidth = entries.stream().
                filter(Objects::nonNull).
                mapToInt(e -> this.getStringWidth(e.getStringValue())).
                max().orElse(-1);
        // constant 20 reference: fi.dy.masa.malilib.gui.widgets.WidgetDropDownList.getRequiredWidth
        int width = Math.max(maxTextWidth, 40) + 20;

        SelectorDropDownList<T> dd = new SelectorDropDownList<>(x - width, y, width, height, 200, entries.size(), entries);
        dd.setEntryChangeListener(entry -> this.setDisplayParameter(valueGetter.get(), entry, () -> valueSetter.accept(entry)));
        dd.setSelectedEntry(defaultValue);
        dd.setHoverText(hoverTextKey);
        postProcessor.accept(dd);

        this.addWidget(dd);
        this.hoveringWidgets.add(dd);

        return dd.getX();
    }

    private int createTypeFilterDropDownList(int x) {
        Set<Config.Type> possibleTypes = LithonateConfigs.getOptions(LithonateConfigGui.category).stream().map(LithonateOption::getType).collect(Collectors.toSet());
        List<Config.Type> items = Arrays.stream(Config.Type.values()).filter(possibleTypes::contains).collect(Collectors.toList());
        items.add(0, null);

        return this.createDropDownList(
                x, items, this.filteredType,
                () -> this.filteredType, type -> this.filteredType = type,
                "lithonate.gui.config_type.label_text",
                dd -> dd.setNullEntry(() -> StringUtils.translate("lithonate.gui.selector_drop_down_list.all"))
        );
    }

    private <T> void setDisplayParameter(T currentValue, T newValue, Runnable valueSetter) {
        if (newValue != currentValue) {
            valueSetter.run();
            this.reDraw();
        }
    }

    public void reDraw() {
        this.reCreateListWidget(); // apply the new config width
        Objects.requireNonNull(this.getListWidget()).resetScrollbarPosition();
        this.initGui();
    }

    public void renderHoveringWidgets(MatrixStack matrixStack, int mouseX, int mouseY) {
        this.hoveringWidgets.forEach(widgetBase -> widgetBase.render(mouseX, mouseY, widgetBase.isMouseOver(mouseX, mouseY), matrixStack));
    }

    public Pair<Integer, Integer> adjustWidths(int guiWidth, int maxTextWidth) {
        int labelWidth;
        int panelWidth = 190;
        guiWidth -= 75;

        // tweak label width first, to make sure the panel is not too close or too far from the label
        labelWidth = MathHelper.clamp(guiWidth - panelWidth, maxTextWidth - 5, maxTextWidth + 100);
        // decrease the panel width if space is not enough
        panelWidth = MathHelper.clamp(guiWidth - labelWidth, 100, panelWidth);
        // decrease the label width for a bit if space is still way not enough (the label text might overlap with the panel now)
        labelWidth = MathHelper.clamp(guiWidth - panelWidth + 25, labelWidth - Math.max((int) (maxTextWidth * 0.4), 30), labelWidth);

        // just in case
        labelWidth = Math.max(labelWidth, 0);
        panelWidth = Math.max(panelWidth, 0);

        return Pair.of(labelWidth, panelWidth);
    }

    @SuppressWarnings({"SingleStatementInBlock", "RedundantIfStatement"})
    private boolean shouldDisplayOption(LithonateOption option) {
        // drop down list filtering logic
        if (this.filteredType != null && option.getType() != this.filteredType) {
            return false;
        }
        // hide debug options unless debug mode on
        if (option.isDebug() && !LithonateConfigs.SETTING_DEBUG_MODE.getBooleanValue()) {
            return false;
        }
        // hide dev only options unless debug mode on and is dev env
        if (option.isDevOnly() && !(LithonateConfigs.SETTING_DEBUG_MODE.getBooleanValue() && FabricUtil.isDevelopmentEnvironment())) {
            return false;
        }
        return true;
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        List<IConfigBase> configs = LithonateConfigs.getOptions(LithonateConfigGui.category)
                .stream()
                .filter(this::shouldDisplayOption)
                .map(LithonateOption::getConfig)
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .collect(Collectors.toList());
        return ConfigOptionWrapper.createFor(configs);
    }
}
