package de.pfannekuchen.lotas.gui.widgets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import de.pfannekuchen.lotas.gui.GuiEntitySpawnManipulation;
import de.pfannekuchen.lotas.mixin.accessors.AccessorEntityLiving;
import de.pfannekuchen.lotas.mods.SpawnManipMod.EntityOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.config.GuiSlider;

public class EntitySliderWidget extends GuiSlider {

	public List<EntityOptions> entities;

	public EntitySliderWidget(int id, int xPos, int yPos, List<EntityOptions> ent, int width, int height) {
		super(id, xPos, yPos, width, height, "", "", 0, ent.size() - 1, 0, true, true, null);
		showDecimal = false;
		this.displayString = dispString + ent.get(0).title;
		this.entities = ent;
	}

	public EntityLiving getEntity(WorldServer world) {
		return entities.get((int) Math.round(sliderValue * (maxValue - minValue) + minValue)).entity;
	}

	
	public void updateSlider() {
		if (this.sliderValue < 0.0F) {
			this.sliderValue = 0.0F;
		}

		if (this.sliderValue > 1.0F) {
			this.sliderValue = 1.0F;
		}

		displayString = dispString + entities.get((int) Math.round(sliderValue * (maxValue - minValue) + minValue)).title
				+ suffix;
	}

	
}
