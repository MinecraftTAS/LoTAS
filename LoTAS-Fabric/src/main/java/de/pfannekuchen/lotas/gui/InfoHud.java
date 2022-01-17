package de.pfannekuchen.lotas.gui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.core.utils.KeybindsUtils;
import de.pfannekuchen.lotas.core.utils.KeystrokeUtils;
import de.pfannekuchen.lotas.core.utils.Timer;
import de.pfannekuchen.lotas.mods.SavestateMod;
import de.pfannekuchen.lotas.mods.TickrateChangerMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Mth;

/**
 * The info hud is a hud that is always being rendered ontop of the screen, it can show some stuff such as coordinates, etc.,
 * any everything can be customized
 * @author Pancake
 */
public class InfoHud extends Screen {
	
	public static class InfoLabel {
		public String displayName;
		public int x;
		public int y;
		public boolean visible;
		public boolean renderRect;
		public String renderText;
		private Callable<String> text;
		
		public InfoLabel(String displayName, Properties configuration, Callable<String> text) {
			this.displayName = displayName;
			this.x = Integer.parseInt(configuration.getProperty(displayName + "_x"));
			this.y = Integer.parseInt(configuration.getProperty(displayName + "_y"));
			this.visible = Boolean.parseBoolean(configuration.getProperty(displayName + "_visible"));
			this.renderRect = Boolean.parseBoolean(configuration.getProperty(displayName + "_rect"));
			this.text = text;
		}
		
		public void tick() {
			try {
				renderText = text.call();
			} catch (Exception e) {
				e.printStackTrace();
				// Lots of NPEs
			}
		}
	}
	
	/** -1, or the current index in {@link InfoHud#lists} that is being dragged by the mouse */
	private int currentlyDraggedIndex = -1;
	private int xOffset; // drag offsets
	private int yOffset;
	
	private int gridSizeX = 14;
	private int gridSizeY = 14;
	
	public Properties configuration;
	public static List<InfoLabel> lists = new ArrayList<>();
	
	boolean resetLayout=false;
	
	public InfoHud() {
		super(new TextComponent(""));
	}
	
	
	private void setDefaults(String string, int y) {
		if (!configuration.getProperty(string + "_x", "err").equals("err"))
			return;

		if ("keystroke".equals(string)) {
			int newpos = MCVer.getGLWindow().getScreenHeight() - 20;
			configuration.setProperty(string + "_x", "0");
			configuration.setProperty(string + "_y", newpos + "");
			configuration.setProperty(string + "_visible", "true");
			configuration.setProperty(string + "_rect", "false");
		} else {

			configuration.setProperty(string + "_x", "0");
			configuration.setProperty(string + "_y", y + "");
			configuration.setProperty(string + "_visible", "false");
			configuration.setProperty(string + "_rect", "false");
		}
		saveConfig();
	}
	
	/**
	 * Returns the object below the mouse
	 */
	public void identify(int mouseX, int mouseY) {
		int index = 0;
		for (InfoLabel label : lists) {
			int x=0;
			int y=0;
			try {
				x = Integer.parseInt(configuration.getProperty(label.displayName + "_x"));
				y = Integer.parseInt(configuration.getProperty(label.displayName + "_y"));
			} catch (NumberFormatException e) {
				configuration.setProperty(label.displayName + "_x", "0");
				configuration.setProperty(label.displayName + "_y", "0");
				saveConfig();
			}
			int w = x + Minecraft.getInstance().font.width(label.renderText);
			int h = y + 15;
			
			if (mouseX >= x && mouseX <= w && mouseY >= y && mouseY <= h) {
				currentlyDraggedIndex = index;
				xOffset = mouseX - x;
				yOffset = mouseY - y;
				return;
			}
			index++;
		}
		currentlyDraggedIndex = -1;
		xOffset = -1;
		yOffset = -1;
	}
	
	@Override public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (mouseButton == 1) {
			identify((int) mouseX, (int) mouseY);
			if (currentlyDraggedIndex != -1) {
				String id = lists.get(currentlyDraggedIndex).displayName;
				lists.get(currentlyDraggedIndex).renderRect = !lists.get(currentlyDraggedIndex).renderRect;
				configuration.setProperty(id + "_rect", configuration.getProperty(id + "_rect").equalsIgnoreCase("true") ? "false" : "true");
				saveConfig();
				currentlyDraggedIndex = -1;
			}
			return true;
		} else if (mouseButton == 2) {
			identify((int) mouseX, (int) mouseY);
			if (currentlyDraggedIndex != -1) {
				String id = lists.get(currentlyDraggedIndex).displayName;
				lists.get(currentlyDraggedIndex).visible = !lists.get(currentlyDraggedIndex).visible;
				configuration.setProperty(id + "_visible", configuration.getProperty(id + "_visible").equalsIgnoreCase("true") ? "false" : "true");
				saveConfig();
				currentlyDraggedIndex = -1;
			}
			return true;
		}
		identify((int) mouseX, (int) mouseY);
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public boolean mouseReleased(double d, double e, int i) {
		currentlyDraggedIndex = -1;
		saveConfig();
		return super.mouseReleased(d, e, i);
	}
	
	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		if (currentlyDraggedIndex != -1) {
			String dragging = lists.get(currentlyDraggedIndex).displayName;
			
			double mousePosX = mouseX - xOffset;
			double mousePosY = mouseY - yOffset;
			
			if (Screen.hasShiftDown()) {
				mousePosX = snapToGridX(mousePosX);
				mousePosY = snapToGridY(mousePosY);
			}
			
			lists.get(currentlyDraggedIndex).x = (int) mousePosX;
			lists.get(currentlyDraggedIndex).y = (int) mousePosY;
			
			configuration.setProperty(dragging + "_x", lists.get(currentlyDraggedIndex).x + "");
			configuration.setProperty(dragging + "_y", lists.get(currentlyDraggedIndex).y + "");
		}
		super.mouseMoved(mouseX, mouseY);
	}
	
	private double snapToGridX(double x) {
		return Math.round(x / gridSizeX) * gridSizeX;
	}

	private double snapToGridY(double y) {
		return Math.round(y / gridSizeY) * gridSizeY;
	}
	
	/**
	 * Saves the Configuration
	 */
	private void saveConfig() {
		try {
			File tasmodDir = new File(Minecraft.getInstance().gameDirectory, "lotas");
			tasmodDir.mkdir();
			File configFile = new File(tasmodDir, "infogui.cfg");
			if (!configFile.exists()) configFile.createNewFile();
			configuration.store(new FileOutputStream(configFile, false), "DO NOT EDIT MANUALLY");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates every tick
	 */
	public void tick() {
		if (checkInit()) return;
		for (InfoLabel label : lists) label.tick();
	}
	
	public boolean checkInit() {
		if (configuration != null) return false;
		/* Check whether already rendered before */
		try {
			configuration = new Properties();
			if (!resetLayout) {
				File tasmodDir = new File(Minecraft.getInstance().gameDirectory, "lotas");
				tasmodDir.mkdir();
				File configFile = new File(tasmodDir, "infogui.cfg");
				if (!configFile.exists()) configFile.createNewFile();
				configuration.load(new FileReader(configFile));
			}else {
				resetLayout = false;
			}
			lists = new ArrayList<InfoLabel>();
			/* ====================== */
			int y = 0;
			
			setDefaults("tickrate", y);
			lists.add(new InfoLabel("tickrate", configuration, () -> {
				if (Minecraft.getInstance().screen == this)
					return "Tickrate";
				return "Tickrate: " + TickrateChangerMod.tickrate;
			}));
			y += 14;
			setDefaults("position", y);
			lists.add(new InfoLabel("position", configuration, () -> {
				if (Minecraft.getInstance().screen == this)
					return "XYZ";
				return String.format("%.2f %.2f %.2f", MCVer.getX(Minecraft.getInstance().player), MCVer.getY(Minecraft.getInstance().player), MCVer.getZ(Minecraft.getInstance().player));
			}));
			y += 14;
			setDefaults("preciseposition", y);
			lists.add(new InfoLabel("preciseposition", configuration, () -> {
				if (Minecraft.getInstance().screen == this)
					return "Precise XYZ";
				return String.format("%f %f %f", MCVer.getX(Minecraft.getInstance().player), MCVer.getY(Minecraft.getInstance().player), MCVer.getZ(Minecraft.getInstance().player));
			}));
			y += 14;
			setDefaults("chunkposition", y);
			lists.add(new InfoLabel("chunkposition", configuration, () -> {
				if (Minecraft.getInstance().screen == this)
					return "Chunk Position";
				//#if MC>=11700
//$$ 				return String.format("%d %d", Minecraft.getInstance().player.chunkPosition().getRegionLocalX(), Minecraft.getInstance().player.chunkPosition().getRegionLocalZ());
				//#else
				return String.format("%d %d %d", Minecraft.getInstance().player.xChunk, Minecraft.getInstance().player.yChunk, Minecraft.getInstance().player.zChunk);
				//#endif
			}));
			y += 14;
			setDefaults("worldseed", y);
			lists.add(new InfoLabel("worldseed", configuration, () -> {
				if (Minecraft.getInstance().screen == this)
					return "Worldseed";
				return Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers().get(0).getLevel().getSeed() + "";
			}));
			y += 14;
			setDefaults("ticks", y);
			lists.add(new InfoLabel("ticks", configuration, () -> {
				if (Minecraft.getInstance().screen == this)
					return "Ticks";
				return TickrateChangerMod.ticksPassedServer + "";
			}));
			y += 14;
			setDefaults("savestates", y);
			lists.add(new InfoLabel("savestates", configuration, () -> {
				if (Minecraft.getInstance().screen == this)
					return "Savestate Count";
				return ("Savestates: " + SavestateMod.TrackerFile.savestateCount);
			}));
			y += 14;
			setDefaults("loadstates", y);
			lists.add(new InfoLabel("loadstates", configuration, () -> {
				if (Minecraft.getInstance().screen == this)
					return "Loadstate Count";
				return ("Loadstates: " + SavestateMod.TrackerFile.loadstateCount);
			}));
			y += 14;
			setDefaults("timer", y);
			lists.add(new InfoLabel("timer", configuration, () -> {
				if (Minecraft.getInstance().screen == this)
					return "Timer";
				return Timer.ticks == -1 ? "Timer is paused" : Timer.getDuration(Duration.ofMillis(Timer.ticks * 50));
			}));
			y += 14;
			setDefaults("rtatimer", y);
			lists.add(new InfoLabel("rtatimer", configuration, () -> {
				if (Minecraft.getInstance().screen == this)
					return "RTATimer";
				if (Timer.running) TickrateChangerMod.rta = Duration.ofMillis(System.currentTimeMillis() - Timer.startTime.toMillis());
				return Timer.ticks == -1 ? "" : ("RTA: " + Timer.getDuration(TickrateChangerMod.rta));
			}));
			y += 14;
			setDefaults("bps", y);
			lists.add(new InfoLabel("bps", configuration, () -> {
				if (Minecraft.getInstance().screen == this)
					return "Speed/BPS";
				double distTraveledLastTickX = MCVer.getX(Minecraft.getInstance().player) - Minecraft.getInstance().player.xOld;
				double distTraveledLastTickZ = MCVer.getZ(Minecraft.getInstance().player) - Minecraft.getInstance().player.zOld;
				//#if MC>=11700
//$$ 				return String.format("%.2f", Mth.sqrt((float) (distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ)) / 0.05F) + " blocks/sec";
				//#else
				return String.format("%.2f", Mth.sqrt((distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ)) / 0.05F) + " blocks/sec";
				//#endif
			}));
			y += 14;
			setDefaults("keystroke", y);
			lists.add(new InfoLabel("keystroke", configuration, () -> {
				if (Minecraft.getInstance().screen == this)
					return "Keystrokes";
				return KeystrokeUtils.getKeystrokes();
			}));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Render the Info Hud only
	 */
	public void drawHud() {
		int xpos = 40;
		int ypos = 190;
		for (InfoLabel label : lists) {
			
			int lx = label.x;
			int ly = label.y;

			int marginX = 5;
			int marginY = 5;


			int widthScaled=MCVer.getGLWindow().getGuiScaledWidth();
			int heightScaled=MCVer.getGLWindow().getGuiScaledHeight();
			
			if (getBBRight(lx, label.renderText) > widthScaled) {
				int offset = getBBRight(lx, label.renderText);
				lx = lx - (offset - widthScaled) - marginX;
			}

			if (getBBDown(ly) > heightScaled) {
				int offset = getBBDown(ly);
				ly = ly - (offset - heightScaled) - marginY;
			}
			
			if (label.visible) {
				drawRectWithText(label.renderText, lx, ly, label.renderRect);
			} else if (Minecraft.getInstance().screen != null) {
				if (Minecraft.getInstance().screen.getClass().getSimpleName().contains("InfoHud")) {
					//#if MC>=11700
//$$ 					MCVer.pushMatrix(MCVer.stack);
					//#else
					MCVer.pushMatrix(null);
					//#endif
		         	GL11.glEnable(GL11.GL_BLEND);
		         	GL11.glBlendFunc(770, 771);
		         	MCVer.drawShadow(label.renderText, lx + 2, ly + 3, 0x40FFFFFF);
		    		GL11.glDisable(GL11.GL_BLEND);
		    		//#if MC>=11700
//$$ 					MCVer.popMatrix(MCVer.stack);
					//#else
					MCVer.popMatrix(null);
					//#endif
				}
			}
			if (Minecraft.getInstance().screen instanceof InfoHud) {
				MCVer.drawShadow("Leftclick to move", width - ypos, xpos - 30, 0x60FF00);
				MCVer.drawShadow("Middleclick to enable", width - ypos, xpos - 20, 0x60FF00);
				MCVer.drawShadow("Rightclick to add black background", width - ypos, xpos - 10, 0x60FF00);
				MCVer.drawShadow("Hold Shift to snap to grid", width - ypos, xpos, 0x60FF00);
				MCVer.drawShadow("CTRL+Shift+R to reset the layout", width - ypos, xpos + 10, 0xEE8100);

				if (Screen.hasShiftDown() && Screen.hasControlDown() && KeybindsUtils.isKeyDown(GLFW.GLFW_KEY_R)) {
					resetLayout = true;
					configuration = null;
				}
			}
		}
	}
	
	/**
	 * Renders a Box with Text in it
	 */
	private void drawRectWithText(String text, int x, int y, boolean rect) {
		if (rect) MCVer.fill(x, y, getBBRight(x, text), getBBDown(y), 0x80000000);
		MCVer.drawShadow(text, x + 2, y + 3, 0xFFFFFF);
		GL11.glEnable(3042 /*GL_BLEND*/);
	}
	
	private int getBBRight(int x, String text) {
		return x + Minecraft.getInstance().font.width(text) + 4;
	}
	
	private int getBBDown(int y) {
		return y + 14;
	}
}
