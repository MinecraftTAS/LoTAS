package de.pfannekuchen.lotas.gui;

import java.io.IOException;
import java.util.HashMap;

import de.pfannekuchen.lotas.core.MCVer;
import de.pfannekuchen.lotas.gui.widgets.EntitySliderWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
//#if MC>=10900
import net.minecraft.init.Enchantments;
import net.minecraft.util.math.BlockPos;
//#else
//$$ import net.minecraft.util.BlockPos;
//$$ import net.minecraft.enchantment.Enchantment;
//#endif
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;

/**
 * Draws a gui where the player can decide to spawn an entity
 * @author Pancake
 *
 */
public class GuiEntitySpawnManipulation extends GuiScreen {
	
	public static HashMap<Integer, String> entities = new HashMap<Integer, String>();
	public static GuiTextField xText;
	public static GuiTextField yText;
	public static GuiTextField zText;
	
	public static EntityLiving e;
	//#if MC>=10900
	public static EnchantmentData[] skelBow = new EnchantmentData[] {new EnchantmentData(Enchantments.UNBREAKING, 1), new EnchantmentData(Enchantments.POWER, 1)};
	public static EnchantmentData[] zombieSword = new EnchantmentData[] {new EnchantmentData(Enchantments.SHARPNESS, 2), new EnchantmentData(Enchantments.UNBREAKING, 2)};
	//#else
//$$ 	public static EnchantmentData[] skelBow = new EnchantmentData[] {new EnchantmentData(Enchantment.unbreaking, 1), new EnchantmentData(Enchantment.power, 1)};
//$$ 	public static EnchantmentData[] zombieSword = new EnchantmentData[] {new EnchantmentData(Enchantment.sharpness, 2), new EnchantmentData(Enchantment.unbreaking, 2)};
	//#endif
	
	public int spawnX = (int) MCVer.player(Minecraft.getMinecraft()).posX;
	public int spawnY = (int) MCVer.player(Minecraft.getMinecraft()).posY;
	public int spawnZ = (int) MCVer.player(Minecraft.getMinecraft()).posZ;
	
	public EntitySliderWidget entity;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		xText.drawTextBox();
		yText.drawTextBox();
		zText.drawTextBox();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (Character.isDigit(typedChar) || !Character.isLetter(typedChar)) {
			xText.textboxKeyTyped(typedChar, keyCode);
			yText.textboxKeyTyped(typedChar, keyCode);
			zText.textboxKeyTyped(typedChar, keyCode);
		}
		try {
			spawnX = Integer.parseInt(xText.getText());
			spawnY = Integer.parseInt(yText.getText());
			spawnZ = Integer.parseInt(zText.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void initGui() {
		
		entities.clear();
		entities.put(0, "Blaze");
		entities.put(1, "Cave Spider");
		entities.put(2, "Creeper");
		entities.put(3, "Enderman");
		entities.put(4, "Ghast");
		//#if MC>=11100
		entities.put(5, "Husk");
		//#else
//$$ 		entities.put(5, "Zombie");
		//#endif
		entities.put(6, "Ghast");
		entities.put(7, "Magma Cube");
		entities.put(8, "Skeleton");
		entities.put(9, "Slime");
		entities.put(10, "Spider");
		entities.put(11, "Witch");
		//#if MC>=11100
		entities.put(12, "Witherskeleton");
		entities.put(13, "Zombie");
		entities.put(14, "Zombievillager");
		//#endif
		
		//#if MC>=11100
		if (mc.world.getDifficulty() == EnumDifficulty.HARD) {
		//#else
//$$ 		if (mc.theWorld.getDifficulty() == EnumDifficulty.HARD) {
		//#endif
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
		
		entity = new EntitySliderWidget(1, 5, 2, entities, width - 10, 25);
		this.buttonList.add(entity);
		this.buttonList.add(new GuiButton(8, width / 9 * 3 + 6, height - 24, width / 9 - 4, 20, "X++"));
		this.buttonList.add(new GuiButton(2, width / 9 * 4 + 6, height - 24, width / 9 - 4, 20, "X--"));
		this.buttonList.add(new GuiButton(5, width / 9 * 5 + 3, height - 24, width / 9 - 4, 20, "Y++"));
		this.buttonList.add(new GuiButton(9, width / 9 * 6 + 3, height - 24, width / 9 - 4, 20, "Y--"));
		this.buttonList.add(new GuiButton(6, width / 9 * 7 + 1, height - 24, width / 9 - 4, 20, "Z++"));
		this.buttonList.add(new GuiButton(4, width / 9 * 8 + 1, height - 24, width / 9 - 4, 20, "Z--"));
		
		xText = new GuiTextField(91, MCVer.getFontRenderer(mc), width / 9 * 3 + 6, height - 46, (int) (width / 4.5) - 6, 20);
		xText.setText(spawnX + "");
		yText = new GuiTextField(92, MCVer.getFontRenderer(mc), width / 9 * 5 + 4, height - 46, (int) (width/ 4.5) - 6, 20);
		yText.setText(spawnY + "");
		zText = new GuiTextField(93, MCVer.getFontRenderer(mc), width / 9 * 7 + 2, height - 46, (int) (width/ 4.5) - 6, 20);
		zText.setText(spawnZ + "");
		
		this.buttonList.add(new GuiButton(69, 5, height - 24, width / 3, 20, "Spawn Entity"));
		this.buttonList.add(new GuiButton(12, 5, height - 46, width / 3, 20, "Done"));
		
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		xText.mouseClicked(mouseX, mouseY, mouseButton);
		yText.mouseClicked(mouseX, mouseY, mouseButton);
		zText.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
		e = entity.getEntity(MCVer.world(mc.getIntegratedServer(), MCVer.player(Minecraft.getMinecraft()).dimension));
		e.setPositionAndRotation(spawnX, spawnY, spawnZ, 0, 0);
		checkEntity();
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		e = entity.getEntity(MCVer.world(mc.getIntegratedServer(), MCVer.player(Minecraft.getMinecraft()).dimension));
		e.setPositionAndRotation(spawnX, spawnY, spawnZ, 0, 0);
		checkEntity();
	}
	
	public void checkEntity() {
		//#if MC>=11100
		if (e instanceof EntityMob) {
			this.buttonList.get(this.buttonList.size() - 2).enabled = e.world.getDifficulty() != EnumDifficulty.PEACEFUL && ((EntityMob) e).getBlockPathWeight(new BlockPos(e.posX, e.getEntityBoundingBox().minY, e.posZ)) >= 0.0F && e.world.getBlockState((new BlockPos(e)).down()).canEntitySpawn(e) && isValidLightLevel(e) && !e.world.collidesWithAnyBlock(e.getEntityBoundingBox());
		} else {
			this.buttonList.get(this.buttonList.size() - 2).enabled = e.world.getBlockState((new BlockPos(e)).down()).canEntitySpawn(e) && !e.world.collidesWithAnyBlock(e.getEntityBoundingBox());
		}
		//#else
		//#if MC>=11000
		//$$ if (e instanceof EntityMob) {
		//$$ 	this.buttonList.get(this.buttonList.size() - 2).enabled = e.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && ((EntityMob) e).getBlockPathWeight(new BlockPos(e.posX, e.getEntityBoundingBox().minY, e.posZ)) >= 0.0F && e.worldObj.getBlockState((new BlockPos(e)).down()).canEntitySpawn(e) && isValidLightLevel(e) && !e.worldObj.collidesWithAnyBlock(e.getEntityBoundingBox());
		//$$ } else {
		//$$ 	this.buttonList.get(this.buttonList.size() - 2).enabled = e.worldObj.getBlockState((new BlockPos(e)).down()).canEntitySpawn(e) && !e.worldObj.collidesWithAnyBlock(e.getEntityBoundingBox());
		//$$ }
		//#else
		//#if MC>=10900
//$$ 		if (e instanceof EntityMob) {
//$$ 			this.buttonList.get(this.buttonList.size() - 2).enabled = e.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && ((EntityMob) e).getBlockPathWeight(new BlockPos(e.posX, e.getEntityBoundingBox().minY, e.posZ)) >= 0.0F && isValidLightLevel(e) && !e.worldObj.collidesWithAnyBlock(e.getEntityBoundingBox());
//$$ 		} else {
//$$ 			this.buttonList.get(this.buttonList.size() - 2).enabled = !e.worldObj.collidesWithAnyBlock(e.getEntityBoundingBox());
//$$ 		}
		//#else
//$$ 		if (e instanceof EntityMob) {
//$$ 			this.buttonList.get(this.buttonList.size() - 2).enabled = e.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && ((EntityMob) e).getBlockPathWeight(new BlockPos(e.posX, e.getEntityBoundingBox().minY, e.posZ)) >= 0.0F && isValidLightLevel(e) && !e.worldObj.checkBlockCollision(e.getEntityBoundingBox());
//$$ 		} else {
//$$ 			this.buttonList.get(this.buttonList.size() - 2).enabled = !e.worldObj.checkBlockCollision(e.getEntityBoundingBox());
//$$ 		}
		//#endif
		//#endif
		//#endif
	}
	
	private boolean isValidLightLevel(EntityLiving entity) {
        BlockPos blockpos = new BlockPos(entity.posX, entity.getEntityBoundingBox().minY, entity.posZ);

        if (MCVer.world(entity).getLightFor(EnumSkyBlock.SKY, blockpos) > 31) {
            return false;
        } else {
            int i = MCVer.world(entity).getLightFromNeighbors(blockpos);

            if (MCVer.world(entity).isThundering()) {
                int j = MCVer.world(entity).getSkylightSubtracted();
                MCVer.world(entity).setSkylightSubtracted(10);
                i = MCVer.world(entity).getLightFromNeighbors(blockpos);
                MCVer.world(entity).setSkylightSubtracted(j);
            }

            return i <= 7;
        }
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 8:
			spawnX++;
			break;
		case 2:
			spawnX--;
			break;
		case 5:
			spawnY++;
			break;
		case 9:
			spawnY--;
			break;
		case 6:
			spawnZ++;
			break;
		case 4:
			spawnZ--;
			break;
		case 12:
			mc.displayGuiScreen(new GuiIngameMenu());
			break;
		case 69:
			//#if MC>=11100
			MCVer.world(mc.getIntegratedServer(), MCVer.player(mc).dimension).spawnEntity(e);
			//#else
//$$ 			MCVer.world(mc.getIntegratedServer(), MCVer.player(Minecraft.getMinecraft()).dimension).spawnEntityInWorld(e);
			//#endif
		default:
			break;
		}
		
		xText.setText(spawnX + "");
		yText.setText(spawnY + "");
		zText.setText(spawnZ + "" );
	}
	
}
