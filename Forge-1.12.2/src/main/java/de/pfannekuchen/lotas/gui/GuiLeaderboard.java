package de.pfannekuchen.lotas.gui;

import java.time.Duration;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;

public class GuiLeaderboard extends GuiScreen {
	
	private GuiLeaderboardSlot slot;
	
	@Override
	public void initGui() {
		slot = new GuiLeaderboardSlot();
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawCenteredString(fontRenderer, "Leaderboard", width / 2, 30, 0xFFFFFF);
		slot.drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public class GuiLeaderboardSlot extends GuiSlot {

		public GuiLeaderboardSlot() {
			super(Minecraft.getMinecraft(), GuiLeaderboard.this.width, GuiLeaderboard.this.height, 32, GuiLeaderboard.this.height - 64, 10);
			this.setShowSelectionBox(false);
		}

		@Override
		protected int getSize() {
			return ChallengeLoader.map.leaderboard.length;
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
			GuiLeaderboard.this.drawDefaultBackground();
		}

		@Override
		protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
            String runner = ChallengeLoader.map.leaderboard[slotIndex].split(";")[0];
            GuiLeaderboard.this.drawString(GuiLeaderboard.this.fontRenderer, runner, xPos + 2, yPos + 1, slotIndex % 2 == 0 ? 16777215 : 9474192);
            String time = getTime(Duration.ofMillis(Integer.parseInt(ChallengeLoader.map.leaderboard[slotIndex].split(";")[1])));
            GuiLeaderboard.this.drawString(GuiLeaderboard.this.fontRenderer, time, xPos + 2 + 213 - GuiLeaderboard.this.fontRenderer.getStringWidth(time), yPos + 1, slotIndex % 2 == 0 ? 16777215 : 9474192);
		}
		
		private String getTime(Duration d) {
			return d.toHours() + ":" + d.toMinutes() % 60 + ":" + d.getSeconds() % 60 + "." + (int) ((d.toMillis() % 1000) / 100);
		}
		
	}
	
}
