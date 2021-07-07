package de.pfannekuchen.lotas.mixin.patches;

import org.spongepowered.asm.mixin.Mixin;
//#if MC>=11601
//$$ import org.spongepowered.asm.mixin.Overwrite;
//$$
//$$ import de.pfannekuchen.lotas.gui.DropManipulationScreen;
//$$
//$$ import java.util.Iterator;
//$$ import java.util.List;
//$$ import net.minecraft.entity.ai.brain.task.LookTargetUtil;
//$$ import net.minecraft.entity.mob.PiglinBrain;
//$$ import net.minecraft.entity.mob.PiglinEntity;
//$$ import net.minecraft.item.ItemStack;
//$$ import net.minecraft.util.Hand;
//$$ import net.minecraft.util.math.Vec3d;
//$$
//$$ @Mixin(PiglinBrain.class)
//$$ public class MixinPiglinBrainPatch {
//$$
//$$ 	@Overwrite
//$$ 	private static void drop(PiglinEntity piglin, List<ItemStack> list, Vec3d vec3d) {
//$$ 		// Hijack list
//$$ 		for (DropManipulationScreen.DropManipulation man : DropManipulationScreen.manipulations) {
//$$ 			if (!man.enabled.isChecked())
//$$ 				continue;
//$$ 			List<ItemStack> list2 = man.redirectDrops((PiglinEntity) null);
//$$ 			if (!list2.isEmpty()) {
//$$ 				list = list2;
//$$ 			}
//$$ 		}
//$$ 		// Drop
//$$ 		if (!list.isEmpty()) {
//$$ 			piglin.swingHand(Hand.OFF_HAND);
//$$ 			@SuppressWarnings("rawtypes")
//$$ 			Iterator var3 = list.iterator();
//$$
//$$ 			while (var3.hasNext()) {
//$$ 				ItemStack itemStack = (ItemStack) var3.next();
//$$ 				LookTargetUtil.give(piglin, itemStack, vec3d.add(0.0D, 1.0D, 0.0D));
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