package de.pfannekuchen.lotas.gui;

import java.time.Duration;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import de.pfannekuchen.lotas.tickratechanger.Timer;
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
		drawCenteredString(fontRendererObj, "Leaderboard", width / 2, 30, 0xFFFFFF);
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
		protected void drawSlot(int entryID, int insideLeft, int yPos, int insideSlotHeight, int mouseXIn, int mouseYIn) {
		    String runner = ChallengeLoader.map.leaderboard[entryID].split(";")[0];
            GuiLeaderboard.this.drawString(GuiLeaderboard.this.fontRendererObj, runner, insideLeft + 2, yPos + 1, entryID % 2 == 0 ? 16777215 : 9474192);
            String time = Timer.getDuration(Duration.ofMillis(Integer.parseInt(ChallengeLoader.map.leaderboard[entryID].split(";")[1])));
            GuiLeaderboard.this.drawString(GuiLeaderboard.this.fontRendererObj, time, insideLeft + 2 + 213 - GuiLeaderboard.this.fontRendererObj.getStringWidth(time), yPos + 1, entryID % 2 == 0 ? 16777215 : 9474192);
		
		}
		
	}
	
}
