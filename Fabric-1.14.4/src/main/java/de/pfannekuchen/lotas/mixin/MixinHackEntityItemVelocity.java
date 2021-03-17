package de.pfannekuchen.lotas.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.pfannekuchen.lotas.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.world.World;

@Mixin(ItemEntity.class)
public abstract class MixinHackEntityItemVelocity extends Entity {

    public MixinHackEntityItemVelocity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(method = "<init>(Lnet/minecraft/world/World;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;setVelocity(DDD)V"))
    public void redirectVelocityCall(ItemEntity it, double x2, double y2, double z2) {
        if (MinecraftClient.getInstance().player == null) return;
        double pX = MinecraftClient.getInstance().player.x - it.x;
        double pZ = MinecraftClient.getInstance().player.z - it.z;
        if (ConfigManager.getBoolean("tools", "manipulateVelocityTowards")) {
            it.setVelocity(Math.round(pX) * 0.03f, .2D, Math.round(pZ) * 0.03f);
        } else if (ConfigManager.getBoolean("tools", "manipulateVelocityAway")) {
            it.setVelocity(Math.round(pX) * -0.03f, .2D, Math.round(pZ) * -0.03f);
        } else {
            this.setVelocity(this.random.nextDouble() * 0.2D - 0.1D, 0.2D, this.random.nextDouble() * 0.2D - 0.1D);
        }
    }

}
