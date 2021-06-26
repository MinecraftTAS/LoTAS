package de.pfannekuchen.lotas.gui;

import java.time.Duration;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.gui.widgets.ButtonWidget;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;

public class GuiChallengeLeaderboard extends GuiScreen {
	
	ChallengeMap map;
	
	public GuiChallengeLeaderboard() {}
	
	public GuiChallengeLeaderboard(ChallengeMap map) {
		this.map = map;
	}
	
	private GuiChallengeSlot slot;
	
	@Override
	public void initGui() {
		slot = new GuiChallengeSlot();
		this.buttonList.add(new ButtonWidget(width / 2 - 100, height - 22, 200, 20, "Done", b -> {
			if (map == null) Minecraft.getMinecraft().displayGuiScreen(new GuiChallengeIngameMenu());
			//#if MC>=10900
			else Minecraft.getMinecraft().displayGuiScreen(new net.minecraft.client.gui.GuiWorldSelection(new GuiMainMenu()));
			//#endif
		}));
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		slot.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(MCVer.getFontRenderer(mc), "Leaderboard for " + (map == null ? ChallengeMap.currentMap.displayName : map.displayName), width / 2, 15, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private class GuiChallengeSlot extends GuiSlot {

		public GuiChallengeSlot() {
			super(Minecraft.getMinecraft(), GuiChallengeLeaderboard.this.width, GuiChallengeLeaderboard.this.height, 32, GuiChallengeLeaderboard.this.height - 24, 10);
			this.setShowSelectionBox(false);
		}

		@Override
		protected int getSize() {
			return map == null ? ChallengeMap.currentMap.leaderboard.length : map.leaderboard.length;
		}

		@Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
			
		}

		@Override
		protected boolean isSelected(int slotIndex) {
			return false;
		}

		@Override
		protected void drawBackground() {
			GuiChallengeLeaderboard.this.drawDefaultBackground();
		}

		@Override
		//#if MC>=11200
		protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
		//#else
//$$ 		protected void drawSlot(int slotIndex, int xPos, int yPos, int insideSlotHeight, int mouseXIn, int mouseYIn) {
		//#endif
			String runner = map == null ? ChallengeMap.currentMap.leaderboard[slotIndex].split(";")[0] : map.leaderboard[slotIndex].split(";")[0];
            GuiChallengeLeaderboard.this.drawString(MCVer.getFontRenderer(Minecraft.getMinecraft()), runner, xPos + 2, yPos + 1, slotIndex % 2 == 0 ? 16777215 : 9474192);
            String time = Timer.getDuration(Duration.ofMillis(Integer.parseInt(map == null ? ChallengeMap.currentMap.leaderboard[slotIndex].split(";")[1] : map.leaderboard[slotIndex].split(";")[1])));
            GuiChallengeLeaderboard.this.drawString(MCVer.getFontRenderer(Minecraft.getMinecraft()), time, xPos + 2 + 213 - MCVer.getFontRenderer(Minecraft.getMinecraft()).getStringWidth(time), yPos + 1, slotIndex % 2 == 0 ? 16777215 : 9474192);
		}
		
	}
	
}