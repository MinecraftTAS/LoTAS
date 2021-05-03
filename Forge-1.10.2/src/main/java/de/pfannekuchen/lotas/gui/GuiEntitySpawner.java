package de.pfannekuchen.lotas.gui;

import java.io.IOException;
import java.util.HashMap;

import de.pfannekuchen.lotas.gui.parts.CustomEntitySlider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Enchantments;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumSkyBlock;
import rlog.RLogAPI;

/**
 * Draws a gui where the player can decide to spawn an entity
 * @author Pancake
 *
 */
public class GuiEntitySpawner extends GuiScreen {
	
	public static HashMap<Integer, String> entities = new HashMap<Integer, String>();
	public static GuiTextField xText;
	public static GuiTextField yText;
	public static GuiTextField zText;
	
	public static EntityLiving e;
	public static EnchantmentData[] skelBow = new EnchantmentData[] {new EnchantmentData(Enchantments.UNBREAKING, 1), new EnchantmentData(Enchantments.POWER, 1)};
	public static EnchantmentData[] zombieSword = new EnchantmentData[] {new EnchantmentData(Enchantments.SHARPNESS, 2), new EnchantmentData(Enchantments.UNBREAKING, 2)};
	
	public int spawnX = (int) Minecraft.getMinecraft().thePlayer.posX;
	public int spawnY = (int) Minecraft.getMinecraft().thePlayer.posY;
	public int spawnZ = (int) Minecraft.getMinecraft().thePlayer.posZ;
	public CustomEntitySlider entity;
	
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
			RLogAPI.logError(e, "Parse Invalid Data #2");
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
		entities.put(5, "Zombie");
		entities.put(6, "Ghast");
		entities.put(7, "Magma Cube");
		entities.put(8, "Skeleton");
		entities.put(9, "Slime");
		entities.put(10, "Spider");
		entities.put(11, "Witch");
		
		if (mc.theWorld.getDifficulty() == EnumDifficulty.HARD) {
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
		
		entity = new CustomEntitySlider(1, 5, 2, entities, width - 10, 25);
		this.buttonList.add(entity);
		this.buttonList.add(new GuiButton(8, width / 9 * 3 + 6, height - 24, width / 9 - 4, 20, "X++"));
		this.buttonList.add(new GuiButton(2, width / 9 * 4 + 6, height - 24, width / 9 - 4, 20, "X--"));
		this.buttonList.add(new GuiButton(5, width / 9 * 5 + 3, height - 24, width / 9 - 4, 20, "Y++"));
		this.buttonList.add(new GuiButton(9, width / 9 * 6 + 3, height - 24, width / 9 - 4, 20, "Y--"));
		this.buttonList.add(new GuiButton(6, width / 9 * 7 + 1, height - 24, width / 9 - 4, 20, "Z++"));
		this.buttonList.add(new GuiButton(4, width / 9 * 8 + 1, height - 24, width / 9 - 4, 20, "Z--"));
		
		xText = new GuiTextField(91, Minecraft.getMinecraft().fontRendererObj, width / 9 * 3 + 6, height - 46, (int) (width / 4.5) - 6, 20);
		xText.setText(spawnX + "");
		yText = new GuiTextField(92, Minecraft.getMinecraft().fontRendererObj, width / 9 * 5 + 4, height - 46, (int) (width/ 4.5) - 6, 20);
		yText.setText(spawnY + "");
		zText = new GuiTextField(93, Minecraft.getMinecraft().fontRendererObj, width / 9 * 7 + 2, height - 46, (int) (width/ 4.5) - 6, 20);
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
		e = entity.getEntity(mc.theIntegratedServer.worldServerForDimension(mc.thePlayer.dimension));
		e.setPositionAndRotation(spawnX, spawnY, spawnZ, 0, 0);
		if (e instanceof EntityMob) {
			this.buttonList.get(this.buttonList.size() - 2).enabled = e.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && ((EntityMob) e).getBlockPathWeight(new BlockPos(e.posX, e.getEntityBoundingBox().minY, e.posZ)) >= 0.0F && e.worldObj.getBlockState((new BlockPos(e)).down()).canEntitySpawn(e) && isValidLightLevel(e) && !e.worldObj.collidesWithAnyBlock(e.getEntityBoundingBox());
		} else {
			this.buttonList.get(this.buttonList.size() - 2).enabled = e.worldObj.getBlockState((new BlockPos(e)).down()).canEntitySpawn(e) && !e.worldObj.collidesWithAnyBlock(e.getEntityBoundingBox());
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		e = entity.getEntity(mc.theIntegratedServer.worldServerForDimension(mc.thePlayer.dimension));
		e.setPositionAndRotation(spawnX, spawnY, spawnZ, 0, 0);
		
		if (e instanceof EntityMob) {
			this.buttonList.get(this.buttonList.size() - 2).enabled = e.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && ((EntityMob) e).getBlockPathWeight(new BlockPos(e.posX, e.getEntityBoundingBox().minY, e.posZ)) >= 0.0F && e.worldObj.getBlockState((new BlockPos(e)).down()).canEntitySpawn(e) && isValidLightLevel(e) && !e.worldObj.collidesWithAnyBlock(e.getEntityBoundingBox());
		} else {
			this.buttonList.get(this.buttonList.size() - 2).enabled = e.worldObj.getBlockState((new BlockPos(e)).down()).canEntitySpawn(e) && !e.worldObj.collidesWithAnyBlock(e.getEntityBoundingBox());
		}
	}
	
	private boolean isValidLightLevel(EntityLiving entity) {
        BlockPos blockpos = new BlockPos(entity.posX, entity.getEntityBoundingBox().minY, entity.posZ);

        if (entity.worldObj.getLightFor(EnumSkyBlock.SKY, blockpos) > 31) {
            return false;
        } else {
            int i = entity.worldObj.getLightFromNeighbors(blockpos);

            if (entity.worldObj.isThundering()) {
                int j = entity.worldObj.getSkylightSubtracted();
                entity.worldObj.setSkylightSubtracted(10);
                i = entity.worldObj.getLightFromNeighbors(blockpos);
                entity.worldObj.setSkylightSubtracted(j);
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
			Minecraft.getMinecraft().displayGuiScreen(new GuiIngameMenu());
			break;
		case 69:
			Minecraft.getMinecraft().theIntegratedServer.worldServerForDimension(mc.thePlayer.dimension).spawnEntityInWorld(e);
		default:
			break;
		}
		
		xText.setText(spawnX + "");
		yText.setText(spawnY + "");
		zText.setText(spawnZ + "" );
	}
	
}
