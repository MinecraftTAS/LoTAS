package de.pfannekuchen.lotas.gui.widgets;

import java.io.IOException;
import java.time.Duration;

import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.challenges.ChallengeLoader;
import de.pfannekuchen.lotas.challenges.ChallengeMap;
import de.pfannekuchen.lotas.tickratechanger.Timer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.util.Identifier;

public class ChallengeMapEntry extends WorldListWidget.Entry {
	
	private static final Identifier WORLD_SELECTION_LOCATION = new Identifier("textures/gui/world_selection.png");
	
	public ChallengeMap map;
	public Identifier loc = null;
	
	public ChallengeMapEntry(WorldListWidget gui, ChallengeMap map) {
		gui.super(gui, map.getSummary(), map.getLevelStorage());
		this.map = map;
		loc = map.resourceLoc;
	}
	
	@Override
	public void delete() {
		
	}
	
	@Override
	public void edit() {
		
	}
	
	@Override
	public void recreate() {
		
	}
	
	@Override
	public void play() {
		ChallengeLoader.map = map;
		try {
			ChallengeLoader.load(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void render(int slotIndex, int y, int x, int l, int m, int mouseX, int o, boolean isSelected, float f) {
        String s = "\u00A76TAS Challenge Map - \u00A7f" + map.displayName;
        String s1 = map.description;
        String s2 = "WR: " + map.leaderboard[0].split(";")[0] + " - " + Timer.getDuration(Duration.ofMillis(Integer.parseInt(map.leaderboard[0].split(";")[1])));
        
        MinecraftClient.getInstance().textRenderer.drawWithShadow(s, x + 32 + 3, y + 1, 16777215);
        MinecraftClient.getInstance().textRenderer.drawWithShadow(s1, x + 32 + 3, y + MinecraftClient.getInstance().textRenderer.fontHeight + 3, 8421504);
        MinecraftClient.getInstance().textRenderer.drawWithShadow(s2, x + 32 + 3, y + MinecraftClient.getInstance().textRenderer.fontHeight + MinecraftClient.getInstance().textRenderer.fontHeight + 3, 8421504);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        
        // DRAW ICON
		GlStateManager.enableBlend();
		MinecraftClient.getInstance().getTextureManager().bindTexture(loc);
		DrawableHelper.blit(x, y, 0, 0, 32, 32, 32, 32);
		GlStateManager.disableBlend();

        if (isSelected) {
            MinecraftClient.getInstance().getTextureManager().bindTexture(WORLD_SELECTION_LOCATION);
            DrawableHelper.fill(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            int j = mouseX - x;
            int i = j < 32 ? 32 : 0;

            DrawableHelper.blit(x, y, 0.0F, (float)i, 32, 32, 256, 256);
        }
	}
	
}