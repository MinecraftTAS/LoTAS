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

import org.lwjgl.opengl.GL11;

import de.pfannekuchen.lotas.core.MCVer;
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
	
	public InfoHud() {
		super(new TextComponent(""));
	}

	public static class InfoLabel {
		public String displayName;
		public int x;
		public int y;
		public boolean visible;
		public boolean renderRect;
		public String renderText;
		private Callable<String> text;
		
		public InfoLabel(String displayName, int x, int y, boolean visible, boolean renderRect, Callable<String> text) {
			this.displayName = displayName;
			this.visible = visible;
			this.x = x;
			this.y = y;
			this.renderRect = renderRect;
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
	
	public Properties configuration;
	public static List<InfoLabel> lists = new ArrayList<>();
	
	private void setDefaults(String string) {
		configuration.setProperty(string + "_x", "0");
		configuration.setProperty(string + "_y", "0");
		configuration.setProperty(string + "_visible", "false");
		configuration.setProperty(string + "_rect", "false");
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
			lists.get(currentlyDraggedIndex).x = (int) (mouseX) - xOffset;
			lists.get(currentlyDraggedIndex).y = (int) (mouseY) - yOffset;
			configuration.setProperty(dragging + "_x", lists.get(currentlyDraggedIndex).x + "");
			configuration.setProperty(dragging + "_y", lists.get(currentlyDraggedIndex).y + "");
		}
		super.mouseMoved(mouseX, mouseY);
	}
	
	/**
	 * Saves the Configuration
	 */
	private void saveConfig() {
		try {
			File tasmodDir = new File(Minecraft.getInstance().gameDirectory, "tasmodog");
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
			File tasmodDir = new File(Minecraft.getInstance().gameDirectory, "tasmodog");
			tasmodDir.mkdir();
			File configFile = new File(tasmodDir, "infogui.cfg");
			if (!configFile.exists()) configFile.createNewFile();
			configuration.load(new FileReader(configFile));
			lists = new ArrayList<InfoLabel>();
			/* ====================== */
			if (configuration.getProperty("tickrate_x", "err").equals("err")) setDefaults("tickrate");
			lists.add(new InfoLabel("tickrate", Integer.parseInt(configuration.getProperty("tickrate_x")), Integer.parseInt(configuration.getProperty("tickrate_y")), Boolean.parseBoolean(configuration.getProperty("tickrate_visible")), Boolean.parseBoolean(configuration.getProperty("tickrate_rect")), () -> {
				return "Tickrate: " + TickrateChangerMod.tickrate;
			}));
			if (configuration.getProperty("position_x", "err").equals("err")) setDefaults("position");
			lists.add(new InfoLabel("position", Integer.parseInt(configuration.getProperty("position_x")), Integer.parseInt(configuration.getProperty("position_y")), Boolean.parseBoolean(configuration.getProperty("position_visible")), Boolean.parseBoolean(configuration.getProperty("position_rect")), () -> {
				return String.format("%.2f %.2f %.2f", MCVer.getX(Minecraft.getInstance().player), MCVer.getY(Minecraft.getInstance().player), MCVer.getZ(Minecraft.getInstance().player));
			}));
			if (configuration.getProperty("preciseposition_x", "err").equals("err")) setDefaults("preciseposition");
			lists.add(new InfoLabel("preciseposition", Integer.parseInt(configuration.getProperty("preciseposition_x")), Integer.parseInt(configuration.getProperty("preciseposition_y")), Boolean.parseBoolean(configuration.getProperty("preciseposition_visible")), Boolean.parseBoolean(configuration.getProperty("preciseposition_rect")), () -> {
				return String.format("%f %f %f", MCVer.getX(Minecraft.getInstance().player), MCVer.getY(Minecraft.getInstance().player), MCVer.getZ(Minecraft.getInstance().player));
			}));
			if (configuration.getProperty("chunkposition_x", "err").equals("err")) setDefaults("chunkposition");
			lists.add(new InfoLabel("chunkposition", Integer.parseInt(configuration.getProperty("chunkposition_x")), Integer.parseInt(configuration.getProperty("chunkposition_y")), Boolean.parseBoolean(configuration.getProperty("chunkposition_visible")), Boolean.parseBoolean(configuration.getProperty("chunkposition_rect")), () -> {
				//#if MC>=11700
//$$ 				return String.format("%d %d", Minecraft.getInstance().player.chunkPosition().getRegionLocalX(), Minecraft.getInstance().player.chunkPosition().getRegionLocalZ());
				//#else
				return String.format("%d %d %d", Minecraft.getInstance().player.xChunk, Minecraft.getInstance().player.yChunk, Minecraft.getInstance().player.zChunk);
				//#endif
			}));
			if (configuration.getProperty("worldseed_x", "err").equals("err")) setDefaults("worldseed");
			lists.add(new InfoLabel("worldseed", Integer.parseInt(configuration.getProperty("worldseed_x")), Integer.parseInt(configuration.getProperty("worldseed_y")), Boolean.parseBoolean(configuration.getProperty("worldseed_visible")), Boolean.parseBoolean(configuration.getProperty("worldseed_rect")), () -> {
				return Minecraft.getInstance().getSingleplayerServer().getPlayerList().getPlayers().get(0).getLevel().getSeed() + "";
			}));
			if (configuration.getProperty("ticks_x", "err").equals("err")) setDefaults("ticks");
			lists.add(new InfoLabel("ticks", Integer.parseInt(configuration.getProperty("ticks_x")), Integer.parseInt(configuration.getProperty("ticks_y")), Boolean.parseBoolean(configuration.getProperty("ticks_visible")), Boolean.parseBoolean(configuration.getProperty("ticks_rect")), () -> {
				return TickrateChangerMod.ticksPassedServer + "";
			}));
			if (configuration.getProperty("savestates_x", "err").equals("err")) setDefaults("savestates");
			if (configuration.getProperty("savestates_x", "err").equals("err")) setDefaults("savestates");
			lists.add(new InfoLabel("savestates", Integer.parseInt(configuration.getProperty("savestates_x")), Integer.parseInt(configuration.getProperty("savestates_y")), Boolean.parseBoolean(configuration.getProperty("savestates_visible")), Boolean.parseBoolean(configuration.getProperty("savestates_rect")), () -> {
				return ("Savestates: " + SavestateMod.TrackerFile.savestateCount);
			}));
			if (configuration.getProperty("loadstates_x", "err").equals("err")) setDefaults("loadstates");
			lists.add(new InfoLabel("loadstates", Integer.parseInt(configuration.getProperty("loadstates_x")), Integer.parseInt(configuration.getProperty("loadstates_y")), Boolean.parseBoolean(configuration.getProperty("loadstates_visible")), Boolean.parseBoolean(configuration.getProperty("loadstates_rect")), () -> {
				return ("Loadstates: " + SavestateMod.TrackerFile.loadstateCount);
			}));
			if (configuration.getProperty("timer_x", "err").equals("err")) setDefaults("timer");
			lists.add(new InfoLabel("timer", Integer.parseInt(configuration.getProperty("timer_x")), Integer.parseInt(configuration.getProperty("timer_y")), Boolean.parseBoolean(configuration.getProperty("timer_visible")), Boolean.parseBoolean(configuration.getProperty("timer_rect")), () -> {
				return Timer.ticks == -1 ? "Timer is paused" : Timer.getDuration(Duration.ofMillis(Timer.ticks * 50));
			}));
			if (configuration.getProperty("rtatimer_x", "err").equals("err")) setDefaults("rtatimer");
			lists.add(new InfoLabel("rtatimer", Integer.parseInt(configuration.getProperty("rtatimer_x")), Integer.parseInt(configuration.getProperty("rtatimer_y")), Boolean.parseBoolean(configuration.getProperty("rtatimer_visible")), Boolean.parseBoolean(configuration.getProperty("rtatimer_rect")), () -> {
				if (Timer.running) TickrateChangerMod.rta = Duration.ofMillis(System.currentTimeMillis() - Timer.startTime.toMillis());
				return Timer.ticks == -1 ? "" : ("RTA: " + Timer.getDuration(TickrateChangerMod.rta));
			}));
			if (configuration.getProperty("bps_x", "err").equals("err")) setDefaults("bps");
			lists.add(new InfoLabel("bps", Integer.parseInt(configuration.getProperty("bps_x")), Integer.parseInt(configuration.getProperty("bps_y")), Boolean.parseBoolean(configuration.getProperty("bps_visible")), Boolean.parseBoolean(configuration.getProperty("bps_rect")), () -> {
				double distTraveledLastTickX = MCVer.getX(Minecraft.getInstance().player) - Minecraft.getInstance().player.xOld;
				double distTraveledLastTickZ = MCVer.getZ(Minecraft.getInstance().player) - Minecraft.getInstance().player.zOld;
				//#if MC>=11700
//$$ 				return String.format("%.2f", Mth.sqrt((float) (distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ)) / 0.05F) + " blocks/sec";
				//#else
				return String.format("%.2f", Mth.sqrt((distTraveledLastTickX * distTraveledLastTickX + distTraveledLastTickZ * distTraveledLastTickZ)) / 0.05F) + " blocks/sec";
				//#endif
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
		for (InfoLabel label : lists) {
			if (label.visible) {
				drawRectWithText(label.renderText, label.x, label.y, label.renderRect);
			} else if (Minecraft.getInstance().screen != null) {
				if (Minecraft.getInstance().screen.getClass().getSimpleName().contains("InfoHud")) {
					//#if MC>=11700
//$$ 					MCVer.pushMatrix(MCVer.stack);
					//#else
					MCVer.pushMatrix(null);
					//#endif
		         	GL11.glEnable(GL11.GL_BLEND);
		         	GL11.glBlendFunc(770, 771);
		         	MCVer.drawShadow(label.renderText, label.x + 2, label.y + 3, 0x40FFFFFF);
		    		GL11.glDisable(GL11.GL_BLEND);
		    		//#if MC>=11700
//$$ 					MCVer.popMatrix(MCVer.stack);
					//#else
					MCVer.popMatrix(null);
					//#endif
				}
			}
		}
	}
	
	/**
	 * Renders a Box with Text in it
	 */
	private void drawRectWithText(String text, int x, int y, boolean rect) {
		if (rect) MCVer.fill(x, y, x + Minecraft.getInstance().font.width(text) + 4, y + 14, 0x80000000);
		MCVer.drawShadow(text, x + 2, y + 3, 0xFFFFFF);
		GL11.glEnable(3042 /*GL_BLEND*/);
	}
	
}
