package de.pfannekuchen.lotas.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.storage.WorldInfo;
import rlog.RLogAPI;
/**
 * Draws the Gui containing the {@link GuiSeedList}. Automatically creates a new world for you with the selected seed
 * @author Pancake
 *
 */
public class GuiSeedsMenu extends GuiScreen {
	
	GuiSeedList list;
	
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(1, this.width / 2 - 154, this.height - 28, 304, 20, I18n.format("selectWorld.create")));
		list = new GuiSeedList(width, height, 32, height - 64, 36);
	}
	
	@Override
	protected void mouseReleased(int arg0, int arg1, int arg2) {
		list.func_148181_b(arg0, arg1, arg2);
		super.mouseReleased(arg0, arg1, arg2);
	}
	
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		list.func_148179_a(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void handleMouseInput() {
		super.handleMouseInput();
	}
    
    @Override
    protected void actionPerformed(GuiButton button) {
    	if (button.id == 1 && GuiSeedList.selectedIndex != -1) {
    		RLogAPI.logDebug("[SeedsMenu] Recreating World with Seed from Seeds Menu ");
    		GuiCreateWorld guicreateworld = new GuiCreateWorld(this);
    		GuiSeedList.SeedEntry et = GuiSeedList.seeds.get(GuiSeedList.selectedIndex);
    		WorldSettings set = new WorldSettings(Long.parseLong(et.seed), GameType.SURVIVAL, true, false, net.minecraft.world.WorldType.DEFAULT);
    		set = set.enableCommands();
    		guicreateworld.func_146318_a(new WorldInfo(set, et.name));
    		Minecraft.getMinecraft().displayGuiScreen(guicreateworld);
    	}
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		list.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRendererObj, "Seeds", this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
}
