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
import de.pfannekuchen.lotas.core.utils.TrajectoriesCalculator;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
//#if MC>10900
import net.minecraft.util.math.Vec3d;
//#endif
import net.minecraftforge.fml.client.config.GuiCheckBox;

/**
 * The Info Gui adds a lot of informatino to the screen
 * @author Pancake
 */
public class HudSettings extends GuiScreen {
	
	/** What object the mouse is dragging currently or null */
	public String dragging;
	/** The widths of the objects drawn to the screen */
	public static HashMap<Settings, Integer> widths = new HashMap<>();
	
	/** List of all Settings */
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
	
	/** All available Settings */
	public static enum Settings {
		XYZ, XYZPRECISE, CXZ, WORLDSEED, FACING, TICKS, TICKRATE, SAVESTATECOUNT, TRAJECTORIES;
	}
	
	/** Configuartion file with all settings in them */
	public static Properties p;

	/** 
	 * Identifies an object below the cursor
	 * @param mouseX Cursor position
	 * @param mouseY Cursor p4osition
	 * @return Returns the object below the cursor or null
	 */
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
	
	/**
	 * Drags the object selected or makes it's rect disappear
	 */
	@Override protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 1) {
			String id = identify(mouseX, mouseY);
			if (id != null) {
				p.setProperty(id + "_hideRect", p.getProperty(id + "_hideRect").equalsIgnoreCase("true") ? "false" : "true");
				try {
					save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return;
		}
		dragging = identify(mouseX, mouseY);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	/**
	 * Moves a selected object to the mouse cursor when dragged
	 */
	@Override protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (dragging != null) {
			p.setProperty(dragging + "_x", (mouseX - 10) + "");
			p.setProperty(dragging + "_y", (mouseY - 10)+ "");
		}
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
	
	@Override
	public void initGui() {
		int y = height;
		int index = 0;
		for (Settings s : Settings.values()) {
			buttonList.add(new GuiCheckBox(index, 1, y -= 13, s.name().toLowerCase(), Boolean.parseBoolean(p.getProperty(s.name() + "_visible"))));
			index++;
		}
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		p.setProperty(Settings.values()[button.id].name() + "_visible", ((GuiCheckBox) button).isChecked() + "");
		try {
			save();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Saves the new position of an object, if dragged to the configuration.
	 */
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (dragging != null) {
			p.setProperty(dragging + "_x", (mouseX - 10) + "");
			p.setProperty(dragging + "_y", (mouseY - 10) 	 + "");
			try {
				save();
			} catch (IOException e) {
				e.printStackTrace();
			}
			dragging = null;
		}
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (dragging == null) super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	/**
	 * Loads the Configuration from the File
	 * @throws FileNotFoundException No configuration exists
	 * @throws IOException Read error
	 */
	public static void load() throws FileNotFoundException, IOException {
		p = new Properties();
		p.load(new FileInputStream(new File(Minecraft.getMinecraft().mcDataDir, "ingameGui.data")));
	}
	
	/**
	 * Draws the Hud onto the screen
	 */
	public static void drawOverlay() {
		if (Minecraft.getMinecraft().currentScreen instanceof HudSettings) Minecraft.getMinecraft().currentScreen.drawDefaultBackground();
		boolean showXYZ = Boolean.parseBoolean(p.getProperty("XYZ_visible"));
		if (showXYZ) {
			int x = Integer.parseInt(p.getProperty("XYZ_x"));
			int y = Integer.parseInt(p.getProperty("XYZ_y"));
			widths.replace(Settings.XYZ, drawRectWithText(String.format("%.2f %.2f %.2f", MCVer.player(Minecraft.getMinecraft()).posX, MCVer.player(Minecraft.getMinecraft()).posY, MCVer.player(Minecraft.getMinecraft()).posZ), x, y, Boolean.parseBoolean(p.getProperty("XYZ_hideRect"))));
		}
		
		boolean showXYZPRECISE = Boolean.parseBoolean(p.getProperty("XYZPRECISE_visible"));
		if (showXYZPRECISE) {
			int x = Integer.parseInt(p.getProperty("XYZPRECISE_x"));
			int y = Integer.parseInt(p.getProperty("XYZPRECISE_y"));
			widths.replace(Settings.XYZPRECISE, drawRectWithText(String.format("%f %f %f", MCVer.player(Minecraft.getMinecraft()).posX, MCVer.player(Minecraft.getMinecraft()).posY, MCVer.player(Minecraft.getMinecraft()).posZ), x, y, Boolean.parseBoolean(p.getProperty("XYZPRECISE_hideRect"))));
		}
		
		boolean showCXZ = Boolean.parseBoolean(p.getProperty("CXZ_visible"));
		if (showCXZ) {
			int x = Integer.parseInt(p.getProperty("CXZ_x"));
			int y = Integer.parseInt(p.getProperty("CXZ_y"));
			widths.replace(Settings.CXZ, drawRectWithText(String.format("%d %d", MCVer.player(Minecraft.getMinecraft()).chunkCoordX, MCVer.player(Minecraft.getMinecraft()).chunkCoordZ), x, y, Boolean.parseBoolean(p.getProperty("CXZ_hideRect"))));
		}
		
		boolean showWORLDSEED = Boolean.parseBoolean(p.getProperty("WORLDSEED_visible"));
		if (showWORLDSEED) {
			int x = Integer.parseInt(p.getProperty("WORLDSEED_x"));
			int y = Integer.parseInt(p.getProperty("WORLDSEED_y"));
			widths.replace(Settings.WORLDSEED, drawRectWithText("World Seed: " + MCVer.world(Minecraft.getMinecraft().getIntegratedServer(), 0).getWorldInfo().getSeed(), x, y, Boolean.parseBoolean(p.getProperty("WORLDSEED_hideRect"))));
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
			drawRectWithTextFixedWidth("Loadstates: " + SavestateMod.TrackerFile.loadstateCount, x, y + 14, Boolean.parseBoolean(p.getProperty("SAVESTATECOUNT_hideRect")), i);
		}
		
		boolean showTRAJECTORIES = Boolean.parseBoolean(p.getProperty("TRAJECTORIES_visible"));
		if (showTRAJECTORIES) {
			int x = Integer.parseInt(p.getProperty("TRAJECTORIES_x"));
			int y = Integer.parseInt(p.getProperty("TRAJECTORIES_y"));
			String message = "Invalid Item";
			//#if MC>10900
			Vec3d vec = TrajectoriesCalculator.calculate();
			if (vec != null) {
				//#if MC>=11200
				message = String.format("%.3f %.3f %.3f", vec.x, vec.y, vec.z);
				//#else
				//$$ message = String.format("%.3f %.3f %.3f", vec.xCoord, vec.yCoord, vec.zCoord);
				//#endif
			}
			//#else
			message = "Unable to calculate Trajectories on 1.8.x :(";
			//#endif
			widths.replace(Settings.TRAJECTORIES, drawRectWithText("Trajectories: " + message, x, y, Boolean.parseBoolean(p.getProperty("TRAJECTORIES_hideRect"))));
		}
	}
	
	/**
	 * Util to draw a text with a rect around it
	 */
	private static int drawRectWithTextFixedWidth(String text, int x, int y, boolean rect, int w) {
		if (!rect) drawRect(x, y, x + w, y + 14, -2147483648);
		MCVer.getFontRenderer(Minecraft.getMinecraft()).drawStringWithShadow(text, x + 2, y + 3, 0xFFFFFF);
		return MCVer.getFontRenderer(Minecraft.getMinecraft()).getStringWidth(text) + 4;
	}
	
	/**
	 * Util to draw a text without a rect around it
	 */
	private static int drawRectWithText(String text, int x, int y, boolean rect) {
		if (!rect) drawRect(x, y, x + MCVer.getFontRenderer(Minecraft.getMinecraft()).getStringWidth(text) + 4, y + 14, -2147483648);
		MCVer.getFontRenderer(Minecraft.getMinecraft()).drawStringWithShadow(text, x + 2, y + 3, 0xFFFFFF);
		return MCVer.getFontRenderer(Minecraft.getMinecraft()).getStringWidth(text) + 4;
	}
	
	/**
	 * Saves a configuartion file
	 * @throws IOException Write error
	 */
	public static void save() throws IOException {
		p.store(new FileOutputStream(new File(Minecraft.getMinecraft().mcDataDir, "ingameGui.data"), false), "This file contains your Ingame Gui Layout");
	}
	
}
