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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class HudSettings extends Screen {
	
	public HudSettings() {
		super(new LiteralText(""));
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
			//#if MC>=11700
//$$ 			addDrawable(new SmallCheckboxWidget(1, y -= 13, s.name().toLowerCase(), Boolean.parseBoolean(p.getProperty(s.name() + "_visible")), b -> {
//$$ 				p.setProperty(Settings.values()[value].name() + "_visible", b.isChecked() + "");
//$$ 				try {
//$$ 					save();
//$$ 				} catch (IOException e) {
//$$ 					e.printStackTrace();
//$$ 				}
//$$ 			}));
			//#else
			addButton(new SmallCheckboxWidget(1, y -= 13, s.name().toLowerCase(), Boolean.parseBoolean(p.getProperty(s.name() + "_visible")), b -> {
				p.setProperty(Settings.values()[value].name() + "_visible", b.isChecked() + "");
				try {
					save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}));
			//#endif
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
	
	//#if MC>=11601
//$$ 	@Override public void render(net.minecraft.client.util.math.MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
//$$ 		if (dragging == null) super.render(matrices, mouseX, mouseY, partialTicks);
	//#else
	@Override public void render(int mouseX, int mouseY, float partialTicks) {
		if (dragging == null) super.render(mouseX, mouseY, partialTicks);
	//#endif
	}
	
	public static void load() throws FileNotFoundException, IOException {
		p = new Properties();
		p.load(new FileInputStream(new File(MinecraftClient.getInstance().runDirectory, "ingameGui.data")));
	}
	
	//#if MC>=11601
//$$ 	public static void drawOverlay(MatrixStack matrices) {
	//#else
	public static void drawOverlay() {
	//#endif
		boolean showXYZ = Boolean.parseBoolean(p.getProperty("XYZ_visible"));
		if (showXYZ) {
			int x = Integer.parseInt(p.getProperty("XYZ_x"));
			int y = Integer.parseInt(p.getProperty("XYZ_y"));
			//#if MC>=11601
//$$ 			widths.replace(Settings.XYZ, drawRectWithText(matrices, String.format("%.2f %.2f %.2f", MinecraftClient.getInstance().player.getPos().getX(), MinecraftClient.getInstance().player.getPos().getY(), MinecraftClient.getInstance().player.getPos().getZ()), x, y, Boolean.parseBoolean(p.getProperty("XYZ_hideRect"))));
			//#else
			widths.replace(Settings.XYZ, drawRectWithText(String.format("%.2f %.2f %.2f", MinecraftClient.getInstance().player.getPos().getX(), MinecraftClient.getInstance().player.getPos().getY(), MinecraftClient.getInstance().player.getPos().getZ()), x, y, Boolean.parseBoolean(p.getProperty("XYZ_hideRect"))));
			//#endif
		}
		
		boolean showXYZPRECISE = Boolean.parseBoolean(p.getProperty("XYZPRECISE_visible"));
		if (showXYZPRECISE) {
			int x = Integer.parseInt(p.getProperty("XYZPRECISE_x"));
			int y = Integer.parseInt(p.getProperty("XYZPRECISE_y"));
			//#if MC>=11601
//$$ 			widths.replace(Settings.XYZPRECISE, drawRectWithText(matrices, String.format("%f %f %f", MinecraftClient.getInstance().player.getPos().getX(), MinecraftClient.getInstance().player.getPos().getY(), MinecraftClient.getInstance().player.getPos().getZ()), x, y, Boolean.parseBoolean(p.getProperty("XYZPRECISE_hideRect"))));
			//#else
			widths.replace(Settings.XYZPRECISE, drawRectWithText(String.format("%f %f %f", MinecraftClient.getInstance().player.getPos().getX(), MinecraftClient.getInstance().player.getPos().getY(), MinecraftClient.getInstance().player.getPos().getZ()), x, y, Boolean.parseBoolean(p.getProperty("XYZPRECISE_hideRect"))));
			//#endif
		}
		
		boolean showCXZ = Boolean.parseBoolean(p.getProperty("CXZ_visible"));
		if (showCXZ) {
			int x = Integer.parseInt(p.getProperty("CXZ_x"));
			int y = Integer.parseInt(p.getProperty("CXZ_y"));
			//#if MC>=11601
			//#if MC>=11700
//$$ 			widths.replace(Settings.CXZ, drawRectWithText(matrices, String.format("%d %d", MinecraftClient.getInstance().player.getChunkPos().x, MinecraftClient.getInstance().player.getChunkPos().z), x, y, Boolean.parseBoolean(p.getProperty("CXZ_hideRect"))));
			//#else
//$$ 			widths.replace(Settings.CXZ, drawRectWithText(matrices, String.format("%d %d", MinecraftClient.getInstance().player.chunkX, MinecraftClient.getInstance().player.chunkZ), x, y, Boolean.parseBoolean(p.getProperty("CXZ_hideRect"))));
			//#endif
			//#else
			widths.replace(Settings.CXZ, drawRectWithText(String.format("%d %d", MinecraftClient.getInstance().player.chunkX, MinecraftClient.getInstance().player.chunkZ), x, y, Boolean.parseBoolean(p.getProperty("CXZ_hideRect"))));
			//#endif
		}
		
		boolean showWORLDSEED = Boolean.parseBoolean(p.getProperty("WORLDSEED_visible"));
		if (showWORLDSEED) {
			int x = Integer.parseInt(p.getProperty("WORLDSEED_x"));
			int y = Integer.parseInt(p.getProperty("WORLDSEED_y"));
			//#if MC>=11601
//$$ 			widths.replace(Settings.WORLDSEED, drawRectWithText(matrices, "World Seed: " + MinecraftClient.getInstance().getServer().getWorld(World.OVERWORLD).getSeed(), x, y, Boolean.parseBoolean(p.getProperty("WORLDSEED_hideRect"))));
			//#else
			widths.replace(Settings.WORLDSEED, drawRectWithText("World Seed: " + MinecraftClient.getInstance().getServer().getWorld(DimensionType.OVERWORLD).getSeed(), x, y, Boolean.parseBoolean(p.getProperty("WORLDSEED_hideRect"))));
			//#endif
		}
		
		boolean showTICKS = Boolean.parseBoolean(p.getProperty("TICKS_visible"));
		if (showTICKS) {
			int x = Integer.parseInt(p.getProperty("TICKS_x"));
			int y = Integer.parseInt(p.getProperty("TICKS_y"));
			//#if MC>=11601
//$$ 			int i = widths.replace(Settings.TICKS, drawRectWithText(matrices, "Server Ticks: " + TickrateChangerMod.ticksPassedServer, x, y + 14, Boolean.parseBoolean(p.getProperty("TICKS_hideRect"))));
//$$ 			drawRectWithTextFixedWidth(matrices, "Client Ticks: " + TickrateChangerMod.ticksPassed, x, y, Boolean.parseBoolean(p.getProperty("TICKS_hideRect")), i);
			//#else
			int i = widths.replace(Settings.TICKS, drawRectWithText("Server Ticks: " + TickrateChangerMod.ticksPassedServer, x, y + 14, Boolean.parseBoolean(p.getProperty("TICKS_hideRect"))));
			drawRectWithTextFixedWidth("Client Ticks: " + TickrateChangerMod.ticksPassed, x, y, Boolean.parseBoolean(p.getProperty("TICKS_hideRect")), i);
			//#endif
		}
		
		boolean showTICKRATE= Boolean.parseBoolean(p.getProperty("TICKRATE_visible"));
		if (showTICKRATE) {
			int x = Integer.parseInt(p.getProperty("TICKRATE_x"));
			int y = Integer.parseInt(p.getProperty("TICKRATE_y"));
			//#if MC>=11601
//$$ 			widths.replace(Settings.TICKRATE, drawRectWithText(matrices, "Tickrate: " + (int) (TickrateChangerMod.tickrate), x, y, Boolean.parseBoolean(p.getProperty("TICKRATE_hideRect"))));
			//#else
			widths.replace(Settings.TICKRATE, drawRectWithText("Tickrate: " + (int) (TickrateChangerMod.tickrate), x, y, Boolean.parseBoolean(p.getProperty("TICKRATE_hideRect"))));
			//#endif
		}
		
		boolean showSAVESTATECOUNT = Boolean.parseBoolean(p.getProperty("SAVESTATECOUNT_visible"));
		if (showSAVESTATECOUNT) {
			int x = Integer.parseInt(p.getProperty("SAVESTATECOUNT_x"));
			int y = Integer.parseInt(p.getProperty("SAVESTATECOUNT_y"));
			//#if MC>=11601
//$$ 			int i = widths.replace(Settings.SAVESTATECOUNT, drawRectWithText(matrices, "Savestates: " + SavestateMod.TrackerFile.savestateCount, x, y, Boolean.parseBoolean(p.getProperty("SAVESTATECOUNT_hideRect"))));
//$$ 			drawRectWithTextFixedWidth(matrices, "Loadstates: " + SavestateMod.TrackerFile.loadstateCount, x, y + 14, Boolean.parseBoolean(p.getProperty("SAVESTATECOUNT_hideRect")), i);
			//#else
			int i = widths.replace(Settings.SAVESTATECOUNT, drawRectWithText("Savestates: " + SavestateMod.TrackerFile.savestateCount, x, y, Boolean.parseBoolean(p.getProperty("SAVESTATECOUNT_hideRect"))));
			drawRectWithTextFixedWidth( "Loadstates: " + SavestateMod.TrackerFile.loadstateCount, x, y + 14, Boolean.parseBoolean(p.getProperty("SAVESTATECOUNT_hideRect")), i);
			//#endif
		}
	}
	
		//#if MC>=11601
//$$ 	private static int drawRectWithTextFixedWidth(MatrixStack matrices, String text, int x, int y, boolean rect, int w) {
//$$ 		if (!rect) fill(matrices, x, y, x + w, y + 14, -2147483648);
//$$ 		MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, text, x + 2, y + 3, 0xFFFFFF);
//$$ 		return MinecraftClient.getInstance().textRenderer.getWidth(text) + 4;
		//#else
	private static int drawRectWithTextFixedWidth(String text, int x, int y, boolean rect, int w) {
		if (!rect) fill(x, y, x + w, y + 14, -2147483648);
		MinecraftClient.getInstance().textRenderer.drawWithShadow(text, x + 2, y + 3, 0xFFFFFF);
		return MinecraftClient.getInstance().textRenderer.getStringWidth(text) + 4;
		//#endif
	}
	
	//#if MC>=11601
//$$ 	private static int drawRectWithText(MatrixStack matrices, String text, int x, int y, boolean rect) {
//$$ 		if (!rect) fill(matrices, x, y, x + MinecraftClient.getInstance().textRenderer.getWidth(text) + 4, y + 14, -2147483648);
//$$ 		MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, text, x + 2, y + 3, 0xFFFFFF);
//$$ 		return MinecraftClient.getInstance().textRenderer.getWidth(text) + 4;
		//#else
	private static int drawRectWithText(String text, int x, int y, boolean rect) {
		if (!rect) fill(x, y, x + MinecraftClient.getInstance().textRenderer.getStringWidth(text) + 4, y + 14, -2147483648);
		MinecraftClient.getInstance().textRenderer.drawWithShadow(text, x + 2, y + 3, 0xFFFFFF);
		return MinecraftClient.getInstance().textRenderer.getStringWidth(text) + 4;
		//#endif
		
	}
	
	public static void save() throws IOException {
		p.store(new FileOutputStream(new File(MinecraftClient.getInstance().runDirectory, "ingameGui.data"), false), "This file contains your Ingame Gui Layout");
	}
	
}
