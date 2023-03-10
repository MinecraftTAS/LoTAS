package de.pfannekuchen.lotas.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.vertex.PoseStack;

import de.pfannekuchen.lotas.core.MCVer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;

/**
 * A searchable dropdown widget.
 */
public class DropdownWidget<T> extends AbstractWidget {

    /**
     * Horizontal padding between elements.
     */
    private static final int PADDING_H = 2;

    private final Font textRenderer;
    public final EditBox searchBox;
    private final Button dropdownButton;
    private final DropdownListWidget dropdown;
    @Nullable
    private AbstractWidget focused;
    private boolean dropdownOpenUp;

    public DropdownWidget(Font textRenderer, List<T> selections, Function<T, String> nameProvider, int x, int y, int width, int height, String title, T value, Consumer<T> saveHandler) {
    	//#if MC>=11601
    //$$ 	super(x, y, width, height, MCVer.literal(title));
    	//#else
    	super(x, y, width, height, title);
    	//#endif
        this.textRenderer = textRenderer;
        this.searchBox = MCVer.EditBox(textRenderer, x, y, width, height, title);
        this.dropdown = new DropdownListWidget(selections, nameProvider, saveHandler, x, y, width, height, title);
        this.dropdownButton = MCVer.Button(x, y, width, height, "...", btn -> {
            dropdown.visible ^= true;
            if(dropdown.visible) {
                if(focused != null) {
                	//#if MC>=11904
                //$$ 	focused.nextFocusPath(new net.minecraft.client.gui.navigation.FocusNavigationEvent.TabNavigation(false));
                	//#else
                    focused.changeFocus(true);
                    //#endif
                }
                //#if MC>=11904
                //$$ focused.nextFocusPath(new net.minecraft.client.gui.navigation.FocusNavigationEvent.TabNavigation(false));
                //#else
                dropdown.changeFocus(true);
                //#endif
                focused = dropdown;
            }
        });
        searchBox.setValue("");
        searchBox.setResponder(this::onSearchBoxTextChanged);
        searchBox.setMaxLength(65536);
        dropdown.visible = false;
        this.focused = null;
        this.dropdownOpenUp = false;
    }

    private void onSearchBoxTextChanged(String searchContents) {
        if(dropdown.updateSuggestedSelections(searchContents)) {
            searchBox.setTextColor(0xCCCCCC);
        } else {
            searchBox.setTextColor(0xff6655);
        }
    }
    
    //#if MC>=11904
    //$$ @Override public void render(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float delta) {
    //$$ MCVer.stack = stack;
    //#else
    //#if MC>=11601
    //$$ @Override public void renderButton(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float delta) {
    //$$ 	MCVer.stack = stack;
    //#else
    @Override public void renderButton(int mouseX, int mouseY, float delta) {
    //#endif
    //#endif
    	//#if MC>=11903
    //$$     int buttonWidth = (this.height);
    //$$     int searchBoxWidth = this.width - buttonWidth - PADDING_H;
    //$$     int buttonX, searchBoxX;
    //$$     if(textRenderer.isBidirectional()) {
    //$$         buttonX = this.getX();
    //$$         searchBoxX = this.getX() + buttonWidth + PADDING_H;
    //$$     } else {
    //$$         searchBoxX = this.getX();
    //$$         buttonX = this.getX() + searchBoxWidth + PADDING_H;
    //$$     }
    //$$     searchBox.setX(searchBoxX);
    //$$     searchBox.setY(this.getY());
    //$$     searchBox.setWidth(searchBoxWidth);
    //$$     dropdownButton.setX(buttonX);
    //$$     dropdownButton.setY(this.getY());
    //$$     dropdownButton.setWidth(buttonWidth);
    //$$     MCVer.render(searchBox, mouseX, mouseY, delta);
    //$$     MCVer.render(dropdownButton, mouseX, mouseY, delta);
    //$$     dropdown.setX(searchBoxX);
    //$$     dropdown.setY(searchBox.getY() + 20 + 1);
    //$$     dropdownOpenUp = false;
    //$$     int dropdownHeight = getLineHeight(textRenderer) * DropdownListWidget.DISPLAYED_LINE_COUNT;
    //$$     if(Minecraft.getInstance().screen != null) {
    //$$         int areaBottom = Minecraft.getInstance().screen.height - 20; // fuck
    //$$         if(dropdown.getY() + dropdownHeight > areaBottom) {
    //$$             dropdown.setY(searchBox.getY() - dropdownHeight);
    //$$             dropdownOpenUp = true;
    //$$         }
    //$$     }
    	//#else
        int buttonWidth = (this.height);
        int searchBoxWidth = this.width - buttonWidth - PADDING_H;
        int buttonX, searchBoxX;
        if(textRenderer.isBidirectional()) {
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
        MCVer.render(searchBox, mouseX, mouseY, delta);
        MCVer.render(dropdownButton, mouseX, mouseY, delta);
        dropdown.x = searchBoxX;
        dropdown.y = searchBox.y + 20 + 1;
        dropdownOpenUp = false;
        int dropdownHeight = getLineHeight(textRenderer) * DropdownListWidget.DISPLAYED_LINE_COUNT;
        if(Minecraft.getInstance().screen != null) {
            int areaBottom = Minecraft.getInstance().screen.height - 20; // fuck
            if(dropdown.y + dropdownHeight > areaBottom) {
                dropdown.y = searchBox.y - dropdownHeight;
                dropdownOpenUp = true;
            }
        }
        dropdown.setWidth(searchBoxWidth + DropdownListWidget.SCROLL_BAR_PADDING + DropdownListWidget.SCROLL_BAR_WIDTH);
        dropdown.setHeight(dropdownHeight);
        //#endif
    }

    //#if MC>=11601
    //#if MC>=11904
    //$$ @Override public void renderWidget(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float partial) {
    //#else
    //$$ @Override public void renderBg(com.mojang.blaze3d.vertex.PoseStack stack, Minecraft mc, int mouseX, int mouseY) {
    //#endif
    //$$ 	MCVer.stack = stack;
    //$$ 	if(dropdown.visible) {
    		//#if MC>=11904
    //$$ 		dropdown.renderWidget(stack, mouseX, mouseY, 1f);
    		//#else
    //$$ 		dropdown.renderButton(stack, mouseX, mouseY, 1f);
    		//#endif
    //$$     }
    //$$ }
    //#else
    @Override public void renderBg(Minecraft mc, int mouseX, int mouseY) {
    	if(dropdown.visible) {
    		dropdown.render(mouseX, mouseY, 1f);    
    		dropdown.renderButton(mouseX, mouseY, 1f);
        }
    }
    //#endif

    /**
     * The height, in pixels, of each line in the drop down menu.
     */
    private static int getLineHeight(Font textRenderer) {
        return textRenderer.lineHeight * 7 / 4;
    }

    public void modcfg_clearFocus() {
        dropdown.reset();
        focused = null;
    }

    
    @Override
    //#if MC>=11904
    //$$ public net.minecraft.client.gui.ComponentPath nextFocusPath(net.minecraft.client.gui.navigation.FocusNavigationEvent focusNavigationEvent) {
    //#else
    public boolean changeFocus(boolean lookForwards) {
    //#endif
        if(focused != null) {
        	//#if MC>=11904
        //$$ 	focused.nextFocusPath(focusNavigationEvent);
        	//#else
            focused.changeFocus(lookForwards);
          //#endif
        } else {
            this.setFocused(true);
        }
        if(focused == searchBox) {
            // searchBox -> (dropdownButton | dropdown) | prev
        	//#if MC>=11904
        //$$     focused = focusNavigationEvent != null ? (dropdown.visible ? dropdown : dropdownButton) : null;
        	//#else
            focused = lookForwards ? (dropdown.visible ? dropdown : dropdownButton) : null;
            //#endif
        } else if(focused == dropdownButton) {
            // dropdownButton -> next | searchBox
        	//#if MC>=11904
        //$$ 	focused = focusNavigationEvent != null ? null : searchBox;
        	//#else
            focused = lookForwards ? null : searchBox;
            //#endif
        } else if(focused == dropdown) {
            // dropdown -> searchBox
            focused = searchBox;
            dropdown.visible = false;
        } else {
            // prev/next -> this
            // dropdown is never open at this stage
        	//#if MC>=11904
        //$$ 	focused = focusNavigationEvent!=null ? searchBox : dropdownButton;
        	//#else
        	focused = lookForwards ? searchBox : dropdownButton;
        	//#endif
        }
        if(focused == null) {
            modcfg_clearFocus();
            //#if MC>=11904
            //$$ return null;
            //#else
            return false;
            //#endif
        } else {
        	//#if MC>=11904
        //$$ 	focused.nextFocusPath(focusNavigationEvent);
        	//#else
            focused.changeFocus(lookForwards);
            this.onFocusedChanged(true);
            //#endif
        }
        //#if MC>=11904
        //$$ return super.nextFocusPath(focusNavigationEvent);
        //#else
        return true;
        //#endif
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(focused != null) {
            if(focused == searchBox && dropdown.visible) {
                if((dropdownOpenUp && keyCode == GLFW.GLFW_KEY_UP) || (!dropdownOpenUp && keyCode == GLFW.GLFW_KEY_DOWN)) {
                    focused = dropdown;
                    //#if MC>=11904
                    //$$ dropdown.nextFocusPath(null);
                    //#else
                    dropdown.changeFocus(true);
                    //#endif
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
            	//#if MC>=11904
            //$$     focused.nextFocusPath(null);
                //#else
                focused.changeFocus(true);
                //#endif
            }
            focused = searchBox;
        	//#if MC>=11904
        //$$     focused.nextFocusPath(null);
            //#else
            focused.changeFocus(true);
            //#endif
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
    private class DropdownListWidget extends AbstractWidget {

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
        //$$ 	super(x, y, width, height, MCVer.literal(message));
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
            DropdownWidget.this.searchBox.setValue(nameProvider.apply(value));
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
        //#if MC>=11904
        //$$ @Override public void renderWidget(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float delta) {
        //#else
        //$$ @Override public void renderButton(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float delta) {
        //#endif
        //$$ 	MCVer.stack = stack;
        //#else
        @Override public void renderButton(int mouseX, int mouseY, float delta) {
        //#endif
            Font textRenderer = DropdownWidget.this.textRenderer;
            int lineHeight = getLineHeight(textRenderer);
            //#if MC>=11903
            //$$ int bgx0 = this.getX();
            //$$ int bgy0 = this.getY();
            //#else
            int bgx0 = this.x;
            int bgy0 = this.y;
            //#endif
            int bgx1 = bgx0 + this.width - SCROLL_BAR_WIDTH - SCROLL_BAR_PADDING;
            int bgy1 = bgy0 + this.height;
            // render background
            MCVer.fill(bgx0 - 1, bgy0 - 1, bgx1 + 1, bgy1 + 1, -6250336);
            MCVer.fill(bgx0, bgy0, bgx1, bgy1, 0xff000000);
            // render selections
            int endIdx = Math.min(suggestedSelections.size(), scrollIdx + DISPLAYED_LINE_COUNT);
            for(int i = scrollIdx; i < endIdx; i++) {
            	//#if MC>=11903
            //$$ 	int y = this.getY() + lineHeight * (i - scrollIdx);
            	//#else
                int y = this.y + lineHeight * (i - scrollIdx);
                //#endif
                if(i == selectedIndex) {
                    int sx0 = bgx0 - 1;
                    int sy0 = y;
                    int sx1 = bgx1 + 1;
                    int sy1 = y + lineHeight;
                    MCVer.fill(sx0 - 1, sy0 - 1, sx1 + 1, sy1 + 1, 0xffffffff);
                    MCVer.fill(sx0, sy0, sx1, sy1, 0xff000000);
                }
                String name = nameProvider.apply(suggestedSelections.get(i));
                int textX;
                if(textRenderer.isBidirectional()) {
                	textX = bgx1 - 2 - (int)textRenderer.width(name);
                } else {
                    textX = bgx0 + 2;
                }
                int textY = y + (lineHeight / 4);
                MCVer.drawShadow(name, textX, textY, 0xffffffff);
            }
            // render scroll bar
            int scrollMax = Math.max(1, suggestedSelections.size() - DISPLAYED_LINE_COUNT);
            float scrollPos = (float)scrollIdx / scrollMax;
            int scrollX0;
            if(textRenderer.isBidirectional()) {
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
            MCVer.fill(scrollX0, scrollY0, scrollX1, scrollY1, 0xffaaaaaa);
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
        //#if MC>=11904
        //$$ public net.minecraft.client.gui.ComponentPath nextFocusPath(net.minecraft.client.gui.navigation.FocusNavigationEvent focusNavigationEvent) {
        //$$ 	net.minecraft.client.gui.ComponentPath ret = super.nextFocusPath(focusNavigationEvent);
        //$$ 	if(ret!=null && selectedIndex < 0) {
        //#else
        public boolean changeFocus(boolean lookForwards) {
            boolean ret = super.changeFocus(lookForwards);
            if(ret && selectedIndex < 0) {
            	//#endif
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
                	//#if MC>=11904
                //$$ 	DropdownWidget.this.nextFocusPath(null);
                	//#else
                    DropdownWidget.this.changeFocus(true);
                    //#endif
                }
            }
            return false;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
        	//#if MC>=11903
        //$$     int offsetIdx = (int)(mouseY - this.getY()) / getLineHeight(DropdownWidget.this.textRenderer);
        	//#else
            int offsetIdx = (int)(mouseY - this.y) / getLineHeight(DropdownWidget.this.textRenderer);
            //#endif
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
//#if MC>=11903
//$$         @Override
//$$         protected void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput var1) {
//$$         }
//#else
//#if MC>=11700
//$$ 		@Override
//$$ 		public void updateNarration(net.minecraft.client.gui.narration.NarrationElementOutput narrationElementOutput) {
//$$ 		}
//#endif
//#endif

		@Override
		public void render(PoseStack var1, int var2, int var3, float var4) {
			
		}

    }
//#if MC>=11903
//$$     @Override
//$$     protected void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput var1) {
//$$     }
//#else
//#if MC>=11700
//$$ 	@Override
//$$ 	public void updateNarration(net.minecraft.client.gui.narration.NarrationElementOutput narrationElementOutput) {
//$$ 	}
//#endif
//#endif

}