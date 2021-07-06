package de.pfannekuchen.lotas.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.LiteralText;

/**
 * A searchable dropdown widget.
 */
public class DropdownWidget<T> extends AbstractButtonWidget {

    /**
     * Horizontal padding between elements.
     */
    private static final int PADDING_H = 2;

    private final TextRenderer textRenderer;
    public final TextFieldWidget searchBox;
    private final ButtonWidget dropdownButton;
    private final DropdownListWidget dropdown;
    @Nullable
    private AbstractButtonWidget focused;
    private boolean dropdownOpenUp;

    public DropdownWidget(TextRenderer textRenderer, List<T> selections, Function<T, String> nameProvider, int x, int y, int width, int height, String title, T value, Consumer<T> saveHandler) {
    	//#if MC>=11601
    //$$ 	super(x, y, width, height, new LiteralText(title));
    	//#else
    	super(x, y, width, height, title);
    	//#endif
        this.textRenderer = textRenderer;
        //#if MC>=11601
        //$$ this.searchBox = new TextFieldWidget(textRenderer, x, y, width, height, new LiteralText(title));
        //#else
        this.searchBox = new TextFieldWidget(textRenderer, x, y, width, height, title);
        //#endif
        this.dropdown = new DropdownListWidget(selections, nameProvider, saveHandler, x, y, width, height, title);
        //#if MC>=11601
        //$$ this.dropdownButton = new ButtonWidget(x, y, width, height, new LiteralText(""), btn -> {
        //#else
        this.dropdownButton = new ButtonWidget(x, y, width, height, "", btn -> {
        //#endif
            dropdown.visible ^= true;
            if(dropdown.visible) {
                if(focused != null) {
                    focused.changeFocus(true);
                }
                dropdown.changeFocus(true);
                focused = dropdown;
            }
        });
        searchBox.setText("???");
        searchBox.setChangedListener(this::onSearchBoxTextChanged);
        searchBox.setMaxLength(65536);
        dropdown.visible = false;
        this.focused = null;
        this.dropdownOpenUp = false;
    }

    private void onSearchBoxTextChanged(String searchContents) {
        if(dropdown.updateSuggestedSelections(searchContents)) {
            searchBox.setEditableColor(0xCCCCCC);
        } else {
            searchBox.setEditableColor(0xff6655);
        }
    }

    //#if MC>=11601
    //$$ @Override public void renderButton(net.minecraft.client.util.math.MatrixStack matrices, int mouseX, int mouseY, float delta) {
    //#else
    @Override public void renderButton(int mouseX, int mouseY, float delta) {
    //#endif
        int buttonWidth = (this.height);
        int searchBoxWidth = this.width - buttonWidth - PADDING_H;
        int buttonX, searchBoxX;
        if(textRenderer.isRightToLeft()) {
            buttonX = this.x;
            searchBoxX = this.x + buttonWidth + PADDING_H;
        } else {
            searchBoxX = this.x;
            buttonX = this.x + searchBoxWidth + PADDING_H;
        }
        searchBox.x = searchBoxX;
        searchBox.y = this.y;
        searchBox.setWidth(searchBoxWidth);
        dropdownButton.x = buttonX;
        dropdownButton.y = this.y;
        dropdownButton.setWidth(buttonWidth);
        //#if MC>=11601
        //$$ searchBox.render(matrices, mouseX, mouseY, delta);
        //$$ dropdownButton.render(matrices, mouseX, mouseY, delta);
        //#else
        searchBox.render(mouseX, mouseY, delta);
      dropdownButton.render(mouseX, mouseY, delta);
        //#endif
        dropdown.x = searchBoxX;
        // height
        dropdown.y = searchBox.y + 20 + 1;
        dropdownOpenUp = false;
        int dropdownHeight = getLineHeight(textRenderer) * DropdownListWidget.DISPLAYED_LINE_COUNT;
        if(MinecraftClient.getInstance().currentScreen != null) {
            int areaBottom = MinecraftClient.getInstance().currentScreen.height - 20; // fuck
            if(dropdown.y + dropdownHeight > areaBottom) {
                dropdown.y = searchBox.y - dropdownHeight;
                dropdownOpenUp = true;
            }
        }
        dropdown.setWidth(searchBoxWidth + DropdownListWidget.SCROLL_BAR_PADDING + DropdownListWidget.SCROLL_BAR_WIDTH);
        dropdown.setHeight(dropdownHeight);
    }

    //#if MC>=11601
    //$$ @Override public void renderBg(net.minecraft.client.util.math.MatrixStack matrices, MinecraftClient mc, int mouseX, int mouseY) {
    //$$     if(dropdown.visible) {
    //$$         dropdown.render(matrices, mouseX, mouseY, 1f);
    //$$         dropdown.renderButton(matrices, mouseX, mouseY, 1f);
    //#else
    @Override public void renderBg(MinecraftClient mc, int mouseX, int mouseY) {
    	if(dropdown.visible) {
    		dropdown.render(mouseX, mouseY, 1f);    
    		dropdown.renderButton(mouseX, mouseY, 1f);
    //#endif
            // for some reason, rendering a single upward facing drop down causes text
            // on some widgets to render in front of the drop down
        }
    }

    /**
     * The height, in pixels, of each line in the drop down menu.
     */
    private static int getLineHeight(TextRenderer textRenderer) {
        return textRenderer.fontHeight * 7 / 4;
    }

    public void modcfg_clearFocus() {
        dropdown.reset();
        focused = null;
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        if(focused != null) {
            focused.changeFocus(lookForwards);
        } else {
            this.setFocused(true);
        }
        if(focused == searchBox) {
            // searchBox -> (dropdownButton | dropdown) | prev
            focused = lookForwards ? (dropdown.visible ? dropdown : dropdownButton) : null;
        } else if(focused == dropdownButton) {
            // dropdownButton -> next | searchBox
            focused = lookForwards ? null : searchBox;
        } else if(focused == dropdown) {
            // dropdown -> searchBox
            focused = searchBox;
            dropdown.visible = false;
        } else {
            // prev/next -> this
            // dropdown is never open at this stage
            focused = lookForwards ? searchBox : dropdownButton;
        }
        if(focused == null) {
            modcfg_clearFocus();
            return false;
        } else {
            focused.changeFocus(lookForwards);
            this.onFocusedChanged(true);
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(focused != null) {
            if(focused == searchBox && dropdown.visible) {
                if((dropdownOpenUp && keyCode == GLFW.GLFW_KEY_UP) || (!dropdownOpenUp && keyCode == GLFW.GLFW_KEY_DOWN)) {
                    focused = dropdown;
                    dropdown.changeFocus(true);
                    // the dropdown always has at least one entry at this point
                    dropdown.setSelectedIndex(0);
                    return true;
                }
            }
            return focused.keyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        if(focused == dropdown) {
            // prevent switching to the search bar when the drop down button is pressed using SPACE
            if(chr == ' ') {
                return false;
            }
            if(focused != null) {
                focused.changeFocus(true);
            }
            focused = searchBox;
            focused.changeFocus(true);
        }
        return searchBox.charTyped(chr, keyCode);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return super.isMouseOver(mouseX, mouseY) || (dropdown.visible && dropdown.isMouseOver(mouseX, mouseY));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(dropdownButton.isMouseOver(mouseX, mouseY)) {
            return dropdownButton.mouseClicked(mouseX, mouseY, button);
        } else if(searchBox.isMouseOver(mouseX, mouseY)) {
            focused = searchBox;
            this.setFocused(true);
            return searchBox.mouseClicked(mouseX, mouseY, button);
        } else if(dropdown.visible && dropdown.isMouseOver(mouseX, mouseY)) {
            return dropdown.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if(dropdown.visible && dropdown.isMouseOver(mouseX, mouseY)) {
            return dropdown.mouseScrolled(mouseX, mouseY, amount);
        }
        return false;
    }

    /**
     * The actual drop down menu widget.
     */
    private class DropdownListWidget extends AbstractButtonWidget {

        private static final int SCROLL_BAR_WIDTH = 5;
        private static final int SCROLL_BAR_PADDING = 4;
        private static final int MIN_SCROLL_BAR_HEIGHT = 5;
        private static final int DISPLAYED_LINE_COUNT = 5;

        private final List<T> selections;
        private final Function<T, String> nameProvider;
        private final List<T> suggestedSelections;
        private final Consumer<T> saveHandler;
        private int selectedIndex;
        private int scrollIdx;
        private boolean frozen;

        public DropdownListWidget(List<T> selections, Function<T, String> nameProvider, Consumer<T> saveHandler, int x, int y, int width, int height, String message) {
        	//#if MC>=11601
        //$$ 	super(x, y, width, height, new LiteralText(message));
        	//#else
        	super(x, y, width, height, message);
        	//#endif
        	this.selections = selections;
            this.nameProvider = nameProvider;
            this.suggestedSelections = new ArrayList<>(selections);
            this.saveHandler = saveHandler;
            this.selectedIndex = -1;
            this.scrollIdx = 0;
            this.frozen = false;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public void setSelectedIndex(int idx) {
            assert idx >= 0 && idx < suggestedSelections.size() : "Index out of range: " + idx;
            frozen = true;
            selectedIndex = idx;
            T value = suggestedSelections.get(idx);
            DropdownWidget.this.searchBox.setText(nameProvider.apply(value));
            saveHandler.accept(value);
            frozen = false;
        }

        /**
         * Updates the suggested selections, checking the match string against the selections.
         *
         * @param match The search text to match.
         * @return true if there is a single exact match, false otherwise.
         */
        public boolean updateSuggestedSelections(String match) {
            if(!frozen) {
                suggestedSelections.clear();
                selectedIndex = -1;
                scrollIdx = 0;
                match = match.toLowerCase();
                T exactMatch = null;
                for(T value : selections) {
                    String localizedName = nameProvider.apply(value).toLowerCase();
                    if(localizedName.contains(match)) {
                        suggestedSelections.add(value);
                    }
                    if(exactMatch == null && localizedName.equals(match)) {
                        exactMatch = value;
                    }
                }
                this.visible = !suggestedSelections.isEmpty() && !match.isEmpty();
                // save the exact value if it exists
                if(exactMatch != null) {
                    saveHandler.accept(exactMatch);
                    return true;
                } else {
                    return false;
                }
            }
            // assume a valid selection if we are not asked to search for one
            return true;
        }

        //#if MC>=11601
        //$$ @Override public void renderButton(net.minecraft.client.util.math.MatrixStack matrices, int mouseX, int mouseY, float delta) {
        //#else
        @Override public void renderButton(int mouseX, int mouseY, float delta) {
        //#endif
            TextRenderer textRenderer = DropdownWidget.this.textRenderer;
            int lineHeight = getLineHeight(textRenderer);
            int bgx0 = this.x;
            int bgy0 = this.y;
            int bgx1 = bgx0 + this.width - SCROLL_BAR_WIDTH - SCROLL_BAR_PADDING;
            int bgy1 = bgy0 + this.height;
            // render background
            //#if MC>=11601
            //$$ DrawableHelper.fill(matrices, bgx0 - 1, bgy0 - 1, bgx1 + 1, bgy1 + 1, -6250336);
            //$$ DrawableHelper.fill(matrices, bgx0, bgy0, bgx1, bgy1, 0xff000000);
            //#else
            DrawableHelper.fill(bgx0 - 1, bgy0 - 1, bgx1 + 1, bgy1 + 1, -6250336);
            DrawableHelper.fill(bgx0, bgy0, bgx1, bgy1, 0xff000000);
            //#endif
            // render selections
            int endIdx = Math.min(suggestedSelections.size(), scrollIdx + DISPLAYED_LINE_COUNT);
            for(int i = scrollIdx; i < endIdx; i++) {
                int y = this.y + lineHeight * (i - scrollIdx);
                if(i == selectedIndex) {
                    int sx0 = bgx0 - 1;
                    int sy0 = y;
                    int sx1 = bgx1 + 1;
                    int sy1 = y + lineHeight;
                    //#if MC>=11601
                    //$$ DrawableHelper.fill(matrices, sx0 - 1, sy0 - 1, sx1 + 1, sy1 + 1, 0xffffffff);
                    //$$ DrawableHelper.fill(matrices, sx0, sy0, sx1, sy1, 0xff000000);
                    //#else
                    DrawableHelper.fill(sx0 - 1, sy0 - 1, sx1 + 1, sy1 + 1, 0xffffffff);
                    DrawableHelper.fill(sx0, sy0, sx1, sy1, 0xff000000);
                    //#endif
                }
                String name = nameProvider.apply(suggestedSelections.get(i));
                int textX;
                if(textRenderer.isRightToLeft()) {
                	//#if MC>=11601
                //$$ 	textX = bgx1 - 2 - (int)textRenderer.getWidth(name);
                	//#else
                	textX = bgx1 - 2 - (int)textRenderer.getStringWidth(name);
                	//#endif
                } else {
                    textX = bgx0 + 2;
                }
                int textY = y + (lineHeight / 4);
                //#if MC>=11601
                //$$ this.drawStringWithShadow(matrices, textRenderer, name, textX, textY, 0xffffffff);
            	//#else
            	this.drawString(textRenderer, name, textX, textY, 0xffffffff);
            	//#endif
            }
            // render scroll bar
            int scrollMax = Math.max(1, suggestedSelections.size() - DISPLAYED_LINE_COUNT);
            float scrollPos = (float)scrollIdx / scrollMax;
            int scrollX0;
            if(textRenderer.isRightToLeft()) {
                scrollX0 = bgx0 - SCROLL_BAR_PADDING - SCROLL_BAR_WIDTH;
            } else {
                scrollX0 = bgx1 + SCROLL_BAR_PADDING;
            }
            int scrollX1 = scrollX0 + SCROLL_BAR_WIDTH;
            int scrollHeight = Math.min(bgy1 - bgy0, Math.max(MIN_SCROLL_BAR_HEIGHT, 2 * (bgy1 - bgy0) / scrollMax));
            int maxNonFullScrollHeight = bgy1 - bgy0 - MIN_SCROLL_BAR_HEIGHT;
            if(suggestedSelections.size() > DISPLAYED_LINE_COUNT && scrollHeight > maxNonFullScrollHeight) {
                scrollHeight = maxNonFullScrollHeight;
            }
            int scrollY0 = (int)(scrollPos * (bgy1 - scrollHeight) + (1 - scrollPos) * bgy0);
            int scrollY1 = scrollY0 + scrollHeight;
            //#if MC>=11601
            //$$ DrawableHelper.fill(matrices, scrollX0, scrollY0, scrollX1, scrollY1, 0xffaaaaaa);
            //#else
            DrawableHelper.fill(scrollX0, scrollY0, scrollX1, scrollY1, 0xffaaaaaa);
            //#endif
        }

        /**
         * Unselects, unfocuses, and hides the drop down.
         */
        public void reset() {
            this.selectedIndex = -1;
            this.scrollIdx = 0;
            this.setFocused(false);
            this.visible = false;
        }

        @Override
        public boolean changeFocus(boolean lookForwards) {
            boolean ret = super.changeFocus(lookForwards);
            if(ret && selectedIndex < 0) {
                scrollIdx = 0;
            }
            return ret;
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if(!suggestedSelections.isEmpty()) {
                if(keyCode == GLFW.GLFW_KEY_DOWN) {
                    int idx = selectedIndex + 1;
                    if(idx >= suggestedSelections.size()) {
                        idx = 0;
                        scrollIdx = 0;
                    } else if(idx - scrollIdx >= DISPLAYED_LINE_COUNT) {
                        scrollIdx++;
                    }
                    setSelectedIndex(idx);
                    return true;
                } else if(keyCode == GLFW.GLFW_KEY_UP) {
                    int idx = selectedIndex - 1;
                    if(idx < 0) {
                        idx = suggestedSelections.size() - 1;
                        scrollIdx = Math.max(0, suggestedSelections.size() - DISPLAYED_LINE_COUNT);
                    } else if(idx - scrollIdx < 0) {
                        scrollIdx--;
                    }
                    setSelectedIndex(idx);
                    return true;
                } else if(keyCode == GLFW.GLFW_KEY_ENTER) {
                    // close drop down and 'select' current selection
                    DropdownWidget.this.changeFocus(true);
                }
            }
            return false;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            int offsetIdx = (int)(mouseY - this.y) / getLineHeight(DropdownWidget.this.textRenderer);
            int absoluteIdx = offsetIdx + scrollIdx;
            if(absoluteIdx < suggestedSelections.size()) {
                setSelectedIndex(absoluteIdx);
                return true;
            }
            return false;
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
            int scrollToIdx = scrollIdx - (int)(amount / 8.0 * getLineHeight(DropdownWidget.this.textRenderer));
            int scrollMax = Math.max(0, suggestedSelections.size() - DISPLAYED_LINE_COUNT);
            if(scrollToIdx < 0) {
                scrollIdx = 0;
            } else if(scrollToIdx >= scrollMax) {
                scrollIdx = scrollMax;
            } else {
                scrollIdx = scrollToIdx;
            }
            return true;
        }
    }
}