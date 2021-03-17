package de.pfannekuchen.lotas.gui;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import rlog.RLogAPI;

public class GuiLootManipulation extends GuiScreen {

	public static Map<String, Boolean> drops = new TreeMap<String, Boolean>();
	public static Map<String, Boolean> entityDrops = new TreeMap<String, Boolean>();
	public static Map<String, Boolean> blockDrops = new TreeMap<String, Boolean>();
	public static String selectedList = "Entity Drops";
	public GuiListExtended list;

	static {
		blockDrops.put("Flint", false);
		blockDrops.put("Glowstone", false);
		blockDrops.put("Sealantern", false);
		blockDrops.put("Wheatseeds", false);
		blockDrops.put("Melons", false);
		blockDrops.put("Apple", false);
		blockDrops.put("Seed", false);
		blockDrops.put("Potato", false);
		blockDrops.put("Deadbush", false);
		blockDrops.put("Carrot", false);
		blockDrops.put("Redstone", false);
		blockDrops.put("Lapis", false);
		blockDrops.put("Cocoa_Beans", false);
		blockDrops.put("Chorus_Plant", false);
		blockDrops.put("Nether_Wart", false);
		blockDrops.put("Mushroom_Block", false);
		
		entityDrops.put("Blaze_Rod", false);
		entityDrops.put("Enderman_Enderpearl", false);
		entityDrops.put("Slime_Slimeball", false);
		entityDrops.put("Witch_Waterbreathing", false);
		entityDrops.put("Witch_Swiftness", false);
		entityDrops.put("Zombiepigman_Sword", false);
		entityDrops.put("Spider_Eye", false);
		entityDrops.put("Shulker_Shell", false);
		entityDrops.put("Witch_Firepot", false);
		entityDrops.put("Witch_Healingpot", false);
		entityDrops.put("Witch_General", false);
		entityDrops.put("Witherskeleton_Skull", false);
		entityDrops.put("Zombie_Potato", false);
		entityDrops.put("Zombie_Carrot", false);
		entityDrops.put("Zombie_Iron", false);
		entityDrops.put("Skeleton_BowArrow", false);
		entityDrops.put("Ghast_Tear", false);
		entityDrops.put("Creeper_Gunpowder", false);
		entityDrops.put("Magma_Creme", false);
		entityDrops.put("Elder_Guardian", false);
		entityDrops.put("Guardian", false);
		
		entityDrops.put("Chicken", false);
		entityDrops.put("Cow", false);
		entityDrops.put("Mooshroom", false);
		entityDrops.put("Pig", false);
		entityDrops.put("Rabbit", false);
		entityDrops.put("Sheep", false);
		entityDrops.put("Squid", false);
		entityDrops.put("Iron_Golem", false);
	}

	@Override
	public void initGui() {
		
		int w = width / 4;
		
		this.buttonList.add(new GuiButton(42069, (width / 2) - 20 - w, 16, w, 20, "Entity Drops"));
		this.buttonList.add(new GuiButton(42069, (width / 2) + 20, 16, w, 20, "Block Drops"));

		this.buttonList.add(new GuiButton(55, (width / 2) - w, height - 26, w * 2, 20, "Done"));

		
		int i = 0;
		int widthOfBtn = (int) (this.width * .95f / 4);
		int heightOfBtn = 20;

		int spaceInBetween = (int) (this.width * .05f / 5);
		list = new GuiListExtended(Minecraft.getMinecraft(), width, height, 42, height - 32, 0) {
			
			@Override
			protected int getSize() {
				return 0;
			}
			
			@Override
			public IGuiListEntry getListEntry(int index) {
				return null;
			}
		};
		for (Entry<String, Boolean> s : drops.entrySet()) {

			int row = (int) Math.floor(i / 4);
			int column = i % 4;
			row += 2;
			this.buttonList.add(new GuiButton(i, column * (widthOfBtn + spaceInBetween) + spaceInBetween, row * 25 - 12 + 16,
					widthOfBtn, heightOfBtn,
					s.getKey().replaceAll("_", " ") + ": " + (drops.get(s.getKey()) ? "\u00A7aYes" : "\u00A7cNo")));

			i++;
		}
	}

	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 42069) {
			for (GuiButton guiButton : buttonList) {
				if (guiButton.displayString.equalsIgnoreCase(selectedList))
					guiButton.enabled = true;
			}
			button.enabled = false;
			selectedList = button.displayString;
			this.buttonList = this.buttonList.subList(0, 3);
			if (selectedList.equalsIgnoreCase("Entity Drops")) {
				drops = entityDrops;
			} else if (selectedList.equalsIgnoreCase("Block Drops")) {
				drops = blockDrops;
			}
			int i = 0;
			int widthOfBtn = (int) (this.width * .95f / 4);
			int heightOfBtn = 20;

			int spaceInBetween = (int) (this.width * .05f / 5);

			for (Entry<String, Boolean> s : drops.entrySet()) {

				int row = (int) Math.floor(i / 4);
				int column = i % 4;
				row += 2;
				this.buttonList.add(new GuiButton(i, column * (widthOfBtn + spaceInBetween) + spaceInBetween, row * 25 - 12 + 16,
						widthOfBtn, heightOfBtn,
						s.getKey().replaceAll("_", " ") + ": " + (drops.get(s.getKey()) ? "\u00A7aYes" : "\u00A7cNo")));

				i++;
			}
			
			
		} else if (button.id == 55) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
		} else {
			String manip = button.displayString.replaceAll(" ", "_").split(":")[0];
			boolean isEnabled = drops.get(manip);
			RLogAPI.logDebug("[LootManipulation] Toggline " + manip + " to " + !isEnabled);
			drops.remove(manip);

			for (Entry<String, Boolean> entry : new HashMap<String, Boolean>(drops).entrySet()) {
				if (entry.getKey().startsWith(manip.split("_")[0])) {
					drops.remove(entry.getKey());
					drops.put(entry.getKey(), false);
				}
			}

			for (Entry<String, Boolean> entry : new HashMap<String, Boolean>(blockDrops).entrySet()) {
				if (entry.getKey().startsWith(manip.split("_")[0])) {
					drops.remove(entry.getKey());
					drops.put(entry.getKey(), false);
				}
			}

			for (Entry<String, Boolean> entry : new HashMap<String, Boolean>(entityDrops).entrySet()) {
				if (entry.getKey().startsWith(manip.split("_")[0])) {
					drops.remove(entry.getKey());
					drops.put(entry.getKey(), false);
				}
			}

			drops.put(manip, !isEnabled);
			if (selectedList.equalsIgnoreCase("Entity Drops")) {
				entityDrops = drops;
			} else if (selectedList.equalsIgnoreCase("Block Drops")) {
				blockDrops = drops;
			}
			drops = new TreeMap<String, Boolean>(drops);
			this.buttonList = this.buttonList.subList(0, 3);
			int i = 0;
			int widthOfBtn = (int) (this.width * .95f / 4);
			int heightOfBtn = 20;

			int spaceInBetween = (int) (this.width * .05f / 5);

			for (Entry<String, Boolean> s : drops.entrySet()) {

				int row = (int) Math.floor(i / 4);
				int column = i % 4;
				row += 2;
				this.buttonList.add(new GuiButton(i, column * (widthOfBtn + spaceInBetween) + spaceInBetween, row * 25 - 12 + 16,
						widthOfBtn, heightOfBtn,
						s.getKey().replaceAll("_", " ") + ": " + (drops.get(s.getKey()) ? "\u00A7aYes" : "\u00A7cNo")));

				i++;
			}
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		list.drawScreen(mouseX, mouseY, partialTicks);
		drawCenteredString(Minecraft.getMinecraft().fontRenderer, "Entity and Block Drop Manipulator", width / 2, 4, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
