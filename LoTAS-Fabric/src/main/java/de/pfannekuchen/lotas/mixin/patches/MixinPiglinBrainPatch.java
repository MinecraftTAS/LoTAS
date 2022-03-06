package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;

//#if MC>=11601
//$$ import java.util.Iterator;
//$$ import java.util.List;
//$$
//$$ import org.spongepowered.asm.mixin.Overwrite;
//$$
//$$ import de.pfannekuchen.lotas.gui.DropManipulationScreen;
//$$ import net.minecraft.client.gui.components.Checkbox;
//$$ import net.minecraft.world.InteractionHand;
//$$ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
//$$ import net.minecraft.world.entity.monster.piglin.Piglin;
//$$ import net.minecraft.world.entity.monster.piglin.PiglinAi;
//$$ import net.minecraft.world.item.ItemStack;
//$$ import net.minecraft.world.phys.Vec3;
//$$
//$$ @Mixin(PiglinAi.class)
//$$ public class MixinPiglinBrainPatch {
//$$
//$$ 	@Overwrite
//$$ 	private static void throwItemsTowardPos(Piglin piglin, List<ItemStack> list, Vec3 vec3d) {
//$$ 		// Hijack list
//$$ 		for (DropManipulationScreen.DropManipulation man : DropManipulationScreen.manipulations) {
//$$ 			if (!((Checkbox) man.enabled).selected())
//$$ 				continue;
//$$ 			List<ItemStack> list2 = man.redirectDrops((Piglin) null, 0);
//$$ 			if (!list2.isEmpty()) {
//$$ 				list = list2;
//$$ 			}
//$$ 		}
//$$ 		// Drop
//$$ 		if (!list.isEmpty()) {
//$$ 			piglin.swing(InteractionHand.OFF_HAND);
//$$ 			@SuppressWarnings("rawtypes")
//$$ 			Iterator var3 = list.iterator();
//$$
//$$ 			while (var3.hasNext()) {
//$$ 				ItemStack itemStack = (ItemStack) var3.next();
//$$ 				BehaviorUtils.throwItem(piglin, itemStack, vec3d.add(0.0D, 1.0D, 0.0D));
//$$ 			}
//$$ 		}
//$$
//$$ 	}
//$$
//$$ }
//#else
@Mixin(net.minecraft.client.Minecraft.class)
public class MixinPiglinBrainPatch {

}
//#endif