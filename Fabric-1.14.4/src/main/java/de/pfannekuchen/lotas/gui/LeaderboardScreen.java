package de.pfannekuchen.lotas.gui;

import java.time.Duration;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.LiteralText;

public class LeaderboardScreen extends Screen {
	
	protected LeaderboardScreen() {
		super(new LiteralText("Leaderboard"));
	}

	private AlwaysSelectedEntryListWidget<?> slot;
	
	@Override
	public void init() {
		slot = new LeaderboardSlot();
		super.init();
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawCenteredString(minecraft.textRenderer, "Leaderboard", width / 2, 30, 0xFFFFFF);
		slot.render(mouseX, mouseY, partialTicks);
		super.render(mouseX, mouseY, partialTicks);
	}
	
	public class LeaderboardSlot extends AlwaysSelectedEntryListWidget<LeaderboardScreen.LeaderboardSlot.LeaderboardSlotWidget> {

		public LeaderboardSlot() {
			super(MinecraftClient.getInstance(), LeaderboardScreen.this.width, LeaderboardScreen.this.height, 32, LeaderboardScreen.this.height - 64, 10);
		}

		@Override
		protected LeaderboardSlotWidget getEntry(int index) {
			return new LeaderboardSlotWidget();
		}
		
		@Override
		protected int getItemCount() {
			return ChallengeLoader.map.leaderboard.length;
		}

		@Override
		protected boolean isSelectedItem(int index) {
			return false;
		}

		@Override
		protected void renderBackground() {
			LeaderboardScreen.this.renderDirtBackground(0);
		}
		
		public class LeaderboardSlotWidget extends AlwaysSelectedEntryListWidget.Entry<LeaderboardScreen.LeaderboardSlot.LeaderboardSlotWidget> {

			@Override
			public void render(int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean hovering, float delta) {
	            String runner = ChallengeLoader.map.leaderboard[index].split(";")[0];
	            MinecraftClient.getInstance().textRenderer.drawWithShadow(runner, x + 2, y + 1, index % 2 == 0 ? 16777215 : 9474192);
	            String time = Timer.getDuration(Duration.ofMillis(Integer.parseInt(ChallengeLoader.map.leaderboard[index].split(";")[1])));
	            MinecraftClient.getInstance().textRenderer.drawWithShadow(time, x + 2 + 213 - MinecraftClient.getInstance().textRenderer.getStringWidth(time), y + 1, index % 2 == 0 ? 16777215 : 9474192);
			}
			
		}
		
	}
	
}
