package de.pfannekuchen.lotas.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.widgets.SmallCheckboxWidget;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

public class HudSettings extends Screen {
	
	public HudSettings() {
		super(new TextComponent(""));
	}

	public String dragging;
	public static HashMap<Settings, Integer> widths = new HashMap<>();
	
	static {
		widths.put(Settings.XYZ, 0);
		widths.put(Settings.XYZPRECISE, 0);
		widths.put(Settings.CXZ, 0);
		widths.put(Settings.WORLDSEED, 0);
		widths.put(Settings.FACING, 0);
		widths.put(Settings.TICKS, 0);
		widths.put(Settings.TICKRATE, 0);
		widths.put(Settings.SAVESTATECOUNT, 0);
	}
	
	public static enum Settings {
		XYZ, XYZPRECISE, CXZ, WORLDSEED, FACING, TICKS, TICKRATE, SAVESTATECOUNT, TRAJECTORIES;
	}
	
	public static Properties p;

	public String identify(int mouseX, int mouseY) {
		final AtomicReference<String> returnable = new AtomicReference<String>(null);
		widths.forEach((a, b) -> {
			int x = Integer.parseInt(p.getProperty(a.name() + "_x"));
			int y = Integer.parseInt(p.getProperty(a.name() + "_y"));
			int w = x + widths.get(a);
			int h = y + 25;
			
			if (mouseX >= x && mouseX <= w && mouseY >= y && mouseY <= h) {
				returnable.set(a.name());
				return;
			}
		});
		return returnable.get();
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (mouseButton == 1) {
			String id = identify((int) mouseX, (int) mouseY);
			if (id != null) {
				p.setProperty(id + "_hideRect", p.getProperty(id + "_hideRect").equalsIgnoreCase("true") ? "false" : "true");
				try {
					save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		dragging = identify((int) mouseX, (int) mouseY);
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (dragging != null) {
			p.setProperty(dragging + "_x", (((int) mouseX) - 10) + "");
			p.setProperty(dragging + "_y", (((int) mouseY) - 10)+ "");
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}
	
	@Override
	public void init() {
		int y = height;
		int index = 0;
		for (Settings s : Settings.values()) {
			final int value = index;
			addButton(new SmallCheckboxWidget(1, y -= 13, s.name().toLowerCase(), Boolean.parseBoolean(p.getProperty(s.name() + "_visible")), b -> {
				p.setProperty(Settings.values()[value].name() + "_visible", b.isChecked() + "");
				try {
					save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}));
			index++;
		}
		super.init();
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (dragging != null) {
			p.setProperty(dragging + "_x", (((int) mouseX) - 10) + "");
			p.setProperty(dragging + "_y", (((int) mouseY) - 10) 	 + "");
			try {
				save();
			} catch (IOException e) {
				e.printStackTrace();
			}
			dragging = null;
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	//#if MC>=11600
//$$ 	@Override public void render(com.mojang.blaze3d.vertex.PoseStack stack, int mouseX, int mouseY, float partialTicks) {
//$$ 		MCVer.stack = stack;
	//#else
	@Override public void render(int mouseX, int mouseY, float partialTicks) {
	//#endif
		if (dragging == null) {
			for(int k = 0; k < this.buttons.size(); ++k) {
				MCVer.render(((AbstractWidget)this.buttons.get(k)), mouseX, mouseY, partialTicks);
			}
		}
	}
	
	public static void load() throws FileNotFoundException, IOException {
		p = new Properties();
		p.load(new FileInputStream(new File(Minecraft.getInstance().gameDirectory, "ingameGui.data")));
	}
	
	public static void drawOverlay() {
		boolean showXYZ = Boolean.parseBoolean(p.getProperty("XYZ_visible"));
		if (showXYZ) {
			int x = Integer.parseInt(p.getProperty("XYZ_x"));
			int y = Integer.parseInt(p.getProperty("XYZ_y"));
			widths.replace(Settings.XYZ, drawRectWithText(String.format("%.2f %.2f %.2f", Minecraft.getInstance().player.position().x(), Minecraft.getInstance().player.position().y(), Minecraft.getInstance().player.position().z()), x, y, Boolean.parseBoolean(p.getProperty("XYZ_hideRect"))));
		}
		
		boolean showXYZPRECISE = Boolean.parseBoolean(p.getProperty("XYZPRECISE_visible"));
		if (showXYZPRECISE) {
			int x = Integer.parseInt(p.getProperty("XYZPRECISE_x"));
			int y = Integer.parseInt(p.getProperty("XYZPRECISE_y"));
			widths.replace(Settings.XYZPRECISE, drawRectWithText(String.format("%f %f %f", Minecraft.getInstance().player.position().x(), Minecraft.getInstance().player.position().y(), Minecraft.getInstance().player.position().z()), x, y, Boolean.parseBoolean(p.getProperty("XYZPRECISE_hideRect"))));
		}
		
		boolean showCXZ = Boolean.parseBoolean(p.getProperty("CXZ_visible"));
		if (showCXZ) {
			int x = Integer.parseInt(p.getProperty("CXZ_x"));
			int y = Integer.parseInt(p.getProperty("CXZ_y"));
			widths.replace(Settings.CXZ, drawRectWithText(String.format("%d %d", Minecraft.getInstance().player.xChunk, Minecraft.getInstance().player.zChunk), x, y, Boolean.parseBoolean(p.getProperty("CXZ_hideRect"))));
		}
		
		boolean showWORLDSEED = Boolean.parseBoolean(p.getProperty("WORLDSEED_visible"));
		if (showWORLDSEED) {
			int x = Integer.parseInt(p.getProperty("WORLDSEED_x"));
			int y = Integer.parseInt(p.getProperty("WORLDSEED_y"));
			widths.replace(Settings.WORLDSEED, drawRectWithText("World Seed: " + MCVer.getCurrentLevel().getSeed(), x, y, Boolean.parseBoolean(p.getProperty("WORLDSEED_hideRect"))));
		}
		
		boolean showTICKS = Boolean.parseBoolean(p.getProperty("TICKS_visible"));
		if (showTICKS) {
			int x = Integer.parseInt(p.getProperty("TICKS_x"));
			int y = Integer.parseInt(p.getProperty("TICKS_y"));
			int i = widths.replace(Settings.TICKS, drawRectWithText("Server Ticks: " + TickrateChangerMod.ticksPassedServer, x, y + 14, Boolean.parseBoolean(p.getProperty("TICKS_hideRect"))));
			drawRectWithTextFixedWidth("Client Ticks: " + TickrateChangerMod.ticksPassed, x, y, Boolean.parseBoolean(p.getProperty("TICKS_hideRect")), i);
		}
		
		boolean showTICKRATE= Boolean.parseBoolean(p.getProperty("TICKRATE_visible"));
		if (showTICKRATE) {
			int x = Integer.parseInt(p.getProperty("TICKRATE_x"));
			int y = Integer.parseInt(p.getProperty("TICKRATE_y"));
			widths.replace(Settings.TICKRATE, drawRectWithText("Tickrate: " + (int) (TickrateChangerMod.tickrate), x, y, Boolean.parseBoolean(p.getProperty("TICKRATE_hideRect"))));
		}
		
		boolean showSAVESTATECOUNT = Boolean.parseBoolean(p.getProperty("SAVESTATECOUNT_visible"));
		if (showSAVESTATECOUNT) {
			int x = Integer.parseInt(p.getProperty("SAVESTATECOUNT_x"));
			int y = Integer.parseInt(p.getProperty("SAVESTATECOUNT_y"));
			int i = widths.replace(Settings.SAVESTATECOUNT, drawRectWithText("Savestates: " + SavestateMod.TrackerFile.savestateCount, x, y, Boolean.parseBoolean(p.getProperty("SAVESTATECOUNT_hideRect"))));
			drawRectWithTextFixedWidth( "Loadstates: " + SavestateMod.TrackerFile.loadstateCount, x, y + 14, Boolean.parseBoolean(p.getProperty("SAVESTATECOUNT_hideRect")), i);
		}
	}
	
	private static int drawRectWithTextFixedWidth(String text, int x, int y, boolean rect, int w) {
		if (!rect) MCVer.fill(x, y, x + w, y + 14, -2147483648);
		MCVer.drawShadow(text, x + 2, y + 3, 0xFFFFFF);
		return Minecraft.getInstance().font.width(text) + 4;
	}
	
	private static int drawRectWithText(String text, int x, int y, boolean rect) {
		if (!rect) MCVer.fill(x, y, x + Minecraft.getInstance().font.width(text) + 4, y + 14, -2147483648);
		MCVer.drawShadow(text, x + 2, y + 3, 0xFFFFFF);
		return Minecraft.getInstance().font.width(text) + 4;
	}
	
	public static void save() throws IOException {
		p.store(new FileOutputStream(new File(Minecraft.getInstance().gameDirectory, "ingameGui.data"), false), "This file contains your Ingame Gui Layout");
	}
	
}
