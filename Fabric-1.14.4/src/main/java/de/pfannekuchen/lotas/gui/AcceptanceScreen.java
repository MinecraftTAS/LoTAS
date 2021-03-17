package de.pfannekuchen.lotas.gui;

import com.mojang.blaze3d.platform.GlStateManager;

import de.pfannekuchen.lotas.ConfigManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class AcceptanceScreen extends Screen {

	public AcceptanceScreen() {
		super(new LiteralText("Accept it."));
	}

	private static final Identifier DEMO_BG = new Identifier("textures/gui/demo_background.png");

	@Override
	public void init() {
		addButton(new ButtonWidget(this.width / 2 - 116, this.height / 2 + 62 + -16, 114, 20, "Accept", c1 -> {
			ConfigManager.setBoolean("hidden", "acceptedDataSending", true);
			ConfigManager.save();
			this.minecraft.openScreen(new TitleScreen());
		}));
		addButton(new ButtonWidget(this.width / 2 + 2, this.height / 2 + 62 + -16, 114, 20, "Decline", c2 -> {
			System.exit(29);
		}));
	}

	public void renderBackground() {
		super.renderBackground();
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(DEMO_BG);
		int i = (this.width - 248) / 2;
		int j = (this.height - 166) / 2;
		this.blit(i, j, 0, 0, 248, 166);
	}

	public void render(int mouseX, int mouseY, float delta) {
		this.renderBackground();
		int i = (this.width - 248) / 2 + 10;
		int j = (this.height - 166) / 2 + 8;
		this.font.draw("LoTAS Cheat prevention", (float) i, (float) j, 2039583);
		j += 12;
		GameOptions gameOptions = this.minecraft.options;
		this.font.draw(I18n.translate("This mod collects a bit of data", gameOptions.keyForward.getLocalizedName(),
				gameOptions.keyLeft.getLocalizedName(), gameOptions.keyBack.getLocalizedName(),
				gameOptions.keyRight.getLocalizedName()), (float) i, (float) j, 5197647);
		this.font.draw(I18n.translate("to prevent cheating."), (float) i, (float) (j + 12), 5197647);
		this.font.draw(
				I18n.translate("Your data will be hashed and encrypted.", gameOptions.keyJump.getLocalizedName()),
				(float) i, (float) (j + 24), 5197647);
		this.font.draw(I18n.translate("\u00A7cYour Data is unreadable to anyone!",
				gameOptions.keyInventory.getLocalizedName()), (float) i, (float) (j + 36), 5197647);
		this.font.drawTrimmed(I18n.translate("If you are confused or worried, pm me on discord: MCPfannkuchenYT#9745."),
				i, j + 68, 218, 2039583);
		super.render(mouseX, mouseY, delta);
	}
}
