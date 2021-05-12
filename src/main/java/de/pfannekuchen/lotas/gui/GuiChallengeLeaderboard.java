package de.pfannekuchen.lotas.gui;

import java.time.Duration;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.EventUtils.Timer;
import de.pfannekuchen.lotas.taschallenges.ChallengeMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;

public class GuiChallengeLeaderboard extends GuiScreen {
	
	private GuiChallengeSlot slot;
	
	@Override
	public void initGui() {
		slot = new GuiChallengeSlot();
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawCenteredString(MCVer.getFontRenderer(mc), "Leaderboard", width / 2, 30, 0xFFFFFF);
		slot.drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private class GuiChallengeSlot extends GuiSlot {

		public GuiChallengeSlot() {
			super(Minecraft.getMinecraft(), GuiChallengeLeaderboard.this.width, GuiChallengeLeaderboard.this.height, 32, GuiChallengeLeaderboard.this.height - 64, 10);
			this.setShowSelectionBox(false);
		}

		@Override
		protected int getSize() {
			return ChallengeMap.currentMap.leaderboard.length;
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
			String runner = ChallengeMap.currentMap.leaderboard[slotIndex].split(";")[0];
            GuiChallengeLeaderboard.this.drawString(MCVer.getFontRenderer(Minecraft.getMinecraft()), runner, xPos + 2, yPos + 1, slotIndex % 2 == 0 ? 16777215 : 9474192);
            String time = Timer.getDuration(Duration.ofMillis(Integer.parseInt(ChallengeMap.currentMap.leaderboard[slotIndex].split(";")[1])));
            GuiChallengeLeaderboard.this.drawString(MCVer.getFontRenderer(Minecraft.getMinecraft()), time, xPos + 2 + 213 - MCVer.getFontRenderer(Minecraft.getMinecraft()).getStringWidth(time), yPos + 1, slotIndex % 2 == 0 ? 16777215 : 9474192);
		}
		
	}
	
}