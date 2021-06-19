package de.pfannekuchen.lotas.gui;

import java.util.HashMap;

import de.pfannekuchen.lotas.gui.widgets.EntitySliderWidget;
import de.pfannekuchen.lotas.gui.widgets.NewButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
//#if MC>=11601
//$$ import net.minecraft.client.util.math.MatrixStack;
//$$ import net.minecraft.entity.SpawnReason;
//$$ import net.minecraft.enchantment.Enchantment;
//#else
import net.minecraft.enchantment.InfoEnchantment;
//#endif
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.world.Difficulty;

/**
 * Draws a gui where the player can decide to spawn an entity
 * @author Pancake
 *
 */
public class SpawnManipulationScreen extends Screen {

	public SpawnManipulationScreen() {
		super(new LiteralText(""));
	}

	public static HashMap<Integer, String> entities = new HashMap<Integer, String>();
	public static TextFieldWidget xText;
	public static TextFieldWidget yText;
	public static TextFieldWidget zText;

	public static LivingEntity e;
	//#if MC>=11601
//$$ 		@SuppressWarnings("serial")
//$$ 	    public static HashMap<Enchantment, Integer> skelBow = new HashMap<Enchantment, Integer>() {{
//$$ 		    put(Enchantments.UNBREAKING, 1);
//$$ 		    put(Enchantments.POWER, 1);}};
//$$ 		@SuppressWarnings("serial")
//$$ 	    public static HashMap<Enchantment, Integer> zombieSword = new HashMap<Enchantment, Integer>() {{
//$$ 		   put(Enchantments.SHARPNESS, 2);
//$$ 		   put(Enchantments.UNBREAKING, 2);}};
	//#else
	public static InfoEnchantment[] skelBow = new InfoEnchantment[] { new InfoEnchantment(Enchantments.UNBREAKING, 1), new InfoEnchantment(Enchantments.POWER, 1) };
	public static InfoEnchantment[] zombieSword = new InfoEnchantment[] { new InfoEnchantment(Enchantments.SHARPNESS, 2), new InfoEnchantment(Enchantments.UNBREAKING, 2) };
	//#endif

	//#if MC>=11601
//$$ 		public int spawnX = (int) MinecraftClient.getInstance().player.getX();
//$$ 		public int spawnY = (int) MinecraftClient.getInstance().player.getY();
//$$ 		public int spawnZ = (int) MinecraftClient.getInstance().player.getZ();
	//#else
	public int spawnX = (int) MinecraftClient.getInstance().player.x;
	public int spawnY = (int) MinecraftClient.getInstance().player.y;
	public int spawnZ = (int) MinecraftClient.getInstance().player.z;
	//#endif
	public EntitySliderWidget entity;

	//#if MC>=11601
//$$ 	    @Override
//$$ 	    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
//$$ 	        super.render(matrices, mouseX, mouseY, partialTicks);
//$$ 	        xText.render(matrices, mouseX, mouseY, partialTicks);
//$$ 	        yText.render(matrices, mouseX, mouseY, partialTicks);
//$$ 	        zText.render(matrices, mouseX, mouseY, partialTicks);
//$$ 	    }
	//#else
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		super.render(mouseX, mouseY, partialTicks);
		xText.render(mouseX, mouseY, partialTicks);
		yText.render(mouseX, mouseY, partialTicks);
		zText.render(mouseX, mouseY, partialTicks);
	}
	//#endif

	@Override
	public boolean charTyped(char typedChar, int keyCode) {
		if (Character.isDigit(typedChar) || !Character.isLetter(typedChar)) {
			xText.charTyped(typedChar, keyCode);
			yText.charTyped(typedChar, keyCode);
			zText.charTyped(typedChar, keyCode);
		}
		try {
			spawnX = Integer.parseInt(xText.getText());
			spawnY = Integer.parseInt(yText.getText());
			spawnZ = Integer.parseInt(zText.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.charTyped(typedChar, keyCode);
	}

	@Override
	public void init() {
		entities.clear();
		entities.put(0, "Blaze");
		entities.put(1, "Cave Spider");
		entities.put(2, "Creeper");
		entities.put(3, "Enderman");
		entities.put(4, "Ghast");
		entities.put(5, "Husk");
		entities.put(6, "Ghast");
		entities.put(7, "Magma Cube");
		entities.put(8, "Skeleton");
		entities.put(9, "Slime");
		entities.put(10, "Spider");
		entities.put(11, "Witch");
		entities.put(12, "Witherskeleton");
		entities.put(13, "Zombie");
		entities.put(14, "Zombievillager");

		if (MinecraftClient.getInstance().world.getDifficulty() == Difficulty.HARD) {
			entities.put(15, "Skeleton with Enchanted Bow");
			entities.put(16, "Zombie with Enchanted Sword");

			entities.put(17, "Skeleton with Leather Armor and Enchanted Bow");
			entities.put(18, "Zombie with Leather Armor and Enchanted Sword");
			entities.put(19, "Skeleton with Gold Armor and Enchanted Bow");
			entities.put(20, "Zombie with Gold Armor and Enchanted Sword");
			entities.put(21, "Skeleton with Chain Armor and Enchanted Bow");
			entities.put(22, "Zombie with Chain Armor and Enchanted Sword");
			entities.put(23, "Skeleton with Iron Armor and Enchanted Bow");
			entities.put(24, "Zombie with Iron Armor and Enchanted Sword");
			entities.put(25, "Skeleton with Diamond Armor and Enchanted Bow");
			entities.put(26, "Zombie with Diamond Armor and Enchanted Sword");
		}

		entity = new EntitySliderWidget(width / 2 - 102, 2, entities, 204, 20, btn -> {

		});
		//#if MC>=11700
//$$ 		addDrawable(entity);
//$$ 		addDrawable(new NewButtonWidget(width / 9 * 3 + 6, height - 24, width / 9 - 4, 20, "X++", btn -> spawnX++));
//$$ 		addDrawable(new NewButtonWidget(width / 9 * 4 + 6, height - 24, width / 9 - 4, 20, "X--", btn -> spawnX--));
//$$ 		addDrawable(new NewButtonWidget(width / 9 * 5 + 3, height - 24, width / 9 - 4, 20, "Y++", btn -> spawnY++));
//$$ 		addDrawable(new NewButtonWidget(width / 9 * 6 + 3, height - 24, width / 9 - 4, 20, "Y--", btn -> spawnY--));
//$$ 		addDrawable(new NewButtonWidget(width / 9 * 7 + 1, height - 24, width / 9 - 4, 20, "Z++", btn -> spawnZ++));
//$$ 		addDrawable(new NewButtonWidget(width / 9 * 8 + 1, height - 24, width / 9 - 4, 20, "Z--", btn -> spawnZ--));
		//#else
		addButton(entity);
		addButton(new NewButtonWidget(width / 9 * 3 + 6, height - 24, width / 9 - 4, 20, "X++", btn -> spawnX++));
		addButton(new NewButtonWidget(width / 9 * 4 + 6, height - 24, width / 9 - 4, 20, "X--", btn -> spawnX--));
		addButton(new NewButtonWidget(width / 9 * 5 + 3, height - 24, width / 9 - 4, 20, "Y++", btn -> spawnY++));
		addButton(new NewButtonWidget(width / 9 * 6 + 3, height - 24, width / 9 - 4, 20, "Y--", btn -> spawnY--));
		addButton(new NewButtonWidget(width / 9 * 7 + 1, height - 24, width / 9 - 4, 20, "Z++", btn -> spawnZ++));
		addButton(new NewButtonWidget(width / 9 * 8 + 1, height - 24, width / 9 - 4, 20, "Z--", btn -> spawnZ--));
		//#endif
		
		//#if MC>=11601
//$$ 		        xText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 9 * 3 + 6, height - 46, (int) (width / 4.5) - 6, 20, new LiteralText(spawnX + ""));
//$$ 		        yText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 9 * 5 + 4, height - 46, (int) (width/ 4.5) - 6, 20, new LiteralText(spawnY + ""));
//$$ 		        zText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 9 * 7 + 2, height - 46, (int) (width/ 4.5) - 6, 20, new LiteralText(spawnZ + ""));
		        //#if MC>=11700
//$$ 		        addDrawable(new NewButtonWidget(5, height - 24, width / 3, 20, "Spawn Entity", btn -> MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(MinecraftClient.getInstance().player.getUuid()).world.spawnEntity(e)));
//$$ 		        addDrawable(new NewButtonWidget(5, height - 46, width / 3, 20, "Done", btn -> MinecraftClient.getInstance().openScreen(new GameMenuScreen(true))));
		        //#else
//$$ 		        addButton(new NewButtonWidget(5, height - 24, width / 3, 20, "Spawn Entity", btn -> MinecraftClient.getInstance().getServer().getPlayerManager().getPlayer(MinecraftClient.getInstance().player.getUuid()).world.spawnEntity(e)));
//$$ 		        addButton(new NewButtonWidget(5, height - 46, width / 3, 20, "Done", btn -> MinecraftClient.getInstance().openScreen(new GameMenuScreen(true))));
		        //#endif
		//#else
		xText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 9 * 3 + 6, height - 46, (int) (width / 4.5) - 6, 20, spawnX + "");
		yText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 9 * 5 + 4, height - 46, (int) (width / 4.5) - 6, 20, spawnY + "");
		zText = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, width / 9 * 7 + 2, height - 46, (int) (width / 4.5) - 6, 20, spawnZ + "");
		addButton(new NewButtonWidget(5, height - 24, width / 3, 20, "Spawn Entity", btn -> MinecraftClient.getInstance().getServer().getWorld(MinecraftClient.getInstance().player.dimension).spawnEntity(e)));
		addButton(new NewButtonWidget(5, height - 46, width / 3, 20, "Done", btn -> minecraft.openScreen(new GameMenuScreen(true))));
		//#endif
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		xText.mouseClicked(mouseX, mouseY, mouseButton);
		yText.mouseClicked(mouseX, mouseY, mouseButton);
		zText.mouseClicked(mouseX, mouseY, mouseButton);
		boolean b = super.mouseClicked(mouseX, mouseY, mouseButton);
		canSpawn();
		return b;
	}

	public void canSpawn() {
		e = entity.getEntity(MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld());
		e.updatePositionAndAngles(spawnX, spawnY, spawnZ, 0, 0);
		if (e instanceof MobEntity) {
			//#if MC>=11601
			//#if MC>=11605
			//#if MC>=11700
//$$ 			ButtonWidget buttons = (ButtonWidget) drawables.get(drawables.size() - 2);
//$$ 			buttons.active = ((MobEntity) e).canSpawn(MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld(), SpawnReason.NATURAL) && MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld().getBlockCollisions(e, e.getBoundingBox()).count() == 0;
			//#else
//$$ 			buttons.get(buttons.size() - 2).active = ((MobEntity) e).canSpawn(MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld(), SpawnReason.NATURAL) && MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld().getBlockCollisions(e, e.getBoundingBox()).count() == 0;
			//#endif
			//#else
//$$ 			buttons.get(buttons.size() - 2).active = ((MobEntity) e).canSpawn(MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld(), SpawnReason.NATURAL) && MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld().doesNotCollide(e.getBoundingBox());
			//#endif
			//#else
			buttons.get(buttons.size() - 2).active = ((MobEntity) e).canSpawn(MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld(), net.minecraft.entity.SpawnType.NATURAL) && MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld().doesNotCollide(e.getBoundingBox());
			//#endif
		} else {
			//#if MC>=11605
			//#if MC>=11700
//$$ 			ButtonWidget buttons = (ButtonWidget) drawables.get(drawables.size() - 2);
//$$ 			buttons.active = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld().getBlockCollisions(e, e.getBoundingBox()).count() == 0;
			//#else
//$$ 			buttons.get(buttons.size() - 2).active = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld().getBlockCollisions(e, e.getBoundingBox()).count() == 0;
			//#endif
			//#else
			buttons.get(buttons.size() - 2).active = MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().get(0).getServerWorld().doesNotCollide(e.getBoundingBox());
			//#endif
		}
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int state) {
		boolean b = super.mouseReleased(mouseX, mouseY, state);
		canSpawn();
		return b;
	}

}
